package com.isi.gestionformation.Controller;

import com.isi.gestionformation.model.Profil;
import com.isi.gestionformation.Service.ProfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/profils")
@RequiredArgsConstructor
public class ProfilController {
    private final ProfilService profilService;

    @GetMapping
    public ResponseEntity<List<Profil>> getAll() {
        return ResponseEntity.ok(profilService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profil> getById(@PathVariable Long id) {
        return profilService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Profil> create(@RequestBody Profil profil) {
        return new ResponseEntity<>(profilService.create(profil), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profil> update(@PathVariable Long id, @RequestBody Profil profil) {
        return ResponseEntity.ok(profilService.update(id, profil));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        profilService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(profilService.count());
    }
}