package pet.project.repository;

import pet.project.config.MyDataSource;
import pet.project.entity.Currency;
import pet.project.entity.ExchangeRate;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcExchangeRateRepository implements ExchangeRateRepository{
    private final DataSource dataSource = MyDataSource.getInstance();

    @Override
    public Optional<ExchangeRate> findById(Long id) throws SQLException {
        final String sql = "select\n" +
                "                ex.id as id,\n" +
                "                ex.rate as rate,\n" +
                "                bc.id as base_id,\n" +
                "                bc.code as base_code,\n" +
                "                bc.fullname as base_full_name,\n" +
                "                bc.sign as base_sign,\n" +
                "                tg.id as target_id,\n" +
                "                tg.code as target_code,\n" +
                "                tg.fullname as target_full_name,\n" +
                "                tg.sign as target_sign\n" +
                "                from exchangerates ex\n" +
                "                join currencies bc on ex.basecurrencyid = bc.id\n" +
                "                join currencies tg on ex.targetcurrencyid = tg.id\n" +
                "                where ex.id = ?\n" +
                "                order by ex.id";

        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,id);
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()){
                return Optional.of(new ExchangeRate(
                        resultSet.getLong("id"),
                        new Currency(resultSet.getLong("base_id"),
                                resultSet.getString("base_code"),
                                resultSet.getString("base_full_name"),
                                resultSet.getString("base_sign")),
                        new Currency(resultSet.getLong("target_id"),
                                resultSet.getString("target_code"),
                                resultSet.getString("target_full_name"),
                                resultSet.getString("target_sign")),
                        resultSet.getBigDecimal("rate")));
            }
            return Optional.empty();

        }
    }

    @Override
    public List<ExchangeRate> findAll() throws SQLException {
        final String sql = "select\n" +
                "                ex.id as id,\n" +
                "                ex.rate as rate,\n" +
                "                bc.id as base_id,\n" +
                "                bc.code as base_code,\n" +
                "                bc.fullname as base_full_name,\n" +
                "                bc.sign as base_sign,\n" +
                "                tg.id as target_id,\n" +
                "                tg.code as target_code,\n" +
                "                tg.fullname as target_full_name,\n" +
                "                tg.sign as target_sign\n" +
                "                from exchangerates ex\n" +
                "                join currencies bc on ex.basecurrencyid = bc.id\n" +
                "                join currencies tg on ex.targetcurrencyid = tg.id\n" +
                "                order by ex.id";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            List<ExchangeRate> exchangeRatesList = new ArrayList<>();
            while (resultSet.next()){
                exchangeRatesList.add(new ExchangeRate(
                        resultSet.getLong("id"),
                        new Currency(resultSet.getLong("base_id"),
                                     resultSet.getString("base_code"),
                                     resultSet.getString("base_full_name"),
                                     resultSet.getString("base_sign")),
                        new Currency(resultSet.getLong("target_id"),
                                     resultSet.getString("target_code"),
                                     resultSet.getString("target_full_name"),
                                     resultSet.getString("target_sign")),
                        resultSet.getBigDecimal("rate")));
            }
            return exchangeRatesList;
        }
    }

    @Override
    public Long save(ExchangeRate element) throws SQLException {
        final String sql = "insert into exchangerates(basecurrencyid,targetcurrencyid,rate) values (?,?,?)";

        try (Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setLong(1,element.getBaseCurrency().getId());
            preparedStatement.setLong(2,element.getTargetCurrency().getId());
            preparedStatement.setBigDecimal(3,element.getRate());
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            resultSet.next();
            Long savedId = resultSet.getLong("id");

            connection.commit();

            return savedId;

        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        final String sql = " delete * from excahngerates where exchangerates.id = ?";

        try (Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,id);
            preparedStatement.execute();
        }
    }
}
