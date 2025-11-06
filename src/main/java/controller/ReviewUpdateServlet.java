package controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import dao.ReviewDAO;
import dto.ReviewDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/review/update")
@MultipartConfig
public class ReviewUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ReviewDAO reviewDao = new ReviewDAO();
	private ReviewDTO review = new ReviewDTO();
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int burgerId = 0;
		for (Part part : req.getParts()) {
		    switch (part.getName()) {
		        case "burgerId":
		            burgerId = Integer.parseInt(new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8)) ;
		            break;
		    }
		}
		int reviewId = Integer.parseInt(req.getParameter("reviewId"));
		String content = req.getParameter("content");
		String unitRating = req.getParameter("rating");
		Double rating = 0.0;
		if (unitRating != null || !unitRating.isEmpty()) {
			rating = Double.parseDouble(unitRating);
		}
		review.setBurgerId(burgerId);
		review.setId(reviewId);
		review.setContent(content);
		review.setRating(rating);
		
		int result = reviewDao.updateReview(review);
		if(result > 0) {
			List<ReviewDTO> reviewList = reviewDao.getReview(burgerId);
			req.setAttribute("reviewList", reviewDao.getReview(burgerId));
		}
		resp.sendRedirect("/semi-project/burger/details?id="+burgerId);
	}

}
