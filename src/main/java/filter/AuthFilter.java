package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebFilter(urlPatterns = { "/user/mypage", "/user/edit", "/password/*" }) // 필요시 "/user/*" 로 확대
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) req;
        HttpServletResponse p = (HttpServletResponse) res;
        HttpSession s = r.getSession(false);

        boolean loggedIn = (s != null && s.getAttribute("LOGIN_UID") != null);
        if (!loggedIn) {
            String target = r.getRequestURI() + (r.getQueryString()==null? "" : "?" + r.getQueryString());
            String next = URLEncoder.encode(target, StandardCharsets.UTF_8);
            p.sendRedirect(r.getContextPath() + "/user/login.jsp?next=" + next);
            return;
        }
        chain.doFilter(req, res);
    }
}
