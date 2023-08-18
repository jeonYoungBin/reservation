package com.reservation.reservation.config;

public final class Code {
    //성공시
    public static int OK_CODE = 200;
    public static String OK_MSG = "OK";
    //데이터가 없는 경우
    public static int NO_CONTENT_CODE = 204;
    public static String NO_CONTENT_MSG = "No Value Present";
    //validation error
    public static int VALIDATE_ERROR_CODE = 207;
    //기타 서버 에러
    public static int INTERNAL_SERVER_ERROR_CODE = 500;
}
