package controller;

import java.io.IOException;

import dao.BoardDAO;
import dto.BoardDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/board/edit")
public class BoardEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int boardId = Integer.parseInt(req.getParameter("id"));
		BoardDAO dao = new BoardDAO();
		BoardDTO board = dao.getBoardById(boardId);
		
		if (board == null) {
			resp.sendRedirect(req.getContextPath() + "/board/list");
			return;
		}
		
		req.setAttribute("board", board);
		req.getRequestDispatcher("/boardEdit.jsp").forward(req, resp);
 		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		
		int boardId = Integer.parseInt(req.getParameter("boardId"));
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		String category = req.getParameter("category");
		
		BoardDTO board = new BoardDTO();
		board.setBoardId(boardId);
		board.setTitle(title);
		board.setContent(content);
		board.setCategory(category);
		
		BoardDAO dao = new BoardDAO();
		int result = dao.updateBoard(board);
		
		resp.setContentType("text/html; charset=UTF-8");
		var out = resp.getWriter();
		
        if (result > 0) {
            out.println("<script>");
            out.println("alert('게시글이 수정되었습니다.');");
            out.println("location.href='" + req.getContextPath() + "/board/view?id=" + boardId + "';");
            out.println("</script>");
        } else {
            out.println("<script>");
            out.println("alert('게시글 수정 실패');");
            out.println("history.back();");
            out.println("</script>");
        }
		
		
		
		
	}
	
}