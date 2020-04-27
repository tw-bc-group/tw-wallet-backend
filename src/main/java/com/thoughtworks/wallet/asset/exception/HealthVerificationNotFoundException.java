package com.thoughtworks.wallet.asset.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.wallet.common.exception.AppException;

import static com.thoughtworks.wallet.common.exception.ErrorCode.HEALTH_VERIFICATION_NOT_FOUND;

public class HealthVerificationNotFoundException extends AppException {
    public HealthVerificationNotFoundException(String phone) {
        super(HEALTH_VERIFICATION_NOT_FOUND, ImmutableMap.of("Phone", phone));
    }
}
