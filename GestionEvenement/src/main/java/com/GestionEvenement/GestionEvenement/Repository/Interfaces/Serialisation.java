package com.GestionEvenement.GestionEvenement.Repository.Interfaces;

import com.GestionEvenement.GestionEvenement.Exception.EvenementDejaExistantException;
import com.GestionEvenement.GestionEvenement.Exception.ParticipantDejaExistantException;

import java.io.IOException;

public interface Serialisation {
    public void  save(Object obj) throws IOException, ParticipantDejaExistantException, EvenementDejaExistantException;
}

