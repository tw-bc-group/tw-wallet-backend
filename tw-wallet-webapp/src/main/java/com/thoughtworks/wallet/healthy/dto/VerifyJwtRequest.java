package com.thoughtworks.wallet.healthy.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class VerifyJwtRequest {
    @NotBlank
    @Getter
    String token;
}
