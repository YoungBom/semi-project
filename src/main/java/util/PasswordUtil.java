
package util;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordUtil {
	private static final String ALGO = "PBKDF2WithHmacSHA256";
	private static final int DEFAULT_ITER = 150_000;
	private static final int SALT_LEN = 16;
	private static final int DK_LEN = 32;

	private PasswordUtil() {
	}

	public static String hash(String raw) {
		byte[] salt = new byte[SALT_LEN];
		new SecureRandom().nextBytes(salt);
		byte[] dk = pbkdf2(raw.toCharArray(), salt, DEFAULT_ITER, DK_LEN);
		return String.format("pbkdf2$%d$%s$%s", DEFAULT_ITER, Base64.getEncoder().encodeToString(salt),
				Base64.getEncoder().encodeToString(dk));
	}

	public static boolean verify(String raw, String stored) {
		try {
			String[] p = stored.split("\\$");
			if (p.length != 4)
				return false;
			int iter = Integer.parseInt(p[1]);
			byte[] salt = Base64.getDecoder().decode(p[2]);
			byte[] hash = Base64.getDecoder().decode(p[3]);
			byte[] dk = pbkdf2(raw.toCharArray(), salt, iter, hash.length);
			if (dk.length != hash.length)
				return false;
			int diff = 0;
			for (int i = 0; i < dk.length; i++)
				diff |= (dk[i] ^ hash[i]);
			return diff == 0;
		} catch (Exception e) {
			return false;
		}
	}

	private static byte[] pbkdf2(char[] password, byte[] salt, int iter, int dkLen) {
		try {
			PBEKeySpec spec = new PBEKeySpec(password, salt, iter, dkLen * 8);
			return SecretKeyFactory.getInstance(ALGO).generateSecret(spec).getEncoded();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
