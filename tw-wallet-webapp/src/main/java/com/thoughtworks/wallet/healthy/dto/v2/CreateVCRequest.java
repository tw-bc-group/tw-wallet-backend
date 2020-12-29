package com.thoughtworks.wallet.healthy.dto.v2;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 申请VC的API
*/

public class CreateVCRequest {
    @NotBlank
    @Getter
    String did;

    @NotBlank
    @Getter
    String phone;

    @NotBlank
    @Getter
    String name;
}
