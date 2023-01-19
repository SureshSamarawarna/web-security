package lk.ijse.dep9.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@WebFilter(urlPatterns = "/*")
public class SecurityFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String authorization = req.getHeader("Authorization");

        if (authorization != null){

            /* Checking the auth scheme */
            if (authorization.startsWith("Basic")){

                // authorization = Basic Af418541345134564
                String encodedCredentials = authorization.replace("Basic ", "");
                byte[] decodedByteArray = Base64.getDecoder().decode(encodedCredentials);
                String decodedCredentials = new String(decodedByteArray, StandardCharsets.UTF_8);
                System.out.println(decodedCredentials); // username:password
                String[] splitCredentials = decodedCredentials.split(":");
                String username =  splitCredentials[0];
                String password =  splitCredentials[1];

                if (verifyLoginCredentials(username, password)){
                    chain.doFilter(req, res);
                    return;
                }
            }
        }

        res.setHeader("WWW-Authenticate", "Basic realm=app");
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private boolean verifyLoginCredentials(String username, String password){
        return username.equals("admin") && password.equals("admin");
    }
}
