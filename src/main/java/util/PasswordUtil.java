package util;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordUtil {
	private static final String ALG = "PBKDF2WithHmacSHA256";
	private static final int ITER = 200_000;
	private static final int KEYLEN = 256; // bits
	private static final int SALT_LEN = 16;

	private PasswordUtil() {
	}

	public static String hash(String plain) {
		byte[] salt = new byte[SALT_LEN];
		new SecureRandom().nextBytes(salt);
		byte[] dk = pbkdf2(plain.toCharArray(), salt, ITER, KEYLEN);
		return String.format("pbkdf2$%d$%s$%s", ITER, Base64.getEncoder().encodeToString(salt),
				Base64.getEncoder().encodeToString(dk));
	}

	public static boolean verify(String plain, String stored) {
		try {
			String[] parts = stored.split("\\$");
			int it = Integer.parseInt(parts[1]);
			byte[] salt = Base64.getDecoder().decode(parts[2]);
			byte[] expect = Base64.getDecoder().decode(parts[3]);
			byte[] got = pbkdf2(plain.toCharArray(), salt, it, expect.length * 8);
			return constantTimeEq(expect, got);
		} catch (Exception e) {
			return false;
		}
	}

	private static byte[] pbkdf2(char[] p, byte[] s, int it, int bits) {
		try {
			PBEKeySpec spec = new PBEKeySpec(p, s, it, bits);
			return SecretKeyFactory.getInstance(ALG).generateSecret(spec).getEncoded();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static boolean constantTimeEq(byte[] a, byte[] b) {
		if (a.length != b.length)
			return false;
		int r = 0;
		for (int i = 0; i < a.length; i++)
			r |= a[i] ^ b[i];
		return r == 0;
	}
}
