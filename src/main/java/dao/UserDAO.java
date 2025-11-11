package dao;

import dto.UserDTO;
import util.DBUtil;
import util.PasswordUtil;
import java.util.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class UserDAO {

	private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	// ===== 공용 매핑 =====
	private UserDTO map(ResultSet rs) throws SQLException {
		UserDTO u = new UserDTO();
		u.setId(rs.getInt("id"));
		u.setUserId(rs.getString("user_id"));
		u.setPwHash(rs.getString("pw_hash")); // 신규 해시
		u.setUserPw(rs.getString("user_pw")); // legacy(Nullable)
		u.setEmail(rs.getString("email"));
		u.setPhone(rs.getString("phone"));

		String b = rs.getString("birth"); // yyyy-MM-dd
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

	// (호환) 이름만 다른 중복 — existsByLoginId로 위임
	public boolean existsByUserId(String userId) {
		return existsByLoginId(userId);
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

	/** FindPassword 1단계용: 아이디 + 휴대폰(숫자만)으로 식별 */
	public Optional<UserDTO> findByLoginIdAndPhone(String userId, String phoneDigits) {
		String sql = "SELECT * FROM `user` WHERE BINARY user_id=? AND phone=?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, userId);
			ps.setString(2, phoneDigits);
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
			ps.setNull(3, Types.VARCHAR); // legacy 칸 비움
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
		String sql = "UPDATE `user` SET email=?, phone=?, birth=?, gender=?, name=?, nickname=?, address=? WHERE id=?";
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
			if (e.getMessage() != null && e.getMessage().contains("Duplicate")) {
				throw new RuntimeException("중복된 데이터 (" + e.getMessage() + ")");
			}
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

	// (호환) user_id로 해시 업데이트
	public int updatePasswordHash(String userId, String newHash) throws Exception {
		String sql = "UPDATE `user` SET pw_hash = ?, user_pw = NULL, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
		try (var conn = DBUtil.getConnection(); var ps = conn.prepareStatement(sql)) {
			ps.setString(1, newHash);
			ps.setString(2, userId);
			return ps.executeUpdate();
		}
	}

	// (권장) PK(id)로 해시 업데이트
	public int updatePasswordHashById(int uid, String newHash) throws Exception {
		String sql = "UPDATE `user` SET pw_hash = ?, user_pw = NULL, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
		try (var conn = DBUtil.getConnection(); var ps = conn.prepareStatement(sql)) {
			ps.setString(1, newHash);
			ps.setInt(2, uid);
			return ps.executeUpdate();
		}
	}

	// (호환 오버로드) ResetPasswordServlet에서 호출한 시그니처
	public int updatePasswordHash(int uid, String newHash) throws Exception {
		return updatePasswordHashById(uid, newHash);
	}

	// ===== 패스워드 토큰 흐름(이메일 기반) =====
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

	

	// ===== 삭제 =====
	public boolean deleteUserById(String userId) {
		String sql = "DELETE FROM `user` WHERE user_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, userId);
			int n = pstmt.executeUpdate();
			return n > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// ===== 아이디 찾기/보안질문 =====
	public Optional<UserDTO> findByPhone(String phone) {
		String sql = "SELECT * FROM `user` WHERE phone = ?";
		try (var c = DBUtil.getConnection(); var ps = c.prepareStatement(sql)) {
			ps.setString(1, phone);
			try (var rs = ps.executeQuery()) {
				return rs.next() ? Optional.of(map(rs)) : Optional.empty();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/** 유저의 보안질문 텍스트를 가져옴 (security_qa.question_tx 우선) */
	public String getSecurityQuestionText(int uid) {
	    String sql = "SELECT question_tx FROM security_qa WHERE user_id=?";
	    try (var c = DBUtil.getConnection(); var ps = c.prepareStatement(sql)) {
	        ps.setInt(1, uid);
	        try (var rs = ps.executeQuery()) {
	            if (rs.next()) {
	                String tx = rs.getString(1);
	                return (tx != null && !tx.isBlank()) ? tx : null;
	            }
	        }
	    } catch (java.sql.SQLException e) {
	        throw new RuntimeException(e);
	    }
	    return null;
	}

		

	/** 보안질문/답 1건 조회(Map) — 필요시 유지 */
	public Optional<java.util.Map<String, Object>> getSecurityQA(int uid) {
		String sql = "SELECT question_id, question_tx FROM security_qa WHERE user_id=?";
		try (var c = DBUtil.getConnection(); var ps = c.prepareStatement(sql)) {
			ps.setInt(1, uid);
			try (var rs = ps.executeQuery()) {
				if (!rs.next())
					return Optional.empty();
				var m = new java.util.HashMap<String, Object>();
				m.put("question_id", rs.getInt("question_id"));
				m.put("question_tx", rs.getString("question_tx"));
				return Optional.of(m);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/** 답 검증 (정규화 + 해시 비교) */
	public boolean verifySecurityAnswer(int uid, String answerPlain) {
		String sql = "SELECT answer_hash FROM security_qa WHERE user_id=?";
		try (var c = DBUtil.getConnection(); var ps = c.prepareStatement(sql)) {
			ps.setInt(1, uid);
			try (var rs = ps.executeQuery()) {
				if (!rs.next())
					return false;
				String stored = rs.getString(1);
				String normalized = PasswordUtil.normalize(answerPlain); // 소문자/trim 등
				return PasswordUtil.verify(normalized, stored);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/** 보안질문 저장/갱신 (upsert). answerPlain은 평문으로 들어오며 내부에서 정규화+해시 */
	public boolean upsertSecurityQA(int uid, int questionId, String questionTx, String answerPlain) {
		String normalized = (answerPlain == null) ? "" : answerPlain.trim().replaceAll("\\s+", " ").toLowerCase();
		String answerHash = PasswordUtil.hash(normalized);

		String sql = """
				INSERT INTO security_qa (user_id, question_id, question_tx, answer_hash, created_at, updated_at)
				VALUES (?, ?, ?, ?, NOW(), NOW())
				ON DUPLICATE KEY UPDATE
				    question_id = VALUES(question_id),
				    question_tx = VALUES(question_tx),
				    answer_hash = VALUES(answer_hash),
				    updated_at = NOW()
				""";

		try (var c = DBUtil.getConnection(); var ps = c.prepareStatement(sql)) {
			ps.setInt(1, uid);
			ps.setInt(2, questionId);
			ps.setString(3, questionTx);
			ps.setString(4, answerHash);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
