package lk.ijse.dep9.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/auth/*", loadOnStartup = 1,
        initParams = {@WebInitParam(name = "redirectUrl", value = "http://localhost:3000/login.html"),
                @WebInitParam(name="successUrl", value="http://localhost:3000")})
public class LoginServlet extends HttpServlet {

    @Override   // logout
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        String redirectUrl = getServletConfig().getInitParameter("redirectUrl");
        resp.sendRedirect(redirectUrl);
    }

    @Override       // login
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() == null ||
                !req.getContentType().startsWith("application/x-www-form-urlencoded")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null  || password == null){
            String redirectUrl = getServletConfig().getInitParameter("redirectUrl");
            resp.setContentType("text/html");
            try (PrintWriter out = resp.getWriter()) {
                out.println("<h1>Invalid login credentials</h1>");
                out.println("<a href=\""+ redirectUrl +"\">Try again</a>");
            }
            return;
        }

        if (verifyLoginCredentials(username, password)){
            HttpSession session = req.getSession();     // Let's create a new session
//            session.setMaxInactiveInterval(5000 * 2);
            System.out.println(session.getId());
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.sendRedirect(getServletConfig().getInitParameter("successUrl"));
        }else{
            String redirectUrl = getServletConfig().getInitParameter("redirectUrl");
            resp.setContentType("text/html");
            try (PrintWriter out = resp.getWriter()) {
                out.println("<h1>Invalid login credentials</h1>");
                out.println("<a href=\""+ redirectUrl +"\">Try again</a>");
            }
        }
    }

    private boolean verifyLoginCredentials(String username, String password){
        return (username.equals("admin") && password.equals("admin"));
    }
}
