package com.GestionEvenement.GestionEvenement.Repository.Classes.load;


import com.GestionEvenement.GestionEvenement.Models.ParticipantModel;
import com.GestionEvenement.GestionEvenement.Repository.Classes.save.SaveEvtModif;
import com.GestionEvenement.GestionEvenement.Repository.Interfaces.Deserialisation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadParticipant  implements Deserialisation<ParticipantModel> {
    private static LoadParticipant instance;
    private ObjectMapper mapper;
    private final File fichier=new File("data/Participants.json");

    @Override
    public List<ParticipantModel> load() {
        List<ParticipantModel> ls=new ArrayList<>();
        try{
            if(fichier.exists()&&fichier.length()!=0){
                ls=mapper.readValue(fichier, new TypeReference<List<ParticipantModel>>() {});
            }
        }catch (IOException e){
            System.err.println("Erreur lors du chargement des participants: "+e.getMessage());
            e.printStackTrace();
        }
        return ls;
    }
    public static synchronized LoadParticipant getInstance() {
        if (instance == null) {
            instance = new LoadParticipant();
        }
        return instance;
    }
}
