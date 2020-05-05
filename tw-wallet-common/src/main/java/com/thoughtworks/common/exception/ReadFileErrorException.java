package com.thoughtworks.common.exception;

import com.google.common.collect.ImmutableMap;

public class ReadFileErrorException extends AppException {
    public ReadFileErrorException(String path) {
        super(ErrorCode.FILE_READ_ERROR, ImmutableMap.of("file-path", path));
    }
}
