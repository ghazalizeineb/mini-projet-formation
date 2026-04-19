package com.isi.gestionformation.Service;

import com.isi.gestionformation.model.Employeur;
import com.isi.gestionformation.Repository.EmployeurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeurService {
    private final EmployeurRepository employeurRepository;

    @Transactional(readOnly = true)
    public List<Employeur> findAll() { return employeurRepository.findAll(); }

    @Transactional(readOnly = true)
    public Optional<Employeur> findById(Long id) { return employeurRepository.findById(id); }

    public Employeur create(Employeur employeur) {
        return employeurRepository.save(employeur);
    }

    public Employeur update(Long id, Employeur employeurModifie) {
        Employeur employeur = employeurRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employeur non trouvé : " + id));
        employeur.setNomEmployeur(employeurModifie.getNomEmployeur());
        return employeurRepository.save(employeur);
    }

    public void delete(Long id) {
        if (!employeurRepository.existsById(id))
            throw new RuntimeException("Employeur non trouvé : " + id);
        employeurRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long count() { return employeurRepository.count(); }
}