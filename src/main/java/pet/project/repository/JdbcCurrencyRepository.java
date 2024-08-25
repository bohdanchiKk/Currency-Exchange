package pet.project.repository;

import lombok.RequiredArgsConstructor;
import pet.project.config.MyDataSource;
import pet.project.entity.Currency;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcCurrencyRepository implements CurrencyRepository {
    private final DataSource dataSource = MyDataSource.getInstance();

    @Override
    public Optional<Currency> findById(Long id) throws SQLException {
        final String sql = "select * from currencies where currencies.id = ?";
        try (Connection connection = MyDataSource.getInstance().getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,id);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()){
                return Optional.of(new pet.project.entity.Currency(
                        resultSet.getString("code"),
                        resultSet.getString("fullName"),
                        resultSet.getString("sign")));
            }
        }
        return Optional.empty();
    }

    @Override
    public List findAll() throws SQLException {
        final String sql = "select * from currencies";
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            List<pet.project.entity.Currency> currencyList = new ArrayList<>();
            while (resultSet.next()){
                currencyList.add(new pet.project.entity.Currency(
                        resultSet.getLong("id"),
                        resultSet.getString("code"),
                        resultSet.getString("fullName"),
                        resultSet.getString("sign")
                ));
            }
            return currencyList;
        }
    }

    @Override
    public Long save(Currency currency) throws SQLException {
        final String sql = "insert into currencies (code, fullName, sign) values (?,?,?)";

        try (Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1,currency.getCode());
            preparedStatement.setString(2,currency.getFullName());
            preparedStatement.setString(3,currency.getSign());

            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            long savedId = resultSet.getLong("id");

            connection.commit();
            return savedId;
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        final String sql = "delete * from currencies where currencies.id = ?";

        try (Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,id);
            preparedStatement.execute();


        }
    }

    @Override
    public Optional<Currency> findByCode(String code) throws SQLException {
        final String sql = "select * from currencies where currencies.code = ?";

        try (Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,code);
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()){
                return Optional.of(new Currency(
                        resultSet.getLong("id"),
                        resultSet.getString("code"),
                        resultSet.getString("fullname"),
                        resultSet.getString("sign")
                ));
            }
            return Optional.empty();
        }
    }
}

