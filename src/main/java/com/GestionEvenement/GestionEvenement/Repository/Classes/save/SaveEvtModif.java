package com.GestionEvenement.GestionEvenement.Repository.Classes.save;

import com.GestionEvenement.GestionEvenement.Exception.EvenementDejaExistantException;
import com.GestionEvenement.GestionEvenement.Models.EvenementModel;
import com.GestionEvenement.GestionEvenement.Repository.Interfaces.Serialisation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SaveEvtModif implements Serialisation {
    private static SaveEvtModif instance;
    private ObjectMapper mapper;
    private  final File fichier=new File("data/Evenements.json");
    @Override
    public void save(Object obj) throws IOException, EvenementDejaExistantException {
        EvenementModel evt= (EvenementModel) obj;

        //lecture des données existante
        List<EvenementModel> evenements;
        if(fichier.exists()&&fichier.length() !=0){
            if (!fichier.getParentFile().exists()) {
                fichier.getParentFile().mkdirs();
            }
            evenements=mapper.readValue(fichier, new TypeReference<List<EvenementModel>>() {});
        }else{
            evenements=new ArrayList<>();
        }

        //remplace l'evenement par sa version modifiée
        evenements=evenements.stream().map(p->p.equals(evt) ? evt: p).collect(Collectors.toList());


        mapper.writeValue(fichier,evenements);
    }

    public static synchronized SaveEvtModif getInstance() {
        if (instance == null) {
            instance = new SaveEvtModif();
        }
        return instance;
    }
}
