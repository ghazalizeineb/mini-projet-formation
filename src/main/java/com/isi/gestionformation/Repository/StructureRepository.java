package com.isi.gestionformation.Repository;

import com.isi.gestionformation.model.Structure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StructureRepository extends JpaRepository<Structure, Long> {
    boolean existsByLibelle(String libelle);
}