package controller;

import java.io.IOException;

import dao.ReviewDao;
import dto.BurgerDTO;
import dto.ReviewDTO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ReviewAddProcess")
@MultipartConfig(
		fileSizeThreshold = 1024 * 1024 * 1,   // 메모리 임시 저장 임계값(1MB) -> 이 크기 초과 시 디스크에 임시 저장
		maxFileSize = 1024 * 1024 * 10,        // 업로드 최대 파일 크기(10MB)
		maxRequestSize = 1024 * 1024 * 50      // 전체 요청 크기(50MB)
)
public class RiviewAddServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		ReviewDTO rv = new ReviewDTO();
		
		// 버거ID, 유저ID는 추후 전달받은 값으로 처리
		int burgerId = 1;
		int userId = 1;
		String content = req.getParameter("content");
		String unitRating = req.getParameter("rating");
		
		Double rating = 0.0;
		if (unitRating != null || unitRating.isEmpty()) {
			rating = Double.parseDouble(unitRating);
		}
		
		rv.setBurgerId(burgerId);
		rv.setUserId(userId);
		rv.setContent(content);
		rv.setRating(rating);
		
		ReviewDao reviewDao = new ReviewDao();
		reviewDao.addReview(rv);
		
		resp.sendRedirect("review.jsp");
		
		
	
	}
}
