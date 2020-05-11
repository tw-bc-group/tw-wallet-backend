package com.thoughtworks.wallet.healthyVerifier.service;

import com.thoughtworks.wallet.healthyVerifier.dto.ChangeHealthVerificationRequest;
import com.thoughtworks.wallet.healthyVerifier.dto.HealthVerificationRequest;
import com.thoughtworks.wallet.healthyVerifier.dto.HealthVerificationResponse;

public interface IHealthyVerifierService {
    HealthVerificationResponse createHealthVerification(HealthVerificationRequest healthVerification);

    HealthVerificationResponse getHealthVerification(String ownerId);

    HealthVerificationResponse changeHealthVerification(ChangeHealthVerificationRequest changeHealthVerificationRequest);
}
