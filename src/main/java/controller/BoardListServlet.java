package controller;

import java.io.IOException;
import java.util.List;

import dao.BoardDAO;
import dto.BoardDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/board/list")
public class BoardListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String category = req.getParameter("category");
		
		BoardDAO dao = new BoardDAO();
		List<BoardDTO> boardList;
		
		if (category == null || category.equals("전체")) {
		    boardList = dao.getBoardList();
		} else {
		    boardList = dao.getBoardListByCategory(category);
		}
		
		req.setAttribute("boardList", boardList);
		req.setAttribute("selectedCategory", category == null ? "전체" : category);
		req.getRequestDispatcher("/boardList.jsp").forward(req, resp);
		
	}
	
}