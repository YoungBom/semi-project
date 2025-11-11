package controller;

import dao.UserDAO;
import dto.UserDTO;
import util.SessionKeys;

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
		// 로그인 페이지로 포워드 (GET /login 대응)
		req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		String id = req.getParameter("user_id");
		String pw = req.getParameter("user_pw");
		boolean remember = "1".equals(req.getParameter("remember_me"));

		Optional<UserDTO> opt = userDao.authenticate(id, pw);
		if (opt.isEmpty()) {
			req.setAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
			req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
			return;
		}

		HttpSession old = req.getSession(false);
		if (old != null)
			old.invalidate();
		HttpSession s = req.getSession(true);

		UserDTO u = opt.get();
		s.setAttribute(SessionKeys.LOGIN_UID, u.getId());
		s.setAttribute(SessionKeys.LOGIN_USERID, u.getUserId());
		s.setAttribute(SessionKeys.LOGIN_NAME, u.getName());
		s.setAttribute(SessionKeys.LOGIN_NICKNAME, u.getNickname());
		s.setAttribute(SessionKeys.LOGIN_ROLE, u.getRole());

		// 로그인 상태 유지(자동로그인)
		if (remember) {
			String token = userDao.issueRememberToken(u.getId(), 30);
			Cookie c = new Cookie("REMEMBER", token);
			c.setMaxAge(60 * 60 * 24 * 30); // 30일
			c.setHttpOnly(true);
			c.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
			if (req.isSecure())
				c.setSecure(true); // HTTPS일 때만 Secure
			resp.addCookie(c);
		}

		resp.sendRedirect(req.getContextPath() + "/main");
	}
}
