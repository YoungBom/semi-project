package controller;

import dao.UserDAO;
import dto.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class AuthLoginServlet extends HttpServlet {

	private final UserDAO userDao = new UserDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// GET 요청은 로그인 폼으로
		req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		String userId = trim(req.getParameter("user_id"));
		String pw = trim(req.getParameter("user_pw"));
		String redirect = trim(req.getParameter("redirect")); // 선택

		if (isBlank(userId) || isBlank(pw)) {
			req.setAttribute("error", "아이디와 비밀번호를 모두 입력해주세요.");
			req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
			return;
		}

		// DAO에서 해시/레거시 비번까지 포함해 검증
		Optional<UserDTO> opt = userDao.authenticate(userId, pw);
		if (opt.isEmpty()) {
			req.setAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
			req.setAttribute("prefill_user_id", userId); // 아이디 값 유지(선택)
			req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
			return;
		}

		UserDTO u = opt.get();

		// 기존 세션 무효화 후 새 세션 발급
		HttpSession old = req.getSession(false);
		if (old != null)
			old.invalidate();
		HttpSession s = req.getSession(true);

		// 헤더/JSP에서 쓰는 세션 키 저장
		s.setAttribute("LOGIN_UID", u.getId()); // PK(Long)
		s.setAttribute("LOGIN_ID", u.getUserId()); // 로그인 아이디
		s.setAttribute("LOGIN_NICKNAME", u.getNickname()); // 헤더에서 표시용
		// 필요하면 역할 등 추가
		s.setAttribute("LOGIN_ROLE", u.getRole());

		

		// 리다이렉트 목적지 결정
		String ctx = req.getContextPath();
		String target = (isBlank(redirect)) ? (ctx + "/main.jsp") : redirect;

		resp.sendRedirect(target);
	}

	// --- helpers ---
	private static String trim(String s) {
		return s == null ? null : s.trim();
	}

	private static boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}
}
