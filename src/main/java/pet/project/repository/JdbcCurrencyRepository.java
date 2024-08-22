package pet.project.repository;

import lombok.RequiredArgsConstructor;
import pet.project.config.MyDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcCurrencyRepository implements CurrencyRepository{
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
                Optional.of(new pet.project.entity.Currency(
                        resultSet.getLong("id"),
                        resultSet.getString("code"),
                        resultSet.getString("fullName"),
                        resultSet.getString("sign")));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<pet.project.entity.Currency> findAll() throws SQLException {
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
    public void delete(Long id) throws SQLException {
        final String sql = "delete * from currencies where currencies.id = ?";

        try (Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,id);
            preparedStatement.execute();


        }
    }
}

