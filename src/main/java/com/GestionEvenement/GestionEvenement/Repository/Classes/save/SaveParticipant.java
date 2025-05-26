package com.GestionEvenement.GestionEvenement.Repository.Classes.save;

import com.GestionEvenement.GestionEvenement.Exception.ParticipantDejaExistantException;
import com.GestionEvenement.GestionEvenement.Models.ParticipantModel;
import com.GestionEvenement.GestionEvenement.Repository.Interfaces.Serialisation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class SaveParticipant implements Serialisation {
    private static SaveParticipant instance;
    private  ObjectMapper mapper=new ObjectMapper();
    private  final File fichier=new File("data/Participants.json");

    @Override
    public void  save(Object obj) throws IOException, ParticipantDejaExistantException {
        ParticipantModel p= (ParticipantModel) obj;

        //lecture des données existante
        List<ParticipantModel> participants;
        if(fichier.exists()&&fichier.length() !=0){
            if (!fichier.getParentFile().exists()) {
                fichier.getParentFile().mkdirs();
            }

            participants=mapper.readValue(fichier, new TypeReference<List<ParticipantModel>>() {});
        }else{
            participants=new ArrayList<>();
        }

        //ajout du nouvel element
        if(participants.contains(p)){
            throw new ParticipantDejaExistantException("Ce participant existe deja");
        }else{
            participants.add(p);
        }
        mapper.writeValue(fichier,participants);
    }
    public static synchronized SaveParticipant getInstance() {
        if (instance == null) {
            instance = new SaveParticipant();
        }
        return instance;
    }

}

