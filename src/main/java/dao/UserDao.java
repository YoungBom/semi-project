// src/main/java/dao/UserDao.java
package dao;

import model.User;
import util.DBUtil;

import java.sql.*;

public class UserDao {

	// ResultSet -> User 매핑
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

	// 회원가입 INSERT //
	
	public int insert(User u) throws SQLException {
		String sql = "INSERT INTO `user` " + "(user_id,user_pw,email,phone,birth,gender,name,nickname,address) "
				+ "VALUES (?,?,?,?,?,?,?,?,?)";
		try (Connection con = DBUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, u.getUser_id());
			ps.setString(2, u.getUser_pw());
			ps.setString(3, u.getEmail());
			ps.setString(4, u.getPhone());
			ps.setString(5, u.getBirth());
			ps.setString(6, u.getGender());
			ps.setString(7, u.getName());
			ps.setString(8, u.getNickname());
			ps.setString(9, u.getAddress());

			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				return rs.next() ? rs.getInt(1) : 0;
			}
		}
	}

	// 로그인 아이디로 조회하기 //
	
	public User findByLoginId(String loginId) throws SQLException {
		String sql = "SELECT * FROM `user` WHERE user_id = ?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, loginId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? map(rs) : null;
			}
		}
	}

	// 기본키로 조회 (마이페이지) //
	
	
	public User findByPk(int pk) throws SQLException {
		String sql = "SELECT * FROM `user` WHERE id = ?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, pk);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? map(rs) : null;
			}
		}
	}

	// 중복 체크 //
	
	public boolean idExists(String loginId) throws SQLException {
		String sql = "SELECT 1 FROM `user` WHERE user_id = ?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, loginId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	public boolean emailExists(String email) throws SQLException {
		String sql = "SELECT 1 FROM `user` WHERE email = ?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	public boolean existsEmailExceptMe(String email, int myId) throws SQLException {
		String sql = "SELECT 1 FROM `user` WHERE email=? AND id<>?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, email);
			ps.setInt(2, myId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	public boolean existsNicknameExceptMe(String nickname, int myId) throws SQLException {
		String sql = "SELECT 1 FROM `user` WHERE nickname=? AND id<>?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, nickname);
			ps.setInt(2, myId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	// 프로필 수정 //
	
	public void updateProfile(int id, String email, String nickname, String phone, String birth, String gender,
			String address) throws SQLException {
		String sql = "UPDATE `user` SET email=?, nickname=?, phone=?, birth=?, gender=?, address=? WHERE id=?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, email);
			ps.setString(2, nickname);
			ps.setString(3, phone);
			ps.setString(4, birth);
			ps.setString(5, gender);
			ps.setString(6, address);
			ps.setInt(7, id);
			ps.executeUpdate();
		}
	}

	// 비밀번호 수정 //
	
	public void updatePassword(int id, String hashed) throws SQLException {
		String sql = "UPDATE `user` SET user_pw=? WHERE id=?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, hashed);
			ps.setInt(2, id);
			ps.executeUpdate();
		}
	}
	
	// 이메일 인증 //
	public User findByEmail(String email) throws SQLException {
	    String sql = "SELECT * FROM `user` WHERE email = ?";
	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, email);
	        try (ResultSet rs = ps.executeQuery()) {
	            return rs.next() ? map(rs) : null;
	        }
	    }
	}

	
}
