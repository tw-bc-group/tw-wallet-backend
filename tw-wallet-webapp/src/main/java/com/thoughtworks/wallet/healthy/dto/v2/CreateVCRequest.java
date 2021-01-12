package com.thoughtworks.wallet.healthy.dto.v2;

import lombok.Getter;

import javax.validation.constraints.*;

import static javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE;

/**
 * 申请VC的API
 */

public class CreateVCRequest {
    @NotBlank
    @Getter
    @Size(min = 5, max = 100, message = "The length of issueId is  between 5 and 100")
    String did;

    @NotBlank
    @Getter
    @Size(min = 5, max = 255, message = "The length of phone is  between 5 and 255")
    String phone;

    @NotBlank
    @Getter
    @Size(min = 2, max = 255, message = "The length of name is between 2 and 255")
    String name;
}
