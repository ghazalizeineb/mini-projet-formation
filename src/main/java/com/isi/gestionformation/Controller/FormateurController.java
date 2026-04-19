package com.isi.gestionformation.Controller;

import com.isi.gestionformation.model.Formateur;
import com.isi.gestionformation.Service.FormateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/formateurs")
@RequiredArgsConstructor
public class FormateurController {
    private final FormateurService formateurService;

    @GetMapping
    public ResponseEntity<List<Formateur>> getAll() {
        return ResponseEntity.ok(formateurService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Formateur> getById(@PathVariable Long id) {
        return formateurService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Formateur>> getByType(@PathVariable String type) {
        return ResponseEntity.ok(formateurService.findByType(type));
    }

    @PostMapping
    public ResponseEntity<Formateur> create(@RequestBody Formateur formateur) {
        return new ResponseEntity<>(formateurService.create(formateur), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Formateur> update(@PathVariable Long id, @RequestBody Formateur formateur) {
        return ResponseEntity.ok(formateurService.update(id, formateur));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        formateurService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(formateurService.count());
    }
}