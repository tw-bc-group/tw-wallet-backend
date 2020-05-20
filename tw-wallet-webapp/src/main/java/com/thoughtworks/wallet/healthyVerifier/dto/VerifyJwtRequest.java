package com.thoughtworks.wallet.healthyVerifier.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

public class VerifyJwtRequest {
    @NotBlank
    @Getter
    String token;
}
