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
	
	public List<String> findReviewImage (int reviewId) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		List<String> images = new ArrayList<>();
		
		try {
			String sql = "SELECT image_path FROM review_image WHERE review_id = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reviewId);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				images.add(rs.getString("image_path"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return images;
	}

}
