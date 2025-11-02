package util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class PasswordUtil {
	private static final String ALGO = "PBKDF2WithHmacSHA256";
	private static final int SALT_LEN = 16;
	private static final int DK_LEN = 32; // bytes
	private static final int ITER = 200_000;

	public static String hash(String raw) {
		try {
			byte[] salt = new byte[SALT_LEN];
			new SecureRandom().nextBytes(salt);
			byte[] h = pbk(raw.toCharArray(), salt, ITER, DK_LEN);
			return "PBKDF2$" + ITER + "$" + b64(salt) + "$" + b64(h);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean verify(String raw, String stored) {
		try {
			String[] p = stored.split("\\$"); // PBKDF2$iter$salt$hash
			int iter = Integer.parseInt(p[1]);
			byte[] salt = b64d(p[2]);
			byte[] expect = b64d(p[3]);
			byte[] actual = pbk(raw.toCharArray(), salt, iter, expect.length);
			if (actual.length != expect.length)
				return false;
			int r = 0;
			for (int i = 0; i < actual.length; i++)
				r |= (actual[i] ^ expect[i]);
			return r == 0;
		} catch (Exception e) {
			return false;
		}
	}

	private static byte[] pbk(char[] pwd, byte[] salt, int it, int dk) throws Exception {
		KeySpec spec = new PBEKeySpec(pwd, salt, it, dk * 8);
		return SecretKeyFactory.getInstance(ALGO).generateSecret(spec).getEncoded();
	}

	private static String b64(byte[] v) {
		return Base64.getEncoder().encodeToString(v);
	}

	private static byte[] b64d(String s) {
		return Base64.getDecoder().decode(s);
	}
}
