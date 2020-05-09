package com.thoughtworks.wallet.healthyVerifier.model;

import com.google.common.collect.ImmutableList;
import com.thoughtworks.common.util.JacksonUtil;
import com.thoughtworks.wallet.gen.tables.records.TblHealthyVerificationClaimRecord;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@RequiredArgsConstructor(staticName = "of")
public class HealthVerificationClaim {
    List<String> context;
    String id;
    String ver;
    String iss;
    long iat;
    long exp;
    List<String> typ;
    HealthyCredential sub;

    // deserialize the JSON string from database of table tbl_healthy_verification_claim
    public HealthVerificationClaim(TblHealthyVerificationClaimRecord tblHealthyVerificationClaimRecord) {
        this.context = ImmutableList.of(tblHealthyVerificationClaimRecord.getContext());
        this.id = tblHealthyVerificationClaimRecord.getId();
        this.ver = tblHealthyVerificationClaimRecord.getVer();
        this.iss = tblHealthyVerificationClaimRecord.getIss();
        this.iat = tblHealthyVerificationClaimRecord.getIat();
        this.exp = tblHealthyVerificationClaimRecord.getExp();
        this.typ = ImmutableList.of(tblHealthyVerificationClaimRecord.getTyp());
        this.sub = JacksonUtil.fromJsonNode(tblHealthyVerificationClaimRecord.getSub(), HealthyCredential.class);
    }
}
