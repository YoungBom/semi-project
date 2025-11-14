package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dao.ReviewDAO;
import dto.ReviewDTO;

@WebServlet("/review/filter")

public class ReviewFilterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 유저, brand 정보 가져오기
		HttpSession s = request.getSession(true);
		int userId = (int) s.getAttribute("LOGIN_UID");
		String brandType = request.getParameter("type");
		
		System.out.println(userId);
		System.out.println(brandType);
		
		ReviewDAO reviewDao = new ReviewDAO();
		List<ReviewDTO> reviewList = new ArrayList<ReviewDTO>();
		
		reviewList = reviewDao.getReviewByBrand(userId, brandType);
		
		if (brandType.equals("All")) {
            // 전체 조회로 되돌리기 — 간단히 리다이렉트하면 ReviewListServlet에서 전체를 세팅
            response.sendRedirect(request.getContextPath() + "/review/list");
            return;
        } else {
        	reviewList = reviewDao.getReviewByBrand(userId, brandType);
            request.setAttribute("reviewList", reviewList);
            // 리뷰 목록 JSP로 포워드 (ReviewListServlet을 호출하지 않고 JSP 바로 사용)
            request.getRequestDispatcher("/listReview.jsp").forward(request, response);
        }
	}

}
