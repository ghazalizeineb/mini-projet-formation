package com.isi.gestionformation.Repository;

import com.isi.gestionformation.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByStructure_Id(Long structureId);
    List<Participant> findByProfil_Id(Long profilId);
}