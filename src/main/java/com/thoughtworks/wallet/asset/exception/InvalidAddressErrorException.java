package com.thoughtworks.wallet.asset.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.wallet.common.exception.AppException;
import com.thoughtworks.wallet.common.exception.ErrorCode;

public class InvalidAddressErrorException extends AppException {
    public InvalidAddressErrorException(String address) {
        super(ErrorCode.INVALID_ADDRESS, ImmutableMap.of("Address", address));
    }
}
