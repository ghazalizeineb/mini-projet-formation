package com.isi.gestionformation.Service;

import com.isi.gestionformation.dto.FormationDTO;
import com.isi.gestionformation.model.*;
import com.isi.gestionformation.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FormationService {
    private final FormationRepository formationRepository;
    private final ParticipantRepository participantRepository;
    private final DomaineRepository domaineRepository;
    private final FormateurRepository formateurRepository;

    @Transactional(readOnly = true)
    public List<Formation> findAll() { 
        return formationRepository.findAll(); 
    }

    @Transactional(readOnly = true)
    public Optional<Formation> findById(Long id) { 
        return formationRepository.findById(id); 
    }

    @Transactional(readOnly = true)
    public List<Formation> findByAnnee(int annee) { 
        return formationRepository.findByAnnee(annee); 
    }

    @Transactional(readOnly = true)
    public List<Formation> findByDomaine(Long domaineId) { 
        return formationRepository.findByDomaine_Id(domaineId); 
    }

    public Formation create(FormationDTO dto) {
        Formation formation = new Formation();
        formation.setTitre(dto.getTitre());
        formation.setAnnee(dto.getAnnee());
        formation.setDuree(dto.getDuree());
        formation.setBudget(dto.getBudget());

        // Récupérer domaine existant par son ID
        if (dto.getDomaineId() != null) {
            Domaine domaine = domaineRepository.findById(dto.getDomaineId())
                .orElseThrow(() -> new RuntimeException("Domaine non trouvé : " + dto.getDomaineId()));
            formation.setDomaine(domaine);
        }

        // Récupérer formateur existant par son ID
        if (dto.getFormateurId() != null) {
            Formateur formateur = formateurRepository.findById(dto.getFormateurId())
                .orElseThrow(() -> new RuntimeException("Formateur non trouvé : " + dto.getFormateurId()));
            formation.setFormateur(formateur);
        }

        return formationRepository.save(formation);
    }

    public Formation update(Long id, FormationDTO dto) {
        Formation formation = formationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Formation non trouvée : " + id));
        
        formation.setTitre(dto.getTitre());
        formation.setAnnee(dto.getAnnee());
        formation.setDuree(dto.getDuree());
        formation.setBudget(dto.getBudget());

        if (dto.getDomaineId() != null) {
            Domaine domaine = domaineRepository.findById(dto.getDomaineId())
                .orElseThrow(() -> new RuntimeException("Domaine non trouvé : " + dto.getDomaineId()));
            formation.setDomaine(domaine);
        }

        if (dto.getFormateurId() != null) {
            Formateur formateur = formateurRepository.findById(dto.getFormateurId())
                .orElseThrow(() -> new RuntimeException("Formateur non trouvé : " + dto.getFormateurId()));
            formation.setFormateur(formateur);
        }

        return formationRepository.save(formation);
    }

    public Formation ajouterParticipant(Long formationId, Long participantId) {
        Formation formation = formationRepository.findById(formationId)
            .orElseThrow(() -> new RuntimeException("Formation non trouvée : " + formationId));
        Participant participant = participantRepository.findById(participantId)
            .orElseThrow(() -> new RuntimeException("Participant non trouvé : " + participantId));
        formation.getParticipants().add(participant);
        return formationRepository.save(formation);
    }

    public Formation retirerParticipant(Long formationId, Long participantId) {
        Formation formation = formationRepository.findById(formationId)
            .orElseThrow(() -> new RuntimeException("Formation non trouvée : " + formationId));
        formation.getParticipants().removeIf(p -> p.getId().equals(participantId));
        return formationRepository.save(formation);
    }

    public void delete(Long id) {
        if (!formationRepository.existsById(id))
            throw new RuntimeException("Formation non trouvée : " + id);
        formationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long count() { 
        return formationRepository.count(); 
    }
}