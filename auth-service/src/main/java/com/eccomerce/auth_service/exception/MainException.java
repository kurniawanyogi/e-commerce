package com.eccomerce.auth_service.exception;

import lombok.Getter;

@Getter
public class MainException extends RuntimeException {
    private final String code;
    private final String message;

    public MainException(String code, String errorMessage) {
        this.code = code;
        this.message = errorMessage;
    }
}
