package com.isi.gestionformation.Controller;

import com.isi.gestionformation.Config.JwtUtil;
import com.isi.gestionformation.model.Utilisateur;
import com.isi.gestionformation.model.Role;
import com.isi.gestionformation.Repository.UtilisateurRepository;
import com.isi.gestionformation.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String login = credentials.get("login");
        String password = credentials.get("password");

        Utilisateur utilisateur = utilisateurRepository.findByLogin(login).orElse(null);

        if (utilisateur == null || !passwordEncoder.matches(password, utilisateur.getPassword())) {
            return ResponseEntity.status(401).body("Login ou mot de passe incorrect !");
        }

        String token = jwtUtil.generateToken(utilisateur.getLogin(), utilisateur.getRole().getNom());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("login", utilisateur.getLogin());
        response.put("role", utilisateur.getRole().getNom());

        return ResponseEntity.ok(response);
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> data) {
        String login = data.get("login");
        String password = data.get("password");

        if (utilisateurRepository.existsByLogin(login)) {
            return ResponseEntity.status(400).body("Login déjà utilisé !");
        }

        // Par défaut le rôle est SIMPLE_UTILISATEUR
        Role role = roleRepository.findByNom("SIMPLE_UTILISATEUR")
            .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setLogin(login);
        utilisateur.setPassword(passwordEncoder.encode(password));
        utilisateur.setRole(role);

        utilisateurRepository.save(utilisateur);

        String token = jwtUtil.generateToken(login, role.getNom());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("login", login);
        response.put("role", role.getNom());

        return ResponseEntity.ok(response);
    }
}