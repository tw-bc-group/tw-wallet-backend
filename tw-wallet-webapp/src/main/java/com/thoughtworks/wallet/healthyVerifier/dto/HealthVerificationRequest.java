package com.thoughtworks.wallet.healthyVerifier.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

public class HealthVerificationRequest {
    @Getter
    String did;

    @Length(min = 11, max = 11, message = "Phone number should only have 11 digits.")
    @Getter
    String phone;

    @JsonCreator
    public HealthVerificationRequest(@JsonProperty("phone") String phone,
                                     @JsonProperty("did") String did) {
        this.did = did;
        this.phone = phone;
    }
}
