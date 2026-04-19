package com.isi.gestionformation.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employeur")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employeur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_employeur", nullable = false, length = 200)
    private String nomEmployeur;
}