package util;
import jakarta.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Base64;

public class CsrfUtil {
  public static String ensureToken(HttpSession s){
    String t = (String) s.getAttribute("_csrf");
    if (t==null) {
      byte[] b=new byte[16]; new SecureRandom().nextBytes(b);
      t = Base64.getUrlEncoder().withoutPadding().encodeToString(b);
      s.setAttribute("_csrf", t);
    }
    return t;
  }
  public static boolean valid(HttpSession s, String t){
    String v = (String) s.getAttribute("_csrf");
    return v!=null && v.equals(t);
  }
}
