package dao;

import dto.UserDTO;
import util.DBUtil;
import util.PasswordUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * UserDAO - 책임: user, (선택) password_reset 관련 DB 접근 - 규칙: DB 연결은
 * DBUtil.getConnection(), try-with-resources 사용 - DTO 세터는 camelCase 사용
 */
public class UserDAO {

	private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	// ======================================================================
	// 0) 공용 Row → DTO 매핑
	// ======================================================================
	private UserDTO map(ResultSet rs) throws SQLException {
		UserDTO u = new UserDTO();
		u.setId(rs.getLong("id")); // PK(Long)
		u.setUserId(rs.getString("user_id")); // 로그인 아이디
		u.setPwHash(rs.getString("pw_hash")); // 새 해시
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
		try {
			u.setCreatedAt(rs.getTimestamp("created_at"));
		} catch (SQLException ignore) {
		}
		try {
			u.setUpdatedAt(rs.getTimestamp("updated_at"));
		} catch (SQLException ignore) {
		}
		return u;
	}

	// ======================================================================
	// 1) 존재 여부 체크 (회원가입 중복검사, 폼 유효성)
	// ======================================================================
	/** user_id 중복 여부 (대소문자 구분/BINARY) */
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

	/** email 중복 여부 */
	public boolean existsByEmail(String email) {
		String sql = "SELECT 1 FROM `user` WHERE email = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/** user_id 존재 여부 (COUNT 기반) */
	public boolean existsByUserId(String userId) {
		String sql = "SELECT COUNT(*) FROM `user` WHERE user_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() && rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// ======================================================================
	// 2) 조회(Select) 계열
	// ======================================================================
	/** user_id로 단건 조회 (로그인/세션 재검증 등) */
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

	/** PK(id)로 단건 조회 (마이페이지) */
	public Optional<UserDTO> findById(int id) {
		String sql = "SELECT * FROM `user` WHERE id = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? Optional.of(map(rs)) : Optional.empty();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/** email로 단건 조회 (프로필/중복/검증) */
	public Optional<UserDTO> findByEmail(String email) {
		String sql = "SELECT * FROM `user` WHERE email = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? Optional.of(map(rs)) : Optional.empty();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/** 아이디 찾기: 이메일 + 닉네임 일치 시 user 정보 반환 */
	public UserDTO findByEmailAndNickname(String email, String nickname) throws Exception {
		String sql = "SELECT id, user_id, email, nickname, pw_hash, role, created_at, updated_at "
				+ "FROM `user` WHERE email = ? AND nickname = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, email);
			ps.setString(2, nickname);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					UserDTO u = new UserDTO();
					u.setId(rs.getLong("id"));
					u.setUserId(rs.getString("user_id"));
					u.setEmail(rs.getString("email"));
					u.setNickname(rs.getString("nickname"));
					u.setPwHash(rs.getString("pw_hash"));
					u.setRole(rs.getString("role"));
					u.setCreatedAt(rs.getTimestamp("created_at"));
					u.setUpdatedAt(rs.getTimestamp("updated_at"));
					return u;
				}
			}
		}
		return null;
	}

	/** 비밀번호 찾기 1단계: user_id + email 일치 검증용 조회 */
	public UserDTO findByLoginIdAndEmail(String userId, String email) throws Exception {
		String sql = "SELECT id, user_id, email, nickname, pw_hash, role, created_at, updated_at "
				+ "FROM `user` WHERE user_id = ? AND email = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, userId);
			ps.setString(2, email);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					UserDTO u = new UserDTO();
					u.setId(rs.getLong("id"));
					u.setUserId(rs.getString("user_id"));
					u.setEmail(rs.getString("email"));
					u.setNickname(rs.getString("nickname"));
					u.setPwHash(rs.getString("pw_hash"));
					u.setRole(rs.getString("role"));
					u.setCreatedAt(rs.getTimestamp("created_at"));
					u.setUpdatedAt(rs.getTimestamp("updated_at"));
					return u;
				}
			}
		}
		return null;
	}

	/** 아이디 찾기: 이름 + 이메일로 user_id 단건 조회 (서블릿에서 Optional로 다루기 편함) */
	public Optional<String> findLoginIdByNameAndEmail(String name, String email) {
		String sql = "SELECT user_id FROM `user` WHERE name = ? AND email = ? LIMIT 1";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, name);
			ps.setString(2, email);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return Optional.of(rs.getString(1));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return Optional.empty();
	}

	// ======================================================================
	// 3) 생성(Insert)
	// ======================================================================
	/** 회원가입: 생성된 PK(id) 반환, 실패 시 0 */
	public int create(UserDTO in, String rawPassword) {
		String sql = "INSERT INTO `user` "
				+ "(user_id, pw_hash, user_pw, email, phone, birth, gender, name, nickname, address, role) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		String hash = PasswordUtil.hash(rawPassword);
		try (Connection c = DBUtil.getConnection();
				PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, in.getUserId());
			ps.setString(2, hash);
			ps.setNull(3, Types.VARCHAR); // legacy 비움
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

	// ======================================================================
	// 4) 인증(Login)
	// ======================================================================
	/** 로그인 검증: pw_hash 우선, 없으면 legacy user_pw 사용 */
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

	// ======================================================================
	// 5) 프로필 수정(Update)
	// ======================================================================
	/** 마이페이지: 기본 프로필 업데이트 */
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

	// ======================================================================
	// 6) 비밀번호 변경/재설정 (세션 방식 + 선택적 토큰 방식)
	// ======================================================================
	/** 마이페이지: 로그인 중 비밀번호 변경 (id 기준) */
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

	/** 비번 재설정(세션 방식): find-password → reset-password 단계에서 user_id로 해시 갱신 */
	public int updatePasswordHash(String userId, String newHash) throws Exception {
		String sql = "UPDATE `user` SET pw_hash = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, newHash);
			ps.setString(2, userId);
			return ps.executeUpdate();
		}
	}

	// ----- (선택) 토큰 방식 보조: password_reset 테이블 사용하는 경우 -----
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
		String ins = "INSERT INTO password_reset(user_id, token, expires_at) "
				+ "VALUES(?, ?, DATE_ADD(NOW(), INTERVAL ? MINUTE))";
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
			String sel = "SELECT user_id FROM password_reset "
					+ "WHERE token=? AND used_at IS NULL AND expires_at>NOW() FOR UPDATE";
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

	/** 내부 토큰 생성기(16진 64자) */
	private static String genToken() {
		byte[] buf = new byte[32];
		new java.security.SecureRandom().nextBytes(buf);
		StringBuilder sb = new StringBuilder(64);
		for (byte b : buf)
			sb.append(String.format("%02x", b));
		return sb.toString();
	}
}
