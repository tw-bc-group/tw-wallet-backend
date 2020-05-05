package com.thoughtworks.wallet.healthyVerifier.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.common.exception.AppException;

import static com.thoughtworks.common.exception.ErrorCode.HEALTH_VERIFICATION_NOT_FOUND;

public class HealthVerificationNotFoundException extends AppException {
    public HealthVerificationNotFoundException(String phone) {
        super(HEALTH_VERIFICATION_NOT_FOUND, ImmutableMap.of("Phone", phone));
    }
}
