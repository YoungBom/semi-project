package filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AuthFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest r = (HttpServletRequest) req;
		HttpServletResponse p = (HttpServletResponse) res;

		String fullUri = r.getRequestURI();
		String uri = fullUri.substring(r.getContextPath().length());

		// 1) 로그인 없이 접근 허용할 경로(화이트리스트)
		boolean whitelisted = uri.equals("/login") || uri.equals("/register") || // 컨트롤러 매핑
				uri.equals("/user/register.jsp") || // JSP 직접 접근을 쓰는 경우
				uri.equals("/user/check-id") || // 아이디 중복확인 AJAX
				uri.startsWith("/resources/"); // 정적 파일

		if (whitelisted) {
			chain.doFilter(req, res);
			return;
		}

		// 2) 보호 경로는 로그인 여부 확인 (세션 키: SessionKeys.LOGIN_UID)
		HttpSession s = r.getSession(false);
		boolean loggedIn = (s != null) && (s.getAttribute(util.SessionKeys.LOGIN_UID) != null);

		if (!loggedIn) {
			p.sendRedirect(r.getContextPath() + "/login");
			return;
		}

		chain.doFilter(req, res);
	}
}
