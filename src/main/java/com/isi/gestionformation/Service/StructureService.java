package com.isi.gestionformation.Service;

import com.isi.gestionformation.model.Structure;
import com.isi.gestionformation.Repository.StructureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StructureService {
    private final StructureRepository structureRepository;

    @Transactional(readOnly = true)
    public List<Structure> findAll() { return structureRepository.findAll(); }

    @Transactional(readOnly = true)
    public Optional<Structure> findById(Long id) { return structureRepository.findById(id); }

    public Structure create(Structure structure) {
        if (structureRepository.existsByLibelle(structure.getLibelle())) {
            throw new RuntimeException("Structure déjà existante : " + structure.getLibelle());
        }
        return structureRepository.save(structure);
    }

    public Structure update(Long id, Structure structureModifiee) {
        Structure structure = structureRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Structure non trouvée : " + id));
        structure.setLibelle(structureModifiee.getLibelle());
        return structureRepository.save(structure);
    }

    public void delete(Long id) {
        if (!structureRepository.existsById(id))
            throw new RuntimeException("Structure non trouvée : " + id);
        structureRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long count() { return structureRepository.count(); }
}