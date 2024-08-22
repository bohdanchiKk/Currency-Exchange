package pet.project.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import pet.project.config.MyDataSource;
import pet.project.entity.Currency;
import pet.project.entity.response.ErrorResponse;
import pet.project.repository.CurrencyRepository;
import pet.project.repository.JdbcCurrencyRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@WebServlet(name = "CurrenciesServlet", urlPatterns = "/currencies")
@RequiredArgsConstructor
public class CurrencyServlet extends HttpServlet {
    private final JdbcCurrencyRepository currencyRepository = new JdbcCurrencyRepository();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            List<Currency> currencyList = currencyRepository.findAll();
            mapper.writeValue(resp.getWriter(),currencyList);

        } catch (SQLException e) {
            resp.setStatus(SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), new ErrorResponse(SC_INTERNAL_SERVER_ERROR,
                    "Something happened with the database, try again later!"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
