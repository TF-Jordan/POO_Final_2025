package com.GestionEvenement.GestionEvenement.Utils;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class MatriculeGenerator {
    private static final Map<String, AtomicInteger> compteurParDate = new HashMap<>();
    private static MatriculeGenerator instance;

    public String genererMatricule() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        AtomicInteger compteur = compteurParDate.computeIfAbsent(date, k -> new AtomicInteger(0));
        int numero = compteur.incrementAndGet();

        return String.format("EVT-%s-%03d", date, numero);
    }

    public static synchronized MatriculeGenerator getInstance() {
        if (instance == null) {
            instance = new MatriculeGenerator();
        }
        return instance;
    }
}

