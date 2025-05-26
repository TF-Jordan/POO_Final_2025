package com.GestionEvenement.GestionEvenement.Models;

import com.GestionEvenement.GestionEvenement.Exception.CapaciteMaxAtteinteException;
import com.GestionEvenement.GestionEvenement.Services.GestionEvenement.GestionEvenement;
import com.GestionEvenement.GestionEvenement.Utils.MatriculeGenerator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConcertModel.class, name = "concert"),
        @JsonSubTypes.Type(value = ConferenceModel.class, name = "conference")
})
public abstract class EvenementModel {
    @Setter(AccessLevel.NONE)
    protected String id= MatriculeGenerator.getInstance().genererMatricule();
    protected String nom;
    protected LocalDate date;
    protected String lieu;
    protected boolean estAnnule=false;
    protected int CapaciteMax;
    protected List<ParticipantModel> participansInscrits;
    protected String organisateurId;

    public  EvenementModel(){}

    public boolean retirerParticipant(String participantId) {
        return participansInscrits.removeIf(p -> p.getId().equals(participantId));
    }

    public abstract String getType();

    public void annuler() {
        this.estAnnule = true;
        String sujet = "Annulation de l'événement : " + nom;
        String message = String.format(
                "Bonjour,\n\nNous vous informons que l'événement \"%s\" prévu le %s à %s a été annulé.\n\nNous vous prions de nous excuser pour ce désagrément.\n\nCordialement,\nL'équipe organisatrice.",
                nom, date.toString(), lieu
        );

        GestionEvenement.getInstance().notifierObservateurs(this, message, sujet);
    }


    public void afficherDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Nom          : %s\n", nom));
        sb.append(String.format("Date         : %s\n", date));
        sb.append(String.format("Lieu         : %s\n", lieu));
        sb.append(String.format("Inscrits     : %d\n", participansInscrits.size()));
        sb.append(String.format("Capacité max : %d participants\n", CapaciteMax));
        sb.append(String.format("Statut       : %s\n", estAnnule ? "Annulé" : "Actif"));
        System.out.println(sb.toString());
    }

    public boolean ajouterParticipant(ParticipantModel p) throws CapaciteMaxAtteinteException {
        if(getParticipansInscrits().size()>=CapaciteMax){
            throw new CapaciteMaxAtteinteException("Capacité maximale atteinte.");
        }else{
            participansInscrits.add(p);
            return true;
        }

    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvenementModel evenement = (EvenementModel) o;
        return Objects.equals(id, evenement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public EvenementModel(String organisateurId, List<ParticipantModel> participansInscrits, int capaciteMax, boolean estAnnule, String lieu, LocalDate date, String nom) {
        this.organisateurId = organisateurId;
        this.participansInscrits = participansInscrits;
        CapaciteMax = capaciteMax;
        this.estAnnule = estAnnule;
        this.lieu = lieu;
        this.date = date;
        this.nom = nom;
    }

}
