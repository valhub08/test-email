package com.example.email_test3.controller;

import com.example.email_test3.dto.request.EmailVerificationRequest;
import com.example.email_test3.dto.request.VerificationCodeRequest;
import com.example.email_test3.dto.response.ApiResponse;
import com.example.email_test3.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailVerificationController {
    private final EmailService emailService;

    @PostMapping("/verification/send")
    public ApiResponse<Void> sendVerificationEmail(
            @Valid @RequestBody EmailVerificationRequest request) {
        emailService.sendVerificationEmail(request.getEmail());
        return ApiResponse.success("인증 이메일이 전송되었습니다.");
    }

    @PostMapping("/verification/verify")
    public ApiResponse<Void> verifyEmail(
            @Valid @RequestBody VerificationCodeRequest request) {
        boolean isValid = emailService.verifyCode(request.getEmail(), request.getCode());
        return isValid
                ? ApiResponse.success("이메일 인증이 완료되었습니다.")
                : ApiResponse.error("인증에 실패했습니다.");
    }
}
