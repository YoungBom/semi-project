package controller;

import java.io.IOException;

import dao.BoardDAO;
import dto.BoardDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/board/view")
public class BoardViewServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String idParam = req.getParameter("id");
		if (idParam == null) {
			resp.sendRedirect(req.getContextPath() + "/board/list");
			return;
		}
		
		int boardId = Integer.parseInt(idParam);
		
		BoardDAO dao = new BoardDAO();
		dao.increaseViewCount(boardId);
		BoardDTO board = dao.getBoardById(boardId);
		
		if (board == null) {
			resp.sendRedirect(req.getContextPath() + "/board/list");
			return;
		}
		
		req.setAttribute("board", board);
		req.getRequestDispatcher("/boardView.jsp").forward(req, resp);
		
	}
}
