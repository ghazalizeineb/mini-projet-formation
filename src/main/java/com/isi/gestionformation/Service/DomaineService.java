package com.isi.gestionformation.Service;

import com.isi.gestionformation.model.Domaine;
import com.isi.gestionformation.Repository.DomaineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DomaineService {
    private final DomaineRepository domaineRepository;

    @Transactional(readOnly = true)
    public List<Domaine> findAll() { return domaineRepository.findAll(); }

    @Transactional(readOnly = true)
    public Optional<Domaine> findById(Long id) { return domaineRepository.findById(id); }

    public Domaine create(Domaine domaine) {
        if (domaineRepository.existsByLibelle(domaine.getLibelle())) {
            throw new RuntimeException("Domaine déjà existant : " + domaine.getLibelle());
        }
        return domaineRepository.save(domaine);
    }

    public Domaine update(Long id, Domaine domaineModifie) {
        Domaine domaine = domaineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Domaine non trouvé : " + id));
        domaine.setLibelle(domaineModifie.getLibelle());
        return domaineRepository.save(domaine);
    }

    public void delete(Long id) {
        if (!domaineRepository.existsById(id))
            throw new RuntimeException("Domaine non trouvé : " + id);
        domaineRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long count() { return domaineRepository.count(); }
}