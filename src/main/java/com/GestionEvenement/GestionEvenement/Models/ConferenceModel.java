package com.GestionEvenement.GestionEvenement.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ConferenceModel extends EvenementModel{
    private String theme;
    private List<Intervenant> intervenants;

    @Override
    public String getType() {
        return "conference";
    }


    @Override
    public void afficherDetails() {
        super.afficherDetails();
        StringBuilder sb = new StringBuilder();
        sb.append("Type         : Conférence\n");
        sb.append(String.format("Thème        : %s\n", theme));
        sb.append("Intervenants :\n");

        if (intervenants.isEmpty()) {
            sb.append("   - Aucun intervenant prévu.\n");
        } else {
            for (Intervenant i : intervenants) {
                sb.append(String.format("   - %s %s (%s)\n", i.getPrenom(), i.getNom(), i.getSpecialite()));
            }
        }
        System.out.println(sb.toString());
    }

}
