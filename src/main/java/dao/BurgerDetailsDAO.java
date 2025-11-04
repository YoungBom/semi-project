package dao;

import java.sql.*;
import dto.BurgerDTO;
import dto.BurgerDetailsDTO;
import util.DBUtil;

public class BurgerDetailsDAO {

    // 버거 기본 정보 조회
    public BurgerDTO getBurgerById(int id) {
        BurgerDTO burger = null;
        String sql = "SELECT * FROM burger WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                burger = new BurgerDTO();
                burger.setId(rs.getInt("id"));
                burger.setName(rs.getString("name"));
                burger.setPrice(rs.getInt("price"));
                burger.setBrand(rs.getString("brand"));
                burger.setImagePath(rs.getString("image_path"));
                burger.setPattyType(rs.getString("patty_type"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return burger;
    }

    // 버거 상세 정보 조회
    public BurgerDetailsDTO getBurgerDetailsById(int burgerId) {
        BurgerDetailsDTO details = null;
        String sql = "SELECT * FROM burger_details WHERE burger_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, burgerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                details = new BurgerDetailsDTO();
                details.setCalories(rs.getInt("calories"));
                details.setCarbohydrates(rs.getInt("carbohydrates"));
                details.setProtein(rs.getInt("protein"));
                details.setFat(rs.getInt("fat"));
                details.setSodium(rs.getInt("sodium"));
                details.setSugar(rs.getInt("sugar"));
                details.setAllergyInfo(rs.getString("allergy_info"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return details;
    }
}
