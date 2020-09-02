package com.thoughtworks.common.exception;

import com.google.common.collect.ImmutableMap;

public class MintException extends AppException {
    public MintException(String rpcUrl) {
        super(ErrorCode.MINT_ERROR, ImmutableMap.of("QuorumRpcUrl", rpcUrl));
    }
}
