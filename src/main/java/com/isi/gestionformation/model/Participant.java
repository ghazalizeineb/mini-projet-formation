package com.isi.gestionformation.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "participant")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(length = 200)
    private String email;

    @Column(length = 20)
    private String tel;

    @ManyToOne
    @JoinColumn(name = "id_structure")
    private Structure structure;

    @ManyToOne
    @JoinColumn(name = "id_profil")
    private Profil profil;
}