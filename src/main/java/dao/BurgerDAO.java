package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dto.BurgerDTO;
import dto.BurgerDetailsDTO;
import util.DBUtil;

public class BurgerDAO {
	
	// 버거 등록
	public int insertBurger(BurgerDTO burger, BurgerDetailsDTO burgerDetails) {
		String sql1 = "INSERT INTO burger (name, price, image_path, brand, patty_type)"
				+ "	VALUES (?, ?, ?, ?, ?)";
		String sql2 = "INSERT INTO burger (burger_id, calories, carbohydrates, protein, fat, sodium, sugar, allergy_info)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		
		try {
			conn = DBUtil.getConnection();
			pstmt1 = conn.prepareStatement(sql1);
			pstmt1.setString(1, burger.getName());	
			pstmt1.setInt(2, burger.getPrice());	
			pstmt1.setString(3, burger.getImagePath());	
			pstmt1.setString(4, burger.getBrand());	
			pstmt1.setString(5, burger.getPattyType());
			pstmt1.executeUpdate();
			
			rs = pstmt1.getGeneratedKeys();
			int burgerId = 0;
			if (rs.next()) burgerId = rs.getInt(1);
			
			pstmt2 = conn.prepareStatement(sql2);
			pstmt2.setInt(1, burgerId);
			pstmt2.setInt(2, burgerDetails.getCalories());
			pstmt2.setInt(3, burgerDetails.getCarbohydrates());
			pstmt2.setInt(4, burgerDetails.getProtein());
			pstmt2.setInt(5, burgerDetails.getFat());
			pstmt2.setInt(6, burgerDetails.getSodium());
			pstmt2.setInt(7, burgerDetails.getSugar());
			pstmt2.setString(8, burgerDetails.getAllergyInfo());
			pstmt2.executeUpdate();
			
			return 1;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("dao 예외발생");
			return 0;
		} finally {
			DBUtil.close(rs, pstmt1, pstmt2, conn);
		}
		
	}
	
	// 버거 수정
	
	
	// 버거 삭제
}
