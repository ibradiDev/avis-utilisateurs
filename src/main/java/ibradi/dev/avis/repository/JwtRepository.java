package ibradi.dev.avis.repository;

import ibradi.dev.avis.entity.Avis;
import ibradi.dev.avis.entity.Jwt;
import org.springframework.data.repository.CrudRepository;

public interface JwtRepository extends CrudRepository<Jwt, Integer> {
}