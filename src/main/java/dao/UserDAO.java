package dao;

import dto.UserDTO;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;

public class UserDAO {

    private DataSource ds;

    public UserDAO() {
        try {
            InitialContext ic = new InitialContext();
            // context.xml: <Resource name="jdbc/semi" .../>
            ds = (DataSource) ic.lookup("java:comp/env/jdbc/semi");
        } catch (Exception e) {
            throw new RuntimeException("DataSource lookup failed", e);
        }
    }

    private Connection getConn() throws SQLException {
        return ds.getConnection();

        // (대체안) 직접 연결하고 싶다면:
        // String url = "jdbc:mysql://127.0.0.1:3306/semi_project?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Seoul";
        // return DriverManager.getConnection(url, "root", "password");
    }

    // --- CREATE ---
    public int create(UserDTO u, String rawPasswordHashAlready) throws SQLException {
        // rawPasswordHashAlready 인자는 이미 PasswordUtil.hash()로 해시된 문자열을 받는다고 가정
        String sql = "INSERT INTO `user`" +
                "(user_id, pw_hash, email, phone, birth, gender, name, nickname, address, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = getConn();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int i=1;
            ps.setString(i++, u.getUserId());
            ps.setString(i++, rawPasswordHashAlready);
            ps.setString(i++, u.getEmail());
            ps.setString(i++, u.getPhone());
            ps.setDate(i++, Date.valueOf(u.getBirth()));
            ps.setString(i++, u.getGender());
            ps.setString(i++, u.getName());
            ps.setString(i++, u.getNickname());
            ps.setString(i++, u.getAddress());
            ps.setString(i++, u.getRole() == null ? "USER" : u.getRole());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    // --- READ ---
    public UserDTO findByLoginId(String userId) throws SQLException {
        String sql = "SELECT * FROM `user` WHERE user_id=?";
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return map(rs);
            }
        }
    }

    public UserDTO findById(int id) throws SQLException {
        String sql = "SELECT * FROM `user` WHERE id=?";
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return map(rs);
            }
        }
    }

    // --- UPDATE ---
    public void updatePassword(int userId, String newHash) throws SQLException {
        String sql = "UPDATE `user` SET pw_hash=? WHERE id=?";
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, newHash);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    public void updateProfile(UserDTO u) throws SQLException {
        String sql = "UPDATE `user` SET email=?, phone=?, nickname=?, address=? WHERE id=?";
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            int i=1;
            ps.setString(i++, u.getEmail());
            ps.setString(i++, u.getPhone());
            ps.setString(i++, u.getNickname());
            ps.setString(i++, u.getAddress());
            ps.setInt(i++, u.getId());
            ps.executeUpdate();
        }
    }

    // --- EXISTS ---
    public boolean existsByUserId(String userId) throws SQLException {
        String sql = "SELECT 1 FROM `user` WHERE user_id=?";
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    public boolean existsByPhone(String phone, Integer excludeUserId) throws SQLException {
        String sql = "SELECT 1 FROM `user` WHERE phone=? " + (excludeUserId != null ? "AND id<>?" : "");
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            int i=1;
            ps.setString(i++, phone);
            if (excludeUserId != null) ps.setInt(i++, excludeUserId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    // --- MAPPER ---
    private UserDTO map(ResultSet rs) throws SQLException {
        UserDTO u = new UserDTO();
        u.setId(rs.getInt("id"));
        u.setUserId(rs.getString("user_id"));
        u.setPwHash(rs.getString("pw_hash"));
        u.setEmail(rs.getString("email"));
        u.setPhone(rs.getString("phone"));
        Date bd = rs.getDate("birth");
        if (bd != null) u.setBirth(bd.toLocalDate());
        u.setGender(rs.getString("gender"));
        u.setName(rs.getString("name"));
        u.setNickname(rs.getString("nickname"));
        u.setAddress(rs.getString("address"));
        u.setRole(rs.getString("role"));
        Timestamp ca = rs.getTimestamp("created_at");
        Timestamp ua = rs.getTimestamp("updated_at");
        if (ca != null) u.setCreatedAt(ca.toLocalDateTime());
        if (ua != null) u.setUpdatedAt(ua.toLocalDateTime());
        return u;
    }
}
