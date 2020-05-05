package com.thoughtworks.common.exception;

import com.google.common.collect.ImmutableMap;

public class QuorumConnectionErrorException extends AppException {
    public QuorumConnectionErrorException(String rpcUrl) {
        super(ErrorCode.QUORUM_CONNECTION_ERROR, ImmutableMap.of("QuorumRpcUrl", rpcUrl));
    }
}
