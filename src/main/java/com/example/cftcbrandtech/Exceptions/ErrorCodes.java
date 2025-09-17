package com.example.cftcbrandtech.Exceptions;

import org.springframework.http.HttpStatus;

public enum ErrorCodes {
    AUTH_INVALID_CREDENTIALS(1000, HttpStatus.UNAUTHORIZED, "Invalid credentials"),
    AUTH_USER_NOT_FOUND(1001, HttpStatus.NOT_FOUND, "User not found"),
    AUTH_TOKEN_EXPIRED(1002, HttpStatus.UNAUTHORIZED, "Token expired"),
    AUTH_TOKEN_INVALID(1003, HttpStatus.UNAUTHORIZED, "Invalid token"),
    AUTH_EMAIL_TAKEN(1004, HttpStatus.BAD_REQUEST, "Email is already taken"),
    AUTH_RESET_TOKEN_INVALID(1005, HttpStatus.UNAUTHORIZED, "Invalid reset token"),
    AUTH_KEYCLOAK_CREATION_FAILED(1006, HttpStatus.EXPECTATION_FAILED, "Keycloak user creation failed."),
    AUTH_DATABASE_SAVE_FAILED(1007, HttpStatus.BAD_REQUEST, "User creation on database failed."),
    INTERNAL_SERVER_ERROR(9000, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error"),
    VALIDATION_ERROR(9001, HttpStatus.BAD_REQUEST, "Validation Failed");

    private final int code;
    private final HttpStatus status;
    private final String message;

    ErrorCodes(int code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}