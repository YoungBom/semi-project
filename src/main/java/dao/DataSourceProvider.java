
package util;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class DataSourceProvider {
    private static final BasicDataSource ds = new BasicDataSource();
    static {
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/burgerhub?serverTimezone=Asia/Seoul&useUnicode=true&characterEncoding=UTF-8");
        ds.setUsername("root");
        ds.setPassword("비밀번호");
        ds.setInitialSize(5);
        ds.setMaxTotal(20);
    }
    public static DataSource get() { return ds; }
}
