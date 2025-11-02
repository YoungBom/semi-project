package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/logout")
public class AuthLogoutServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession s = req.getSession(false);
		if (s != null)
			s.invalidate();
		resp.sendRedirect(req.getContextPath() + "/user/login.jsp");
	}
}
