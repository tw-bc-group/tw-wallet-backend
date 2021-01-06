package com.thoughtworks.wallet.healthy.dto.v2;

import lombok.Getter;

import javax.validation.constraints.*;

/**
 申请VC的API
*/

public class CreateVCRequest {
    @NotBlank
    @Getter
    @Pattern(regexp = "^DID:TW:.{1,255}")
    String did;

    @NotBlank
    @Getter
    @Size(min=5, max=255,message = "The length of phone is  between 3 and 255")
    String phone;

    @NotBlank
    @Getter
    @Size(min=3, max=255,message = "The length of name is between 3 and 255")
    String name;
}
