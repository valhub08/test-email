package com.example.email_test3.exception;

import lombok.Getter;

@Getter
public class EmailException extends RuntimeException {
    private final EmailErrorCode errorCode;

    public EmailException(EmailErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}