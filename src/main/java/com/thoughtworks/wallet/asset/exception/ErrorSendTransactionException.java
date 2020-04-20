package com.thoughtworks.wallet.asset.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.wallet.common.exception.AppException;
import com.thoughtworks.wallet.common.exception.ErrorCode;

public class ErrorSendTransactionException extends AppException {
    public ErrorSendTransactionException(String errorMessage) {
        super(ErrorCode.SEND_TRANSACTION_ERROR, ImmutableMap.of("error", errorMessage));
    }
}