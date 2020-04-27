package com.thoughtworks.wallet.asset.service.impl;

import com.thoughtworks.wallet.asset.exception.HealthVerificationNotFoundException;
import com.thoughtworks.wallet.asset.request.HealthVerificationRequest;
import com.thoughtworks.wallet.asset.response.HealthVerificationResponse;
import com.thoughtworks.wallet.asset.service.IHealthyVerifierService;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.thoughtworks.wallet.gen.Tables.TBL_HEALTHY_VERIFICATION;

@Slf4j
@Service
public class HealthyVerifierService implements IHealthyVerifierService {
    private final DSLContext dslContext;

    public HealthyVerifierService(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public HealthVerificationResponse getHealthVerificationByPhone(String phone) {
        final HealthVerificationResponse healthVerification = dslContext
            .selectFrom(TBL_HEALTHY_VERIFICATION)
            .where(TBL_HEALTHY_VERIFICATION.PHONE.eq(phone))
            .fetchOneInto(HealthVerificationResponse.class);

        if (healthVerificationNotFound(healthVerification)) {
            throw new HealthVerificationNotFoundException(phone);
        }

        return healthVerification;
    }

    @Override
    public void createHealthVerification(HealthVerificationRequest healthVerification) {
        dslContext
            .insertInto(TBL_HEALTHY_VERIFICATION)
            .set(TBL_HEALTHY_VERIFICATION.PHONE, healthVerification.getPhone())
            .set(TBL_HEALTHY_VERIFICATION.STATUS, healthVerification.getStatus())
            .execute();
    }

    private boolean healthVerificationNotFound(HealthVerificationResponse healthVerification) {
        return Objects.isNull(healthVerification);
    }
}
