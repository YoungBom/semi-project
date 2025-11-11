package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import dao.ReviewDAO;


@WebServlet("/review/list")
public class ReviewListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ReviewDAO reviewdao = new ReviewDAO();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId = Integer.parseInt(request.getParameter("userId"));
		
		ReviewDAO reviewDao = new ReviewDAO();
		// 객체 리스트로 받아오
		reviewAllList = reviewDao.listUpReview(userId);
		request.setAttribute("reviewAllList", reviewAllList);
		response.sendRedirect("${pageContext.request.contextPath}/mypage.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
