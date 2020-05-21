package com.thoughtworks.wallet.healthy.exception;


import com.thoughtworks.common.exception.AppException;
import com.thoughtworks.common.exception.ErrorCode;

import java.util.Collections;

public class QuorumConnectionErrorException extends AppException {
    public QuorumConnectionErrorException() {
        super(ErrorCode.QUORUM_CONNECTION_ERROR, Collections.emptyMap());
    }
}
