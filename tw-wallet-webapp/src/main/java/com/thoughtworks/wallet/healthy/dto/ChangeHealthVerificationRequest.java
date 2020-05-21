package com.thoughtworks.wallet.healthy.dto;

import com.thoughtworks.wallet.healthy.model.HealthyStatus;
import lombok.Data;

@Data
public class ChangeHealthVerificationRequest {
    String ownerId;
    HealthyStatus healthyStatus;
}
