package com.thoughtworks.wallet.asset.service;

import com.thoughtworks.wallet.asset.request.HealthVerificationRequest;
import com.thoughtworks.wallet.asset.response.HealthVerificationResponse;

public interface IHealthyVerifierService {
    HealthVerificationResponse getHealthVerificationByPhone(String phone);

    void createHealthVerification(HealthVerificationRequest healthVerification);
}
