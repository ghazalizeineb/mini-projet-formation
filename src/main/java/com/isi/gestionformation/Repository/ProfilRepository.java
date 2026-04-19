package com.isi.gestionformation.Repository;

import com.isi.gestionformation.model.Profil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilRepository extends JpaRepository<Profil, Long> {
    boolean existsByLibelle(String libelle);
}