package com.tribhaskar.auth.controller;

/**
 * This holds the static API paths with the versioning as [v1]
 * Updated functionality in the APIs in any of the future release can be
 * versioned as [v2] and so on ...
 * */
public class ApiPaths {
    public static final String API_CONTEXT_PATH = "/coinRank";
    public static final String API_VERSION = "/api/v1";
    public static final String USER = "/user";
    public static final String USER_API_PREFIX = API_CONTEXT_PATH + API_VERSION + USER;
    public static final String USER_REGISTER = USER_API_PREFIX + "/register";
    public static final String USER_LOGIN = USER_API_PREFIX + "/login";
    public static final String USER_VERIFY_EMAIL = USER_API_PREFIX + "/verify-email";
    public static final String USER_RESEND_OTP = USER_API_PREFIX + "/resend-otp";
    public static final String USER_FORGOT_PASSWORD = USER_API_PREFIX + "/forgot-password";
    public static final String USER_RESET_PASSWORD = USER_API_PREFIX + "/reset-password";
    public static final String USER_TEST = USER_API_PREFIX + "/test";

    private ApiPaths(){}
}
