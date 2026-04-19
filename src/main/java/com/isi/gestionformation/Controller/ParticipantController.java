package com.isi.gestionformation.Controller;

import com.isi.gestionformation.model.Participant;
import com.isi.gestionformation.Service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;

    @GetMapping
    public ResponseEntity<List<Participant>> getAll() {
        return ResponseEntity.ok(participantService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participant> getById(@PathVariable Long id) {
        return participantService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/structure/{structureId}")
    public ResponseEntity<List<Participant>> getByStructure(@PathVariable Long structureId) {
        return ResponseEntity.ok(participantService.findByStructure(structureId));
    }

    @GetMapping("/profil/{profilId}")
    public ResponseEntity<List<Participant>> getByProfil(@PathVariable Long profilId) {
        return ResponseEntity.ok(participantService.findByProfil(profilId));
    }

    @PostMapping
    public ResponseEntity<Participant> create(@RequestBody Participant participant) {
        return new ResponseEntity<>(participantService.create(participant), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participant> update(@PathVariable Long id, @RequestBody Participant participant) {
        return ResponseEntity.ok(participantService.update(id, participant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        participantService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(participantService.count());
    }
}