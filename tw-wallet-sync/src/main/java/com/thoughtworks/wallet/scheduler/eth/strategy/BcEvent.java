package com.thoughtworks.wallet.scheduler.eth.strategy;

import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 标识区块链事件
 */
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface BcEvent {
}
