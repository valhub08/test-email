package com.example.email_test3.service;

import com.example.email_test3.constants.EmailConstants;
import com.example.email_test3.exception.EmailErrorCode;
import com.example.email_test3.exception.EmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {
    private final StringRedisTemplate redisTemplate;

    public String generateAndSaveCode(String email) {
        String code = generateRandomCode();
        saveVerificationCode(email, code);
        return code;
    }

    public boolean verifyCode(String email, String code) {
        try {
            String key = getRedisKey(email);
            String storedCode = redisTemplate.opsForValue().get(key);

            if (storedCode == null || !storedCode.equals(code)) {
                throw new EmailException(EmailErrorCode.INVALID_VERIFICATION_CODE);
            }

            redisTemplate.delete(key);
            return true;
        } catch (Exception e) {
            throw new EmailException(EmailErrorCode.REDIS_OPERATION_FAILED);
        }
    }

    private String generateRandomCode() {
        Random random = new Random();
        return String.format("%0" + EmailConstants.VERIFICATION_CODE_LENGTH + "d",
                random.nextInt((int) Math.pow(10, EmailConstants.VERIFICATION_CODE_LENGTH)));
    }

    private void saveVerificationCode(String email, String code) {
        try {
            String key = getRedisKey(email);
            redisTemplate.opsForValue().set(key, code,
                    EmailConstants.VERIFICATION_CODE_EXPIRY_MINUTES, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new EmailException(EmailErrorCode.REDIS_OPERATION_FAILED);
        }
    }

    private String getRedisKey(String email) {
        return EmailConstants.REDIS_EMAIL_PREFIX + email;
    }
}