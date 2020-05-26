package com.thoughtworks.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    QUORUM_CONNECTION_ERROR(INTERNAL_SERVER_ERROR, 500_00, "Quorum 连接错误"),
    SIGN_JWT_ERROR(INTERNAL_SERVER_ERROR, 500_01, "生成签名错误"),
    SEND_TRANSACTION_ERROR(INTERNAL_SERVER_ERROR, 500_02, "交易发送失败"),
    FILE_READ_ERROR(INTERNAL_SERVER_ERROR, 500_03, "文件读取错误"),
    HEALTH_VERIFICATION_CREATE_FAILED(BAD_REQUEST, 500_04, "用户健康认证信息上链失败"),
    VERIFY_TRANSACTION_ERROR(INTERNAL_SERVER_ERROR, 500_05, "验证签名发生错误"),
    TRANSFER_ERROR(INTERNAL_SERVER_ERROR, 500_06, "转账发生错误"),

    INVALID_ADDRESS(BAD_REQUEST, 400_01, "用户地址格式有误"),
    HEALTH_VERIFICATION_NOT_FOUND(BAD_REQUEST, 400_02, "该用户暂无健康认证信息"),
    HEALTH_VERIFICATION_ALREADY_EXIST(BAD_REQUEST, 400_03, "该用户已创建健康认证信息"),
    INSERT_INTO_DATABASE_ERROR(BAD_REQUEST, 400_04, "健康认证信息入库错误");

    @NonNull
    private HttpStatus status;

    private int errCode;

    @NonNull
    private String message;
}

