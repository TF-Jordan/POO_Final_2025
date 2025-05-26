package com.GestionEvenement.GestionEvenement.Services.Notification;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;


@Service
@Slf4j
public class EmailNotificationService implements NotificationService{

    private final String senderEmail = "jordantoupi@gmail.com";
    private final String senderPassword = "@@22p278@@";

    /**
     * Envoie un e-mail de notification à un destinataire.
     *
     * @param destinataire L'adresse email du participant à notifier.
     * @param sujet Le sujet de l'e-mail.
     * @param message Le contenu du message à envoyer.
     * @throws MessagingException si l'envoi échoue.
     */
    public void envoyerEmail(String destinataire, String sujet, String message) throws MessagingException {
        // Configuration des propriétés SMTP pour Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");                     // Authentification activée
        props.put("mail.smtp.starttls.enable", "true");          // TLS activé
        props.put("mail.smtp.host", "smtp.gmail.com");           // Serveur SMTP de Gmail
        props.put("mail.smtp.port", "587");                      // Port TLS

        // Création d'une session avec authentification
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        // Création de l'e-mail
        Message email = new MimeMessage(session);
        email.setFrom(new InternetAddress(senderEmail));         // Expéditeur
        email.setRecipient(Message.RecipientType.TO, new InternetAddress(destinataire)); // Destinataire
        email.setSubject(sujet);                                  // Sujet
        email.setText(message);                                   // Corps du message

        // Envoi de l’e-mail
        Transport.send(email);
    }


    @Override
    public void envoyerNotification(String destinataire, String sujet, String message) {
        // Simulation d'envoi d'email
        log.info("Email envoyé à: {}", destinataire);
        log.info("Sujet: {}", sujet);
        log.info("Message: {}", message);

        // Simulation du temps d'envoi
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public CompletableFuture<Void> envoyerNotificationAsync(String destinataire, String sujet, String message) {
        return CompletableFuture.runAsync(() -> {
            envoyerNotification(destinataire, sujet, message);
        });
    }
}
