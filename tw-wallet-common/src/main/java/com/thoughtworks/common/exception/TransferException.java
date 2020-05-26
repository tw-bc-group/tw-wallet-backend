package com.thoughtworks.common.exception;

import com.google.common.collect.ImmutableMap;

public class TransferException extends AppException {
    public TransferException(String rpcUrl) {
        super(ErrorCode.TRANSFER_ERROR, ImmutableMap.of("QuorumRpcUrl", rpcUrl));
    }
}
