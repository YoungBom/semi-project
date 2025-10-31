package review.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DBUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.swing.text.DateFormatter;

import dto.BurgerDTO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.*;

@WebServlet("/ReviewController")
@MultipartConfig(
		fileSizeThreshold = 1024 * 1024 * 1,   // 메모리 임시 저장 임계값(1MB) -> 이 크기 초과 시 디스크에 임시 저장
		maxFileSize = 1024 * 1024 * 10,        // 업로드 최대 파일 크기(10MB)
		maxRequestSize = 1024 * 1024 * 50      // 전체 요청 크기(50MB)
)
public class ReviewController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override 
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		// 리뷰 정보 가져오기
		
		int burgerId = 0;
		int userId = 0;
		String content = req.getParameter("content");
		String unitRating = req.getParameter("rating");
		
		Double rating = 0.0;
		if (unitRating != null || !unitRating.isEmpty()) {
			rating = Double.parseDouble(unitRating);
		}		
		
		UserDTO user = new UserDTO();
		BurgerDTO burger = new BurgerDTO();
		
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		int rs3 = 0;

		String sql = "";
		String sq2 = "";
		try(
			Connection conn = DBUtil.getConnection();
				) 
		{
			// 추후 상세페이지에서 버거 id 정보 전달받아 동적 바인딩하기
			sql = "SELECT * FROM burger WHERE id = 1";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			// 추후 로그인되면 사용자 id 정보 전달받아 동적 바인딩하기
			sq2 = "SELECT * FROM user WHERE id = 1";
			stmt = conn.createStatement();
			rs2 = stmt.executeQuery(sq2);
			
			if (rs.next() && rs2.next()) {
				burgerId = rs.getInt("id");
				userId = rs2.getInt("id");
			}
			
			sql = "INSERT INTO review(burger_id, user_id, rating, content) VALUES (?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, burgerId);
			pstmt.setInt(2, userId);
			pstmt.setDouble(3, rating);
			pstmt.setString(4, content);

			rs3 = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("⚠️ 잘못된 평점 값 입력: " + unitRating);
		    rating = 0.0; // 혹은 디폴트값
		}
		
		resp.sendRedirect("review.jsp");
		
		
	}
	
}
