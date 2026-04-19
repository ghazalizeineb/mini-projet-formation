package com.isi.gestionformation.dto;

import lombok.Data;

@Data
public class FormationDTO {
    private String titre;
    private int annee;
    private int duree;
    private double budget;
    private Long domaineId;
    private Long formateurId;
}