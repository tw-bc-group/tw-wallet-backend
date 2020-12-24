package com.thoughtworks.wallet.healthy.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

public class IssuerRequest {
    @NotBlank
    @Getter
    String name;
}
