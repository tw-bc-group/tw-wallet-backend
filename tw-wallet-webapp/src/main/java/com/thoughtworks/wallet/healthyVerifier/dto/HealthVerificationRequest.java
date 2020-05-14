package com.thoughtworks.wallet.healthyVerifier.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class HealthVerificationRequest {
    @Getter
    String did;

    @Length(min = 11, max = 11, message = "Phone number should only have 11 digits.")
    @Getter
    String phone;

    @Getter
    @Max(value = 50, message = "Please input valid temperature, should between 30° to 50°")
    @Min(value = 30, message = "Please input valid temperature, should between 30° to 50°")
    float temperature;

    @NotBlank
    @Getter
    String contact;

    @NotBlank
    @Getter
    String symptoms;

    @JsonCreator
    public HealthVerificationRequest(@JsonProperty("phone") String phone,
                                     @JsonProperty("did") String did,
                                     @JsonProperty("temperature") float temperature,
                                     @JsonProperty("contact") String contact,
                                     @JsonProperty("symptoms") String symptoms) {
        this.did = did;
        this.phone = phone;
        this.temperature = temperature;
        this.contact = contact;
        this.symptoms = symptoms;
    }
}
