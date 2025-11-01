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
import dto.UserDTO;
import util.DBUtil;

public class ReviewDao {
	
	List<ReviewDTO> rvList = new ArrayList<ReviewDTO>();
	ReviewDTO review = new ReviewDTO();
	
	Connection conn = DBUtil.getConnection();
	PreparedStatement pstmt = null;
	Statement stmt = null;
	ResultSet rs = null;
	int rs2 = 0;
	
	public void addReview(ReviewDTO rv) {
		try {
			String sql = "INSERT INTO review(burger_id, user_id, rating, content) VALUES (?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, rv.getBurgerId());
			pstmt.setInt(2, rv.getBurgerId());
			pstmt.setDouble(3, rv.getRating());
			pstmt.setString(4, rv.getContent());
			pstmt.executeUpdate();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rvList.add(rv);
	}
}
