package dao;

import model.User;
import util.DBUtil;

import java.sql.*;

public class UserDao {

	private User map(ResultSet rs) throws SQLException {
		User u = new User();
		u.setId(rs.getInt("id"));
		u.setUser_id(rs.getString("user_id"));
		u.setUser_pw(rs.getString("user_pw"));
		u.setEmail(rs.getString("email"));
		u.setPhone(rs.getString("phone"));
		u.setBirth(rs.getString("birth"));
		u.setGender(rs.getString("gender"));
		u.setName(rs.getString("name"));
		u.setNickname(rs.getString("nickname"));
		u.setAddress(rs.getString("address"));
		return u;
	}

	public User findByPk(int id) throws SQLException {
		String sql = "SELECT * FROM `user` WHERE id=?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			if (con == null)
				throw new SQLException("DB connection is null");
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? map(rs) : null;
			}
		}
	}

	public User findByLoginId(String loginId) throws SQLException {
		String sql = "SELECT * FROM `user` WHERE user_id=?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			if (con == null)
				throw new SQLException("DB connection is null");
			ps.setString(1, loginId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? map(rs) : null;
			}
		}
	}

	public User findByEmail(String email) throws SQLException {
		String sql = "SELECT * FROM `user` WHERE email=?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			if (con == null)
				throw new SQLException("DB connection is null");
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? map(rs) : null;
			}
		}
	}

	public boolean idExists(String loginId) throws SQLException {
		String sql = "SELECT 1 FROM `user` WHERE user_id=?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			if (con == null)
				throw new SQLException("DB connection is null");
			ps.setString(1, loginId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	public boolean emailExists(String email) throws SQLException {
		String sql = "SELECT 1 FROM `user` WHERE email=?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			if (con == null)
				throw new SQLException("DB connection is null");
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	public int insert(User u) throws SQLException {
		String sql = "INSERT INTO `user` (user_id,user_pw,email,phone,birth,gender,name,nickname,address) "
				+ "VALUES (?,?,?,?,?,?,?,?,?)";
		try (Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			if (con == null)
				throw new SQLException("DB connection is null");
			int i = 1;
			ps.setString(i++, u.getUser_id());
			ps.setString(i++, u.getUser_pw());
			ps.setString(i++, u.getEmail());
			ps.setString(i++, u.getPhone());
			ps.setString(i++, u.getBirth());
			ps.setString(i++, u.getGender());
			ps.setString(i++, u.getName());
			ps.setString(i++, u.getNickname());
			ps.setString(i++, u.getAddress());
			int rows = ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					u.setId(rs.getInt(1));
			}
			return rows;
		}
	}

	public int updateProfile(int id, String nickname, String phone, String address, String birth, String gender)
			throws SQLException {
		String sql = "UPDATE `user` SET nickname=?, phone=?, address=?, birth=?, gender=? WHERE id=?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			if (con == null)
				throw new SQLException("DB connection is null");
			ps.setString(1, nickname);
			ps.setString(2, phone);
			ps.setString(3, address);
			ps.setString(4, birth);
			ps.setString(5, gender);
			ps.setInt(6, id);
			return ps.executeUpdate();
		}
	}

	public int updatePassword(int id, String hashedPw) throws SQLException {
		String sql = "UPDATE `user` SET user_pw=? WHERE id=?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			if (con == null)
				throw new SQLException("DB connection is null");
			ps.setString(1, hashedPw);
			ps.setInt(2, id);
			return ps.executeUpdate();
		}
	}

	public boolean nicknameExistsExcept(String nickname, int exceptUserId) throws SQLException {
		String sql = "SELECT 1 FROM `user` WHERE nickname=? AND id<>?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			if (con == null)
				throw new SQLException("DB connection is null");
			ps.setString(1, nickname);
			ps.setInt(2, exceptUserId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}
}
