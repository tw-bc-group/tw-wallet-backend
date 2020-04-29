package com.thoughtworks.wallet.healthyVerifier.exception;

import com.thoughtworks.wallet.common.exception.AppException;
import com.thoughtworks.wallet.common.exception.ErrorCode;

import java.util.Collections;

public class QuorumConnectionErrorException extends AppException {
    public QuorumConnectionErrorException() {
        super(ErrorCode.QUORUM_CONNECTION_ERROR, Collections.emptyMap());
    }
}
