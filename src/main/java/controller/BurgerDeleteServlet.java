package controller;

import java.io.IOException;

import dao.BurgerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/burger/delete")
public class BurgerDeleteServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private BurgerDAO burgerDAO = new BurgerDAO();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		
		int result = burgerDAO.deleteBurger(id);
        resp.setContentType("text/html; charset=UTF-8");
        if (result > 0) {
            resp.getWriter().println("<script>");
            resp.getWriter().println("alert('버거가 삭제되었습니다.');");
            resp.getWriter().println("location.href='" + req.getContextPath() + "/burger/list';");
            resp.getWriter().println("</script>");
        } else {
            resp.getWriter().println("<script>");
            resp.getWriter().println("alert('삭제 실패! 다시 시도해주세요.');");
            resp.getWriter().println("history.back();");
            resp.getWriter().println("</script>");
        }
	}
}
