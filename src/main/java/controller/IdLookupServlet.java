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
		req.setCharacterEncoding("UTF-8");
		String email = req.getParameter("email");

		try {
			var u = userDao.findByEmail(email); // 필요 시 구현
			if (u == null) {
				req.setAttribute("msg", "해당 이메일로 가입된 아이디 안내를 전송했습니다.");
			} else {
				// 실제 운영은 이메일 전송. 여기서는 마스킹 표기
				req.setAttribute("result", "아이디: " + mask(u.getUser_id()));
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
