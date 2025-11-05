package dao;

import util.DBUtil;
import java.sql.*;
import java.time.Instant;

public class PasswordResetDAO {

	public void createToken(int userId, String token, Instant expiresAt) throws SQLException {
		String sql = "INSERT INTO password_reset(user_id, token, expires_at) VALUES (?,?,?)";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ps.setString(2, token);
			ps.setTimestamp(3, Timestamp.from(expiresAt));
			ps.executeUpdate();
		}
	}

	public ResetToken findByToken(String token) throws SQLException {
		String sql = "SELECT id, user_id, token, expires_at FROM password_reset "
				+ "WHERE token=? AND used_at IS NULL AND expires_at > CURRENT_TIMESTAMP";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, token);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next())
					return null;
				ResetToken rt = new ResetToken();
				rt.id = rs.getInt("id");
				rt.userId = rs.getInt("user_id");
				rt.token = rs.getString("token");
				rt.expiresAt = rs.getTimestamp("expires_at").toInstant();
				return rt;
			}
		}
	}

	public int markUsed(int id) throws SQLException {
		String sql = "UPDATE password_reset SET used_at=CURRENT_TIMESTAMP WHERE id=?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, id);
			return ps.executeUpdate();
		}
	}

	// 내부 DTO
	public static class ResetToken {
		public int id;
		public int userId;
		public String token;
		public Instant expiresAt;
	}
}
