package ibradi.dev.avis.repository;

import ibradi.dev.avis.entity.Jwt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.stream.Stream;

public interface JwtRepository extends CrudRepository<Jwt, Integer> {
	Optional<Jwt> findByValue(String token);

	@Query("FROM Jwt j WHERE j.utilisateur.email = :email AND j.deactivated = :deactivated AND j.expired = :expired")
	Optional<Jwt> findUtilisateurValidToken(String email, boolean deactivated, boolean expired);

	@Query("FROM Jwt j WHERE j.utilisateur.email = :email")
	Stream<Jwt> findAllByUtilisateur(String email);

	void deleteAllByExpiredAndDeactivated(boolean expired, boolean deactivated);
}