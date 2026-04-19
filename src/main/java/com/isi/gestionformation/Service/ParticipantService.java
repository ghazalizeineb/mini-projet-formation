package com.isi.gestionformation.Service;

import com.isi.gestionformation.model.Participant;
import com.isi.gestionformation.Repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    @Transactional(readOnly = true)
    public List<Participant> findAll() { return participantRepository.findAll(); }

    @Transactional(readOnly = true)
    public Optional<Participant> findById(Long id) { return participantRepository.findById(id); }

    @Transactional(readOnly = true)
    public List<Participant> findByStructure(Long structureId) {
        return participantRepository.findByStructure_Id(structureId);
    }

    @Transactional(readOnly = true)
    public List<Participant> findByProfil(Long profilId) {
        return participantRepository.findByProfil_Id(profilId);
    }

    public Participant create(Participant participant) {
        return participantRepository.save(participant);
    }

    public Participant update(Long id, Participant participantModifie) {
        Participant participant = participantRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Participant non trouvé : " + id));
        participant.setNom(participantModifie.getNom());
        participant.setPrenom(participantModifie.getPrenom());
        participant.setEmail(participantModifie.getEmail());
        participant.setTel(participantModifie.getTel());
        participant.setStructure(participantModifie.getStructure());
        participant.setProfil(participantModifie.getProfil());
        return participantRepository.save(participant);
    }

    public void delete(Long id) {
        if (!participantRepository.existsById(id))
            throw new RuntimeException("Participant non trouvé : " + id);
        participantRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long count() { return participantRepository.count(); }
}