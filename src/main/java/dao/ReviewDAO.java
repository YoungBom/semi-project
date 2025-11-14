package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dto.BurgerDTO;
import dto.ReviewDTO;
import dto.ReviewImageDTO;
import dto.UserDTO;
import util.DBUtil;

public class ReviewDAO {
	
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
	
	// 유저 조인 필요
	public List<ReviewDTO> getReview(int burgerId) {
	    Map<Integer, ReviewDTO> reviewMap = new LinkedHashMap<>();
	    Connection conn = DBUtil.getConnection();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	    	String sql =
	    		    "SELECT r.id AS review_id, r.rating, r.content, r.created_at, r.updated_at, " +
	    		    "u.id AS user_id, u.nickname,u.profile_image , ri.image_path " +
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
	                review.setUserId(rs.getInt("user_id"));
	                review.setNickname(rs.getString("nickname"));
	                review.setUserProfileImg(rs.getString("profile_image"));
	                review.setContent(rs.getString("content"));
	                review.setCreatedAt(rs.getTimestamp("created_at"));
	                review.setUpdatedAt(rs.getTimestamp("updated_at"));
	                review.setRating(rs.getInt("rating"));
	                review.setImageList(new ArrayList<>());
	                reviewMap.put(reviewId, review);
	                
	            }

	            // ✅ 이미지 최대 3장까지만 저장
	            String imagePath = rs.getString("image_path");
	            
	            if(imagePath != null) {
	            	
	            	String lower = imagePath.toLowerCase();
	            	
	            	boolean allowed = 
	            		lower.endsWith(".jpg") ||
	            		lower.endsWith(".jpeg") ||
	            		lower.endsWith(".png") ||
	            		lower.endsWith(".gif");
	            	
	            	if ( allowed && review.getImageList().size() < 3) {
	            		review.getImageList().add(imagePath);
	            	}
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
	
	public int updateReview(ReviewDTO review) {	
		Connection conn = DBUtil.getConnection();
	    PreparedStatement pstmt = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    int result = 0;
	    
	    String sql = "";
	    try {
	    	sql ="UPDATE review "
	    			+ "SET content= ?, "
	    			+ "rating = ? "
	    			+ "WHERE burger_id = ? AND id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, review.getContent());
			pstmt.setDouble(2, review.getRating());
			pstmt.setInt(3, review.getBurgerId());
			pstmt.setInt(4, review.getId());			
			result = pstmt.executeUpdate();
			pstmt.close(); // ⚡ 중요: 닫고 새로 생성
			
			sql = "DELETE FROM review_image WHERE review_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, review.getId());
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        DBUtil.close(pstmt, conn);
	    }
		
		return result;
	}
	
	public List<ReviewDTO> listUpReview(int userId) {
		Connection conn = DBUtil.getConnection();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    
	    List<ReviewDTO> reviewList = new ArrayList<ReviewDTO>();
	    
	    try {
	    	String sql  = "SELECT r.id AS id, r.burger_id, "
	                   + "b.name AS burger_name, b.brand AS brand_name, "
	                   + "u.id AS user_id, u.name AS user_name, r.rating, "
	                   + "r.content, u.nickname, r.created_at, r.updated_at, "
	                   + "GROUP_CONCAT(ri.image_path) AS image_paths " // 이미지 여러장 한번에 가져오기
	                   + "FROM review r "
	                   + "JOIN burger b ON r.burger_id = b.id "
	                   + "JOIN user u ON r.user_id = u.id "
	                   + "LEFT JOIN review_image ri ON r.id = ri.review_id "
	                   + "WHERE r.user_id = ? "
	                   + "GROUP BY r.id "
	                   + "ORDER BY r.updated_at";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				ReviewDTO review = new ReviewDTO();
				review.setId(rs.getInt("id"));
				review.setBurgerId(rs.getInt("burger_id"));
				review.setburgerName(rs.getString("burger_name"));
				review.setBrand(rs.getString("brand_name"));
				review.setUserId(rs.getInt("user_id"));
				review.setUserName(rs.getString("user_name"));
				review.setRating(rs.getDouble("rating"));
				review.setContent(rs.getString("content"));
				review.setNickname(rs.getString("nickname"));
				review.setCreatedAt(rs.getTimestamp("created_at"));
				review.setUpdatedAt(rs.getTimestamp("updated_at"));
				
				// 이미지 문자열을 리스트로 변환
				// 여러장으로 묶인 이미지 문자열을 배열에 나눠서 저장하기
	            String imagePathsStr = rs.getString("image_paths");
	            if (imagePathsStr != null && !imagePathsStr.isEmpty()) {
	                review.setImageList(Arrays.asList(imagePathsStr.split(",")));
	            } else {
	                review.setImageList(new ArrayList<>());
	            }
				reviewList.add(review);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        DBUtil.close(rs, pstmt, conn);
	    }
	    return reviewList;
	}
	
	public List<ReviewDTO> getReviewByBrand(int userId, String brandType) {
		Connection conn = DBUtil.getConnection();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    List<ReviewDTO> reviewList = new ArrayList<ReviewDTO>();
	    
	    try {
	    	String sql = "SELECT r.id as id, "
	    			+ "burger_id, "
	    			+ "b.name as burger_name, "
	    			+ "b.brand as brand_name, "
	    			+ "u.id as user_id, "
	    			+ "u.name as user_name, "
	    			+ "r.rating, r.content, u.nickname, r.created_at, r.updated_at, "
	    			+ "GROUP_CONCAT(ri.image_path) AS image_list "
	    			+ "FROM review r "
	    			+ "JOIN burger b ON r.burger_id = b.id "
	    			+ "JOIN user u ON r.user_id = u.id "
	    			+ "LEFT JOIN review_image ri ON r.id = ri.review_id "
	    			+ "WHERE r.user_id = ? ";
	    			
	    			if (brandType != null && !brandType.trim().isEmpty()) {
	    		        sql += "AND b.brand LIKE ? ";
	    		    }
	    			sql += "GROUP BY r.id "
	    			+ "ORDER BY r.updated_at";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setInt(1, userId);
			if (brandType != null && !brandType.trim().isEmpty()) {
	            pstmt.setString(2, "%" + brandType.trim() + "%");
	        }
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ReviewDAO reviewDao = new ReviewDAO();
				ReviewDTO review = new ReviewDTO();
				
				review.setId(rs.getInt("id"));
				review.setBurgerId(rs.getInt("burger_id"));
				review.setburgerName(rs.getString("burger_name"));
				review.setBrand(rs.getString("brand_name"));
				review.setUserId(rs.getInt("user_id"));
				review.setUserName(rs.getString("user_name"));
				review.setRating(rs.getDouble("rating"));
				review.setContent(rs.getString("content"));
				review.setNickname(rs.getString("nickname"));
				review.setCreatedAt(rs.getTimestamp("created_at"));
				review.setUpdatedAt(rs.getTimestamp("updated_at"));
				
				// 이미지 문자열을 리스트로 변환
				// 여러장으로 묶인 이미지 문자열을 배열에 나눠서 저장하기
	            String imagePathsStr = rs.getString("image_list");
	            if (imagePathsStr != null && !imagePathsStr.isEmpty()) {
	                review.setImageList(Arrays.asList(imagePathsStr.split(",")));
	            } else {
	                review.setImageList(new ArrayList<>());
	            }
				
				reviewList.add(review);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reviewList;
	}
	
}
