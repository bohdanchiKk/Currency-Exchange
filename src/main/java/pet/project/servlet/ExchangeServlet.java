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
import java.math.MathContext;
import java.sql.SQLException;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@WebServlet(name = "ExchangerServlet", urlPatterns = "/exchange")
public class ExchangeServlet extends HttpServlet {
    private final ExchangeRateRepository repository = new JdbcExchangeRateRepository();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrency = req.getParameter("from");
        String targetCurrency = req.getParameter("to");
        String amount = req.getParameter("amount");

        if (baseCurrency.equals(targetCurrency)){
            resp.setStatus(SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), new ErrorResponse(SC_BAD_REQUEST,"Identical currencies!"));
            return;
        }

        try {
            Optional<ExchangeRate> pair = repository.findByCode(baseCurrency,targetCurrency);

            if (pair.isPresent()){
                BigDecimal converted = pair.get().getRate().multiply(BigDecimal.valueOf(Double.parseDouble(amount)));
                pair.get().setConvertedAmount(converted);
                mapper.writeValue(resp.getWriter(),pair.get());
            }else if (!pair.isPresent()){
                Optional<ExchangeRate> reverserPair = repository.findByCode(targetCurrency,baseCurrency);

                if (reverserPair.isPresent()){
                    BigDecimal currentRate = reverserPair.get().getRate();
                    BigDecimal reverseRate = BigDecimal.ONE.divide(currentRate, MathContext.DECIMAL32);
                    BigDecimal converted = reverseRate.multiply(BigDecimal.valueOf(Double.parseDouble(amount)));

                    reverserPair.get().setConvertedAmount(converted);
                    mapper.writeValue(resp.getWriter(),reverserPair.get());
                }
            }else {
                resp.setStatus(SC_BAD_REQUEST);
                mapper.writeValue(resp.getWriter(), new ErrorResponse(SC_BAD_REQUEST,
                        "Pair is incorrect!"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
