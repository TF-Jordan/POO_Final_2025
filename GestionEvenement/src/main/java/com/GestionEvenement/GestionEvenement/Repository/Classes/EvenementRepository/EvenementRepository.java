package com.GestionEvenement.GestionEvenement.Repository.Classes.EvenementRepository;


import com.GestionEvenement.GestionEvenement.Exception.EvenementDejaExistantException;
import com.GestionEvenement.GestionEvenement.Models.EvenementModel;
import com.GestionEvenement.GestionEvenement.Repository.Interfaces.Deserialisation;
import com.GestionEvenement.GestionEvenement.Repository.Interfaces.Serialisation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EvenementRepository implements Serialisation, Deserialisation {
    private ObjectMapper mapper=new ObjectMapper();
    private  final File fichier=new File("data/Evenements.json");



    @Override
    public void save(Object obj) throws IOException, EvenementDejaExistantException {
        EvenementModel evt= (EvenementModel) obj;
        mapper.registerModule(new JavaTimeModule());
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

        //ajout du nouvel element
        if(evenements.contains(evt)){
            throw new EvenementDejaExistantException("Cet evenement existe deja");
        }else{
            evenements.add(evt);
        }
        mapper.writeValue(fichier,evenements);
    }

    @Override
    public List<EvenementModel> load() {
        List<EvenementModel> ls=new ArrayList<>();
        try{
            if(fichier.exists()&&fichier.length()!=0){
                ls= mapper.readValue(fichier, new TypeReference<List<EvenementModel>>() {});
            }
        }catch (IOException e){
            System.err.println("Erreur lors du chargement des evenement: "+e.getMessage());
            e.printStackTrace();
        }
        return ls;
    }

    public void delete(String id) {
        List<EvenementModel> ls=new ArrayList<>();
        try{
            if(fichier.exists()&&fichier.length()!=0){
                ls= mapper.readValue(fichier, new TypeReference<List<EvenementModel>>() {});
                ls.remove(id);
                for(EvenementModel e:ls){
                    this.save(e);
                }
            }
        }catch (IOException | EvenementDejaExistantException e){
            System.err.println("Erreur lors du chargement des evenement: "+e.getMessage());
            e.printStackTrace();
        }
    }


}
