package dao;

import dto.UserDTO;
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

    // (1) 이메일로 사용자 조회  ← IdLookupServlet, PasswordForgotServlet에서 요구
    public UserDTO findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ?";
        try (Connection con = DataSourceProvider.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    // (2) PK로 조회  ← UserEditServlet, PasswordChangeServlet 등에서 요구 (findByPk)
    public UserDTO findByPk(long id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (Connection con = DataSourceProvider.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    // (3) 로그인 ID로 조회  ← AuthLoginServlet 등
    public UserDTO findByLoginId(String userId) throws SQLException {
        String sql = "SELECT * FROM user WHERE user_id = ?";
        try (Connection con = DataSourceProvider.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    // (4) 이메일 중복 체크  ← SignupServlet에서 사용
    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM user WHERE email = ?";
        try (Connection con = DataSourceProvider.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // (5) 회원 생성  ← SignupServlet
    public long create(UserDTO u) throws SQLException {
        String sql = """
            INSERT INTO user
            (user_id, email, name, nickname, phone, address, gender, birth, pw_hash, role, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
            """;
        try (Connection con = DataSourceProvider.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int i=1;
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
                if (rs.next()) return rs.getLong(1);
            }
        }
        return 0L;
    }

    // (6) 프로필 업데이트 (서블릿이 파라미터 개별 전달) ← UserEditServlet의 updateProfile(uid, ...)
    public int updateProfile(long id, String email, String nickname, String phone,
                             String birth, String gender, String address) throws SQLException {
        String sql = """
            UPDATE user
               SET email=?, nickname=?, phone=?, birth=?, gender=?, address=?, updated_at=NOW()
             WHERE id=?
            """;
        try (Connection con = DataSourceProvider.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            int i=1;
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

    // (7) 비밀번호 변경  ← PasswordChangeServlet
    public int updatePassword(long id, String newHash) throws SQLException {
        String sql = "UPDATE user SET pw_hash=?, updated_at=NOW() WHERE id=?";
        try (Connection con = DataSourceProvider.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newHash);
            ps.setLong(2, id);
            return ps.executeUpdate();
        }
    }
}
