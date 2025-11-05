package controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
		int burgerId = 0;

		// multipart/form-data에서는 getParameter가 안 먹힐 수 있으므로 Part에서 직접 가져오기 시도
		for (Part part : req.getParts()) {
		    switch (part.getName()) {
		        case "burgerId":
		            burgerId = Integer.parseInt(new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8)) ;
		            break;
		    }
		}
		/*
		 * try { idParam = req.getParameter("burgerId");
		 * 
		 * // 그래도 null이라면 수동으로 파트 탐색 if (idParam == null || idParam.isEmpty()) { Part
		 * idPart = req.getPart("burgerId"); if (idPart != null) { idParam = new
		 * String(idPart.getInputStream().readAllBytes(), "UTF-8");
		 * System.out.println(idParam); } }
		 * 
		 * if (idParam == null || idParam.isEmpty()) {
		 * System.out.println("❌ burgerId 누락 — main으로 리다이렉트됨");
		 * resp.sendRedirect("main.jsp"); return; }
		 * 
		 * } catch (Exception e) { e.printStackTrace(); resp.sendRedirect("main.jsp");
		 * return; }
         * int burgerId = Integer.parseInt(idParam);
		 */
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
		
		// 이미지가 없을 경우 에러발생으로 예외 처리 / 이미지 있는 경우 반환
		Collection<Part> parts = null;

		try {
		    parts = req.getParts();
		} catch (IllegalStateException | IOException | ServletException e) {
		    e.printStackTrace();
		    parts = null;
		}

		try {
			// 이미지 유무체크
		    boolean hasValidImage = false;

		    if (parts != null) {
		        for (Part part : parts) {
		            // "images" input이고 실제 파일 이름이 존재하고 크기도 0보다 큰 경우만 처리
		            if ("images".equals(part.getName()) &&
		                part.getSubmittedFileName() != null &&
		                part.getSize() > 0) {

		                hasValidImage = true; // 실제 이미지 있음
		                ReviewImageDTO ri = new ReviewImageDTO();

		                String uuid = UUID.randomUUID().toString();
		                String fileName = uuid + "_" + part.getSubmittedFileName();

		                String filePath = UPLOAD_DIR + File.separator + fileName;

		                try {
		                    part.write(filePath);
		                } catch (IOException e) {
		                    e.printStackTrace();
		                    continue; // 저장 실패 시 다음 파일로
		                }

		                ri.setReviewId(reviewId);
		                ri.setImagePath(filePath);
		                reviewImageDao.addReviewImage(ri);
		            }
		        }
		    }

		    // ✅ 파일이 완전히 없거나 유효하지 않을 경우 기본값 저장
		    if (!hasValidImage) {
		        ReviewImageDTO ri = new ReviewImageDTO();
		        ri.setReviewId(reviewId);
		        ri.setImagePath(""); // 빈 경로 저장
		        reviewImageDao.addReviewImage(ri);
		    }

		} catch (Exception e) {
		    e.printStackTrace();
		}
		resp.sendRedirect("burgerDetails.jsp?burgerId="+burgerId);
	}
}
