package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns = { "/user/mypage", "/user/edit", "/password/*" })
public class AuthFilter implements Filter {
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest r = (HttpServletRequest) req;
		HttpServletResponse p = (HttpServletResponse) res;
		HttpSession s = r.getSession(false);

		boolean loggedIn = (s != null && s.getAttribute("LOGIN_UID") != null);
		if (!loggedIn) {
			String target = r.getRequestURI() + (r.getQueryString() == null ? "" : "?" + r.getQueryString());
			r.getSession(true).setAttribute("AFTER_LOGIN_REDIRECT", target);
			p.sendRedirect(r.getContextPath() + "/user/login.jsp");
			return;
		}
		chain.doFilter(req, res);
	}
}
