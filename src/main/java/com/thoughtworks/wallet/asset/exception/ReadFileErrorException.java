package com.thoughtworks.wallet.asset.exception;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.wallet.common.exception.AppException;
import com.thoughtworks.wallet.common.exception.ErrorCode;

public class ReadFileErrorException extends AppException {
    public ReadFileErrorException(String path) {
        super(ErrorCode.FILE_READ_ERROR, ImmutableMap.of("file-path", path));
    }
}
