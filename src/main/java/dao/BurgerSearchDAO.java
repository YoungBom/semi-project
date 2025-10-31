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
		List<BurgerDTO> burgerList = new ArrayList<BurgerDTO>();
		String sql = "SELECT * FROM burger ORDER BY brand , name";
		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				BurgerDTO b = new BurgerDTO();
				b.setName(rs.getString("name"));
				b.setBrand(rs.getNString("brand"));
				b.setPrice(rs.getInt("price"));
				b.setImagePath(rs.getString("image_path"));
				burgerList.add(b);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(conn , pstmt , rs);
		}
		
		return burgerList;
	}
	
	
	
	
}
