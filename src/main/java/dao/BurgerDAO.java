package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.BurgerDTO;
import dto.BurgerDetailsDTO;
import util.DBUtil;

public class BurgerDAO {
	
	// 버거 등록
	public int insertBurger(BurgerDTO burger, BurgerDetailsDTO burgerDetails) {
		String sql1 = "INSERT INTO burger (name, price, image_path, brand, patty_type)"
				+ "	VALUES (?, ?, ?, ?, ?)";
		String sql2 = "INSERT INTO burger_details (burger_id, calories, carbohydrates, protein, fat, sodium, sugar, allergy_info)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		
		try (Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement(sql1 , Statement.RETURN_GENERATED_KEYS);
			PreparedStatement pstmt2 =conn.prepareStatement(sql2)) {
			
			pstmt1.setString(1, burger.getName());	
			pstmt1.setInt(2, burger.getPrice());	
			pstmt1.setString(3, burger.getImagePath());	
			pstmt1.setString(4, burger.getBrand());	
			pstmt1.setString(5, burger.getPattyType());
			pstmt1.executeUpdate();
			
			ResultSet rs = pstmt1.getGeneratedKeys();
			int burgerId = 0;
			if (rs.next()) burgerId = rs.getInt(1);
			
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
			return 0;
		}
	}
	
	// 버거 수정
		// 단일 버거 + 버거디테일 불러오기
	public BurgerDTO getBurgerById(int id) {
		String sql = """ 
				SELECT b.id, b.name, b.price, b.image_path, b.brand, b.patty_type, b.is_new,
					d.calories, d.carbohydrates, d.protein, d.fat, d.sodium, d.sugar, d.allergy_info
				FROM burger b
				JOIN burger_details d ON b.id = d.burger_id
				WHERE b.id = ?
				""";
		
		BurgerDTO burger = null;
		
		try (Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setInt(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					burger = new BurgerDTO();
					burger.setId(rs.getInt("id"));
					burger.setName(rs.getString("name"));
					burger.setPrice(rs.getInt("price"));
					burger.setImagePath(rs.getString("image_path"));
					burger.setBrand(rs.getString("brand"));
					burger.setPattyType(rs.getString("patty_type"));
					burger.setNewBurger(rs.getBoolean("is_new"));
					
					BurgerDetailsDTO details = new BurgerDetailsDTO();
					details.setCalories(rs.getInt("calories"));
					details.setCarbohydrates(rs.getInt("carbohydrates"));
					details.setProtein(rs.getInt("protein"));
					details.setFat(rs.getInt("fat"));
					details.setSodium(rs.getInt("sodium"));
					details.setSugar(rs.getInt("sugar"));
					details.setAllergyInfo(rs.getString("allergy_info"));
					burger.setDetails(details);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("getBurgerById 예외 발생");
		}
		return burger;
	}
		// 버거 업데이트
	public int updateBurger(BurgerDTO burger, BurgerDetailsDTO burgerDetails) {
	    String sql1 = "UPDATE burger SET name=?, price=?, image_path=?, brand=?, patty_type=? WHERE id=?";
	    String sql2 = "UPDATE burger_details SET calories=?, carbohydrates=?, protein=?, fat=?, sodium=?, sugar=?, allergy_info=? WHERE burger_id=?";
		
		try (Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement(sql1);
			PreparedStatement pstmt2 =conn.prepareStatement(sql2)) {
			
			pstmt1.setString(1, burger.getName());	
			pstmt1.setInt(2, burger.getPrice());	
			pstmt1.setString(3, burger.getImagePath());	
			pstmt1.setString(4, burger.getBrand());	
			pstmt1.setString(5, burger.getPattyType());
			pstmt1.setInt(6, burger.getId());
			pstmt1.executeUpdate();
			
	        pstmt2.setInt(1, burgerDetails.getCalories());
	        pstmt2.setInt(2, burgerDetails.getCarbohydrates());
	        pstmt2.setInt(3, burgerDetails.getProtein());
	        pstmt2.setInt(4, burgerDetails.getFat());
	        pstmt2.setInt(5, burgerDetails.getSodium());
	        pstmt2.setInt(6, burgerDetails.getSugar());
	        pstmt2.setString(7, burgerDetails.getAllergyInfo());
	        pstmt2.setInt(8, burger.getId());
	        pstmt2.executeUpdate();
			
			return 1;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("updateBuger 예외발생");
			return 0;
		}
	}
	// 모든 버거 조회
	public List<BurgerDTO> getAllBurgers() {
		List<BurgerDTO> list = new ArrayList<>();
		String sql = "SELECT * FROM burger ORDER BY id DESC";
		
		try (Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery()) {
			
	        while (rs.next()) {
	            BurgerDTO burger = new BurgerDTO();
	            burger.setId(rs.getInt("id"));
	            burger.setName(rs.getString("name"));
	            burger.setPrice(rs.getInt("price"));
	            burger.setBrand(rs.getString("brand"));
	            burger.setImagePath(rs.getString("image_path"));
	            burger.setNewBurger(rs.getBoolean("is_new"));
	            list.add(burger);
	        }
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// new
	public void updateIsNew(int id, boolean isNew) {
	    String sql = "UPDATE burger SET is_new = ? WHERE id = ?";
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setBoolean(1, isNew);
	        pstmt.setInt(2, id);
	        pstmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	// 버거 삭제
	public int deleteBurger(int id) {
	    int result = 0;
	    String sql = "DELETE FROM burger WHERE id = ?";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setInt(1, id);
	        result = pstmt.executeUpdate();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}
}
