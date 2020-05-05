package com.thoughtworks.wallet.healthyVerifier.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.common.exception.AppException;

import static com.thoughtworks.common.exception.ErrorCode.INVALID_HEALTHY_STATUS;

public class InvalidHealthyStatusException extends AppException {
    public InvalidHealthyStatusException(String status) {
        super(INVALID_HEALTHY_STATUS, ImmutableMap.of("Healthy Status", status));
    }
}

