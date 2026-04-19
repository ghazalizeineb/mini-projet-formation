package com.isi.gestionformation.Service;

import com.isi.gestionformation.model.Formateur;
import com.isi.gestionformation.Repository.FormateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FormateurService {
    private final FormateurRepository formateurRepository;

    @Transactional(readOnly = true)
    public List<Formateur> findAll() { return formateurRepository.findAll(); }

    @Transactional(readOnly = true)
    public Optional<Formateur> findById(Long id) { return formateurRepository.findById(id); }

    @Transactional(readOnly = true)
    public List<Formateur> findByType(String type) { return formateurRepository.findByType(type); }

    public Formateur create(Formateur formateur) {
        if (formateurRepository.existsByEmail(formateur.getEmail())) {
            throw new RuntimeException("Email déjà utilisé : " + formateur.getEmail());
        }
        return formateurRepository.save(formateur);
    }

    public Formateur update(Long id, Formateur formateurModifie) {
        Formateur formateur = formateurRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Formateur non trouvé : " + id));
        formateur.setNom(formateurModifie.getNom());
        formateur.setPrenom(formateurModifie.getPrenom());
        formateur.setEmail(formateurModifie.getEmail());
        formateur.setTel(formateurModifie.getTel());
        formateur.setType(formateurModifie.getType());
        formateur.setEmployeur(formateurModifie.getEmployeur());
        return formateurRepository.save(formateur);
    }

    public void delete(Long id) {
        if (!formateurRepository.existsById(id))
            throw new RuntimeException("Formateur non trouvé : " + id);
        formateurRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long count() { return formateurRepository.count(); }
}