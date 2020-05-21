package com.thoughtworks.wallet.healthy.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.common.exception.AppException;

import static com.thoughtworks.common.exception.ErrorCode.HEALTH_VERIFICATION_NOT_FOUND;

public class HealthVerificationNotFoundException extends AppException {
    public HealthVerificationNotFoundException(String ownerId) {
        super(HEALTH_VERIFICATION_NOT_FOUND, ImmutableMap.of("ownerID", ownerId));
    }
}
