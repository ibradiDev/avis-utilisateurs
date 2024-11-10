package ibradi.dev.avis.repository;

import ibradi.dev.avis.entity.Avis;
import org.springframework.data.repository.CrudRepository;

public interface AvisRepository extends CrudRepository<Avis, Integer> {
}