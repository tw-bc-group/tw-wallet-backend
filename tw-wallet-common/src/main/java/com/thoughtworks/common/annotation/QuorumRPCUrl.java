package com.thoughtworks.common.annotation;

import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Value("${quorum.rpc.url}")
@Retention(RetentionPolicy.RUNTIME)
public @interface QuorumRPCUrl {
}
