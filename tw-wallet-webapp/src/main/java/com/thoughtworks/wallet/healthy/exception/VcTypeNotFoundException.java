package com.thoughtworks.wallet.healthy.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.common.exception.AppException;

import static com.thoughtworks.common.exception.ErrorCode.VC_TYPE_NOT_FOUND;

public class VcTypeNotFoundException extends AppException {
    public VcTypeNotFoundException(Integer id) {
        super(VC_TYPE_NOT_FOUND, ImmutableMap.of("id", id));
    }
}
