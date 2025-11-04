package controller;

import dao.UserDao;
import model.User;
import util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class AuthLoginServlet extends HttpServlet {
	private final UserDao userDao = new UserDao();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String id = req.getParameter("user_id");
		String pw = req.getParameter("user_pw");

		try {
			User u = userDao.findByLoginId(id);
			boolean ok = (u != null) && PasswordUtil.verify(pw, u.getUser_pw());
			if (!ok) {
				req.setAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
				req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
				return;
			}

			HttpSession old = req.getSession(false);
			if (old != null)
				old.invalidate();
			HttpSession s = req.getSession(true);
			s.setAttribute("LOGIN_UID", u.getId());
			s.setAttribute("LOGIN_NAME", u.getName());

			String go = (String) s.getAttribute("AFTER_LOGIN_REDIRECT");
			if (go != null) {
				s.removeAttribute("AFTER_LOGIN_REDIRECT");
				resp.sendRedirect(go);
			} else {
				resp.sendRedirect(req.getContextPath() + "/");
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
	}
}
