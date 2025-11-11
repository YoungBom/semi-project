package util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * PBKDF2-HMAC-SHA256 저장형식: pbkdf2$iterations$saltBase64$hashBase64
 */
public final class PasswordUtil {
	private static final String ALG = "PBKDF2WithHmacSHA256";
	private static final int ITER = 200_000;
	private static final int SALT_LEN = 16; // 128-bit
	private static final int DK_LEN = 32; // 256-bit

	private PasswordUtil() {
	}

	public static String hash(String password) {
		try {
			byte[] salt = new byte[SALT_LEN];
			SecureRandom.getInstanceStrong().nextBytes(salt);
			byte[] dk = pbkdf2(password.toCharArray(), salt, ITER, DK_LEN);
			return String.format("pbkdf2$%d$%s$%s", ITER, Base64.getEncoder().encodeToString(salt),
					Base64.getEncoder().encodeToString(dk));
		} catch (Exception e) {
			throw new RuntimeException("Password hash error", e);
		}
	}

	public static boolean verify(String password, String stored) {
		if (stored == null || stored.isEmpty())
			return false;
		if (!stored.startsWith("pbkdf2$"))
			return password.equals(stored); // 평문 호환(마이그레이션 단계)
		try {
			String[] parts = stored.split("\\$");
			int it = Integer.parseInt(parts[1]);
			byte[] salt = Base64.getDecoder().decode(parts[2]);
			byte[] hash = Base64.getDecoder().decode(parts[3]);
			byte[] test = pbkdf2(password.toCharArray(), salt, it, hash.length);
			return constantTimeEquals(hash, test);
		} catch (Exception e) {
			return false;
		}
	}

	private static byte[] pbkdf2(char[] pwd, byte[] salt, int iter, int dkLen)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		PBEKeySpec spec = new PBEKeySpec(pwd, salt, iter, dkLen * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance(ALG);
		return skf.generateSecret(spec).getEncoded();
	}

	private static boolean constantTimeEquals(byte[] a, byte[] b) {
		if (a == null || b == null || a.length != b.length)
			return false;
		int r = 0;
		for (int i = 0; i < a.length; i++)
			r |= a[i] ^ b[i];
		return r == 0;
	}
	public static String normalize(String s) {
	    if (s == null) return "";
	    String t = s.trim().replaceAll("\\s+", " ");
	    return t.toLowerCase();
	}

}
