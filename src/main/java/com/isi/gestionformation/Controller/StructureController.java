package com.isi.gestionformation.Controller;

import com.isi.gestionformation.model.Structure;
import com.isi.gestionformation.Service.StructureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/structures")
@RequiredArgsConstructor
public class StructureController {
    private final StructureService structureService;

    @GetMapping
    public ResponseEntity<List<Structure>> getAll() {
        return ResponseEntity.ok(structureService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Structure> getById(@PathVariable Long id) {
        return structureService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Structure> create(@RequestBody Structure structure) {
        return new ResponseEntity<>(structureService.create(structure), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Structure> update(@PathVariable Long id, @RequestBody Structure structure) {
        return ResponseEntity.ok(structureService.update(id, structure));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        structureService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(structureService.count());
    }
}