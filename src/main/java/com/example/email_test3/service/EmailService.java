package com.example.email_test3.service;

import com.example.email_test3.constants.EmailConstants;
import com.example.email_test3.exception.EmailErrorCode;
import com.example.email_test3.exception.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final EmailTemplateService templateService;
    private final VerificationCodeService verificationCodeService;

    public void sendVerificationEmail(String email) {
        String code = verificationCodeService.generateAndSaveCode(email);
        String htmlContent = templateService.generateVerificationEmailTemplate(code);

        try {
            sendEmail(email, htmlContent);
        } catch (MessagingException e) {
            log.error("Failed to send verification email to: {}", email, e);
            throw new EmailException(EmailErrorCode.EMAIL_SEND_FAILED);
        }
    }

    public boolean verifyCode(String email, String code) {
        return verificationCodeService.verifyCode(email, code);
    }

    private void sendEmail(String email, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject(EmailConstants.EMAIL_SUBJECT);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}