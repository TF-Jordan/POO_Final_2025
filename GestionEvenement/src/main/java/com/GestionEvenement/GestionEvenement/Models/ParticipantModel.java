package com.GestionEvenement.GestionEvenement.Models;


import com.GestionEvenement.GestionEvenement.Utils.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantModel   {
    private String id;
    private String nom;
    String motDePasse;
    private String email;
    private Role role;

    public ParticipantModel(String id, String email, String motDePasse, String nom) {
        this.id = id;
        this.role = Role.PARTICIPANT;
        this.email = email;
        this.motDePasse = motDePasse;
        this.nom = nom;
    }


    public Role getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantModel that = (ParticipantModel) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
