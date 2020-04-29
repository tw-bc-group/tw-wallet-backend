package com.thoughtworks.wallet.healthyVerifier.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.thoughtworks.wallet.healthyVerifier.HealthVerificationRequest;
import com.thoughtworks.wallet.healthyVerifier.HealthVerificationResponse;
import com.thoughtworks.wallet.healthyVerifier.exception.HealthVerificationAlreadyExistException;
import com.thoughtworks.wallet.healthyVerifier.exception.InsertIntoDatabaseErrorException;
import com.thoughtworks.wallet.healthyVerifier.model.HealthVerificationClaim;
import com.thoughtworks.wallet.healthyVerifier.model.HealthyCredential;
import com.thoughtworks.wallet.healthyVerifier.model.HealthyStatus;
import com.thoughtworks.wallet.healthyVerifier.model.HealthyStatusWrapper;
import com.thoughtworks.wallet.healthyVerifier.utils.ClaimIdUtil;
import com.thoughtworks.wallet.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Map;

import static com.thoughtworks.wallet.gen.Tables.TBL_HEALTHY_VERIFICATION_CLAIM;

@Slf4j
@Service
public class HealthyVerifierService implements IHealthyVerifierService {
    private final DSLContext dslContext;
    private final ClaimIdUtil claimIdUtil;

    private final String VER = "0.1";
    private final String issuerDid = "did:tw:849E687Bda2B7E89868646f39BB01AE070ADf3f7";
    final ImmutableList<String> context = ImmutableList.of("https://blockchain.thoughtworks.cn/credentials/v1/");
    final ImmutableList<String> credentialType = ImmutableList.of("HealthyCredential");
    // 假设 claim 30 天后过期
    final int expireHours = 30 * 24;

    public HealthyVerifierService(DSLContext dslContext, ClaimIdUtil claimIdUtil) {
        this.dslContext = dslContext;
        this.claimIdUtil = claimIdUtil;
    }

    @Override
    public HealthVerificationResponse createHealthVerification(HealthVerificationRequest healthVerification) {
        HealthVerificationClaim claim = generateHealthyVerificationClaim(healthVerification.getDid(), healthVerification.getPhone());

        final int insertedNumber;
        try {
            insertedNumber = dslContext
                .insertInto(TBL_HEALTHY_VERIFICATION_CLAIM)
                .set(TBL_HEALTHY_VERIFICATION_CLAIM.CONTEXT, claim.getContext().get(0))
                .set(TBL_HEALTHY_VERIFICATION_CLAIM.VER, claim.getVer())
                .set(TBL_HEALTHY_VERIFICATION_CLAIM.ID, claim.getId())
                .set(TBL_HEALTHY_VERIFICATION_CLAIM.ISS, claim.getIss())
                .set(TBL_HEALTHY_VERIFICATION_CLAIM.IAT, claim.getIat())
                .set(TBL_HEALTHY_VERIFICATION_CLAIM.EXP, claim.getExp())
                .set(TBL_HEALTHY_VERIFICATION_CLAIM.TYP, claim.getTyp().get(0))
                .set(TBL_HEALTHY_VERIFICATION_CLAIM.SUB, JacksonUtil.beanToJSonStr(claim.getSub()))
                .execute();

        } catch (DataIntegrityViolationException e) {
            log.error("Healthy verification of owner:{} is already existed.", healthVerification.getDid());
            throw new HealthVerificationAlreadyExistException(healthVerification.getDid());
        }

        if (insertedNumber != 1) {
            log.error("Insert into database error: can not insert healthy verification of owner: {}.", healthVerification.getDid());
            throw new InsertIntoDatabaseErrorException(healthVerification.getDid());
        }

        return HealthVerificationResponse.of(
            claim.getContext(),
            claim.getId(),
            claim.getVer(),
            claim.getIss(),
            claim.getIat(),
            claim.getExp(),
            claim.getTyp(),
            claim.getSub()
        );
    }

    private HealthVerificationClaim generateHealthyVerificationClaim(String did, String phone) {
        final String claimId = claimIdUtil.generateClaimId(did, VER);

        final Calendar calendar = Calendar.getInstance();
        final long currentTime = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + expireHours);
        final long expireTime = calendar.getTime().getTime();

        final HealthyStatusWrapper healthyStatus = generateHealthyStatus();
        return HealthVerificationClaim.of(
            context,
            claimId,
            VER,
            issuerDid,
            currentTime,
            expireTime,
            credentialType,
            HealthyCredential.of(did, phone, healthyStatus));
    }

    private HealthyStatusWrapper generateHealthyStatus() {
        // TODO: mock healthy status: random between healthy and unhealthy
        Map<Integer, String> healthyStatus = ImmutableMap.of(0, HealthyStatus.HEALTHY.getStatus(), 1, HealthyStatus.UNHEALTHY.getStatus());
        int no = (int) (Math.random() * 2);
        return HealthyStatusWrapper.of(healthyStatus.get(no));
    }
}
