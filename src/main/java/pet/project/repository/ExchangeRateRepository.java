package pet.project.repository;

import pet.project.entity.ExchangeRate;

import java.sql.SQLException;
import java.util.Optional;

public interface ExchangeRateRepository extends CrudRepository<ExchangeRate> {
    Optional<ExchangeRate> findByCode(String baseCurrencyCode, String targetCurrencyCode) throws SQLException;
}
