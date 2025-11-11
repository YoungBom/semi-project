package filter;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = {
        "/burger/list"
})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("LOGIN_ROLE") == null) {
            res.sendRedirect(req.getContextPath() + "/user/login.jsp");
            return;
        }

        String role = (String) session.getAttribute("LOGIN_ROLE");

        if (!"ADMIN".equalsIgnoreCase(role)) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
            req.setAttribute("errorMessage", "관리자만 접근할 수 있습니다.");
            req.getRequestDispatcher("/main").forward(req, res);
            return;
        }

        chain.doFilter(request, response);
    }
}
