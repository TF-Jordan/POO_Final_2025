package com.GestionEvenement.GestionEvenement.controller;

import com.GestionEvenement.GestionEvenement.Exception.ParticipantDejaExistantException;
import com.GestionEvenement.GestionEvenement.Models.ParticipantModel;
import com.GestionEvenement.GestionEvenement.Repository.Classes.save.SaveParticipant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
