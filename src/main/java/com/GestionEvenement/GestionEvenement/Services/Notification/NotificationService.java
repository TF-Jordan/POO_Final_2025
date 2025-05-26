package com.GestionEvenement.GestionEvenement.Services.Notification;


import java.util.concurrent.CompletableFuture;

public interface NotificationService {
    void envoyerNotification(String destinataire, String sujet, String message);
    CompletableFuture<Void> envoyerNotificationAsync(String destinataire, String sujet, String message);
}