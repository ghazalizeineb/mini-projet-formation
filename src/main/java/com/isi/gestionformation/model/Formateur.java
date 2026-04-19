package com.isi.gestionformation.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "formateur")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Formateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, unique = true, length = 200)
    private String email;

    @Column(length = 20)
    private String tel;

    @Column(nullable = false, length = 20)
    private String type; // INTERNE ou EXTERNE

    @ManyToOne
    @JoinColumn(name = "id_employeur")
    private Employeur employeur;
}