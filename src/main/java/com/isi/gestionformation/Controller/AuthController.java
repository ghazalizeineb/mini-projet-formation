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
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final ParticipantRepository participantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String login = body.get("login");
        String password = body.get("password");

        System.out.println("=== LOGIN ===");
        System.out.println("Login: " + login);

        Utilisateur user = utilisateurRepository.findByLogin(login).orElse(null);

        if (user == null) {
            System.out.println("Utilisateur non trouvé !");
            return ResponseEntity.status(401).body("Login ou mot de passe incorrect");
        }

        System.out.println("User trouvé: " + user.getLogin());
        System.out.println("Password match: " + passwordEncoder.matches(password, user.getPassword()));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body("Login ou mot de passe incorrect");
        }

        String token = jwtUtil.generateToken(user.getLogin(), user.getRole().getNom());

        return ResponseEntity.ok(Map.of(
            "token", token,
            "login", user.getLogin(),
            "role", user.getRole().getNom(),
            "participantId", user.getParticipant() != null ? user.getParticipant().getId() : ""
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String login = body.get("login");
        String password = body.get("password");
        String nom = body.get("nom");
        String prenom = body.get("prenom");

        if (login == null || login.isBlank())
            return ResponseEntity.badRequest().body("Login obligatoire");
        if (password == null || password.length() < 6)
            return ResponseEntity.badRequest().body("Mot de passe trop court (min. 6 caractères)");
        if (nom == null || nom.isBlank())
            return ResponseEntity.badRequest().body("Nom obligatoire");
        if (prenom == null || prenom.isBlank())
            return ResponseEntity.badRequest().body("Prénom obligatoire");
        if (utilisateurRepository.existsByLogin(login))
            return ResponseEntity.badRequest().body("Login déjà utilisé");

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
    @GetMapping("/hash/{password}")
public String hash(@PathVariable String password) {
    return passwordEncoder.encode(password);
}
}