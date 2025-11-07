package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.ReviewImageDTO;
import util.DBUtil;

public class ReviewImageDAO {
	
	public void addReviewImage (ReviewImageDTO ri) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		int rs2 = 0;
		
		try {
			String sql = "INSERT INTO review_image(review_id, image_path) VALUES (?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ri.getReviewId());
			pstmt.setString(2, ri.getImagePath());
			pstmt.executeUpdate();
		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(conn != null) conn.close();} catch (SQLException e) {e.printStackTrace(); }
			try { if(pstmt != null) pstmt.close();} catch (SQLException e) {e.printStackTrace(); }
			try { if(stmt != null) stmt.close();} catch (SQLException e) {e.printStackTrace(); }
			try { if(rs != null) rs.close();} catch (SQLException e) {e.printStackTrace(); }
		}
	}
	
	public void cleanReviewImage(int reviewId) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		
		try {
			String sql ="UPDATE review_image"
					+ "SET image_path = '' "
					+ "WHERE review_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reviewId);
			rs = pstmt.executeQuery();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { if(conn != null) conn.close();} catch (SQLException e) {e.printStackTrace(); }
			try { if(pstmt != null) pstmt.close();} catch (SQLException e) {e.printStackTrace(); }
			try { if(rs != null) rs.close();} catch (SQLException e) {e.printStackTrace(); }
		}
	}
	
	public void updateReviewImage(ReviewImageDTO ri) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		
		// 1. 기존의 image_path를 모두 ""(빈값)으로 설정한다.
		// 2. 기존의 image_path에 새로운 image_path를 설정한다. 
		try {			
			String sql ="UPDATE review_image"
					+ "SET image_path = ? "
					+ "WHERE review_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ri.getImagePath());
			pstmt.setInt(2, ri.getReviewId());
			count = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
