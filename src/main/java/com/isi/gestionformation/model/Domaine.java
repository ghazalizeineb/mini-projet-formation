package com.isi.gestionformation.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "domaine")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Domaine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String libelle;
}