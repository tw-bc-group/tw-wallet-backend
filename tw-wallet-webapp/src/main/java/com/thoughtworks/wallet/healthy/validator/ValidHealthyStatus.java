package com.thoughtworks.wallet.healthy.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ValidHealthyStatusValidator.class})
public @interface ValidHealthyStatus {
    boolean required() default true;

    String message() default "不合法的健康状态，只能为 ‘healthy’ 和 ‘unhealthy’。";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
