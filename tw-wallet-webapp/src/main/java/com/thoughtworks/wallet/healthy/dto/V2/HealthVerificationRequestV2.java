package com.thoughtworks.wallet.healthy.dto.V2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.wallet.healthy.model.Result;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 申请VC的API
*/

public class HealthVerificationRequestV2 {
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
