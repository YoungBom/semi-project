package controller;

import dao.UserDao;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/user/mypage")
public class MypageServlet extends HttpServlet {
	private final UserDao userDao = new UserDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession s = req.getSession(false);
		int uid = (int) s.getAttribute("LOGIN_UID");
		try {
			User me = userDao.findByPk(uid);
			req.setAttribute("me", me);

			Object flash = s.getAttribute("FLASH");
			if (flash != null) {
				req.setAttribute("FLASH", flash);
				s.removeAttribute("FLASH");
			}

			req.getRequestDispatcher("/user/mypage.jsp").forward(req, resp);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
