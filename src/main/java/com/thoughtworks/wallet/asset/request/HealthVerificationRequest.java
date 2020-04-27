package com.thoughtworks.wallet.asset.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.wallet.asset.exception.InvalidHealthyStatusException;
import com.thoughtworks.wallet.asset.model.HealthyStatus;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Value
public class HealthVerificationRequest {
    @Length(min = 11, max = 11, message = "Phone number should only have 11 digits.")
    String phone;

    @NotBlank
    String status;

    @JsonCreator
    public HealthVerificationRequest(@JsonProperty("phone") String phone,
                                     @JsonProperty("status") String status) {
        if (!HealthyStatus.isValid(status)) {
            throw new InvalidHealthyStatusException(status);
        }

        this.phone = phone;
        this.status = status;
    }
}
