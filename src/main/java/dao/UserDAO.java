package dao;

import dto.UserDTO;
import util.DBUtil;
import util.PasswordUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class UserDAO {

	private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	// ===== 공용 매핑 =====
	private UserDTO map(ResultSet rs) throws SQLException {
		UserDTO u = new UserDTO();
		u.setId(rs.getInt("id")); // PK(Integer)
		u.setUserId(rs.getString("user_id")); // 로그인용 아이디
		u.setPwHash(rs.getString("pw_hash")); // 해시(신규)
		u.setUserPw(rs.getString("user_pw")); // legacy(Nullable)
		u.setEmail(rs.getString("email"));
		u.setPhone(rs.getString("phone"));

		String b = rs.getString("birth"); // DB: VARCHAR(yyyy-MM-dd)
		if (b != null && !b.isBlank()) {
			try {
				u.setBirth(LocalDate.parse(b, DF));
			} catch (Exception ignore) {
				u.setBirth(null);
			}
		}

		u.setGender(rs.getString("gender"));
		u.setName(rs.getString("name"));
		u.setNickname(rs.getString("nickname"));
		u.setAddress(rs.getString("address"));
		u.setRole(rs.getString("role"));
		return u;
	}

	// ===== 존재 여부 =====
	public boolean existsByLoginId(String userId) {	
		String sql = "SELECT 1 FROM `user` WHERE BINARY user_id = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean existsByEmail(String email) {
		String sql = "SELECT 1 FROM `user` WHERE email=?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// ===== 조회 =====
	public Optional<UserDTO> findByLoginId(String loginId) {
		 String sql = "SELECT * FROM `user` WHERE BINARY user_id = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, loginId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? Optional.of(map(rs)) : Optional.empty();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Optional<UserDTO> findById(int id) {
		String sql = "SELECT * FROM `user` WHERE id=?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? Optional.of(map(rs)) : Optional.empty();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Optional<UserDTO> findByEmail(String email) {
		String sql = "SELECT * FROM `user` WHERE email=?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? Optional.of(map(rs)) : Optional.empty();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Optional<String> findLoginIdByNameAndEmail(String name, String email) {
		String sql = "SELECT user_id FROM `user` WHERE name=? AND email=?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, name);
			ps.setString(2, email);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? Optional.of(rs.getString(1)) : Optional.empty();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// ===== 생성 =====
	/** 생성된 PK(int) 반환, 실패 시 0 */
	public int create(UserDTO in, String rawPassword) {
		String sql = "INSERT INTO `user` (user_id,pw_hash,user_pw,email,phone,birth,gender,name,nickname,address,role) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		String hash = PasswordUtil.hash(rawPassword);
		try (Connection c = DBUtil.getConnection();
				PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, in.getUserId());
			ps.setString(2, hash);
			ps.setNull(3, Types.VARCHAR); // legacy 칸은 비움
			ps.setString(4, in.getEmail());
			ps.setString(5, in.getPhone());
			ps.setString(6, in.getBirth() != null ? in.getBirth().format(DF) : null);
			ps.setString(7, in.getGender());
			ps.setString(8, in.getName());
			ps.setString(9, in.getNickname());
			ps.setString(10, in.getAddress());
			ps.setString(11, in.getRole() != null ? in.getRole() : "USER");

			ps.executeUpdate();
			try (ResultSet keys = ps.getGeneratedKeys()) {
				if (keys.next()) {
					int id = keys.getInt(1);
					in.setId(id);
					in.setPwHash(hash);
					in.setUserPw(null);
					return id;
				}
			}
			return 0;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// ===== 인증 =====
	public Optional<UserDTO> authenticate(String loginId, String rawPassword) {
		Optional<UserDTO> opt = findByLoginId(loginId);
		if (opt.isEmpty())
			return Optional.empty();

		UserDTO u = opt.get();
		String stored = (u.getPwHash() != null && !u.getPwHash().isEmpty()) ? u.getPwHash() : u.getUserPw();
		if (stored == null || stored.isEmpty())
			return Optional.empty();

		return PasswordUtil.verify(rawPassword, stored) ? Optional.of(u) : Optional.empty();
	}

	// ===== 프로필 수정 =====
	public boolean updateProfile(int uid, String email, String phone, String birth, String gender, String name,
			String nickname, String address) {
		String sql = "UPDATE `user` SET email=?, phone=?, birth=?, gender=?, name=?, nickname=?, address=? "
				+ "WHERE id=?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, email);
			ps.setString(2, phone);
			ps.setString(3, birth);
			ps.setString(4, gender);
			ps.setString(5, name);
			ps.setString(6, nickname);
			ps.setString(7, address);
			ps.setInt(8, uid);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// ===== 비밀번호 변경 / 재설정 =====
	public boolean changePassword(int uid, String newRawPassword) {
		String newHash = PasswordUtil.hash(newRawPassword);
		String sql = "UPDATE `user` SET pw_hash=?, user_pw=NULL WHERE id=?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, newHash);
			ps.setInt(2, uid);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static String genToken() {
		byte[] buf = new byte[32];
		new java.security.SecureRandom().nextBytes(buf);
		StringBuilder sb = new StringBuilder(64);
		for (byte b : buf)
			sb.append(String.format("%02x", b));
		return sb.toString();
	}

	public Optional<String> createPasswordResetTokenByLoginIdOrEmail(String loginOrEmail, int ttlMinutes) {
		Integer uid = null;
		String find = "SELECT id FROM `user` WHERE BINARY user_id = ? OR email = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(find)) {
			ps.setString(1, loginOrEmail);
			ps.setString(2, loginOrEmail);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					uid = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		if (uid == null)
			return Optional.empty();

		String token = genToken();
		String ins = "INSERT INTO password_reset(user_id, token, expires_at) VALUES(?,?, DATE_ADD(NOW(), INTERVAL ? MINUTE))";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(ins)) {
			ps.setInt(1, uid);
			ps.setString(2, token);
			ps.setInt(3, ttlMinutes);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return Optional.of(token);
	}

	public boolean updatePasswordByToken(String token, String newRawPassword) {
		try (Connection c = DBUtil.getConnection()) {
			c.setAutoCommit(false);

			Integer uid = null;
			String sel = "SELECT user_id FROM password_reset WHERE token=? AND used_at IS NULL AND expires_at>NOW() FOR UPDATE";
			try (PreparedStatement ps = c.prepareStatement(sel)) {
				ps.setString(1, token);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next())
						uid = rs.getInt(1);
				}
			}
			if (uid == null) {
				c.rollback();
				return false;
			}

			String newHash = PasswordUtil.hash(newRawPassword);
			try (PreparedStatement ps = c.prepareStatement("UPDATE `user` SET pw_hash=?, user_pw=NULL WHERE id=?")) {
				ps.setString(1, newHash);
				ps.setInt(2, uid);
				ps.executeUpdate();
			}
			try (PreparedStatement ps = c.prepareStatement("UPDATE password_reset SET used_at=NOW() WHERE token=?")) {
				ps.setString(1, token);
				ps.executeUpdate();
			}

			c.commit();
			return true;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// ===== 자동로그인(remember_me) =====
	public String issueRememberToken(int uid, int days) {
		String token = java.util.UUID.randomUUID().toString().replace("-", "");
		String sql = "INSERT INTO remember_me(user_id, token, expires_at) "
				+ "VALUES(?, ?, DATE_ADD(NOW(), INTERVAL ? DAY))";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, uid);
			ps.setString(2, token);
			ps.setInt(3, days);
			ps.executeUpdate();
			return token;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Optional<UserDTO> findByRememberToken(String token) {
		String sql = "SELECT u.* FROM remember_me r " + "JOIN `user` u ON r.user_id = u.id "
				+ "WHERE r.token=? AND r.expires_at > NOW()";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, token);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? Optional.of(map(rs)) : Optional.empty();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteRememberToken(String token) {
		String sql = "DELETE FROM remember_me WHERE token=?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, token);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public boolean existsByUserId(String userId) {
	    boolean exists = false;
	    String sql = "SELECT COUNT(*) FROM user WHERE user_id = ?";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, userId);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                exists = rs.getInt(1) > 0;
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return exists;
	}
}








