package com.thoughtworks.wallet.healthy.repository;

import com.thoughtworks.common.util.JacksonUtil;
import com.thoughtworks.wallet.healthy.model.V2.HealthVerificationClaimV2;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static com.thoughtworks.wallet.gen.Tables.TBL_HEALTHY_VERIFICATION_CLAIM_V2;

@Slf4j
@Service
public class HealthVerificationDAOV2 {
    private final DSLContext dslContext;

    public HealthVerificationDAOV2(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public int insertHealthVerificationClaim(HealthVerificationClaimV2 claim) {
        return dslContext
                .insertInto(TBL_HEALTHY_VERIFICATION_CLAIM_V2)
                .set(TBL_HEALTHY_VERIFICATION_CLAIM_V2.ID, claim.getVc().getId())
                .set(TBL_HEALTHY_VERIFICATION_CLAIM_V2.PAYLOAD, JacksonUtil.toJsonNode(claim))
                .execute();
    }
}
