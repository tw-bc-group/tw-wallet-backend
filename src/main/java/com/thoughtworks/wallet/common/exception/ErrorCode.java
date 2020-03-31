package com.thoughtworks.wallet.common.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public enum ErrorCode {
    QUORUM_CONNECTION_ERROR(INTERNAL_SERVER_ERROR, "Quorum 连接错误");
    private HttpStatus status;
    private String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

