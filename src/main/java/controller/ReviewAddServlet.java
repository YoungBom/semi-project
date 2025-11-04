package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Collection;
import java.util.UUID;

import dao.ReviewDao;
import dao.ReviewImageDAO;
import dto.BurgerDTO;
import dto.ReviewDTO;
import dto.ReviewImageDTO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/ReviewAddProcess")
@MultipartConfig(
		fileSizeThreshold = 1024 * 1024 * 1,   // 메모리 임시 저장 임계값(1MB) -> 이 크기 초과 시 디스크에 임시 저장
		maxFileSize = 1024 * 1024 * 10,        // 업로드 최대 파일 크기(10MB)
		maxRequestSize = 1024 * 1024 * 50      // 전체 요청 크기(50MB)
)
public class ReviewAddServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	// 외부 저장 경로 지정 (⚠️ 서버 외부 절대경로)
    private static final String UPLOAD_DIR = "d:\\upload"; 
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		ReviewDTO rv = new ReviewDTO();
		ReviewDao reviewDao = new ReviewDao();
		
		// 버거ID, 유저ID는 추후 전달받은 값으로 처리
		// 1️⃣ URL에서 버거 ID 파라미터 읽기 (예: ?id=3)
        String idParam = req.getParameter("burgerId");

        if (idParam == null || idParam.isEmpty()) {
        	resp.sendRedirect("main.jsp");
            return;
        }

        int burgerId = Integer.parseInt(idParam);
		int userId = 1;
		String content = req.getParameter("content");
		String unitRating = req.getParameter("rating");
		
		Double rating = 0.0;
		if (unitRating != null || !unitRating.isEmpty()) {
			rating = Double.parseDouble(unitRating);
		}
		
		rv.setBurgerId(burgerId);
		rv.setUserId(userId);
		rv.setContent(content);
		rv.setRating(rating);

		// 파일 가져오기
		ReviewImageDAO reviewImageDao = new ReviewImageDAO();

		File uploadDir = new File(UPLOAD_DIR);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		
		// 리뷰 삽입 한번만 진행하기 떄문에 해당변수는 반복문 밖에 있어야
		int reviewId = reviewDao.addReview(rv);
		Collection<Part> parts = req.getParts();
		
		if(parts == null || parts.isEmpty()) {
			ReviewImageDTO ri = new ReviewImageDTO();
			String filePath = "";
			ri.setReviewId(reviewId);
			ri.setImagePath(filePath);
			
			reviewImageDao.addReviewImage(ri);
		} else {
			for (Part part : parts) {
				if (part.getName().equals("images") && part.getSize() > 0) {
					ReviewImageDTO ri = new ReviewImageDTO();
					String uuid = UUID.randomUUID().toString();
					String fileName = uuid + part.getSubmittedFileName();
					String filePath = UPLOAD_DIR + File.separator + fileName;
					part.write(filePath);
					ri.setReviewId(reviewId);
					ri.setImagePath(filePath);
					
					reviewImageDao.addReviewImage(ri);
				}
			}
		}
		resp.sendRedirect("burgerDetails?id="+burgerId);
	}
}
