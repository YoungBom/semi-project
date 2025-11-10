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

		String ctx = r.getContextPath(); // 예: /semi-project
		String uri = r.getRequestURI(); // 예: /semi-project/resources/css/user.css

		// 1) 정적 리소스는 필터 제외 (CSS/JS/IMG, 파비콘)
		if (uri.startsWith(ctx + "/resources/") || uri.equals(ctx + "/favicon.ico")) {
			chain.doFilter(req, res);
			return;
		}

		// 2) 공개 경로(로그인/회원가입/비번재설정 등)는 필터 제외
		if (uri.equals(ctx + "/login") || uri.equals(ctx + "/user/login.jsp") || uri.equals(ctx + "/user/register.jsp")
				|| uri.startsWith(ctx + "/user/find") || // 아이디 찾기 등 사용 시
				uri.startsWith(ctx + "/user/reset")) { // 비번 재설정 등 사용 시
			chain.doFilter(req, res);
			return;
		}

		// 3) 그 외는 기존 인증 체크
		HttpSession s = r.getSession(false);
		boolean loggedIn = (s != null)
				&& (s.getAttribute("LOGIN_USER_ID") != null || s.getAttribute("LOGIN_UID") != null); // 프로젝트 키 중 하나라도
																										// 있으면 로그인

		if (!loggedIn) {
			p.sendRedirect(ctx + "/login");
			return;
		}

		chain.doFilter(req, res);
	}
}
