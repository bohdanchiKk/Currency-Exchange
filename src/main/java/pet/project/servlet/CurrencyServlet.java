package pet.project.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import pet.project.entity.Currency;
import pet.project.entity.response.ErrorResponse;
import pet.project.repository.JdbcCurrencyRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebServlet(name = "CurrenciesServlet", urlPatterns = "/currencies")
@RequiredArgsConstructor
public class CurrencyServlet extends HttpServlet {
    private final JdbcCurrencyRepository currencyRepository = new JdbcCurrencyRepository();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String INTEGRITY_CONSTRAINT_VIOLATION_CODE = "23505";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        try {
            if (id != null){
                Optional<Currency> currency = currencyRepository.findById(Long.valueOf(id));
                mapper.writeValue(resp.getWriter(),currency.get());
            }else {
                List<Currency> currencyList = currencyRepository.findAll();
                mapper.writeValue(resp.getWriter(),currencyList);
            }
        } catch (SQLException e) {
            resp.setStatus(SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), new ErrorResponse(SC_INTERNAL_SERVER_ERROR,
                    "Something happened with the database, try again later!"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String fullName = req.getParameter("fullName");
        String sign = req.getParameter("sign");

        if (code.isEmpty() || fullName.isEmpty() || sign.isEmpty()){
            resp.setStatus(SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), new ErrorResponse(SC_BAD_REQUEST,"Missing or invalid parameter(s)"));
            return;
        }
        if (code.length() > 3){
            resp.setStatus(SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), new ErrorResponse(SC_BAD_REQUEST, "Code must contain only 3 letters!"));
            return;
        }

        try {
            Currency currency = new Currency(code,fullName,sign);
            Long savedCurrencyId = currencyRepository.save(currency);
            currency.setId(savedCurrencyId);

            mapper.writeValue(resp.getWriter(),currency);

        } catch (SQLException e) {
            if (e.getSQLState().equals(INTEGRITY_CONSTRAINT_VIOLATION_CODE)){
                resp.setStatus(SC_CONFLICT);
                mapper.writeValue(resp.getWriter(),new ErrorResponse(SC_CONFLICT,e.getMessage()));
            }
            resp.setStatus(SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(),new ErrorResponse((SC_INTERNAL_SERVER_ERROR),"Something wrong with DB, try again later!"));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
