package pet.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import pet.project.config.MyDataSource;
import pet.project.entity.Currency;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {

        final DataSource dataSource = MyDataSource.getInstance();
        ObjectMapper mapper = new ObjectMapper();
        StringWriter writer = new StringWriter();

        try(Connection connection = dataSource.getConnection()) {
            String sql = "select * from Currencies";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            List<Currency> currencyList = new ArrayList<>();

            while (resultSet.next()){
                currencyList.add(new Currency(resultSet.getLong("id"),
                        resultSet.getString("code"),
                        resultSet.getString("fullname"),
                        resultSet.getString("sign")));
            }
            mapper.writeValue(writer,currencyList);
            System.out.println(writer.toString());
        }
    }
}
