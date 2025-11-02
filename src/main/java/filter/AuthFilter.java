package filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AuthFilter implements Filter {
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest r = (HttpServletRequest) req;
		HttpServletResponse p = (HttpServletResponse) res;
		HttpSession s = r.getSession(false);
		boolean loggedIn = s != null && s.getAttribute("LOGIN_USER_ID") != null;
		if (!loggedIn) {
			p.sendRedirect(r.getContextPath() + "/login");
			return;
		}
		chain.doFilter(req, res);
	}
}
