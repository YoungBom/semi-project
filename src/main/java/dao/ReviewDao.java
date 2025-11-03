package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.BurgerDTO;
import dto.ReviewDTO;
import dto.ReviewImageDTO;
import dto.UserDTO;
import util.DBUtil;

public class ReviewDao {
	
	ReviewDTO review = new ReviewDTO();
	ReviewImageDTO reviewImage = new ReviewImageDTO();
	
	public int addReview(ReviewDTO rv) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		int rs2 = 0;
		int reviewId = 0;
		
		try {
			String sql = "INSERT INTO review(burger_id, user_id, rating, content) VALUES (?,?,?,?)";
			pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, rv.getBurgerId());
			pstmt.setInt(2, rv.getBurgerId());
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
			try { if(stmt != null) stmt.close();} catch (SQLException e) {e.printStackTrace(); }
			try { if(rs != null) rs.close();} catch (SQLException e) {e.printStackTrace(); }
		}
		
		return reviewId;
	}
	
//	public List<ReviewDTO> getAllReview() {		
//		
//	}
}
