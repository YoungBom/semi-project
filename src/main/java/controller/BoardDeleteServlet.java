package controller;

import java.io.IOException;

import dao.BoardDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/board/delete")
public class BoardDeleteServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		int boardId = Integer.parseInt(req.getParameter("id"));
		BoardDAO dao = new BoardDAO();
		int result = dao.deleteBoard(boardId);
		
        resp.setContentType("text/html; charset=UTF-8");
        var out = resp.getWriter();
        if (result > 0) {
            out.println("<script>");
            out.println("alert('게시글이 삭제되었습니다.');");
            out.println("location.href='" + req.getContextPath() + "/board/list';");
            out.println("</script>");
        } else {
            out.println("<script>");
            out.println("alert('삭제 실패');");
            out.println("history.back();");
            out.println("</script>");
        }
	}
	
	
}
