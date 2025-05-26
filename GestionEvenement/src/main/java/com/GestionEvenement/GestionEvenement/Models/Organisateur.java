package com.GestionEvenement.GestionEvenement.Models;



import com.GestionEvenement.GestionEvenement.Exception.EvenementDejaExistantException;
import com.GestionEvenement.GestionEvenement.Exception.EvenementIntrouvableException;
import com.GestionEvenement.GestionEvenement.Exception.EvenementNonExistant;
import com.GestionEvenement.GestionEvenement.Repository.Classes.save.SaveEvtModif;
import com.GestionEvenement.GestionEvenement.Services.GestionEvenement.GestionEvenement;
import com.GestionEvenement.GestionEvenement.Utils.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class Organisateur extends ParticipantModel {
    private List<EvenementModel> evenementsOrganises;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
    private GestionEvenement gestionnaire=GestionEvenement.getInstance();

    public Organisateur(){}
    public Organisateur(String id, String nom, String motDePasse, String email, Role role) {
        super(id, nom, motDePasse, email, Role.ORGANISATEUR);
    }

    public Organisateur(String id, String email, String motDePasse, String nom, List<EvenementModel> evenementsOrganises, GestionEvenement gestionnaire) {
        super(id, email, motDePasse, nom);
        this.evenementsOrganises = evenementsOrganises;
        this.gestionnaire = gestionnaire;
    }

    public Organisateur(List<EvenementModel> evenementsOrganises, GestionEvenement gestionnaire) {
        this.evenementsOrganises = evenementsOrganises;
        this.gestionnaire = gestionnaire;
    }

    public void CreerEvt(EvenementModel evt) throws IOException, EvenementDejaExistantException {
        GestionEvenement.getInstance().ajouterEvenement(evt); //sauvegarde dnas le systeme de gestion global
        evenementsOrganises.add(evt); //enregistre dans la liste de ses evenements
    }

    public void annulerEvenement(String idEvenement) throws EvenementNonExistant, IOException, EvenementDejaExistantException {
        EvenementModel evenement = rechercherEvenementOrganise(idEvenement);
        evenement.annuler();
        GestionEvenement gestionnaire = GestionEvenement.getInstance();
        SaveEvtModif.getInstance().save(evenement);

        StringBuilder message = new StringBuilder();

        message.append("Événement : ").append(evenement.getNom()).append("\n")
                .append("Date prévue : ").append(evenement.getDate().format(DATE_FORMATTER)).append("\n")
                .append("Lieu : ").append(evenement.getLieu()).append("\n\n")
                .append("Nous regrettons de vous informer que cet événement a été annulé par l'organisateur.\n")
                .append("Si vous aviez effectué un paiement, un remboursement sera traité dans les plus brefs délais.\n\n");

        gestionnaire.notifierObservateurs(evenement, String.valueOf(message),"ÉVÉNEMENT ANNULÉ");
    }

    public void reporterEvenement(String idEvenement, LocalDateTime nouvelleDate) throws IOException, EvenementDejaExistantException {
        EvenementModel evenement = rechercherEvenementOrganise(idEvenement);
        LocalDate ancienne=evenement.getDate();
        evenement.setDate(LocalDate.from(nouvelleDate));
        SaveEvtModif.getInstance().save(evenement);

        StringBuilder message = new StringBuilder();

        message.append("Événement : ").append(evenement.getNom()).append("\n")
                .append("Lieu : ").append(evenement.getLieu()).append(" (inchangé)\n\n")
                .append("ANCIENNE DATE : ").append(ancienne.format(DATE_FORMATTER)).append("\n")
                .append("NOUVELLE DATE : ").append(nouvelleDate.format(DATE_FORMATTER)).append("\n\n")
                .append("Votre inscription reste valide pour la nouvelle date.\n")
                .append("Si vous ne pouvez pas y assister, vous pouvez vous désinscrire via le lien habituel.\n\n")
                .append("Organisateur : ").append(this.getNom());

        gestionnaire.notifierObservateurs(evenement, String.valueOf(message),"ÉVÉNEMENT REPORTÉ");
    }

    public void changerLieuEvenement(String idEvenement, String nouveauLieu) throws IOException, EvenementDejaExistantException {
        EvenementModel evenement = rechercherEvenementOrganise(idEvenement);
        String ancien=evenement.getLieu();
        evenement.setLieu(nouveauLieu);
        SaveEvtModif.getInstance().save(evenement);

        StringBuilder message = new StringBuilder();

        message.append("Événement : ").append(evenement.getNom()).append("\n")
                .append("Date : ").append(evenement.getDate().format(DATE_FORMATTER)).append(" (inchangée)\n\n")
                .append("ANCIEN LIEU : ").append(ancien).append("\n")
                .append("NOUVEAU LIEU : ").append(nouveauLieu).append("\n");

        gestionnaire.notifierObservateurs(evenement, String.valueOf(message),"CHANGEMENT DE LIEU");
    }

    public void modifierCapacite(String idEvenement, int nouvelleCapacite) throws IOException, EvenementDejaExistantException {
        EvenementModel evenement = rechercherEvenementOrganise(idEvenement);
        if (nouvelleCapacite >= evenement.getCapaciteMax()) {
            evenement.setCapaciteMax(nouvelleCapacite);
            SaveEvtModif.getInstance().save(evenement);
        }
    }

    private EvenementModel rechercherEvenementOrganise(String nom) {
        return this.evenementsOrganises.stream()
                .filter(e -> e.getNom().equals(nom))
                .findFirst()
                .orElseThrow(() -> new EvenementIntrouvableException("Événement non trouvé: " + nom));
    }

}
