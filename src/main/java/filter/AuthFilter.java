package filter;

import util.SessionKeys;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns = {"/review/add", "/review/delete", "/review/update"})
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) req;
        HttpServletResponse p = (HttpServletResponse) res;
        HttpSession s = r.getSession(false);
        boolean loggedIn = (s != null) && (s.getAttribute(SessionKeys.LOGIN_UID) != null);

        if (!loggedIn) {
            p.setContentType("text/html; charset=UTF-8");
            p.getWriter().write(
                "<script>" +
                "alert('로그인 후 이용해주세요.');" +
                "history.back();" +
                "</script>"
            );
            return;
        }
        chain.doFilter(req, res);
    }
}