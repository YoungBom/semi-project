package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.BurgerDTO;
import util.DBUtil;

public class BurgerSearchDAO {
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	// 전체 버거 조회 메소드
	public List<BurgerDTO> getAllburger() {
		List<BurgerDTO> list= new ArrayList<BurgerDTO>();
		String sql = "SELECT * FROM burger ORDER BY brand , name";
		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				BurgerDTO burger = new BurgerDTO();
				burger.setId(rs.getInt("id"));
	            burger.setName(rs.getString("name"));
	            burger.setPrice(rs.getInt("price"));
	            burger.setBrand(rs.getString("brand"));
	            burger.setImagePath(rs.getString("image_path"));
	            burger.setNewBurger(rs.getBoolean("is_new"));
	            burger.setPattyType(rs.getString("patty_type"));
	            burger.setAvgRating(rs.getDouble("avg_rating"));
				list.add(burger);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(conn , pstmt , rs);
		}
		
		return list;
	}
	

	
}
