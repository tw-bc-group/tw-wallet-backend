package com.thoughtworks.wallet.healthyVerifier.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
@AllArgsConstructor
public enum VerifyResultEnum {
    TRUE,
    FALSE,
    NOT_SUPPORT;
}

