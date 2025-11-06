package controller;

import dao.UserDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/user/check-id")   // 클라이언트에서 fetch 하는 URL과 반드시 동일
public class CheckIdServlet extends HttpServlet {
  private final UserDAO userDao = new UserDAO();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("application/json; charset=UTF-8");
    resp.setHeader("Cache-Control", "no-store");

    String uid = req.getParameter("user_id");
    boolean available = (uid != null && !uid.isBlank()) && !userDao.existsByLoginId(uid.trim());

    resp.getWriter().write("{\"available\":" + available + "}");
  }
}
