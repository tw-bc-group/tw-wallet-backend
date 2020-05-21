package com.thoughtworks.wallet.healthy.validator;

import com.thoughtworks.wallet.healthy.model.HealthyStatus;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidHealthyStatusValidator implements ConstraintValidator<ValidHealthyStatus, String> {
    public boolean required = false;

    public void initialize(ValidHealthyStatus constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    public boolean isValid(String healthyStatus, ConstraintValidatorContext context) {
        if (!required) {
            if (StringUtils.isEmpty(healthyStatus)) {
                return false;
            }
        }
        return isValidHealthyStatus(healthyStatus);
    }

    private boolean isValidHealthyStatus(String status) {
        try {
            HealthyStatus.valueOf(status.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
