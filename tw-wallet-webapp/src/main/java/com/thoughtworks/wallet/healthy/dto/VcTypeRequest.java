package com.thoughtworks.wallet.healthy.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class VcTypeRequest {
    @NotBlank
    @Getter
    String name;
    @NotBlank
    @Getter
    Integer issuerId;
    @NotBlank
    @Getter
    List<String> content;
}
