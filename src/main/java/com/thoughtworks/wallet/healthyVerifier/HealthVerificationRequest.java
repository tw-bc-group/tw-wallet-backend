package com.thoughtworks.wallet.healthyVerifier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.wallet.healthyVerifier.exception.InvalidHealthyStatusException;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Value
public class HealthVerificationRequest {
    @Length(min = 11, max = 11, message = "Phone number should only have 11 digits.")
    String phone;

    @NotBlank
    HealthyStatus status;

    @JsonCreator
    public HealthVerificationRequest(@JsonProperty("phone") String phone,
                                     @JsonProperty("status") String status) {
        HealthyStatus healthyStatus;
        try {
            healthyStatus = HealthyStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidHealthyStatusException(status);
        }

        this.phone = phone;
        this.status = healthyStatus;
    }
}
