package dao;

import dto.UserDTO;

import java.sql.*;

public class PasswordForgotDAO {

	// 이메일로 사용자 조회
	public UserDTO findUserByEmail(String email) throws SQLException {
		String sql = "SELECT * FROM `user` WHERE email = ?";
		try (Connection con = DataSourceProvider.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? map(rs) : null;
			}
		}
	}

	// 로그인 아이디로 사용자 조회
	public UserDTO findUserByLoginId(String loginId) throws SQLException {
		String sql = "SELECT * FROM `user` WHERE user_id = ?";
		try (Connection con = DataSourceProvider.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, loginId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? map(rs) : null;
			}
		}
	}

	// 요청 로그 적재(옵션) — 테이블이 있을 때만 사용
	public void insertRequestLog(Long userId, String email, String loginId, String clientIp) throws SQLException {
		String sql = "INSERT INTO password_forgot_log(user_id, email, login_id, client_ip) VALUES (?,?,?,?)";
		try (Connection con = DataSourceProvider.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			if (userId == null)
				ps.setNull(1, Types.BIGINT);
			else
				ps.setLong(1, userId);
			ps.setString(2, email);
			ps.setString(3, loginId);
			ps.setString(4, clientIp);
			ps.executeUpdate();
		}
	}

	// --- ResultSet -> UserDTO 매핑 ---
	private UserDTO map(ResultSet rs) throws SQLException {
		UserDTO u = new UserDTO();
		u.setId(rs.getLong("id"));
		u.setUserId(rs.getString("user_id"));
		u.setPasswordHash(rs.getString("pw_hash")); // 해시 컬럼명 주의
		u.setEmail(rs.getString("email"));
		u.setPhone(rs.getString("phone"));
		u.setBirth(rs.getString("birth"));
		u.setGender(rs.getString("gender"));
		u.setName(rs.getString("name"));
		u.setNickname(rs.getString("nickname"));
		u.setAddress(rs.getString("address"));
		Timestamp c = rs.getTimestamp("created_at");
		Timestamp m = rs.getTimestamp("updated_at");
		if (c != null)
			u.setCreatedAt(c.toLocalDateTime());
		if (m != null)
			u.setUpdatedAt(m.toLocalDateTime());
		u.setRole(rs.getString("role"));
		return u;
	}
}
