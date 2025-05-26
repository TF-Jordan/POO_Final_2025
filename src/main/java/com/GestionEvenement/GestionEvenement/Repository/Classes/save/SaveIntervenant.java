package com.GestionEvenement.GestionEvenement.Repository.Classes.save;

import com.GestionEvenement.GestionEvenement.Exception.EvenementDejaExistantException;
import com.GestionEvenement.GestionEvenement.Exception.ParticipantDejaExistantException;
import com.GestionEvenement.GestionEvenement.Models.Intervenant;
import com.GestionEvenement.GestionEvenement.Repository.Interfaces.Serialisation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@Data
public class SaveIntervenant implements Serialisation {
    private SaveIntervenant instance;
    private  ObjectMapper mapper;
    private  final File fichier=new File("data/IntervenantAuxConferences.json");
    @Override
    public void  save(Object obj) throws IOException, EvenementDejaExistantException, ParticipantDejaExistantException {
        Intervenant i= (Intervenant) obj;

        //lecture des données existante
        List<Intervenant> intervenants;
        if(fichier.exists()&&fichier.length() !=0){
            if (!fichier.getParentFile().exists()) {
                fichier.getParentFile().mkdirs();
            }
            intervenants=mapper.readValue(fichier, new TypeReference<List<Intervenant>>() {});
        }else{
            intervenants=new ArrayList<>();
        }

        //ajout du nouvel element
        if(intervenants.contains(i)){
            throw new ParticipantDejaExistantException("Ce participant existe deja");
        }else{
            intervenants.add(i);
        }
        mapper.writeValue(fichier,intervenants);
    }


}
