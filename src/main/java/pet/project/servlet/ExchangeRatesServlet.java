package pet.project.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import pet.project.entity.Currency;
import pet.project.entity.ExchangeRate;
import pet.project.entity.response.ErrorResponse;
import pet.project.repository.JdbcCurrencyRepository;
import pet.project.repository.JdbcExchangeRateRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@WebServlet(name = "ExchangeRatesServlet", urlPatterns = "/exchangeRates")
@RequiredArgsConstructor
public class ExchangeRatesServlet extends HttpServlet {
    private final JdbcExchangeRateRepository exchangeRateRepository = new JdbcExchangeRateRepository();
    private final JdbcCurrencyRepository currencyRepository = new JdbcCurrencyRepository();
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
            resp.setStatus(SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), new ErrorResponse(SC_INTERNAL_SERVER_ERROR,
                    "Something happened with the database, try again later!"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCode = req.getParameter("baseCurrencyCode");
        String targetCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");
        if (baseCode.equals(targetCode)){
            resp.setStatus(SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), new ErrorResponse(SC_BAD_REQUEST,"Identical currencies!"));
            return;
        }

        try {
            if (exchangeRateRepository.findByCode(baseCode,targetCode).isPresent() ||
                exchangeRateRepository.findByCode(targetCode,baseCode).isPresent()){
                resp.setStatus(SC_BAD_REQUEST);
                mapper.writeValue(resp.getWriter(),new ErrorResponse(SC_BAD_REQUEST,"Pair already exists!"));
                return;

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            Optional<Currency> baseCurrency = currencyRepository.findByCode(baseCode);
            Optional<Currency> targetCurrency = currencyRepository.findByCode(targetCode);

            if (baseCurrency.isPresent() && targetCurrency.isPresent()){
                ExchangeRate exchangeRate = new ExchangeRate(baseCurrency.get(),targetCurrency.get(), BigDecimal.valueOf(Double.parseDouble(rate)));
                Long savedId = exchangeRateRepository.save(exchangeRate);
                exchangeRate.setId(savedId);

                mapper.writeValue(resp.getWriter(),exchangeRate);
            }

        } catch (SQLException e) {
            resp.setStatus(SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), new ErrorResponse(SC_INTERNAL_SERVER_ERROR,
                    "Something happened with the database, try again later!"));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final String id = req.getParameter("id");
            exchangeRateRepository.delete(Long.valueOf(id));
        } catch (SQLException e) {
            resp.setStatus(SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), new ErrorResponse(SC_INTERNAL_SERVER_ERROR,
                    "Something happened with the database, try again later!"));
        }
    }
}
