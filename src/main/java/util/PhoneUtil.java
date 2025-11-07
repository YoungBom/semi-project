package util;

public final class PhoneUtil {
    private PhoneUtil(){}

    // "010-1234-5678"로 통일. 실패 시 null 반환
    public static String normalize(String input) {
        if (input == null) return null;
        String digits = input.replaceAll("\\D+", "");
        if (digits.startsWith("82")) { // +82 처리
            digits = "0" + digits.substring(2);
        }
        if (digits.length() == 10) {
            return digits.substring(0,3) + "-" + digits.substring(3,6) + "-" + digits.substring(6);
        } else if (digits.length() == 11) {
            return digits.substring(0,3) + "-" + digits.substring(3,7) + "-" + digits.substring(7);
        }
        return null;
    }
}
