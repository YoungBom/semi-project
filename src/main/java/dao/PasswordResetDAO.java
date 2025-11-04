package dao;

import util.DBUtil;

import java.sql.*;
import java.time.Instant;

public class PasswordResetDAO {

	// 토큰 발급(생성)
	public void createToken(int userId, String token, Instant expiresAt) throws SQLException {
		String sql = "INSERT INTO password_reset(user_id, token, expires_at) VALUES (?,?,?)";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			if (con == null)
				throw new SQLException("DB connection is null");
			ps.setInt(1, userId);
			ps.setString(2, token);
			ps.setTimestamp(3, Timestamp.from(expiresAt));
			ps.executeUpdate();
		}
	}

	// 유효(미사용 & 미만료) 토큰 조회 //
	public ResetToken findValidToken(String token) throws SQLException {
		String sql = "SELECT id, user_id, token, expires_at, used_at " + "FROM password_reset WHERE token=?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			if (con == null)
				throw new SQLException("DB connection is null");
			ps.setString(1, token);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next())
					return null;
				Timestamp used = rs.getTimestamp("used_at");
				Timestamp exp = rs.getTimestamp("expires_at");
				if (used != null)
					return null;
				if (exp == null || exp.toInstant().isBefore(Instant.now()))
					return null;

				ResetToken rt = new ResetToken();
				rt.id = rs.getInt("id");
				rt.userId = rs.getInt("user_id");
				rt.token = rs.getString("token");
				rt.expiresAt = exp.toInstant();
				return rt;
			}
		}
	}

	// 토큰 사용 처리(단일) //
	public void markUsed(int id) throws SQLException {
		String sql = "UPDATE password_reset SET used_at=NOW() WHERE id=?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			if (con == null)
				throw new SQLException("DB connection is null");
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}

	// 해당 유저의 만료된/사용된 토큰 정리(선택) //
	public int cleanupExpiredForUser(int userId) throws SQLException {
		String sql = "DELETE FROM password_reset " + "WHERE user_id=? AND (used_at IS NOT NULL OR expires_at < NOW())";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			if (con == null)
				throw new SQLException("DB connection is null");
			ps.setInt(1, userId);
			return ps.executeUpdate();
		}
	}

	// 내부 DTO //
	public static class ResetToken {
		public int id;
		public int userId;
		public String token;
		public Instant expiresAt;
	}
}
