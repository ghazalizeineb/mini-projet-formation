package com.isi.gestionformation.Repository;

import com.isi.gestionformation.model.Domaine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomaineRepository extends JpaRepository<Domaine, Long> {
    boolean existsByLibelle(String libelle);
}