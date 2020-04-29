package com.thoughtworks.wallet.healthyVerifier.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.wallet.common.exception.AppException;

import static com.thoughtworks.wallet.common.exception.ErrorCode.HEALTH_VERIFICATION_ALREADY_EXIST;

public class HealthVerificationAlreadyExistException extends AppException {
    public HealthVerificationAlreadyExistException(String did) {
        super(HEALTH_VERIFICATION_ALREADY_EXIST, ImmutableMap.of("ownerId", did));
    }
}
