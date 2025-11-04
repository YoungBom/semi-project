package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
  private static final String URL =
      "jdbc:mysql://localhost:3306/semi_project?characterEncoding=UTF-8&serverTimezone=Asia/Seoul";
  private static final String USER = "root";
  private static final String PASSWORD = "1234";

  public static Connection getConnection() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      return DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
