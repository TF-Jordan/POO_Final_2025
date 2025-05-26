package com.GestionEvenement.GestionEvenement.Repository.Classes.load;

import com.GestionEvenement.GestionEvenement.Models.Intervenant;
import com.GestionEvenement.GestionEvenement.Repository.Interfaces.Deserialisation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadInt implements Deserialisation<Intervenant> {
    private LoadInt instance;
    private ObjectMapper mapper;
    private final File fichier=new File("data/Participants.json");

    @Override
    public List<Intervenant> load() {
        List<Intervenant> ls=new ArrayList<>();
        try{
            if(fichier.exists()&&fichier.length()!=0){
                ls=mapper.readValue(fichier, new TypeReference<List<Intervenant>>() {});
            }
        }catch (IOException e){
            System.err.println("Erreur lors du chargement des participants: "+e.getMessage());
            e.printStackTrace();
        }
        return ls;
    }
}
