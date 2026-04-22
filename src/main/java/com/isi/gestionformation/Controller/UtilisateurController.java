package com.isi.gestionformation.Controller;

import com.isi.gestionformation.model.Utilisateur;
import com.isi.gestionformation.Service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @GetMapping
    public ResponseEntity<List<Utilisateur>> getAll() {
        return ResponseEntity.ok(utilisateurService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getById(@PathVariable Long id) {
        return utilisateurService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Utilisateur> create(@RequestBody Utilisateur utilisateur) {
        return new ResponseEntity<>(utilisateurService.create(utilisateur), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> update(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        return ResponseEntity.ok(utilisateurService.update(id, utilisateur));
    }

    // ← endpoint lier participant
    @PutMapping("/{id}/participant/{participantId}")
    public ResponseEntity<Utilisateur> lierParticipant(
            @PathVariable Long id,
            @PathVariable Long participantId) {
        return ResponseEntity.ok(utilisateurService.lierParticipant(id, participantId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        utilisateurService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ← méthode count ajoutée
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(utilisateurService.count());
    }
}