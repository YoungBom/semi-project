package controller;

import java.io.IOException;

import dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/mypage")

public class MyPageServlet extends HttpServlet {
	private final UserDao userDao = new UserDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession s = req.getSession(false);
		if (s == null || s.getAttribute("LOGIN_UID") == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		int uid = (int) s.getAttribute("LOGIN_UID");
		try {
			var me = userDao.findByPk(uid);
			req.setAttribute("me", me);
			req.getRequestDispatcher("/user/mypage.jsp").forward(req, resp);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
