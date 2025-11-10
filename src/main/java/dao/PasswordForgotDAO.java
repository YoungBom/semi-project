package dao;

import dto.UserDTO;
import util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PasswordForgotDAO {

    private static final String BASE_SELECT =
        "SELECT id, user_id, pw_hash, user_pw, email, phone, birth, gender, name, nickname, address, role FROM `user` ";
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public UserDTO findUserByEmail(String email) throws SQLException {
        String sql = BASE_SELECT + "WHERE email = ?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        }
    }

    public UserDTO findUserByLoginId(String loginId) throws SQLException {
        String sql = BASE_SELECT + "WHERE user_id = ?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, loginId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        }
    }

    public void insertRequestLog(Integer userId, String email, String loginId, String clientIp) {
        String sql = "INSERT INTO password_forgot_log(user_id, email, login_id, client_ip) VALUES (?,?,?,?)";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (userId == null) ps.setNull(1, Types.INTEGER); else ps.setInt(1, userId);
            ps.setString(2, email);
            ps.setString(3, loginId);
            ps.setString(4, clientIp);
            ps.executeUpdate();
        } catch (SQLException ignore) { /* 옵션 테이블이면 무시 */ }
    }

    private UserDTO map(ResultSet rs) throws SQLException {
        UserDTO u = new UserDTO();
        u.setId(rs.getInt("id"));
        u.setUserId(rs.getString("user_id"));
        u.setPwHash(rs.getString("pw_hash"));
        u.setUserPw(rs.getString("user_pw"));
        u.setEmail(rs.getString("email"));
        u.setPhone(rs.getString("phone"));
        String b = rs.getString("birth");
        if (b != null && !b.isBlank()) {
            try { u.setBirth(LocalDate.parse(b, DF)); } catch (Exception ignore) { u.setBirth(null); }
        }
        u.setGender(rs.getString("gender"));
        u.setName(rs.getString("name"));
        u.setNickname(rs.getString("nickname"));
        u.setAddress(rs.getString("address"));
        u.setRole(rs.getString("role"));
        return u;
    }
}
