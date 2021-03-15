package com.thoughtworks.wallet.retry;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
@Aspect
@Order(1)
public class RetryAspect {

    private static final String EXECUTION = "@annotation(retryListener)";

    @Around(EXECUTION)
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint, RetryListener retryListener) throws Throwable {
        Object object = null;
        try {
            object = joinPoint.proceed();
        } catch (Exception exception) {
            exceptionHandler(exception, joinPoint, retryListener);
            throw exception;
        }
        return object;
    }

    private void exceptionHandler(Throwable throwable, ProceedingJoinPoint joinPoint, RetryListener retryListener) {
        if (!RetryHelper.isRetry()) {
            Optional<Class<? extends Throwable>> rollbackClazzOptional = Arrays.stream(retryListener.retryFor()).filter(clazz -> clazz.isInstance(throwable)).findFirst();
            Optional<Class<? extends Throwable>> noRollbackClazzOptional = Arrays.stream(retryListener.noRetryFor()).filter(clazz -> clazz.isInstance(throwable)).findFirst();
            if (!noRollbackClazzOptional.isPresent() && rollbackClazzOptional.isPresent()) {

                RetryRequestContext retryRequestContext = new RetryRequestContext();
                retryRequestContext.setTargetClass(joinPoint.getTarget().getClass());
                retryRequestContext.setArgs(joinPoint.getArgs());
                retryRequestContext.setMethodName(joinPoint.getSignature().getName());

                //TODO: 可以存贮在数据库，后面重试

            }
        }
    }
}
