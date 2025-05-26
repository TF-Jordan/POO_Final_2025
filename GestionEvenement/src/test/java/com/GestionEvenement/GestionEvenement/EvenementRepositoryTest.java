package com.GestionEvenement.GestionEvenement;

import com.GestionEvenement.GestionEvenement.Exception.EvenementDejaExistantException;
import com.GestionEvenement.GestionEvenement.Models.ConferenceModel;
import com.GestionEvenement.GestionEvenement.Repository.Classes.EvenementRepository.EvenementRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EvenementRepositoryTest {

	private static EvenementRepository repository;
	private static final File testFile = new File("data/Evenements.json");

	@BeforeAll
	static void setup() throws IOException {
		// Créer le répertoire data s'il n'existe pas
		File dataDir = new File("data");
		if (!dataDir.exists()) {
			boolean created = dataDir.mkdirs();
			if (!created) {
				throw new RuntimeException("Impossible de créer le répertoire data");
			}
		}

		// Supprimer le fichier de test s'il existe déjà
		if (testFile.exists()) {
			testFile.delete();
		}

		// Créer un fichier vide pour éviter les erreurs
		boolean fileCreated = testFile.createNewFile();
		if (!fileCreated) {
			throw new RuntimeException("Impossible de créer le fichier de test");
		}

		// Initialiser le repository
		repository = new EvenementRepository();
	}

	@BeforeEach
	void setupEach() {
		// S'assurer que le fichier existe avant chaque test
		if (!testFile.exists()) {
			try {
				testFile.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException("Erreur lors de la création du fichier de test", e);
			}
		}
	}

	@Test
	@Order(1)
	void testSaveConference() throws IOException, EvenementDejaExistantException {
		ConferenceModel evt = new ConferenceModel();
		evt.setNom("Conférence IA");
		evt.setDate(LocalDate.from(LocalDateTime.of(2025, 6, 1, 18, 0)));
		evt.setLieu("Amphi 300");

		repository.save(evt);

		assertTrue(testFile.exists(), "Le fichier JSON doit être créé.");
		assertTrue(testFile.length() > 0, "Le fichier ne doit pas être vide.");
	}

	@Test
	@Order(2)
	void testLoadEvenements() {
		List<ConferenceModel> evenements = (List<ConferenceModel>)(List<?>) repository.load();

		assertNotNull(evenements, "La liste des événements ne doit pas être nulle.");
		assertFalse(evenements.isEmpty(), "Au moins un événement doit être chargé.");

		// Vérifications plus flexibles
		ConferenceModel premier = evenements.get(0);
		assertNotNull(premier.getId(), "L'ID de l'événement ne doit pas être nul.");
		assertEquals("Conférence IA", premier.getNom(), "Le nom doit correspondre.");
		assertEquals("Amphi 300", premier.getLieu(), "Le lieu doit correspondre.");
	}


	@AfterAll
	static void cleanup() {
		// Nettoyer le fichier de test
		if (testFile.exists()) {
			boolean deleted = testFile.delete();
			if (!deleted) {
				System.err.println("Attention: Impossible de supprimer le fichier de test");
			}
		}

		// Optionnel : supprimer aussi le répertoire s'il est vide
		File dataDir = new File("data");
		if (dataDir.exists() && dataDir.isDirectory()) {
			String[] files = dataDir.list();
			if (files != null && files.length == 0) {
				dataDir.delete();
			}
		}
	}
}