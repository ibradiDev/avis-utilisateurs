package ibradi.dev.avis.service;

import ibradi.dev.avis.entity.Utilisateur;
import ibradi.dev.avis.entity.Validation;
import ibradi.dev.avis.repository.ValidationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MINUTES;

@Transactional
@Slf4j
@AllArgsConstructor
@Service
public class ValidationService {
	private ValidationRepository validationRepository;
	private NotificationService notificationService;


	public void enregister(Utilisateur utilisateur) {
		Validation validation = validationRepository
				.findByUtilisateur(utilisateur).orElse(new Validation());
//		Si l'utilisateur existe déja, on met à jour sa date d'activation
		if (validation.getId() >= 1)
			validation.setActivation(Instant.now());
		else
			validation.setUtilisateur(utilisateur);

		Instant creation = Instant.now();
		validation.setCreation(creation);

		Instant expiration = creation.plus(10, MINUTES);
		validation.setExpiration(expiration);

		Random random = new Random();
		int randomInteger = random.nextInt(999999);
		String code = String.format("%06d", randomInteger);
		validation.setCode(code);

		validationRepository.save(validation);
		notificationService.envoyer(validation);
	}

	public Validation lireEnFonctionDuCode(String code) {
		return validationRepository
				.findByCode(code)
				.orElseThrow(() -> new RuntimeException("Code d'activation invalide"));
	}

	@Scheduled(cron = "0 */1 * * * *")
	public void cleanTable() {
		Instant now = Instant.now();
		log.info("Nettoyage de la table :: {}", now);
		validationRepository.deleteAllByExpirationBefore(now);
	}
}