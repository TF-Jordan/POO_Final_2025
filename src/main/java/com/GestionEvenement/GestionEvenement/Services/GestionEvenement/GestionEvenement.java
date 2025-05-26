package com.GestionEvenement.GestionEvenement.Services.GestionEvenement;


import com.GestionEvenement.GestionEvenement.Exception.EvenementDejaExistantException;
import com.GestionEvenement.GestionEvenement.Exception.EvenementNonExistantException;
import com.GestionEvenement.GestionEvenement.Exception.ParticipantDejaExistantException;
import com.GestionEvenement.GestionEvenement.Models.EvenementModel;
import com.GestionEvenement.GestionEvenement.Models.ParticipantModel;
import com.GestionEvenement.GestionEvenement.Repository.Classes.EvenementRepository.EvenementRepository;
import com.GestionEvenement.GestionEvenement.Repository.Classes.load.LoadParticipant;
import com.GestionEvenement.GestionEvenement.Repository.Classes.save.SaveParticipant;
import com.GestionEvenement.GestionEvenement.Services.Notification.EmailNotificationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Data
@Slf4j
@Component
@AllArgsConstructor
@NoArgsConstructor
public class GestionEvenement {  //singleton
    private static GestionEvenement instance;
    private Map<String, EvenementModel> evenements = new HashMap<>();

    @Autowired
    private EvenementRepository evenementRepository;
    @Autowired
    private EmailNotificationService emailService;

    public void ajouterEvenement(EvenementModel evenement) throws EvenementDejaExistantException, IOException {
        if (evenements.containsKey(evenement.getId())) {
            throw new EvenementDejaExistantException("L'événement existe déjà !");
        }

        evenements.put(evenement.getId(), evenement);
        evenementRepository.save(evenement);
        log.info("Événement ajouté: {}", evenement.getNom());
    }

    public void supprimerEvenement(String id) throws EvenementNonExistantException {
        if (!evenements.containsKey(id)) {
            throw new EvenementNonExistantException("Cet événement n'existe pas !");
        }

        EvenementModel evenement = evenements.get(id);
        evenements.remove(id);
        evenementRepository.delete(id);
        log.info("Événement supprimé: {}", evenement.getNom());
    }
    public void supprimerParticipant(String id) throws EvenementNonExistantException, ParticipantDejaExistantException {
        List<ParticipantModel> participants=LoadParticipant.getInstance().load();
        Map<String ,ParticipantModel> p=participants.stream().collect(Collectors.toMap(ParticipantModel::getId, obj->obj));
        if(!p.containsKey(id)){
            throw new EvenementNonExistantException("Ce participant n'existe pas");
        }else {
            p.remove(id);
            p.forEach((key,value)-> {
                try {
                    SaveParticipant.getInstance().save(value);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ParticipantDejaExistantException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        EvenementModel evenement = evenements.get(id);
        evenements.remove(id);
        evenementRepository.delete(id);
        log.info("Événement supprimé: {}", evenement.getNom());
    }

    public EvenementModel rechercherEvenementParNom(String nom) {
        return evenements.values().stream()
                .filter(e -> e.getNom().equalsIgnoreCase(nom))
                .findFirst()
                .orElse(null);
    }

    public EvenementModel rechercherEvenementParId(String id) {
        return evenements.get(id);
    }

    public List<EvenementModel> rechercherEvenementsParType(String type) {
        return evenements.values().stream()
                .filter(e -> e.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public List<EvenementModel> obtenirTousLesEvenements() {
        return new ArrayList<>(evenements.values());
    }

    public void chargerEvenements() {
        List<EvenementModel> evenementsCharges = evenementRepository.load();
        evenements.clear();
        evenementsCharges.forEach(e -> evenements.put(e.getId(), e));
        log.info("Chargés {} événements", evenements.size());
    }


    public void notifier(EvenementModel evenement, String message, String sujet) {
        notifierObservateurs(evenement, message, sujet);
    }

    public void notifierObservateurs(EvenementModel evenement, String message, String sujet) {
        List<CompletableFuture<Void>> futures = evenement.getParticipansInscrits().stream()
                .map(participant -> emailService.envoyerNotificationAsync(
                        participant.getEmail(), sujet, message))
                .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .exceptionally(ex -> {
                    log.error("Erreur lors de l'envoi des notifications: {}", ex.getMessage());
                    return null;
                });
    }

    public static synchronized GestionEvenement getInstance() {
        if (instance == null) {
            instance = new GestionEvenement();
        }
        return instance;
    }


}
