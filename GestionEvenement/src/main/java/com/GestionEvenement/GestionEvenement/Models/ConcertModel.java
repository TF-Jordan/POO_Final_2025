package com.GestionEvenement.GestionEvenement.Models;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Data
@Service
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConcertModel extends EvenementModel{
    private String artiste;
    private String genreMusical;
    private double prixBillet;

    public ConcertModel(String organisateurId, List<ParticipantModel> participansInscrits, int capaciteMax, boolean estAnnule, String lieu, LocalDate date, String nom, String artiste, String genreMusical, double prixBillet) {
        super(organisateurId, participansInscrits, capaciteMax, estAnnule, lieu, date, nom);
        this.artiste=artiste;
        this.genreMusical=genreMusical;
        this.prixBillet=prixBillet;
    }

    public ConcertModel() {
        super();
    }

    @Override
    public String getType() {
        return "concert";
    }


    @Override
    public void afficherDetails() {
        super.afficherDetails();
        StringBuilder sb = new StringBuilder();
        sb.append("Type         : Concert\n");
        sb.append(String.format("Artiste      : %s\n", artiste));
        sb.append(String.format("Genre        : %s\n", genreMusical));
        sb.append(String.format("Prix billet  : %.2f €\n", prixBillet));
        System.out.println(sb.toString());
    }


}
