package com.thoughtworks.wallet.healthyVerifier.dto;

import com.thoughtworks.wallet.healthyVerifier.model.HealthyStatus;
import lombok.Data;

@Data
public class ChangeHealthVerificationRequest {
    String ownerId;
    HealthyStatus healthyStatus;
}
