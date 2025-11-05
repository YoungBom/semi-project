package util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

public class PasswordUtil {
    private static final int ITER = 150_000;
    private static final int KEYLEN = 256; // bits
    private static final int SALT_BYTES = 16; // consider 24 or 32

    public static String hash(String password) {
        Objects.requireNonNull(password, "password");
        try {
            byte[] salt = new byte[SALT_BYTES];
            new SecureRandom().nextBytes(salt);
            byte[] dk = pbkdf2(password.toCharArray(), salt, ITER, KEYLEN);
            return "PBKDF2$sha256$" + ITER + "$" + b64(salt) + "$" + b64(dk);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verify(String password, String stored) {
        if (password == null || stored == null) return false;
        try {
            // stored: PBKDF2$sha256$iter$salt$hash  (sha256 토큰은 옵션)
            String[] p = stored.split("\\$");
            if (p.length != 5 && p.length != 4) return false;
            String scheme = p[0];
            if (!"PBKDF2".equals(scheme)) return false;

            int offset = (p.length == 5) ? 1 : 0; // sha256 토큰 유무
            int iter = Integer.parseInt(p[1 + offset]);
            byte[] salt = db64(p[2 + offset]);
            byte[] hash = db64(p[3 + offset]);

            byte[] test = pbkdf2(password.toCharArray(), salt, iter, hash.length * 8);
            return slowEquals(hash, test);
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] pbkdf2(char[] pw, byte[] salt, int iter, int bits) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(pw, salt, iter, bits);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } finally {
            // 민감 데이터 메모리 정리
            java.util.Arrays.fill(pw, '\0');
        }
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        int max = Math.max(a.length, b.length);
        int r = a.length ^ b.length; // 길이 차이도 누적
        for (int i = 0; i < max; i++) {
            byte x = i < a.length ? a[i] : 0;
            byte y = i < b.length ? b[i] : 0;
            r |= x ^ y;
        }
        return r == 0;
    }

    private static String b64(byte[] x) { return Base64.getEncoder().encodeToString(x); }
    private static byte[] db64(String s) { return Base64.getDecoder().decode(s); }
}
