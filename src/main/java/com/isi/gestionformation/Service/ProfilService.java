package com.isi.gestionformation.Service;

import com.isi.gestionformation.model.Profil;
import com.isi.gestionformation.Repository.ProfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfilService {
    private final ProfilRepository profilRepository;

    @Transactional(readOnly = true)
    public List<Profil> findAll() { return profilRepository.findAll(); }

    @Transactional(readOnly = true)
    public Optional<Profil> findById(Long id) { return profilRepository.findById(id); }

    public Profil create(Profil profil) {
        if (profilRepository.existsByLibelle(profil.getLibelle())) {
            throw new RuntimeException("Profil déjà existant : " + profil.getLibelle());
        }
        return profilRepository.save(profil);
    }

    public Profil update(Long id, Profil profilModifie) {
        Profil profil = profilRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Profil non trouvé : " + id));
        profil.setLibelle(profilModifie.getLibelle());
        return profilRepository.save(profil);
    }

    public void delete(Long id) {
        if (!profilRepository.existsById(id))
            throw new RuntimeException("Profil non trouvé : " + id);
        profilRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long count() { return profilRepository.count(); }
}