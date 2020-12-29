package com.thoughtworks.wallet.healthy.repository;

import com.thoughtworks.common.util.JacksonUtil;
import com.thoughtworks.wallet.gen.tables.records.TblHealthyVerificationClaimV2Record;
import com.thoughtworks.wallet.healthy.exception.HealthVerificationNotFoundException;
import com.thoughtworks.wallet.healthy.dto.v2.VerifiableCredentialJwt;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.thoughtworks.wallet.gen.Tables.TBL_HEALTHY_VERIFICATION_CLAIM_V2;

@Slf4j
@Service
public class HealthVerificationDAOV2 {
    private final DSLContext dslContext;

    public HealthVerificationDAOV2(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public int insertHealthVerificationClaim(VerifiableCredentialJwt claim) {
        return dslContext
                .insertInto(TBL_HEALTHY_VERIFICATION_CLAIM_V2)
                .set(TBL_HEALTHY_VERIFICATION_CLAIM_V2.CLAIM_ID, claim.getVc().getId())
                .set(TBL_HEALTHY_VERIFICATION_CLAIM_V2.OWNER_ID, claim.getVc().getSub().getId())
                .set(TBL_HEALTHY_VERIFICATION_CLAIM_V2.ISSUER_ID, claim.getIss())
                .set(TBL_HEALTHY_VERIFICATION_CLAIM_V2.JWT, claim.getToken())
                .set(TBL_HEALTHY_VERIFICATION_CLAIM_V2.PAYLOAD, JacksonUtil.toJsonNode(claim))
                .execute();
    }

    public List<String> getHealthVerificationClaim(String ownerId) {
        Result<TblHealthyVerificationClaimV2Record> claims =  Optional.ofNullable(dslContext
                .selectFrom(TBL_HEALTHY_VERIFICATION_CLAIM_V2)
                .where(TBL_HEALTHY_VERIFICATION_CLAIM_V2.OWNER_ID.equal(ownerId))
                .fetch()).orElseThrow(() -> new HealthVerificationNotFoundException(ownerId));

        return claims.stream().map(hvcs->hvcs.getJwt()).collect(Collectors.toList());
    }
}
