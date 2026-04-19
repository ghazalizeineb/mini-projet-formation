package com.isi.gestionformation.Service;

import com.isi.gestionformation.model.Domaine;
import com.isi.gestionformation.model.Formateur;
import com.isi.gestionformation.model.Formation;
import com.isi.gestionformation.model.Participant;
import com.isi.gestionformation.Repository.DomaineRepository;
import com.isi.gestionformation.Repository.FormateurRepository;
import com.isi.gestionformation.Repository.FormationRepository;
import com.isi.gestionformation.Repository.ParticipantRepository;
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

    public Formation create(Formation formation) {
        if (formation.getDomaine() != null && formation.getDomaine().getId() != null) {
            Domaine domaine = domaineRepository.findById(formation.getDomaine().getId())
                .orElseThrow(() -> new RuntimeException("Domaine non trouvé : " + formation.getDomaine().getId()));
            formation.setDomaine(domaine);
        }
        if (formation.getFormateur() != null && formation.getFormateur().getId() != null) {
            Formateur formateur = formateurRepository.findById(formation.getFormateur().getId())
                .orElseThrow(() -> new RuntimeException("Formateur non trouvé : " + formation.getFormateur().getId()));
            formation.setFormateur(formateur);
        }
        return formationRepository.save(formation);
    }

    public Formation update(Long id, Formation formationModifiee) {
        Formation formation = formationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Formation non trouvée : " + id));
        formation.setTitre(formationModifiee.getTitre());
        formation.setAnnee(formationModifiee.getAnnee());
        formation.setDuree(formationModifiee.getDuree());
        formation.setBudget(formationModifiee.getBudget());

        if (formationModifiee.getDomaine() != null && formationModifiee.getDomaine().getId() != null) {
            Domaine domaine = domaineRepository.findById(formationModifiee.getDomaine().getId())
                .orElseThrow(() -> new RuntimeException("Domaine non trouvé : " + formationModifiee.getDomaine().getId()));
            formation.setDomaine(domaine);
        }
        if (formationModifiee.getFormateur() != null && formationModifiee.getFormateur().getId() != null) {
            Formateur formateur = formateurRepository.findById(formationModifiee.getFormateur().getId())
                .orElseThrow(() -> new RuntimeException("Formateur non trouvé : " + formationModifiee.getFormateur().getId()));
            formation.setFormateur(formateur);
        }
        return formationRepository.save(formation);
    }

    public Formation ajouterParticipant(Long formationId, Long participantId) {
        Formation formation = formationRepository.findById(formationId)
            .orElseThrow(() -> new RuntimeException("Formation non trouvée : " + formationId));
        Participant participant = participantRepository.findById(participantId)
            .orElseThrow(() -> new RuntimeException("Participant non trouvé : " + participantId));

        if (formation.getParticipants().contains(participant)) {
            throw new RuntimeException("Participant déjà inscrit à cette formation");
        }
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