package com.thoughtworks.wallet.healthy.dto.v2;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class VerifierRequest {
    @NotBlank
    @Getter
    @Size(min = 5, max = 100, message = "The length of id is between 5 and 100")
    String id;
    @NotBlank
    @Getter
    String name;
    @NotEmpty
    @Getter
    List<String> vcTypes;
}
