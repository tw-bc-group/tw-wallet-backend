package com.thoughtworks.common.annotation;

import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Value("${quorum.dc-ep.center_bank_rsa_pk}")
@Retention(RetentionPolicy.RUNTIME)
public @interface CenterBankPrivateKey {
}
