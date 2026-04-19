package com.isi.gestionformation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "formation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @Column(nullable = false)
    @NotNull(message = "L'année est obligatoire")
    private Integer annee;

    @Column(nullable = false)
    @NotNull(message = "La durée est obligatoire")
    private Integer duree;

    @Column(nullable = false)
    @NotNull(message = "Le budget est obligatoire")
    private Double budget;

    @ManyToOne
    @JoinColumn(name = "id_domaine")
    @JsonIgnoreProperties({"formations"})
    private Domaine domaine;

    @ManyToOne
    @JoinColumn(name = "id_formateur")
    @JsonIgnoreProperties({"formations", "employeur"})
    private Formateur formateur;

    @ManyToMany
    @JoinTable(
        name = "formation_participant",
        joinColumns = @JoinColumn(name = "id_formation"),
        inverseJoinColumns = @JoinColumn(name = "id_participant")
    )
    @JsonIgnoreProperties({"formations"})
    private List<Participant> participants = new ArrayList<>();
}