package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class AuthLogoutServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession s = req.getSession(false);
		if (s != null)
			s.invalidate();
		resp.sendRedirect(req.getContextPath() + "/"); // ✅ 메인으로
	}

	// (임시) GET 눌러도 동작하게 하고 싶으면 주석 해제
	   @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	       throws ServletException, IOException { doPost(req, resp); }
}
