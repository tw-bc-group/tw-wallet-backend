package com.thoughtworks.wallet.healthyVerifier.service;

import com.thoughtworks.wallet.healthyVerifier.HealthVerificationRequest;
import com.thoughtworks.wallet.healthyVerifier.HealthVerificationResponse;

public interface IHealthyVerifierService {
    HealthVerificationResponse getHealthVerificationByPhone(String phone);

    void createHealthVerification(HealthVerificationRequest healthVerification);
}
