package com.GestionEvenement.GestionEvenement.Repository.Interfaces;


import java.util.List;

public interface Deserialisation<T> {
    public List<T> load();
}
