package dao;

import model.User;
import util.DBUtil;

import java.sql.*;

public class PasswordForgotDAO {

	// 이메일로 사용자 조회 //
	public User findUserByEmail(String email) throws SQLException {
		String sql = "SELECT * FROM `user` WHERE email = ?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			if (con == null)
				throw new SQLException("DB connection is null");
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? map(rs) : null;
			}
		}
	}

	// 로그인 아이디로 사용자 조회 //
	public User findUserByLoginId(String loginId) throws SQLException {
		String sql = "SELECT * FROM `user` WHERE user_id = ?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			if (con == null)
				throw new SQLException("DB connection is null");
			ps.setString(1, loginId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? map(rs) : null;
			}
		}
	}

	// 요청 로그 적재(선택 기능) — 로그 테이블 없으면 호출하지 마세요 //
	public void insertRequestLog(Integer userId, String email, String loginId, String clientIp) throws SQLException {
		String sql = "INSERT INTO password_forgot_log(user_id, email, login_id, client_ip) VALUES (?,?,?,?)";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			if (con == null)
				throw new SQLException("DB connection is null");
			if (userId == null)
				ps.setNull(1, Types.INTEGER);
			else
				ps.setInt(1, userId);
			ps.setString(2, email);
			ps.setString(3, loginId);
			ps.setString(4, clientIp);
			ps.executeUpdate();
		}
	}

	// --- ResultSet -> User 매핑 (UserDao의 map과 동일 규칙) ---
	private User map(ResultSet rs) throws SQLException {
		User u = new User();
		u.setId(rs.getInt("id"));
		u.setUser_id(rs.getString("user_id"));
		u.setUser_pw(rs.getString("user_pw"));
		u.setEmail(rs.getString("email"));
		u.setPhone(rs.getString("phone"));
		u.setBirth(rs.getString("birth"));
		u.setGender(rs.getString("gender"));
		u.setName(rs.getString("name"));
		u.setNickname(rs.getString("nickname"));
		u.setAddress(rs.getString("address"));
		return u;
	}
}
