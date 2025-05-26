package com.GestionEvenement.GestionEvenement.controller;


import com.GestionEvenement.GestionEvenement.Exception.EvenementDejaExistantException;
import com.GestionEvenement.GestionEvenement.Exception.EvenementNonExistantException;
import com.GestionEvenement.GestionEvenement.Models.EvenementModel;
import com.GestionEvenement.GestionEvenement.Models.ParticipantModel;
import com.GestionEvenement.GestionEvenement.Services.GestionEvenement.GestionEvenement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/evenements")
@CrossOrigin(origins = "*")
@Slf4j
public class EvenementController {

    @Autowired
    private GestionEvenement gestionEvenement;

    @PostMapping
    public ResponseEntity<?> creerEvenement(@RequestBody EvenementModel evenement) {
        try {
            gestionEvenement.ajouterEvenement(evenement);
            return ResponseEntity.status(HttpStatus.CREATED).body(evenement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la création");
        }
    }

    @GetMapping
    public ResponseEntity<List<EvenementModel>> obtenirTousLesEvenements() {
        return ResponseEntity.ok(gestionEvenement.obtenirTousLesEvenements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenirEvenement(@PathVariable String id) {
        EvenementModel evenement = gestionEvenement.rechercherEvenementParId(id);
        if (evenement != null) {
            return ResponseEntity.ok(evenement);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/recherche/nom/{nom}")
    public ResponseEntity<?> rechercherParNom(@PathVariable String nom) {
        EvenementModel evenement = gestionEvenement.rechercherEvenementParNom(nom);
        if (evenement != null) {
            return ResponseEntity.ok(evenement);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<EvenementModel>> rechercherParType(@PathVariable String type) {
        return ResponseEntity.ok(gestionEvenement.rechercherEvenementsParType(type));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerEvenement(@PathVariable String id) {
        try {
            gestionEvenement.supprimerEvenement(id);
            return ResponseEntity.noContent().build();
        } catch (EvenementNonExistantException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/participants")
    public ResponseEntity<?> ajouterParticipant(@PathVariable String id, @RequestBody ParticipantModel participant) {
        try {
            EvenementModel evenement = gestionEvenement.rechercherEvenementParId(id);
            if (evenement == null) {
                return ResponseEntity.notFound().build();
            }

            boolean ajoute = evenement.ajouterParticipant(participant);
            if (ajoute) {
                gestionEvenement.ajouterEvenement(evenement); // Sauvegarde
                return ResponseEntity.ok("Participant ajouté avec succès");
            }
            return ResponseEntity.badRequest().body("Participant déjà inscrit");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'inscription");
        }
    }

    @DeleteMapping("/{id}/participants/{participantId}")
    public ResponseEntity<?> retirerParticipant(@PathVariable String id, @PathVariable String participantId) {
        EvenementModel evenement = gestionEvenement.rechercherEvenementParId(id);
        if (evenement == null) {
            return ResponseEntity.notFound().build();
        }

        boolean retire = evenement.retirerParticipant(participantId);
        if (retire) {
            try {
                gestionEvenement.ajouterEvenement(evenement); // Sauvegarde
            } catch (IOException e) {
                // Ignore car on met à jour
            } catch (EvenementDejaExistantException e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.ok("Participant retiré avec succès");
        }
        return ResponseEntity.badRequest().body("Participant non trouvé");
    }

    @PutMapping("/{id}/annuler")
    public ResponseEntity<?> annulerEvenement(@PathVariable String id) {
        EvenementModel evenement = gestionEvenement.rechercherEvenementParId(id);
        if (evenement == null) {
            return ResponseEntity.notFound().build();
        }

        evenement.annuler();
        try {
            gestionEvenement.ajouterEvenement(evenement); // Sauvegarde
        } catch (IOException | EvenementDejaExistantException e) {
            // Ignore car on met à jour
        }
        return ResponseEntity.ok("Événement annulé et participants notifiés");
    }
}
