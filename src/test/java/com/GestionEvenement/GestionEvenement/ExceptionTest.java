package com.GestionEvenement.GestionEvenement;

import com.GestionEvenement.GestionEvenement.Exception.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExceptionTest {

    @Test
    void testCapaciteMaxAtteinteException() {
        String msg = "Capacité maximale atteinte";
        CapaciteMaxAtteinteException exception = new CapaciteMaxAtteinteException(msg);
        assertEquals(msg, exception.getMessage());
    }

    @Test
    void testEvenementDejaExistantException() {
        String msg = "Événement déjà existant";
        EvenementDejaExistantException exception = new EvenementDejaExistantException(msg);
        assertEquals(msg, exception.getMessage());
    }

    @Test
    void testEvenementIntrouvableException() {
        String msg = "Événement introuvable";
        EvenementIntrouvableException exception = new EvenementIntrouvableException(msg);
        assertEquals(msg, exception.getMessage());
    }

    @Test
    void testEvenementNonExistantException() {
        String msg = "Événement non existant";
        EvenementNonExistantException exception = new EvenementNonExistantException(msg);
        assertEquals(msg, exception.getMessage());
    }

    @Test
    void testParticipantDejaExistantException() {
        String msg = "Participant déjà existant";
        ParticipantDejaExistantException exception = new ParticipantDejaExistantException(msg);
        assertEquals(msg, exception.getMessage());
    }

    @Test
    void testParticipantNonExistantException() {
        String msg = "Participant non existant";
        ParticipantNonExistantException exception = new ParticipantNonExistantException(msg);
        assertEquals(msg, exception.getMessage());
    }
}
