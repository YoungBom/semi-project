package controller;

import dao.UserDao;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/mypage")
public class MyPageServlet extends HttpServlet {
	private final UserDao dao = new UserDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer uid = (Integer) req.getSession().getAttribute("LOGIN_UID");
		if (uid == null) {
			resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
			return;
		}
		try {
			User me = dao.findByPk(uid);
			req.setAttribute("me", me);
			req.getRequestDispatcher("/mypage.jsp").forward(req, resp);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
