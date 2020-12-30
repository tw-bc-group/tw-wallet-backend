package com.thoughtworks.wallet.healthy.dto.v2;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class VerifierRequest {
    @NotBlank
    @Getter
    String name;
    @NotBlank
    @Getter
    String privateKey;
    @NotEmpty
    @Getter
    List<String> vcTypes;
}
