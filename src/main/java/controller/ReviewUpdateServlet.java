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
import jakarta.servlet.http.HttpSession;
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
		HttpSession s = req.getSession(true);
		int userId = (int) s.getAttribute("LOGIN_UID");
		int burgerId = 0;
		int reviewId = 0;
		String imageCheck = "";
		for (Part part : req.getParts()) {
		    switch (part.getName()) {
		        case "burgerId":
		            burgerId = Integer.parseInt(new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8)) ;
		            break;
		        case "reviewId":
		        	reviewId = Integer.parseInt(new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8)) ;
		            break;
		        case "imageCheck":
		        	imageCheck = new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
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
		
		// 파일 가져오기
		ReviewImageDAO reviewImageDao = new ReviewImageDAO();
		// 기존의 이미지 경로 가져오기
		// review update 전 기존 image_path 가져오기 => 이미지 정보가 사라지기 때문에
		List<String> imagePaths = reviewImageDao.findReviewImage(reviewId);
		
		// review_image update 전 review 먼저 update 실행 => 이미지 정보가 사라지기 때문에
		int result = reviewDao.updateReview(review);
		
		
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

			// ✅ 파일이 완전히 없거나 유효하지 않을경우 DB 저장하지 않음
		    // 기존에 저장된 이미지파일 없음 + 수정시에도 파일 추가하지 않음
		    if (!hasValidImage && imagePaths == null) {  }
		    // 기존에 저장된 이미지 파일이 있음 + 수정시 파일 추가하지 않음 => 기존에 있던 이미지 그대로 출력
		    if ("true".equals(imageCheck) && !hasValidImage && imagePaths != null) {
		    	for (String imagePath : imagePaths) {
		    		ReviewImageDTO ri = new ReviewImageDTO();
		    		String fileName = imagePath;
		    		ri.setReviewId(reviewId); 
		    		ri.setImagePath(fileName); 
		    		reviewImageDao.addReviewImage(ri);	
		    	}
		    }
		 } catch (Exception e) { e.printStackTrace(); }
		
		// 마이페이지에서 리뷰 수정 시 redirect -> mypage로 이동
		String redirect = req.getParameter("redirect");
		if (redirect != null && !redirect.isEmpty()) { // redirect 지정값(마이페이지에서 수정한 경우)이 있는 경우 mypage로
			resp.sendRedirect("/semi-project/review/list");
		} else {
			resp.sendRedirect("/semi-project/burger/details?id="+burgerId);			
		}
	}

}
