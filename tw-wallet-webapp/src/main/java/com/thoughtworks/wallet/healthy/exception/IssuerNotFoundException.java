package com.thoughtworks.wallet.healthy.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.common.exception.AppException;

import static com.thoughtworks.common.exception.ErrorCode.ISSUER_NOT_FOUND;

public class IssuerNotFoundException extends AppException {
    public IssuerNotFoundException() { super(ISSUER_NOT_FOUND, ImmutableMap.of()); }
    public IssuerNotFoundException(Integer id) {
        super(ISSUER_NOT_FOUND, ImmutableMap.of("id", id));
    }
}
