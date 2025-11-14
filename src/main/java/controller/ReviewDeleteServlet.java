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
import jakarta.servlet.http.HttpSession;

@WebServlet("/review/delete")
public class ReviewDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ReviewDAO reviewDao = new ReviewDAO();
		
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int burgerId = Integer.parseInt(req.getParameter("burgerId"));
		int reviewId = Integer.parseInt(req.getParameter("reviewId"));
		HttpSession s = req.getSession(true);
		int userId = (int) s.getAttribute("LOGIN_UID");
		// 마이페이지에서 리뷰 삭제 시 redirect -> mypage로 이동
		String redirect = req.getParameter("redirect");
		
		int result = reviewDao.deleteReview(burgerId, reviewId);
		
		if (result > 0) {
			// 삭제되면 완료 알림창 띄우기
		}
		
		if (redirect != null && !redirect.isEmpty()) { // redirect 지정값(마이페이지에서 삭제한 경우)이 있는 경우 mypage로
			req.getRequestDispatcher("/review/list").forward(req, resp);
		} else {
			resp.sendRedirect("/semi-project/burger/details?id="+burgerId);			
		}
		
		
	}
	
}
