package pet.project.repository;

import java.util.Currency;
import java.util.Optional;

public interface CrudRepository {
    Optional<Currency> findById(Long id);
}
