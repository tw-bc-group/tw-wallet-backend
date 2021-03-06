package com.thoughtworks.wallet.healthy.dto.v2;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class VerifierVcTypesRequest {
    @NotEmpty
    @Getter
    List<String> vcTypes;

    @NotEmpty
    @Getter
    String name;
}
