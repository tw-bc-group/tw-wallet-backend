package com.thoughtworks.wallet.healthy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VerifyResultEnum {
    TRUE,
    FALSE,
    NOT_SUPPORT;
}

