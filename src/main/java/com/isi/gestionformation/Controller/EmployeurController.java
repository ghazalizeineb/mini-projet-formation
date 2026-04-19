package com.isi.gestionformation.Controller;

import com.isi.gestionformation.model.Employeur;
import com.isi.gestionformation.Service.EmployeurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employeurs")
@RequiredArgsConstructor
public class EmployeurController {
    private final EmployeurService employeurService;

    @GetMapping
    public ResponseEntity<List<Employeur>> getAll() {
        return ResponseEntity.ok(employeurService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employeur> getById(@PathVariable Long id) {
        return employeurService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Employeur> create(@RequestBody Employeur employeur) {
        return new ResponseEntity<>(employeurService.create(employeur), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employeur> update(@PathVariable Long id, @RequestBody Employeur employeur) {
        return ResponseEntity.ok(employeurService.update(id, employeur));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeurService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(employeurService.count());
    }
}