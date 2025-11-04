// controller/IdLookupServlet.java
package controller;

import dao.UserDao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/id/lookup")
public class IdLookupServlet extends HttpServlet {
	private final UserDao userDao = new UserDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/user/id_lookup.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = (req.getParameter("email") + "").trim();
		try {
			var u = userDao.findByEmail(email);
			if (u != null) {
				String loginIdMasked = mask(u.getUser_id());
				req.setAttribute("msg", "해당 이메일로 가입된 아이디: " + loginIdMasked);
			} else {
				req.setAttribute("msg", "해당 이메일로 가입된 아이디 안내를 전송했습니다."); // 존재여부 감추기
			}
			req.getRequestDispatcher("/user/id_lookup.jsp").forward(req, resp);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private String mask(String s) {
		if (s == null || s.length() <= 3)
			return "***";
		return s.substring(0, 3) + "*".repeat(Math.max(1, s.length() - 3));
	}
}
