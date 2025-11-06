package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dto.BurgerDTO;
import dto.ReviewDTO;
import dto.ReviewImageDTO;
import dto.UserDTO;
import util.DBUtil;

public class ReviewDAO {
	
	ReviewDTO review = new ReviewDTO();
	
	public int addReview(ReviewDTO rv) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int rs2 = 0;
		int reviewId = 0;
		
		try {
			String sql = "INSERT INTO review(burger_id, user_id, rating, content) VALUES (?,?,?,?)";
			pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, rv.getBurgerId());
			pstmt.setInt(2, rv.getUserId());
			pstmt.setDouble(3, rv.getRating());
			pstmt.setString(4, rv.getContent());
			pstmt.executeUpdate();	
			
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				reviewId = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if(conn != null) conn.close();} catch (SQLException e) {e.printStackTrace(); }
			try { if(pstmt != null) pstmt.close();} catch (SQLException e) {e.printStackTrace(); }
			try { if(rs != null) rs.close();} catch (SQLException e) {e.printStackTrace(); }
		}
		
		return reviewId;
	}
	
	// 유저, 버거 조인 필요
	public List<ReviewDTO> getReview(int burgerId) {
	    Map<Integer, ReviewDTO> reviewMap = new LinkedHashMap<>();
	    Connection conn = DBUtil.getConnection();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        String sql =
	            "SELECT r.id AS review_id, r.rating, r.content, r.created_at, u.nickname, ri.image_path " +
	            "FROM review r " +
	            "LEFT JOIN review_image ri ON r.id = ri.review_id " +
	            "JOIN user u ON r.user_id = u.id " +
	            "WHERE r.burger_id = ? " +
	            "ORDER BY r.created_at DESC";

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, burgerId);
	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            int reviewId = rs.getInt("review_id");
	            ReviewDTO review = reviewMap.get(reviewId);

	            if (review == null) {
	                review = new ReviewDTO();
	                review.setId(reviewId);
	                review.setNickname(rs.getString("nickname"));
	                review.setContent(rs.getString("content"));
	                review.setCreatedAt(rs.getTimestamp("created_at"));
	                review.setRating(rs.getInt("rating"));
	                review.setImageList(new ArrayList<>());
	                reviewMap.put(reviewId, review);
	            }

	            // ✅ 이미지 최대 3장까지만 저장
	            String imagePath = rs.getString("image_path");
	            if (imagePath != null && review.getImageList().size() < 3) {
	                review.getImageList().add(imagePath);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.close(rs, pstmt, conn);
	    }

	    return new ArrayList<>(reviewMap.values());
	}
	
	public int deleteReview(int burgerId, int reviewId) {
		Connection conn = DBUtil.getConnection();
	    PreparedStatement pstmt = null;
	    int result = 0;
	    
	    try {
	    	String sql = "DELETE FROM review WHERE burger_id = ? AND id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, burgerId);
			pstmt.setInt(2, reviewId);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
}
