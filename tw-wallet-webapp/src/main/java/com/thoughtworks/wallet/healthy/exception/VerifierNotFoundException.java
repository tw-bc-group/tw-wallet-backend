package com.thoughtworks.wallet.healthy.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.common.exception.AppException;

import static com.thoughtworks.common.exception.ErrorCode.VERIFIER_NOT_FOUND;

public class VerifierNotFoundException extends AppException {
    public VerifierNotFoundException(Integer id) {
        super(VERIFIER_NOT_FOUND, ImmutableMap.of("id", id));
    }
}
