package pet.project.repository;

import java.util.Currency;
import java.util.Optional;

public class JdbcCurrencyRepository implements CurrencyRepository{

    @Override
    public Optional<Currency> findById(Long id) {
        return Optional.empty();
    }
}
