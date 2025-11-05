package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
	public List<ReviewDTO> getReview(int burgerId, int userId) {		
		List<ReviewDTO> recordList = new ArrayList<ReviewDTO>();
		Connection conn = DBUtil.getConnection();
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		try {
			String sql = "SELECT "
					+ "r.id as review_id,"
					+ "b.id as burger_id,"
					+ "u.id as user_id,"
					+ "rating,"
					+ "content,"
					+ "created_at,"
					+ "updated_at,"
					+ "ri.image_path as image_path,"
					+ "nickname"
					+ "FROM review r"
					+ "RIGHT JOIN review_image ri ON r.id=ri.review_id"
					+ "JOIN burger b ON ? = b.id"
					+ "JOIN user u ON r.? = u.id;";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, burgerId);
			pstmt.setInt(2, userId);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ReviewDTO review = new ReviewDTO();
				// 리뷰아이디 추가하기(리뷰아이디 즉 게시물등록한id)가 똑같으면 사진을 list로 배열
				review.setNickname(rs.getString("nickname"));
				review.setContent(rs.getString("content"));
				Timestamp createdAtTime = rs.getTimestamp("created_at");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				review.setWriteCreatedAtTime(sdf.format(createdAtTime));
				review.setImagePath(rs.getString("image_path"));
				
				recordList.add(review);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return recordList;
	}
}
