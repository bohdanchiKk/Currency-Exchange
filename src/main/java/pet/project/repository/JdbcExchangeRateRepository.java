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
                return Optional.of(makeExchangeRate(resultSet));
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
                "                bc.fullname as base_name,\n" +
                "                bc.sign as base_sign,\n" +
                "                tg.id as target_id,\n" +
                "                tg.code as target_code,\n" +
                "                tg.fullname as target_name,\n" +
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
                exchangeRatesList.add(makeExchangeRate(resultSet));
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

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            Long savedId = resultSet.getLong("id");

            connection.commit();

            return savedId;

        }
    }

    @Override
    public void update(ExchangeRate element) throws SQLException {
        final String sql = "update exchangerates set (basecurrencyid,targetcurrencyid,rate) " +
                "=(?,?,?) where id = ?";

        try (Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,element.getBaseCurrency().getId());
            preparedStatement.setLong(2,element.getTargetCurrency().getId());
            preparedStatement.setBigDecimal(3,element.getRate());
            preparedStatement.setLong(4,element.getId());

            preparedStatement.execute();
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

    @Override
    public Optional<ExchangeRate> findByCode(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        String sql = "SELECT\n" +
                "    er.id AS id,\n" +
                "    bc.id AS base_id,\n" +
                "    bc.code AS base_code,\n" +
                "    bc.fullname AS base_name,\n" +
                "    bc.sign AS base_sign,\n" +
                "    tc.id AS target_id,\n" +
                "    tc.code AS target_code,\n" +
                "    tc.fullname AS target_name,\n" +
                "    tc.sign AS target_sign,\n" +
                "    er.rate AS rate\n" +
                "FROM exchangerates er\n" +
                "         JOIN currencies bc ON er.basecurrencyid = bc.id\n" +
                "         JOIN currencies tc ON er.targetcurrencyid = tc.id\n" +
                "WHERE (\n" +
                "          basecurrencyid = (SELECT c.id FROM currencies c WHERE c.code = ?) AND\n" +
                "          targetcurrencyid = (SELECT c2.id FROM currencies c2 WHERE c2.code = ?))";
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,baseCurrencyCode);
            preparedStatement.setString(2,targetCurrencyCode);
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()){
                return Optional.of(makeExchangeRate(resultSet));
            }
        }
        return Optional.empty();
    }

    public static ExchangeRate makeExchangeRate(ResultSet resultSet) throws SQLException {
        return new ExchangeRate(
                resultSet.getLong("id"),
                new Currency(resultSet.getLong("base_id"),
                        resultSet.getString("base_code"),
                        resultSet.getString("base_name"),
                        resultSet.getString("base_sign")),
                new Currency(resultSet.getLong("target_id"),
                        resultSet.getString("target_code"),
                        resultSet.getString("target_name"),
                        resultSet.getString("target_sign")),
                resultSet.getBigDecimal("rate"),
                null);
    }
}
