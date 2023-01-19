package lk.ijse.dep9.api.filter;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SecurityFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getServletPath().startsWith("/auth")){
            chain.doFilter(req, res);
            return;
        }

        String authorization = req.getHeader("Authorization");

        if (authorization != null){
            if (authorization.startsWith("Bearer")){
                String token = authorization.replace("Bearer ", "");

                SecretKey secretKey = Keys.hmacShaKeyFor("SE-ijse-dep9123456789-Panadura#1"
                        .getBytes(StandardCharsets.UTF_8));

                try {
                    Jwts.parserBuilder()
                            .setSigningKey(secretKey).build().parse(token);
                    chain.doFilter(req, res);
                    return;
                }catch (JwtException e){
                    //res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }

        res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
