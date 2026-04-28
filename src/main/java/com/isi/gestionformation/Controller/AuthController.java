package com.isi.gestionformation.Controller;

import com.isi.gestionformation.Config.JwtUtil;
import com.isi.gestionformation.Repository.ParticipantRepository;
import com.isi.gestionformation.Repository.RoleRepository;
import com.isi.gestionformation.Repository.UtilisateurRepository;
import com.isi.gestionformation.model.Participant;
import com.isi.gestionformation.model.Role;
import com.isi.gestionformation.model.Utilisateur;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final ParticipantRepository participantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // === Rate limiting (anti-force brute) ===
    private final Map<String, LoginAttempt> loginAttempts = new ConcurrentHashMap<>();

    private static class LoginAttempt {
        int count;
        long firstAttemptTime;
        long blockUntil;
    }

    // === Liste des emails jetables à bloquer ===
    private final List<String> disposableDomains = List.of(
        "mailinator.com", "guerrillamail.com", "tempmail.com", "10minutemail.com",
        "yopmail.com", "throwawaymail.com", "guerrillamail.net", "mailnator.com",
        "sharklasers.com", "grr.la", "pokemail.net", "spam4.me", "bccto.me"
    );

    // ==========================================
    // 1. VALIDATION MOT DE PASSE FORT
    // ==========================================
    private boolean isPasswordStrong(String password) {
        if (password == null) return false;
        if (password.length() < 8) return false;
        if (!password.matches(".*[A-Z].*")) return false;      // au moins 1 majuscule
        if (!password.matches(".*[a-z].*")) return false;      // au moins 1 minuscule
        if (!password.matches(".*[0-9].*")) return false;      // au moins 1 chiffre
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) return false; // caractère spécial
        
        // Interdire les mots de passe trop communs
        String lowerPwd = password.toLowerCase();
        if (lowerPwd.contains("password") || lowerPwd.contains("123456") || 
            lowerPwd.contains("azerty") || lowerPwd.contains("qwerty")) {
            return false;
        }
        return true;
    }

    // ==========================================
    // 2. VALIDATION EMAIL (FORMAT)
    // ==========================================
    private boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    // ==========================================
    // 3. BLOQUAGE EMAILS JETABLES
    // ==========================================
    private boolean isDisposableEmail(String email) {
        try {
            String domain = email.substring(email.indexOf('@') + 1).toLowerCase();
            return disposableDomains.contains(domain);
        } catch (Exception e) {
            return true;
        }
    }

    // ==========================================
    // 4. RATE LIMITING - VERIFICATION BLOCAGE
    // ==========================================
    private boolean isBlocked(String ip) {
        LoginAttempt attempt = loginAttempts.get(ip);
        if (attempt == null) return false;
        if (attempt.blockUntil > System.currentTimeMillis()) return true;
        
        // Nettoyer après 15 minutes sans nouvelle tentative
        if (System.currentTimeMillis() - attempt.firstAttemptTime > 15 * 60 * 1000) {
            loginAttempts.remove(ip);
            return false;
        }
        return false;
    }

    private void registerFailedAttempt(String ip) {
        LoginAttempt attempt = loginAttempts.computeIfAbsent(ip, k -> new LoginAttempt());
        attempt.count++;
        attempt.firstAttemptTime = System.currentTimeMillis();
        
        if (attempt.count >= 5) {
            attempt.blockUntil = System.currentTimeMillis() + 15 * 60 * 1000; // bloqué 15 min
        }
        loginAttempts.put(ip, attempt);
    }

    private void resetFailedAttempts(String ip) {
        loginAttempts.remove(ip);
    }

    // ==========================================
    // 5. ENDPOINT LOGIN (SÉCURISÉ)
    // ==========================================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        
        // Vérifier rate limiting
        if (isBlocked(ip)) {
            return ResponseEntity.status(429).body("Trop de tentatives. Réessayez dans 15 minutes.");
        }
        
        String login = body.get("login");
        String password = body.get("password");

        System.out.println("=== LOGIN ===");
        System.out.println("Login: " + login);
        System.out.println("IP: " + ip);

        Utilisateur user = utilisateurRepository.findByLogin(login).orElse(null);

        if (user == null) {
            System.out.println("Utilisateur non trouvé !");
            registerFailedAttempt(ip);
            return ResponseEntity.status(401).body("Login ou mot de passe incorrect");
        }

        System.out.println("User trouvé: " + user.getLogin());
        System.out.println("Password match: " + passwordEncoder.matches(password, user.getPassword()));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            registerFailedAttempt(ip);
            return ResponseEntity.status(401).body("Login ou mot de passe incorrect");
        }

        // Succès : réinitialiser les tentatives
        resetFailedAttempts(ip);

        String token = jwtUtil.generateToken(user.getLogin(), user.getRole().getNom());

        return ResponseEntity.ok(Map.of(
            "token", token,
            "login", user.getLogin(),
            "role", user.getRole().getNom(),
            "participantId", user.getParticipant() != null ? user.getParticipant().getId() : ""
        ));
    }

    // ==========================================
    // 6. ENDPOINT REGISTER (SÉCURISÉ)
    // ==========================================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String login = body.get("login");
        String password = body.get("password");
        String nom = body.get("nom");
        String prenom = body.get("prenom");
        String ip = request.getRemoteAddr();

        // Vérifier rate limiting pour l'inscription aussi
        if (isBlocked(ip)) {
            return ResponseEntity.status(429).body("Trop de tentatives. Réessayez dans 15 minutes.");
        }

        // === VALIDATIONS ===
        
        // 1. Login (email) obligatoire
        if (login == null || login.isBlank())
            return ResponseEntity.badRequest().body("Email obligatoire");
        
        // 2. Format email valide
        if (!isValidEmail(login))
            return ResponseEntity.badRequest().body("Format d'email invalide");
        
        // 3. Email jetable interdit
        if (isDisposableEmail(login))
            return ResponseEntity.badRequest().body("Les emails temporaires ne sont pas autorisés");
        
        // 4. Mot de passe fort
        if (password == null || !isPasswordStrong(password))
            return ResponseEntity.badRequest().body("Mot de passe faible : min 8 caractères, 1 majuscule, 1 minuscule, 1 chiffre, 1 caractère spécial");
        
        // 5. Nom obligatoire
        if (nom == null || nom.isBlank() || nom.length() < 2)
            return ResponseEntity.badRequest().body("Nom obligatoire (minimum 2 caractères)");
        
        // 6. Prénom obligatoire
        if (prenom == null || prenom.isBlank() || prenom.length() < 2)
            return ResponseEntity.badRequest().body("Prénom obligatoire (minimum 2 caractères)");
        
        // 7. Email déjà utilisé ? (message générique pour éviter énumération)
        if (utilisateurRepository.existsByLogin(login))
            return ResponseEntity.badRequest().body("Cet email n'est pas disponible");

        // === CRÉATION COMPTE ===
        
        Role role = roleRepository.findByNom("SIMPLE_UTILISATEUR")
            .orElseThrow(() -> new RuntimeException("Rôle SIMPLE_UTILISATEUR introuvable"));

        Participant participant = new Participant();
        participant.setNom(nom);
        participant.setPrenom(prenom);
        participant.setEmail(login);
        participantRepository.save(participant);

        Utilisateur user = new Utilisateur();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setParticipant(participant);
        utilisateurRepository.save(user);

        // Réinitialiser les tentatives après inscription réussie
        resetFailedAttempts(ip);

        String token = jwtUtil.generateToken(user.getLogin(), role.getNom());

        return ResponseEntity.ok(Map.of(
            "token", token,
            "login", user.getLogin(),
            "role", role.getNom(),
            "participantId", participant.getId(),
            "nom", nom,
            "prenom", prenom
        ));
    }

    // ==========================================
    // 7. ENDPOINT HASH (DÉSACTIVÉ EN PRODUCTION)
    // ==========================================
    // ⚠️ ATTENTION : Ce endpoint est DANGEREUX en production.
    // Décommentez la ligne @PreAuthorize si vous utilisez Spring Security,
    // ou mieux : supprimez-le complètement.
    
    // @PreAuthorize("hasRole('ADMIN')")  // Décommentez si vous avez Spring Security
    @GetMapping("/hash/{password}")
    public String hash(@PathVariable String password) {
        // En production, ce endpoint devrait être protégé ou supprimé
        return passwordEncoder.encode(password);
    }
}