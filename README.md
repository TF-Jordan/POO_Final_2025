# Système de Gestion d'Événements Distribué

## 📋 Description du Projet

Ce projet implémente un système complet de gestion d'événements utilisant les concepts avancés de la Programmation Orientée Objet (POO). Le système permet de gérer différents types d'événements (concerts, conférences), l'inscription des participants, et la notification en temps réel.

## 🏗️ Architecture du Projet

### Modèles de Données

#### Hiérarchie des Événements
- **EvenementModel** (classe abstraite) : Classe de base pour tous les événements
  - **ConcertModel** : Événements musicaux avec artiste, genre musical et prix
  - **ConferenceModel** : Conférences avec thème et liste d'intervenants

#### Gestion des Utilisateurs
- **ParticipantModel** : Classe de base pour les utilisateurs
- **Organisateur** : Hérite de ParticipantModel, peut créer et gérer des événements
- **Intervenant** : Spécialistes participant aux conférences

### Patterns de Conception Implémentés

#### 1. **Singleton Pattern**
- `GestionEvenement` : Instance unique pour la gestion globale des événements
- `SaveEvtModif`, `SaveParticipant` : Instances uniques pour la persistance

#### 2. **Observer Pattern**
- Système de notifications automatiques aux participants
- Notifications asynchrones via `CompletableFuture`

#### 3. **Strategy Pattern**
- Interface `Serialisation` et `Deserialisation` pour différentes stratégies de persistance

### Services

#### GestionEvenement (Service Principal)
```java
// Fonctionnalités principales
- ajouterEvenement()
- supprimerEvenement()
- rechercherEvenementParNom/ParId/ParType()
- notifierObservateurs()
```

#### Système de Notification
- Notifications email asynchrones
- Alertes pour annulation, report, changement de lieu

## 🚀 Fonctionnalités

### Gestion des Événements
- ✅ Création d'événements (Concerts, Conférences)
- ✅ Modification des détails (date, lieu, capacité)
- ✅ Annulation avec notifications automatiques
- ✅ Recherche par nom, ID ou type

### Gestion des Participants
- ✅ Inscription/désinscription
- ✅ Gestion de la capacité maximale
- ✅ Notifications automatiques

### Persistance des Données
- ✅ Sérialisation JSON avec Jackson
- ✅ Sauvegarde automatique des modifications
- ✅ Chargement au démarrage

### Notifications
- ✅ Système Observer pour les notifications temps réel
- ✅ Envoi asynchrone d'emails
- ✅ Templates de messages personnalisés

## 🛠️ Technologies Utilisées

- **Java 11+** : Langage principal
- **Spring Boot** : Framework d'application
- **Jackson** : Sérialisation/Désérialisation JSON
- **Lombok** : Réduction du code boilerplate
- **SLF4J** : Logging
- **CompletableFuture** : Programmation asynchrone

## 📁 Structure du Projet

```
src/main/java/com/GestionEvenement/
├── Models/
│   ├── EvenementModel.java          # Classe abstraite de base
│   ├── ConcertModel.java            # Événements musicaux
│   ├── ConferenceModel.java         # Conférences
│   ├── ParticipantModel.java        # Utilisateurs de base
│   ├── Organisateur.java            # Gestionnaires d'événements
│   └── Intervenant.java             # Spécialistes conférences
├── Services/
│   └── GestionEvenement/
│       └── GestionEvenement.java    # Service principal (Singleton)
├── Repository/
│   ├── Interfaces/
│   │   ├── Serialisation.java       # Interface de sauvegarde
│   │   └── Deserialisation.java     # Interface de chargement
│   └── Classes/
│       ├── EvenementRepository.java # Persistance événements
│       ├── save/                    # Classes de sauvegarde
│       └── load/                    # Classes de chargement
├── Exception/                       # Exceptions personnalisées
├── Controller/                      # API REST
└── Utils/                          # Utilitaires
```

## 🔧 Installation et Utilisation

### Prérequis
- Java 11 ou supérieur
- Maven 3.6+
- Spring Boot 2.x

### Installation

1. **Cloner le repository**
```bash
git clone [URL_DU_REPOSITORY]
cd GestionEvenement
```

2. **Compiler le projet**
```bash
mvn clean compile
```

3. **Lancer l'application**
```bash
mvn spring-boot:run
```

### Utilisation

#### Création d'un Concert
```java
ConcertModel concert = new ConcertModel(
    "ORG001",              // ID organisateur
    new ArrayList<>(),      // Participants
    500,                   // Capacité max
    false,                 // Non annulé
    "Palais des Sports",   // Lieu
    LocalDate.of(2025, 6, 15), // Date
    "Concert Rock 2025",   // Nom
    "The Rockers",         // Artiste
    "Rock",                // Genre
    45.0                   // Prix
);
```

#### Création d'une Conférence
```java
ConferenceModel conference = new ConferenceModel(
    "CONF001",             // ID
    "Intelligence Artificielle", // Thème
    Arrays.asList(         // Intervenants
        new Intervenant("I001", "Dupont", "Jean", "jean@email.com", "IA")
    )
);
```

### API Endpoints

#### Participants
- `POST /api/participants/inscription` : Inscription d'un participant

## 🧪 Gestion des Exceptions

### Exceptions Personnalisées Implémentées
- `EvenementDejaExistantException` : Tentative d'ajout d'un événement existant
- `EvenementIntrouvableException` : Recherche d'un événement inexistant
- `ParticipantDejaExistantException` : Inscription d'un participant déjà existant
- `CapaciteMaxAtteinteException` : Dépassement de la capacité maximale

### Exemple de Gestion
```java
try {
    evenement.ajouterParticipant(participant);
} catch (CapaciteMaxAtteinteException e) {
    log.error("Capacité maximale atteinte pour l'événement: {}", evenement.getNom());
    // Traitement approprié
}
```

## 📊 Système de Notifications

### Types de Notifications Automatiques
1. **Annulation d'événement**
2. **Report de date**
3. **Changement de lieu**
4. **Modification de capacité**

### Implémentation Observer
```java
public void notifierObservateurs(EvenementModel evenement, String message, String sujet) {
    List<CompletableFuture<Void>> futures = evenement.getParticipansInscrits().stream()
        .map(participant -> emailService.envoyerNotificationAsync(
            participant.getEmail(), sujet, message))
        .collect(Collectors.toList());
    
    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
        .exceptionally(ex -> {
            log.error("Erreur lors de l'envoi des notifications: {}", ex.getMessage());
            return null;
        });
}
```

## 💾 Persistance des Données

### Format de Stockage
- **JSON** : Format principal avec Jackson
- **Fichiers** : Stockage dans le dossier `data/`
  - `Evenements.json` : Liste des événements
  - `Participants.json` : Liste des participants
  - `IntervenantAuxConferences.json` : Intervenants des conférences

### Exemple de Structure JSON
```json
{
  "type": "concert",
  "id": "EVT001",
  "nom": "Concert Rock 2025",
  "date": "2025-06-15",
  "lieu": "Palais des Sports",
  "capaciteMax": 500,
  "artiste": "The Rockers",
  "genreMusical": "Rock",
  "prixBillet": 45.0
}
```

## 🔍 Recherche et Filtrage

### Méthodes de Recherche Disponibles
- Par nom d'événement
- Par ID d'événement
- Par type d'événement (concert/conférence)
- Liste complète des événements

### Utilisation des Streams Java
```java
public List<EvenementModel> rechercherEvenementsParType(String type) {
    return evenements.values().stream()
        .filter(e -> e.getType().equalsIgnoreCase(type))
        .collect(Collectors.toList());
}
```

## 🚀 Fonctionnalités Avancées

### Programmation Asynchrone
- Notifications email en arrière-plan
- Utilisation de `CompletableFuture` pour les performances

### Polymorphisme
- Méthode `afficherDetails()` redéfinie dans chaque type d'événement
- Gestion uniforme des différents types d'événements

## 📈 Évolutions Futures

### Améliorations Prévues
- [ ] Tests unitaires avec JUnit (couverture 70%+)
- [ ] Interface utilisateur web
- [ ] Base de données relationnelle
- [ ] Authentification et autorisation
- [ ] API REST complète
- [ ] Système de paiement intégré

### Optimisations Techniques
- [ ] Cache Redis pour les performances
- [ ] Monitoring et métriques
- [ ] Documentation OpenAPI/Swagger
- [ ] Docker containerization

## 🐛 Problèmes Connus

1. **Initialisation ObjectMapper** : Dans certaines classes, l'ObjectMapper n'est pas initialisé
2. **Gestion des dates** : Inconsistance entre LocalDate et LocalDateTime
3. **Validation des données** : Manque de validation d'entrée

## 👥 Contribution

Ce projet a été développé dans le cadre du TP#3 - POO. 

### Guidelines de Développement
- Respecter les principes SOLID
- Commenter le code complexe
- Utiliser les design patterns appropriés
- Gérer les exceptions de manière appropriée

## 📄 Licence

Projet académique - Tous droits réservés.

---

**Auteur** : [Votre Nom]  
**Date** : Mai 2025  
**Version** : 1.0.0
