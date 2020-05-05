package com.thoughtworks.common.exception;

import com.google.common.collect.ImmutableMap;

public class InvalidAddressErrorException extends AppException {
    public InvalidAddressErrorException(String address) {
        super(ErrorCode.INVALID_ADDRESS, ImmutableMap.of("Address", address));
    }
}
