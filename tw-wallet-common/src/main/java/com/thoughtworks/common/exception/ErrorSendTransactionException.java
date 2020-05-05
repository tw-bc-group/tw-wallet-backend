package com.thoughtworks.common.exception;
import com.google.common.collect.ImmutableMap;

public class ErrorSendTransactionException extends AppException {
    public ErrorSendTransactionException(String errorMessage) {
        super(ErrorCode.SEND_TRANSACTION_ERROR, ImmutableMap.of("error", errorMessage));
    }
}