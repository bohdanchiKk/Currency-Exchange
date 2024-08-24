package pet.project.repository;

import java.sql.SQLException;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    Optional<T> findById(Long id) throws SQLException;
    List<T> findAll() throws SQLException;
    Long save(T element) throws SQLException;
    void delete(Long id) throws SQLException;
}
