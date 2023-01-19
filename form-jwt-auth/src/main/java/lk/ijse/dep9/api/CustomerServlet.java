package lk.ijse.dep9.api;

import com.github.javafaker.Faker;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.dep9.dto.CustomerDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CustomerServlet", urlPatterns = {"/customers", "/customers/"}, loadOnStartup = 1)
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CustomerDTO> customerList = new ArrayList<>();
        Faker faker = new Faker();

        for (int i = 0; i < 50; i++) {
            customerList.add(new CustomerDTO(String.format("C%03d", (i + 1)), faker.name().fullName(), faker.address().fullAddress(), faker.regexify("0\\d{2}-\\d{7}")));
        }

        resp.setContentType("application/json");
        JsonbBuilder.create().toJson(customerList, resp.getWriter());
    }
}
