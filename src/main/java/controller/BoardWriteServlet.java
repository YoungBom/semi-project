package controller;

import java.io.IOException;
import java.io.PrintWriter;

import dao.BoardDAO;
import dto.BoardDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/board/write")
public class BoardWriteServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/boardWrite.jsp").forward(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		req.setCharacterEncoding("UTF-8");
		
		HttpSession session = req.getSession();
		Object loginUidObj = session.getAttribute("LOGIN_UID");
		String writerNickname = (String) session.getAttribute("LOGIN_NICKNAME");
		
		if (loginUidObj == null) {
		    resp.setContentType("text/html; charset=UTF-8");
		    out.println("<script>");
		    out.println("alert('로그인 후 이용해주세요');");
		    out.println("location.href='" + req.getContextPath() + "/user/login.jsp';");
		    out.println("</script>");
		    out.close();
			return;
		}
		
		int userId = (int) loginUidObj;
		
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		String category = req.getParameter("category");
		
		BoardDTO board = new BoardDTO();
		board.setTitle(title);
		board.setContent(content);
		board.setCategory(category);
		board.setWriterId(String.valueOf(userId));
		board.setCategory(category);
		board.setWriterNickname(writerNickname);
		
		BoardDAO dao = new BoardDAO();
		
		int result = dao.insertBoard(board);
		if (result > 0) {
		    resp.setContentType("text/html; charset=UTF-8");
		    out.println("<script>");
		    out.println("alert('게시글 등록 성공');");
		    out.println("location.href='" + req.getContextPath() + "/board/list';");
		    out.println("</script>");
		    out.close();
		    return;
		} else {
		    resp.setContentType("text/html; charset=UTF-8");
		    out.println("<script>");
		    out.println("alert('게시글 등록 실패');");
		    out.println("history.back();");  // 이전 페이지로 돌아가기
		    out.println("</script>");
		    out.close();
		    return;
		}
		
		
	}
	
}
