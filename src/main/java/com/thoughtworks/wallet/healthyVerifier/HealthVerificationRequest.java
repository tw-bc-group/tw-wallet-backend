package com.thoughtworks.wallet.healthyVerifier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.wallet.healthyVerifier.validator.ValidHealthyStatus;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

public class HealthVerificationRequest {
    @Length(min = 11, max = 11, message = "Phone number should only have 11 digits.")
    @Getter
    String phone;

    @ValidHealthyStatus
    String status;

    @JsonCreator
    public HealthVerificationRequest(@JsonProperty("phone") String phone,
                                     @JsonProperty("status") String status) {
        this.phone = phone;
        this.status = status;
    }

    public HealthyStatus getStatus() {
        return HealthyStatus.valueOf(status.toUpperCase());
    }
}
