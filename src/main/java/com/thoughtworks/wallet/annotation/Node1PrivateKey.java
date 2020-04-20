package com.thoughtworks.wallet.annotation;

import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Value("${quorum.node1.private-key}")
@Retention(RetentionPolicy.RUNTIME)
public @interface Node1PrivateKey {
}
