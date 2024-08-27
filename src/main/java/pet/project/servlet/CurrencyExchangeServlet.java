package pet.project.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pet.project.entity.ExchangeRate;
import pet.project.entity.response.ErrorResponse;
import pet.project.repository.ExchangeRateRepository;
import pet.project.repository.JdbcExchangeRateRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(name = "ExchangeServlet", urlPatterns = "/exchangeRate/*")
public class CurrencyExchangeServlet extends HttpServlet {
    private final ExchangeRateRepository repository = new JdbcExchangeRateRepository();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getPathInfo().replaceAll("/", "");

        if (url.length() == 6) {
            String baseCurrencyCode = url.substring(0, 3);
            String targetCurrencyCode = url.substring(3);

            try {
                Optional<ExchangeRate> exchangeRate = repository.findByCode(baseCurrencyCode, targetCurrencyCode);
                if (exchangeRate.isPresent()) {
                    mapper.writeValue(resp.getWriter(), exchangeRate.get());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(),new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST,"Exchange pairs are incorrect!"));
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp); // Delegate to the appropriate method (GET, POST, etc.)
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getPathInfo().replaceAll("/", "");
        String parameter = req.getReader().readLine();
        String paramRateValue = parameter.replace("rate=", "");
//        String rate = req.getParameter("rate");

        if (url.length() == 6) {
            String baseCurrencyCode = url.substring(0, 3);
            String targetCurrencyCode = url.substring(3);

            try {
                Optional<ExchangeRate> exchangeRate = repository.findByCode(baseCurrencyCode, targetCurrencyCode);
                if (exchangeRate.isPresent()) {
                    exchangeRate.get().setRate(BigDecimal.valueOf(Double.parseDouble(paramRateValue)));
                    repository.update(exchangeRate.get());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(),new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST,"Exchange pairs are incorrect!"));
        }
    }
}
