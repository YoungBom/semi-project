package util;

public final class SessionKeys {
    private SessionKeys() {}

    /** 로그인한 사용자의 PK (정수) */
    public static final String LOGIN_UID    = "LOGIN_UID";
    /** 로그인한 사용자의 로그인 아이디(문자열) */
    public static final String LOGIN_USERID = "LOGIN_USERID";
    /** 로그인한 사용자의 표시 이름(옵션) */
    public static final String LOGIN_NAME   = "LOGIN_NAME";
}
