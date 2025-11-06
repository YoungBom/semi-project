package controller;

import java.io.IOException;
import java.util.List;

import dao.ReviewDAO;
import dto.ReviewDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/review/delete")
public class ReviewDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ReviewDAO reviewDao = new ReviewDAO();
		
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int burgerId = Integer.parseInt(req.getParameter("burgerId"));
		int reviewId = Integer.parseInt(req.getParameter("reviewId"));
		
		int result = reviewDao.deleteReview(burgerId, reviewId);
		if (result > 0) {
			List<ReviewDTO> reviewList = reviewDao.getReview(burgerId);
			req.setAttribute("reviewList", reviewList);
		}
		resp.sendRedirect("/semi-project/burger/details?id="+burgerId);
	}
	
}
