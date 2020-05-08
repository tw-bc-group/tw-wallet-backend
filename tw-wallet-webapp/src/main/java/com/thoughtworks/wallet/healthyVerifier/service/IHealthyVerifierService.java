package com.thoughtworks.wallet.healthyVerifier.service;

import com.thoughtworks.wallet.healthyVerifier.HealthVerificationRequest;
import com.thoughtworks.wallet.healthyVerifier.HealthVerificationResponse;

public interface IHealthyVerifierService {
    HealthVerificationResponse createHealthVerification(HealthVerificationRequest healthVerification);

    HealthVerificationResponse getHealthVerification(String ownerId);
}
