package com.thoughtworks.wallet.asset.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.wallet.common.exception.AppException;
import com.thoughtworks.wallet.common.exception.ErrorCode;

public class ErrorIdentityCreationException extends AppException {
    public ErrorIdentityCreationException(String errorMessage) {
        super(ErrorCode.IDENTITY_CREATE_ERROR, ImmutableMap.of("error", errorMessage));
    }
}