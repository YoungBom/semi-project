package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/demo_app?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&characterEncoding=utf8";
	private static final String USER = "root"; // 또는 appuser
	private static final String PASS = "비밀번호";

	public static Connection getConnection() throws RuntimeException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(URL, USER, PASS);
		} catch (Exception e) {
			throw new RuntimeException("DB 연결 실패", e);
		}
	}
}
