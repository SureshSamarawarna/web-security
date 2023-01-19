package lk.ijse.dep9.api;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.dep9.service.ServiceFactory;
import lk.ijse.dep9.service.ServiceTypes;
import lk.ijse.dep9.service.custom.UserService;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;

@WebServlet(name = "SecurityServlet", urlPatterns = {"/auth/login", "/auth/login/"}, loadOnStartup = 1)
public class SecurityServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contentType = req.getContentType();
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (contentType == null || !contentType.startsWith("application/x-www-form-urlencoded") ||
                username == null || password == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UserService userService = ServiceFactory.getInstance().
                getService(ServiceTypes.USER, UserService.class);

        if (!userService.verifyUser(username, password)){
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Let's create a JWT for this user
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 3);

        JwtBuilder jwt = Jwts.builder()
                .setSubject(username)                   // To whom?
                .setIssuer("dep-9")                     // From whom?
                .setExpiration(calendar.getTime())
                .setIssuedAt(new Date())
                .claim("powered", "dep");   // private or public claims

        // Let's sign the JWT
        SecretKey secretKey = Keys.hmacShaKeyFor("SE-ijse-dep9123456789-Panadura#1"
                .getBytes(StandardCharsets.UTF_8));
        String jws = jwt.signWith(secretKey).compact();

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        JsonbBuilder.create().toJson(jws, resp.getWriter());
    }
}
