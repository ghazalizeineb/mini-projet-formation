package com.isi.gestionformation.Controller;

import com.isi.gestionformation.dto.FormationDTO;
import com.isi.gestionformation.model.Formation;
import com.isi.gestionformation.Service.FormationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/formations")
@RequiredArgsConstructor
public class FormationController {
    private final FormationService formationService;

    @GetMapping
    public ResponseEntity<List<Formation>> getAll() {
        return ResponseEntity.ok(formationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Formation> getById(@PathVariable Long id) {
        return formationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/annee/{annee}")
    public ResponseEntity<List<Formation>> getByAnnee(@PathVariable int annee) {
        return ResponseEntity.ok(formationService.findByAnnee(annee));
    }

    @GetMapping("/domaine/{domaineId}")
    public ResponseEntity<List<Formation>> getByDomaine(@PathVariable Long domaineId) {
        return ResponseEntity.ok(formationService.findByDomaine(domaineId));
    }

    @PostMapping
    public ResponseEntity<Formation> create(@RequestBody FormationDTO dto) {
        return new ResponseEntity<>(formationService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Formation> update(@PathVariable Long id, @RequestBody FormationDTO dto) {
        return ResponseEntity.ok(formationService.update(id, dto));
    }

    @PostMapping("/{formationId}/participants/{participantId}")
    public ResponseEntity<Formation> ajouterParticipant(
            @PathVariable Long formationId,
            @PathVariable Long participantId) {
        return ResponseEntity.ok(formationService.ajouterParticipant(formationId, participantId));
    }

    @DeleteMapping("/{formationId}/participants/{participantId}")
    public ResponseEntity<Formation> retirerParticipant(
            @PathVariable Long formationId,
            @PathVariable Long participantId) {
        return ResponseEntity.ok(formationService.retirerParticipant(formationId, participantId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        formationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(formationService.count());
    }
}