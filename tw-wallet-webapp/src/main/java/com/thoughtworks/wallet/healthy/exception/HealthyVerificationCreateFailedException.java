package com.thoughtworks.wallet.healthy.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.common.exception.AppException;

import static com.thoughtworks.common.exception.ErrorCode.HEALTH_VERIFICATION_CREATE_FAILED;

public class HealthyVerificationCreateFailedException extends AppException {
    public HealthyVerificationCreateFailedException(String ownerId) {
        super(HEALTH_VERIFICATION_CREATE_FAILED, ImmutableMap.of("ownerID", ownerId));
    }
}
