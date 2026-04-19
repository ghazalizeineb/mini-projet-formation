package com.isi.gestionformation.Repository;

import com.isi.gestionformation.model.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
    List<Formation> findByAnnee(int annee);
    List<Formation> findByDomaine_Id(Long domaineId);
}