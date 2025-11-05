package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/semi_project?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&characterEncoding=utf8";
	private static final String USER = "root"; // 학원 DB 계정
	private static final String PASS = "1234"; // 학원 DB 비번

	public static Connection getConnection() throws RuntimeException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(URL, USER, PASS);
		} catch (Exception e) {
			throw new RuntimeException("DB 연결 실패", e);
		}
	}
}
