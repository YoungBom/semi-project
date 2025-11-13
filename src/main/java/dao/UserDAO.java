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
        u.setId(rs.getInt("id"));              // PK(Integer)
        u.setUserId(rs.getString("user_id"));  // 로그인용 아이디
        u.setPwHash(rs.getString("pw_hash"));  // 해시(신규)
        u.setUserPw(rs.getString("user_pw"));  // legacy(Nullable)
        u.setEmail(rs.getString("email"));
        u.setPhone(rs.getString("phone"));
        u.setProfileImage(rs.getString("profile_image"));

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
        if (opt.isEmpty()) return Optional.empty();

        UserDTO u = opt.get();
        String stored = (u.getPwHash() != null && !u.getPwHash().isEmpty()) ? u.getPwHash() : u.getUserPw();
        if (stored == null || stored.isEmpty()) return Optional.empty();

        return PasswordUtil.verify(rawPassword, stored) ? Optional.of(u) : Optional.empty();
    }

    // ===== 프로필 수정 =====
    public boolean updateProfile(int uid, String email, String phone, String birth, String gender,
                                 String name, String nickname, String address) {
        String sql = "UPDATE `user` SET email=?, phone=?, birth=?, gender=?, name=?, nickname=?, address=? WHERE id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

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
   // 비밀번호 해시를 직접 설정 (user_pw는 NULL로)
    public boolean updatePasswordHash(int uid, String newHash) {
        String sql = "UPDATE `user` SET pw_hash = ?, user_pw = NULL WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
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
        for (byte b : buf) sb.append(String.format("%02x", b));
        return sb.toString();
    }
    
    
    public Optional<String> createPasswordResetTokenByLoginIdOrEmail(String loginOrEmail, int ttlMinutes) {
        Integer uid = null;
        String find = "SELECT id FROM `user` WHERE BINARY user_id = ? OR email = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(find)) {
            ps.setString(1, loginOrEmail);
            ps.setString(2, loginOrEmail);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) uid = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (uid == null) return Optional.empty();

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
                    if (rs.next()) uid = rs.getInt(1);
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

    // ===== (자동로그인 관련; 지금은 미사용 가능) =====
    public Optional<UserDTO> findByRememberToken(String token) {
        String sql = "SELECT u.* FROM remember_me r JOIN `user` u ON r.user_id = u.id " +
                     "WHERE r.token=? AND r.expires_at > NOW()";
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
                if (rs.next()) exists = rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }

    // 유저 삭제 로직
    public boolean deleteUserById(String userId) {
        String sql = "DELETE FROM `user` WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            int n = pstmt.executeUpdate();
            return n > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int updateProfileImg(String userId, String fileName) {
        String sql = "UPDATE user SET profile_image = ? WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fileName);
            ps.setString(2, userId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public UserDTO findByUserId(String userId) {
        String sql = "SELECT * FROM user WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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

    /** 휴대폰 번호(숫자만)로 사용자 조회 — 아이디 찾기(FindIdServlet)에서 사용 */
    public Optional<UserDTO> findByPhoneOnly(String phoneDigits) {
        if (phoneDigits == null) return Optional.empty();
        String digits = phoneDigits.replaceAll("\\D+", ""); // 숫자만

        String sql =
            "SELECT * FROM `user` " +
            " WHERE REPLACE(REPLACE(REPLACE(phone,'-',''),' ',''),'+','') = ?";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, digits);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


   // ** 로그인아이디 + 휴대폰(숫자만)으로 사용자 조회 — 비밀번호 찾기에서(FindPasswordServlet) 사용 */
    public Optional<UserDTO> findByLoginIdAndPhone(String userId, String phoneDigits) {
        if (userId == null || userId.isBlank() || phoneDigits == null) return Optional.empty();

        // 숫자만
        String digits = phoneDigits.replaceAll("\\D+", "");

        // 국내/국제 표기 보정: 010xxxx ↔ 8210xxxx
        String alt = digits;
        if (digits.startsWith("0") && digits.length() >= 10) {
            alt = "82" + digits.substring(1);
        } else if (digits.startsWith("82") && digits.length() >= 11) {
            alt = "0" + digits.substring(2);
        }

        String sql =
            "SELECT * FROM `user` " +
            " WHERE BINARY user_id = ? " +
            "   AND ( REPLACE(REPLACE(REPLACE(phone,'-',''),' ',''),'+','') = ? " +
            "      OR REPLACE(REPLACE(REPLACE(phone,'-',''),' ',''),'+','') = ? )";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, digits);
            ps.setString(3, alt);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** 해당 사용자에게 설정된 보안질문 텍스트 반환 (없으면 빈 문자열) */
    public String getSecurityQuestionText(int uid) {
        // 스키마 가정: user_security_qa(user_id INT PK, question_id INT, answer_hash VARCHAR(255))
        //             security_question(id INT PK, question_text VARCHAR(255))
        String sql = "SELECT q.question_text " +
                     "FROM user_security_qa uqa JOIN security_question q ON uqa.question_id = q.id " +
                     "WHERE uqa.user_id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, uid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString(1);
                return "";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** 사용자 보안질문 답 검증: 입력 평문(answerRaw) vs 저장된 answer_hash */
    public boolean verifySecurityAnswer(int uid, String answerRaw) throws Exception {
        String sql = "SELECT uqa.answer_hash " +
                     "FROM user_security_qa uqa " +
                     "WHERE uqa.user_id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, uid);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return false;
                String stored = rs.getString(1);
                if (stored == null || stored.isBlank()) return false;
                return PasswordUtil.verify(answerRaw, stored);
            }
     }
    
    }
 // (1) 사전 질문 목록
    public java.util.List<java.util.Map<String,Object>> listSecurityQuestions() {
        String sql = "SELECT id, question_text FROM security_question WHERE active=1 ORDER BY id";
        java.util.List<java.util.Map<String,Object>> list = new java.util.ArrayList<>();
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                java.util.Map<String,Object> m = new java.util.HashMap<>();
                m.put("id", rs.getInt("id"));
                m.put("text", rs.getString("question_text"));
                list.add(m);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    // (2) 현재 사용자 설정 조회
    public java.util.Map<String,Object> getSecurityQA(int uid) {
        String sql = """
            SELECT q.question_id, q.question_tx,
                   COALESCE(q.question_tx, sq.question_text) AS qtext_resolved,
                   q.updated_at
              FROM security_qa q
              LEFT JOIN security_question sq ON sq.id = q.question_id
             WHERE q.user_id = ?
            """;
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, uid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    java.util.Map<String,Object> m = new java.util.HashMap<>();
                    m.put("qid", rs.getInt("question_id"));
                    m.put("qtext", rs.getString("question_tx"));
                    m.put("qtext_resolved", rs.getString("qtext_resolved"));
                    m.put("updated_at", rs.getTimestamp("updated_at"));
                    return m;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return java.util.Collections.emptyMap();
    }

    // (3) 저장/갱신 (질문 1개, 커스텀 질문 허용)
 // 저장/갱신 (질문 1개, 커스텀 질문 허용) — MySQL 8.0.20+ 호환
    public boolean upsertSecurityQA(int uid, int qid, String questionTx, String answerRaw) throws Exception {
        String normAns = util.PasswordUtil.normalize(answerRaw);
        String hash = util.PasswordUtil.hash(normAns);

        String sql = """
            INSERT INTO security_qa (user_id, question_id, question_tx, answer_hash, created_at, updated_at)
            VALUES (?, ?, ?, ?, NOW(), NOW())
            AS new
            ON DUPLICATE KEY UPDATE
              question_id = new.question_id,
              question_tx = new.question_tx,
              answer_hash = new.answer_hash,
              updated_at  = NOW()
            """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, uid);
            ps.setInt(2, qid);
            ps.setString(3, (questionTx == null || questionTx.isBlank()) ? null : questionTx.trim());
            ps.setString(4, hash);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    }


