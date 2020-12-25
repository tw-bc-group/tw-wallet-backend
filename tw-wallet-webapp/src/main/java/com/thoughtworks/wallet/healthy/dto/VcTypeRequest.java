package com.thoughtworks.wallet.healthy.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class VcTypeRequest {
    @NotBlank
    @Getter
    String name;
    @NotNull
    @Getter
    Integer issuerId;
    @NotEmpty
    @Getter
    List<String> content;
}
