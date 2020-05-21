package com.thoughtworks.wallet.healthy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.thoughtworks.common.util.JacksonUtil;
import com.thoughtworks.wallet.gen.tables.records.TblHealthyVerificationClaimRecord;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HealthVerificationClaim {
    @JsonProperty(value = "@context")
    private List<String>      context;
    private String            id;
    private String            ver;
    private String            iss;
    private long              iat;
    private long              exp;
    private List<String>      typ;
    private HealthyCredential sub;
    @JsonIgnore
    private String            token;

    /**
     * deserialize the JSON string from database of table tbl_healthy_verification_claim
     * @param tblHealthyVerificationClaimRecord
     */
    public HealthVerificationClaim(TblHealthyVerificationClaimRecord tblHealthyVerificationClaimRecord) {
        this.context = ImmutableList.of(tblHealthyVerificationClaimRecord.getContext());
        this.id = tblHealthyVerificationClaimRecord.getId();
        this.ver = tblHealthyVerificationClaimRecord.getVer();
        this.iss = tblHealthyVerificationClaimRecord.getIss();
        this.iat = tblHealthyVerificationClaimRecord.getIat();
        this.exp = tblHealthyVerificationClaimRecord.getExp();
        this.typ = ImmutableList.of(tblHealthyVerificationClaimRecord.getTyp());
        this.sub = JacksonUtil.fromJsonNode(tblHealthyVerificationClaimRecord.getSub(), HealthyCredential.class);
        this.token = tblHealthyVerificationClaimRecord.getToken();
    }
}
