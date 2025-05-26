package com.GestionEvenement.GestionEvenement.controller;

import com.GestionEvenement.GestionEvenement.Exception.ParticipantDejaExistantException;
import com.GestionEvenement.GestionEvenement.Models.ParticipantModel;
import com.GestionEvenement.GestionEvenement.Repository.Classes.save.SaveParticipant;
import com.GestionEvenement.GestionEvenement.Services.GestionEvenement.GestionEvenement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/participants")
public class ParticipantControler {

    @PostMapping("/inscription")
    public ResponseEntity<?> inscriptionParticipant(@RequestBody ParticipantModel participantModel) throws ParticipantDejaExistantException, IOException {
        System.out.println(participantModel);
        SaveParticipant.getInstance().save(participantModel);
        return ResponseEntity.ok("gevs");
    }

    @DeleteMapping("/desinscription")
    public ResponseEntity<?> desinscrireParticipant(@RequestBody String id) throws ParticipantDejaExistantException {
        GestionEvenement.getInstance().supprimerParticipant(id);
        return ResponseEntity.ok("Participant Suprimé avec succes");
    }
}
