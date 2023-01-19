package lk.ijse.dep9.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = "/*",
        initParams = @WebInitParam(name = "redirectUrl", value = "http://localhost:3000/login.html"))
public class SecurityFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        if (req.getServletPath().equals("/auth") && req.getPathInfo() != null &&
                (req.getPathInfo().equals("/login") || (req.getPathInfo().equals("logout")))){
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = req.getSession(false);
        /* if session is null, there rhere is a no session token */
        if (session != null){
            chain.doFilter(req, res);
            return;
        }

        String redirectUrl = getFilterConfig().getInitParameter("redirectUrl");
        res.sendRedirect(redirectUrl);
//        res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
