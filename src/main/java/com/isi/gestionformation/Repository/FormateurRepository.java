package com.isi.gestionformation.Repository;

import com.isi.gestionformation.model.Formateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FormateurRepository extends JpaRepository<Formateur, Long> {
    boolean existsByEmail(String email);
    List<Formateur> findByType(String type);
}