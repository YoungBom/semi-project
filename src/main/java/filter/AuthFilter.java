package filter;

import util.SessionKeys;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) req;
        HttpServletResponse p = (HttpServletResponse) res;
        HttpSession s = r.getSession(false);
        boolean loggedIn = (s != null) && (s.getAttribute(SessionKeys.LOGIN_UID) != null);

        if (!loggedIn) {
            String next = URLEncoder.encode(r.getRequestURI(), StandardCharsets.UTF_8);
            p.sendRedirect(r.getContextPath() + "/login?next=" + next);
            return;
        }
        chain.doFilter(req, res);
    }
}