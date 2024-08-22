package pet.project.repository;

import java.sql.SQLException;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

public interface CrudRepository {
    Optional<Currency> findById(Long id) throws SQLException;
    List<pet.project.entity.Currency> findAll() throws SQLException;
    void delete(Long id) throws SQLException;
}
