package com.thoughtworks.wallet.healthyVerifier.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.wallet.common.exception.AppException;

import static com.thoughtworks.wallet.common.exception.ErrorCode.INSERT_INTO_DATABASE_ERROR;

public class InsertIntoDatabaseErrorException extends AppException {
    public InsertIntoDatabaseErrorException(String did) {
        super(INSERT_INTO_DATABASE_ERROR, ImmutableMap.of("ownerId", did));
    }
}

