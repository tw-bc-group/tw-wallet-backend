package com.thoughtworks.wallet.healthy.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.common.exception.AppException;

import static com.thoughtworks.common.exception.ErrorCode.VERIFY_HEALTH_VC_ERROR;
import static com.thoughtworks.common.exception.ErrorCode.VERIFY_TRANSACTION_ERROR;

public class VerifyHealthyVCJwtException extends AppException {
    public VerifyHealthyVCJwtException(String reason) {
        super(VERIFY_HEALTH_VC_ERROR, ImmutableMap.of("reason", reason));
    }
}
