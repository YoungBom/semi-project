package controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import dao.ReviewDAO;
import dao.ReviewImageDAO;
import dto.ReviewDTO;
import dto.ReviewImageDTO;
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
	// 외부 저장 경로 지정 (⚠️ 서버 외부 절대경로)
	private static final String UPLOAD_DIR = "d:\\upload"; 
    
	private ReviewDAO reviewDao = new ReviewDAO();
	private ReviewDTO review = new ReviewDTO();
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 기존의 정보 가져오기
		int burgerId = 0;
		int reviewId = 0;
		for (Part part : req.getParts()) {
		    switch (part.getName()) {
		        case "burgerId":
		            burgerId = Integer.parseInt(new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8)) ;
		            break;
		        case "reviewId":
		        	reviewId = Integer.parseInt(new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8)) ;
		            break;
		    }
		}
		String content = req.getParameter("content");
		String unitRating = req.getParameter("rating");
		Double rating = 0.0;
		if (unitRating != null && !unitRating.isEmpty()) {
			rating = Double.parseDouble(unitRating);
		}
		
		review.setBurgerId(burgerId);
		review.setId(reviewId);
		review.setContent(content);
		review.setRating(rating);
		
		int result = reviewDao.updateReview(review);
		
		// 파일 가져오기
		ReviewImageDAO reviewImageDao = new ReviewImageDAO();
		
		File uploadDir = new File(UPLOAD_DIR);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		// 이미지가 없을 경우 에러발생으로 예외 처리 / 이미지 있는 경우 반환
		Collection<Part> parts = null;

		 try { parts = req.getParts(); } catch (IllegalStateException | IOException |
		 ServletException e) { e.printStackTrace(); parts = null; }
		 
		 try { // 이미지 유무체크 
			boolean hasValidImage = false;
		 
			if (parts != null) { 
				 for (Part part : parts) { // "images" input이고 실제 파일 이름이존재하고 크기도 0보다 큰 경우만 처리 
					 if ("images".equals(part.getName()) &&
							 part.getSubmittedFileName() != null && 
							 part.getSize() > 0) {
						 
						 	 hasValidImage = true; // 실제 이미지 있음 
						 	 
						 	 ReviewImageDTO ri = new ReviewImageDTO();
						 	 String uuid = UUID.randomUUID().toString(); 
						 	 String fileName = uuid + "_" + part.getSubmittedFileName();
						 	 String filePath = UPLOAD_DIR + File.separator + fileName;

						 	 try { part.write(filePath); 
						 	 } catch (IOException e) { 
						 		 e.printStackTrace();
						 		 continue; // 저장 실패 시 다음 파일로 
						 	 }

						 	 ri.setReviewId(reviewId); 
						 	 ri.setImagePath(fileName); 
						 	 reviewImageDao.addReviewImage(ri);
						 	 }
					 } 
				 } 

		 // ✅ 파일이 완전히 없거나 유효하지 않을 경우 기본값 저장 
		if (!hasValidImage) { 
			ReviewImageDTO ri = new ReviewImageDTO(); 
			ri.setReviewId(reviewId); 
			ri.setImagePath(""); // 빈 경로저장 
			reviewImageDao.addReviewImage(ri); }
		 } catch (Exception e) { e.printStackTrace(); }
		
		if(result > 0) {
			List<ReviewDTO> reviewList = reviewDao.getReview(burgerId);
			req.setAttribute("reviewList", reviewDao.getReview(burgerId));
		}
		resp.sendRedirect("/semi-project/burger/details?id="+burgerId);
	}

}
