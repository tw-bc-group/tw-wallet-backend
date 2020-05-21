package com.thoughtworks.wallet.healthy.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

public class VerifyJwtRequest {
    @NotBlank
    @Getter
    String token;
}
