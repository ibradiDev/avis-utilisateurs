package ibradi.dev.avis.repository;

import ibradi.dev.avis.entity.Utilisateur;
import ibradi.dev.avis.entity.Validation;
import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.util.Optional;

public interface ValidationRepository extends CrudRepository<Validation, Integer> {

	Optional<Validation> findByCode(String code);

	Optional<Validation> findByUtilisateur(Utilisateur utilisateur);

	void deleteAllByExpirationBefore(Instant now);
}