package com.example.email_test3.constants;

public class EmailConstants {
    public static final String EMAIL_SUBJECT = "삼디 이메일 인증";
    public static final int VERIFICATION_CODE_LENGTH = 6;
    public static final int VERIFICATION_CODE_EXPIRY_MINUTES = 3;
    public static final String REDIS_EMAIL_PREFIX = "email:verification:";
}