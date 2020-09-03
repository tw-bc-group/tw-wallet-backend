package com.thoughtworks.wallet.asset.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.common.exception.AppException;

import static com.thoughtworks.common.exception.ErrorCode.DCEP_NOT_FOUND;

public class DCEPNotFoundException extends AppException {
    public DCEPNotFoundException(String serialNumber) {
        super(DCEP_NOT_FOUND, ImmutableMap.of("serialNumber", serialNumber));
    }
}
