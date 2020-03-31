package com.thoughtworks.wallet.asset.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.wallet.common.exception.AppException;
import com.thoughtworks.wallet.common.exception.ErrorCode;

public class QuorumConnectionErrorException extends AppException {
    public QuorumConnectionErrorException(String rpcUrl) {
        super(ErrorCode.QUORUM_CONNECTION_ERROR, ImmutableMap.of("QuorumRpcUrl", rpcUrl));
    }
}
