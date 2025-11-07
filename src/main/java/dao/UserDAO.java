package dao;

import dto.UserDTO;
import util.DBUtil;

import java.sql.*;

public class UserDAO {

	private UserDTO mapRow(ResultSet rs) throws SQLException {
		UserDTO u = new UserDTO();
		u.setId(rs.getLong("id"));
		u.setUserId(rs.getString("user_id"));
		u.setEmail(rs.getString("email"));
		u.setName(rs.getString("name"));
		u.setNickname(rs.getString("nickname"));
		u.setPhone(rs.getString("phone"));
		u.setAddress(rs.getString("address"));
		u.setGender(rs.getString("gender"));
		u.setBirth(rs.getString("birth"));
		u.setPasswordHash(rs.getString("pw_hash"));
		u.setRole(rs.getString("role"));
		Timestamp c = rs.getTimestamp("created_at");
		Timestamp m = rs.getTimestamp("updated_at");
		u.setCreatedAt(c != null ? c.toLocalDateTime() : null);
		u.setUpdatedAt(m != null ? m.toLocalDateTime() : null);
		return u;
	}

	public UserDTO findByEmail(String email) throws SQLException {
		final String sql = "SELECT * FROM user WHERE email = ? LIMIT 1";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return mapRow(rs);
			}
		}
		return null;
	}

	public UserDTO findByPk(long id) throws SQLException {
		final String sql = "SELECT * FROM user WHERE id = ? LIMIT 1";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return mapRow(rs);
			}
		}
		return null;
	}

	public UserDTO findByLoginId(String userId) throws SQLException {
		final String sql = "SELECT * FROM user WHERE user_id = ? LIMIT 1";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return mapRow(rs);
			}
		}
		return null;
	}

	public boolean existsByEmail(String email) throws SQLException {
		final String sql = "SELECT 1 FROM user WHERE email = ? LIMIT 1";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	public long insert(UserDTO u) throws SQLException {
		final String sql = """
				INSERT INTO user
				(user_id, email, name, nickname, phone, address, gender, birth, pw_hash, role, created_at, updated_at)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
				""";
		try (Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			int i = 1;
			ps.setString(i++, u.getUserId());
			ps.setString(i++, u.getEmail());
			ps.setString(i++, u.getName());
			ps.setString(i++, u.getNickname());
			ps.setString(i++, u.getPhone());
			ps.setString(i++, u.getAddress());
			ps.setString(i++, u.getGender());
			ps.setString(i++, u.getBirth());
			ps.setString(i++, u.getPasswordHash());
			ps.setString(i++, u.getRole() != null ? u.getRole() : "USER");
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					return rs.getLong(1);
			}
		}
		return 0L;
	}

	public int updateProfile(long id, String email, String nickname, String phone, String birth, String gender,
			String address) throws SQLException {
		final String sql = """
				UPDATE user
				   SET email=?, nickname=?, phone=?, birth=?, gender=?, address=?, updated_at=NOW()
				 WHERE id=?
				""";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			int i = 1;
			ps.setString(i++, email);
			ps.setString(i++, nickname);
			ps.setString(i++, phone);
			ps.setString(i++, birth);
			ps.setString(i++, gender);
			ps.setString(i++, address);
			ps.setLong(i++, id);
			return ps.executeUpdate();
		}
	}

	public int updatePassword(long id, String newHash) throws SQLException {
		final String sql = "UPDATE user SET pw_hash=?, updated_at=NOW() WHERE id=?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, newHash);
			ps.setLong(2, id);
			return ps.executeUpdate();
		}
	}

	// (호환용) findById - 기존 코드가 사용하는 곳이 있을 수 있어 유지
	public UserDTO findById(long id) throws SQLException {
		final String sql = "SELECT * FROM `user` WHERE id = ? LIMIT 1";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return mapRow(rs);
			}
		}
		return null;
	}

	/** 아이디 중복확인 */
	public boolean existsByLoginId(String userId) {
		if (userId == null || userId.isBlank())
			return false;
		final String sql = "SELECT 1 FROM `user` WHERE `user_id` = ? LIMIT 1";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, userId.trim());
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// 별칭 메서드(외부 코드 호환 위해 유지)
	public long create(UserDTO u) throws SQLException {
		return insert(u);
	}

	/* ===== 전화번호 중복/업데이트 관련 ===== */

	/** 전화번호 존재 여부 */
	public boolean existsByPhone(String phone) throws SQLException {
		final String sql = "SELECT 1 FROM user WHERE phone = ? LIMIT 1";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, phone);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/** 특정 사용자 제외하고 전화번호 존재 여부 (수정 화면용) */
	public boolean existsByPhoneExceptUser(String phone, long excludeUserId) throws SQLException {
		final String sql = "SELECT 1 FROM user WHERE phone = ? AND id <> ? LIMIT 1";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, phone);
			ps.setLong(2, excludeUserId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/** 전화번호 단일 업데이트 (선택) */
	public int updatePhone(long id, String phone) throws SQLException {
		final String sql = "UPDATE user SET phone=?, updated_at=NOW() WHERE id=?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, phone);
			ps.setLong(2, id);
			return ps.executeUpdate();
		}
	}
}
