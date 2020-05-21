package com.thoughtworks.wallet.healthy.repository;

import com.thoughtworks.common.util.JacksonUtil;
import com.thoughtworks.wallet.healthy.model.HealthVerificationClaim;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static com.thoughtworks.wallet.gen.Tables.TBL_HEALTHY_VERIFICATION_CLAIM;

@Slf4j
@Service
public class HealthVerificationDAO {
    private final DSLContext dslContext;

    public HealthVerificationDAO(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public int insertHealthVerificationClaim(HealthVerificationClaim claim) {
        return dslContext
                .insertInto(TBL_HEALTHY_VERIFICATION_CLAIM)
                .set(TBL_HEALTHY_VERIFICATION_CLAIM.CONTEXT, claim.getContext().get(0))
                .set(TBL_HEALTHY_VERIFICATION_CLAIM.VER, claim.getVer())
                .set(TBL_HEALTHY_VERIFICATION_CLAIM.ID, claim.getId())
                .set(TBL_HEALTHY_VERIFICATION_CLAIM.ISS, claim.getIss())
                .set(TBL_HEALTHY_VERIFICATION_CLAIM.IAT, claim.getIat())
                .set(TBL_HEALTHY_VERIFICATION_CLAIM.EXP, claim.getExp())
                .set(TBL_HEALTHY_VERIFICATION_CLAIM.TYP, claim.getTyp().get(0))
                .set(TBL_HEALTHY_VERIFICATION_CLAIM.SUB, JacksonUtil.toJsonNode(claim.getSub()))
                .set(TBL_HEALTHY_VERIFICATION_CLAIM.TOKEN, claim.getToken())
                .execute();
    }

    public int updateHealthVerificationClaim(HealthVerificationClaim claim) {
        return dslContext
                .update(TBL_HEALTHY_VERIFICATION_CLAIM)
                .set(TBL_HEALTHY_VERIFICATION_CLAIM.EXP, claim.getExp())
                .set(TBL_HEALTHY_VERIFICATION_CLAIM.SUB, JacksonUtil.toJsonNode(claim.getSub()))
                .where(TBL_HEALTHY_VERIFICATION_CLAIM.ID.eq(claim.getId()))
                .execute();
    }
}
