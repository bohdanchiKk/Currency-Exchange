package pet.project.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import pet.project.entity.ExchangeRate;
import pet.project.repository.JdbcExchangeRateRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "ExchangeRatesServlet", urlPatterns = "/exchangeRates")
@RequiredArgsConstructor
public class ExchangeRatesServlet extends HttpServlet {
    private final JdbcExchangeRateRepository exchangeRateRepository = new JdbcExchangeRateRepository();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String id = req.getParameter("id");
            if (id != null){
                Optional<ExchangeRate> exchangeRate = exchangeRateRepository.findById(Long.valueOf(id));
                if (exchangeRate.isPresent()){
                    mapper.writeValue(resp.getWriter(),exchangeRate.get());
                }
            }
            List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
            mapper.writeValue(resp.getWriter(),exchangeRateList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
