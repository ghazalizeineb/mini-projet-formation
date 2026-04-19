package com.isi.gestionformation.Controller;

import com.isi.gestionformation.model.Domaine;
import com.isi.gestionformation.Service.DomaineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/domaines")
@RequiredArgsConstructor
public class DomaineController {
    private final DomaineService domaineService;

    @GetMapping
    public ResponseEntity<List<Domaine>> getAll() {
        return ResponseEntity.ok(domaineService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Domaine> getById(@PathVariable Long id) {
        return domaineService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Domaine> create(@RequestBody Domaine domaine) {
        return new ResponseEntity<>(domaineService.create(domaine), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Domaine> update(@PathVariable Long id, @RequestBody Domaine domaine) {
        return ResponseEntity.ok(domaineService.update(id, domaine));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        domaineService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(domaineService.count());
    }
}