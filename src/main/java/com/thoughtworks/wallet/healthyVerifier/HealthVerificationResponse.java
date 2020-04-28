package com.thoughtworks.wallet.healthyVerifier;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HealthVerificationResponse {
    @NonNull
    private String phone;

    @NonNull
    private String status;

    @NonNull
    private Date verificationTime;
}
