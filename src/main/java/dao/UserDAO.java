package dao;

import dto.UserDTO;
import util.DBUtil;
import util.PasswordUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * UserDAO
 *
 * - 회원가입 / 로그인 / 프로필 수정 - 비밀번호 변경/찾기 (이메일 토큰 + 보안질문) - 보안질문 설정 - 아이디/비밀번호 찾기
 * (전화번호 기반) - 관리자용 유저 관리
 */
public class UserDAO {

	private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	// =====================================================
	// 공용 매핑 유틸
	// =====================================================

	/**
	 * ResultSet -> UserDTO 공통 매핑 여러 조회 메소드에서 재사용
	 */
	private UserDTO map(ResultSet rs) throws SQLException {
		UserDTO u = new UserDTO();
		u.setId(rs.getInt("id")); // PK
		u.setUserId(rs.getString("user_id")); // 로그인용 아이디
		u.setPwHash(rs.getString("pw_hash")); // 비밀번호 해시
		u.setUserPw(rs.getString("user_pw")); // legacy(Nullable)

		u.setEmail(rs.getString("email"));
		u.setPhone(rs.getString("phone"));
		u.setProfileImage(rs.getString("profile_image"));

		String b = rs.getString("birth"); // VARCHAR(yyyy-MM-dd)
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

	// =====================================================
	// 존재 여부 / 기본 조회 (회원가입·로그인·공통)
	// =====================================================

	/**
	 * user_id 존재 여부 (대소문자까지 구분) - 회원가입 아이디 중복 체크 등에서 사용
	 */
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

	/**
	 * user_id 존재 여부 (COUNT) - 기존에 쓰이던 중복체크 로직에서 사용
	 */
	public boolean existsByUserId(String userId) {
		boolean exists = false;
		String sql = "SELECT COUNT(*) FROM user WHERE user_id = ?";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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

	/**
	 * email 존재 여부 - 회원가입 중복 체크
	 */
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

	/**
	 * 로그인용 아이디로 조회 - AuthLoginServlet, authenticate() 내부 등에서 사용
	 */
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

	/**
	 * PK(id)로 조회 - 마이페이지, 세션에 저장된 uid로 사용자 정보 불러올 때 사용
	 */
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

	/**
	 * 이메일로 조회 - 이메일 기반 인증이 필요할 때 사용 가능
	 */
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

	/**
	 * 이름 + 이메일로 로그인 아이디 찾기 - (구방식) 이름/이메일로 아이디 찾기 기능에서 사용
	 */
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

	/**
	 * user_id로 간단 조회 - 관리자 페이지, 특정 유저 상세 조회 등에 사용 (필요 필드만 채움)
	 */
	public UserDTO findByUserId(String userId) {
		String sql = "SELECT * FROM user WHERE user_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					UserDTO u = new UserDTO();
					u.setId(rs.getInt("id"));
					u.setUserId(rs.getString("user_id"));
					u.setEmail(rs.getString("email"));
					u.setName(rs.getString("name"));
					u.setNickname(rs.getString("nickname"));
					u.setPhone(rs.getString("phone"));
					u.setProfileImage(rs.getString("profile_image"));
					return u;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// =====================================================
	// 회원 생성 / 프로필 수정
	// =====================================================

	/**
	 * 회원 생성 - RegisterServlet 에서 사용
	 *
	 * @return 생성된 PK(id), 실패 시 0
	 */
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

	/**
	 * 프로필 수정 - UserEditServlet (/user/edit) 에서 사용
	 */
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
			// 유니크 제약 위반 등 감지 시 메시지 포함해서 던짐
			if (e.getMessage() != null && e.getMessage().contains("Duplicate")) {
				throw new RuntimeException("중복된 데이터 (" + e.getMessage() + ")");
			}
			throw new RuntimeException(e);
		}
	}

	/**
	 * 프로필 이미지 업데이트 - 프로필 이미지 업로드 서블릿에서 사용
	 */
	public int updateProfileImg(String userId, String fileName) {
		String sql = "UPDATE user SET profile_image = ? WHERE user_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, fileName);
			ps.setString(2, userId);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	// =====================================================
	// 인증 / 로그인 / 비밀번호 변경
	// =====================================================

	/**
	 * 로그인 인증 - AuthLoginServlet 에서 사용
	 */
	public Optional<UserDTO> authenticate(String loginId, String rawPassword) {
		Optional<UserDTO> opt = findByLoginId(loginId);
		if (opt.isEmpty()) {
			return Optional.empty();
		}

		UserDTO u = opt.get();
		String stored = (u.getPwHash() != null && !u.getPwHash().isEmpty()) ? u.getPwHash() : u.getUserPw();

		if (stored == null || stored.isEmpty()) {
			return Optional.empty();
		}

		return PasswordUtil.verify(rawPassword, stored) ? Optional.of(u) : Optional.empty();
	}

	/**
	 * 마이페이지에서 비밀번호 변경 - PasswordChangeServlet 등에서 사용
	 */
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

	// =====================================================
	// 비밀번호 찾기 (이메일 토큰 기반)
	// - 기존 password_reset 테이블을 사용하는 방식
	// =====================================================

	/** 내부용 랜덤 토큰 생성 */
	private static String genToken() {
		byte[] buf = new byte[32];
		new java.security.SecureRandom().nextBytes(buf);
		StringBuilder sb = new StringBuilder(64);
		for (byte b : buf) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	/**
	 * 로그인 아이디 또는 이메일로 비밀번호 재설정 토큰 생성 - 이메일 링크 방식 비밀번호 찾기에서 사용 (선택 사항)
	 */
	public Optional<String> createPasswordResetTokenByLoginIdOrEmail(String loginOrEmail, int ttlMinutes) {
		Integer uid = null;
		String find = "SELECT id FROM `user` WHERE BINARY user_id = ? OR email = ?";

		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(find)) {

			ps.setString(1, loginOrEmail);
			ps.setString(2, loginOrEmail);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					uid = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		if (uid == null) {
			return Optional.empty();
		}

		String token = genToken();
		String ins = "INSERT INTO password_reset(user_id, token, expires_at) "
				+ "VALUES(?,?, DATE_ADD(NOW(), INTERVAL ? MINUTE))";
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

	/**
	 * 이메일 토큰으로 비밀번호 재설정 - password_reset.token 기반 (아직 미구현 추후 구현 예정)
	 */
	public boolean updatePasswordByToken(String token, String newRawPassword) {
		try (Connection c = DBUtil.getConnection()) {
			c.setAutoCommit(false);

			Integer uid = null;
			String sel = "SELECT user_id FROM password_reset "
					+ "WHERE token=? AND used_at IS NULL AND expires_at>NOW() FOR UPDATE";

			try (PreparedStatement ps = c.prepareStatement(sel)) {
				ps.setString(1, token);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						uid = rs.getInt(1);
					}
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

	/**
	 * 보안질문 검증을 통과한 뒤 비밀번호 재설정 - ResetPasswordServlet 에서 사용
	 */
	public boolean updatePasswordHash(int userId, String newPlainPassword) {
		if (newPlainPassword == null || newPlainPassword.isBlank()) {
			return false;
		}

		String hash = PasswordUtil.hash(newPlainPassword.trim());
		String sql = "UPDATE user SET pw_hash = ?, user_pw = NULL WHERE id = ?";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, hash);
			ps.setInt(2, userId);

			int updated = ps.executeUpdate();
			return updated > 0;
		} catch (SQLException e) {
			throw new RuntimeException("비밀번호 해시 업데이트 실패", e);
		}
	}

	// =====================================================
	// 보안질문 기능 (SecuritySetupServlet / FindId / FindPassword 공통)
	// =====================================================

	/**
	 * 보안질문 목록 조회 - SecuritySetupServlet.doGet 에서 사용 - JSP: ${questions} -> q.id,
	 * q.text
	 */
	public List<Map<String, Object>> listSecurityQuestions() {
		String sql = "SELECT id, question_text FROM security_question WHERE active = 1 ORDER BY id";

		List<Map<String, Object>> list = new ArrayList<>();
		try (Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Map<String, Object> row = new HashMap<>();
				row.put("id", rs.getInt("id"));
				row.put("text", rs.getString("question_text")); // JSP: ${q.text}
				list.add(row);
			}
		} catch (SQLException e) {
			throw new RuntimeException("보안질문 목록 조회 실패", e);
		}
		return list;
	}

	/**
	 * 특정 유저의 보안질문 설정 조회 - SecuritySetupServlet.doGet 에서 사용 - JSP: ${current.qid},
	 * ${current.qtext_resolved} 등
	 */
	public Map<String, Object> getSecurityQA(int userId) {
		String sql = "SELECT q.user_id, q.question_id, q.question_tx, sq.question_text " + "FROM security_qa q "
				+ "LEFT JOIN security_question sq ON q.question_id = sq.id " + "WHERE q.user_id = ?";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, userId);

			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					return null; // 아직 보안질문 안 설정한 유저
				}

				Map<String, Object> row = new HashMap<>();
				int qid = rs.getInt("question_id");
				String qtext = rs.getString("question_tx");
				String resolved = qtext;

				if (resolved == null || resolved.isBlank()) {
					resolved = rs.getString("question_text");
				}

				row.put("user_id", rs.getInt("user_id"));
				row.put("qid", qid);
				row.put("qtext", qtext);
				row.put("qtext_resolved", resolved);
				return row;
			}
		} catch (SQLException e) {
			throw new RuntimeException("사용자 보안질문 조회 실패", e);
		}
	}

	/**
	 * 보안질문/답변 설정 (INSERT or UPDATE) - SecuritySetupServlet.doPost 에서 사용
	 */
	public boolean upsertSecurityQA(int userId, int questionId, String questionTx, String answerPlain) {
		if (answerPlain == null || answerPlain.isBlank()) {
			return false;
		}

		String hash = PasswordUtil.hash(answerPlain.trim());
		String trimmedQuestionTx = (questionTx == null || questionTx.isBlank()) ? null : questionTx.trim();

		String sql = "INSERT INTO security_qa (user_id, question_id, question_tx, answer_hash) " + "VALUES (?,?,?,?) "
				+ "ON DUPLICATE KEY UPDATE " + "question_id = VALUES(question_id), "
				+ "question_tx = VALUES(question_tx), " + "answer_hash = VALUES(answer_hash)";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, userId);
			ps.setInt(2, questionId);
			if (trimmedQuestionTx != null) {
				ps.setString(3, trimmedQuestionTx);
			} else {
				ps.setNull(3, Types.VARCHAR);
			}
			ps.setString(4, hash);

			int updated = ps.executeUpdate();
			return updated > 0;
		} catch (SQLException e) {
			throw new RuntimeException("보안질문 저장/수정 실패", e);
		}
	}

	/**
	 * 비밀번호/아이디 찾기 화면에서 질문 텍스트만 필요할 때 - FindIdServlet / FindPasswordServlet 에서 사용
	 */
	public String getSecurityQuestionText(int userId) {
		String sql = "SELECT COALESCE(q.question_tx, sq.question_text) AS question_text " + "FROM security_qa q "
				+ "LEFT JOIN security_question sq ON q.question_id = sq.id " + "WHERE q.user_id = ?";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, userId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("question_text");
				} else {
					return null;
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("보안질문 텍스트 조회 실패", e);
		}
	}

	/**
	 * 사용자가 입력한 보안답변 검증 - FindIdServlet / FindPasswordServlet 에서 사용
	 */
	public boolean verifySecurityAnswer(int userId, String answerPlain) {
		if (answerPlain == null || answerPlain.isBlank()) {
			return false;
		}

		String sql = "SELECT answer_hash FROM security_qa WHERE user_id = ?";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, userId);

			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					// 보안질문 자체가 설정 안 되어 있으면 false
					return false;
				}
				String storedHash = rs.getString("answer_hash");
				return PasswordUtil.verify(answerPlain.trim(), storedHash);
			}
		} catch (SQLException e) {
			throw new RuntimeException("보안답변 검증 실패", e);
		}
	}

	// =====================================================
	// 아이디 / 비밀번호 찾기 (전화번호 기반)
	// =====================================================

	/**
	 * 전화번호만으로 유저 찾기 - FindIdServlet 1단계 (전화번호 입력) 에서 사용 - Servlet 쪽에서
	 * phoneDigits(숫자만) 넣으면, DB에 '010-1234-5678' 처럼 저장되어 있어도 REPLACE로 매칭
	 */
	public Optional<UserDTO> findByPhoneOnly(String phoneDigits) {
		String sql = "SELECT * FROM user WHERE REPLACE(phone, '-', '') = ?";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, phoneDigits);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? Optional.of(map(rs)) : Optional.empty();
			}
		} catch (SQLException e) {
			throw new RuntimeException("전화번호로 사용자 조회 실패", e);
		}
	}

	/**
	 * 아이디 + 전화번호로 유저 찾기 - FindPasswordServlet 1단계 (아이디+전화번호 입력) 에서 사용
	 */
	public Optional<UserDTO> findByLoginIdAndPhone(String loginId, String phoneDigits) {
		String sql = "SELECT * FROM user WHERE user_id = ? AND REPLACE(phone, '-', '') = ?";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, loginId);
			ps.setString(2, phoneDigits);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? Optional.of(map(rs)) : Optional.empty();
			}
		} catch (SQLException e) {
			throw new RuntimeException("아이디+전화번호로 사용자 조회 실패", e);
		}
	}

	// =====================================================
	// 관리자용 유저 관리
	// =====================================================

	/**
	 * 유저 삭제 - 관리자 페이지에서 특정 user_id를 삭제할 때 사용
	 */
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

	/**
	 * 전체 일반 유저 목록 - 관리자용 유저 리스트 화면에서 사용 (role='USER' 만)
	 */
	public List<UserDTO> getAllUserList() {
		List<UserDTO> userList = new ArrayList<>();
		String sql = "SELECT * FROM user WHERE role = 'USER'";

		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				UserDTO user = new UserDTO();
				user.setId(rs.getInt("id"));
				user.setUserId(rs.getString("user_id"));
				user.setName(rs.getString("name"));
				user.setNickname(rs.getString("nickname"));
				user.setEmail(rs.getString("email"));
				user.setCreatedAt(rs.getTimestamp("created_at"));
				user.setRole(rs.getString("role"));
				userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}

	public int setAuthorizeAdmin(int id) {
		String sql = "UPDATE user SET role = 'ADMIN' WHERE id = ?";
		int result = 0;
		
		try(Connection conn = DBUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			result = pstmt.executeUpdate();	
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		return result;
	}
	
}
