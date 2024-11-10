package ibradi.dev.avis.repository;

import ibradi.dev.avis.entity.Validation;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ValidationRepository extends CrudRepository<Validation, Integer> {

	Optional<Validation> findByCode(String code);
}