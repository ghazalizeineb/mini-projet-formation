package com.isi.gestionformation.Service;

import com.isi.gestionformation.model.Utilisateur;
import com.isi.gestionformation.Repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Utilisateur> findAll() { return utilisateurRepository.findAll(); }

    @Transactional(readOnly = true)
    public Optional<Utilisateur> findById(Long id) { return utilisateurRepository.findById(id); }

    @Transactional(readOnly = true)
    public Optional<Utilisateur> findByLogin(String login) { return utilisateurRepository.findByLogin(login); }

    public Utilisateur create(Utilisateur utilisateur) {
        if (utilisateurRepository.existsByLogin(utilisateur.getLogin())) {
            throw new RuntimeException("Login déjà utilisé : " + utilisateur.getLogin());
        }
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur update(Long id, Utilisateur utilisateurModifie) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + id));
        utilisateur.setLogin(utilisateurModifie.getLogin());
        utilisateur.setRole(utilisateurModifie.getRole());
        if (utilisateurModifie.getPassword() != null && !utilisateurModifie.getPassword().isBlank()) {
            utilisateur.setPassword(passwordEncoder.encode(utilisateurModifie.getPassword()));
        }
        return utilisateurRepository.save(utilisateur);
    }

    public void delete(Long id) {
        if (!utilisateurRepository.existsById(id))
            throw new RuntimeException("Utilisateur non trouvé : " + id);
        utilisateurRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long count() { return utilisateurRepository.count(); }
}