package util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {
	private static final int ITER = 150000;
	private static final int KEYLEN = 256; // bits

	public static String hash(String password) {
		try {
			byte[] salt = new byte[16];
			new SecureRandom().nextBytes(salt);
			byte[] dk = pbkdf2(password.toCharArray(), salt, ITER, KEYLEN);
			return "PBKDF2$" + ITER + "$" + b64(salt) + "$" + b64(dk);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean verify(String password, String stored) {
		try {
			// stored: PBKDF2$iter$salt$hash
			String[] p = stored.split("\\$");
			if (p.length != 4 || !"PBKDF2".equals(p[0]))
				return false;
			int iter = Integer.parseInt(p[1]);
			byte[] salt = db64(p[2]);
			byte[] hash = db64(p[3]);
			byte[] test = pbkdf2(password.toCharArray(), salt, iter, hash.length * 8);
			return slowEquals(hash, test);
		} catch (Exception e) {
			return false;
		}
	}

	private static byte[] pbkdf2(char[] pw, byte[] salt, int iter, int bits) throws Exception {
		PBEKeySpec spec = new PBEKeySpec(pw, salt, iter, bits);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		return skf.generateSecret(spec).getEncoded();
	}

	private static boolean slowEquals(byte[] a, byte[] b) {
		if (a.length != b.length)
			return false;
		int r = 0;
		for (int i = 0; i < a.length; i++)
			r |= a[i] ^ b[i];
		return r == 0;
	}

	private static String b64(byte[] x) {
		return Base64.getEncoder().encodeToString(x);
	}

	private static byte[] db64(String s) {
		return Base64.getDecoder().decode(s);
	}
}
