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
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("user_id"); 
		String pw = req.getParameter("user_pw");

		try {
			User u = userDao.findByLoginId(id);
			boolean ok = (u != null) && PasswordUtil.verify(pw, u.getUser_pw());
			if (ok) {
				HttpSession old = req.getSession(false);
				if (old != null)
					old.invalidate();
				HttpSession s = req.getSession(true);
				s.setAttribute("LOGIN_UID", u.getId()); // PK
				s.setAttribute("LOGIN_NAME", u.getName());
				resp.sendRedirect(req.getContextPath() + "/main.jsp");
			} else {
				req.setAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
				req.getRequestDispatcher("/user/login.jsp").forward(req, resp);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
