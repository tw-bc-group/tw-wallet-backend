package com.thoughtworks.wallet.annotation;

import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Value("${quorum.tw-point.contract-address}")
@Retention(RetentionPolicy.RUNTIME)
public @interface TWPointContractAddress {
}
