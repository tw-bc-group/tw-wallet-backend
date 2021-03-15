package com.thoughtworks.wallet.retry;


import com.thoughtworks.wallet.BizTypeEnum;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Documented
public @interface RetryListener {

    BizTypeEnum bizType();

    Class<? extends Throwable>[] retryFor() default Exception.class;

    Class<? extends Throwable>[] noRetryFor() default {};

    int maxRetryTime() default 3;

    long expiredTime() default 60 * 60;
}
