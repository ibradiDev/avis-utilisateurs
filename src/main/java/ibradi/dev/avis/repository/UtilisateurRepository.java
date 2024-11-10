package ibradi.dev.avis.repository;

import ibradi.dev.avis.entity.Utilisateur;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Integer> {
	Optional<Utilisateur> findByEmail(String email);

}