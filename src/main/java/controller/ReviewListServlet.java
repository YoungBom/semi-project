package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import dao.ReviewDAO;
import dto.ReviewDTO;


@WebServlet("/review/list")
public class ReviewListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ReviewDAO reviewdao = new ReviewDAO();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession s = request.getSession(true);
		int userId = (int) s.getAttribute("LOGIN_UID");
		
		ReviewDAO reviewDao = new ReviewDAO();
		// 객체 리스트로 받아오
		List<ReviewDTO> reviewAllList = reviewDao.listUpReview(userId);

		request.setAttribute("reviewAllList", reviewAllList);
		request.getRequestDispatcher("/listReview.jsp").forward(request, response);			
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
