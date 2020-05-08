package com.thoughtworks.wallet.healthyVerifier.model;

import com.google.common.collect.ImmutableList;
import com.thoughtworks.common.util.JacksonUtil;
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
    public HealthVerificationClaim(String context, String id, String ver, String iss, long iat, long exp, String typ, String sub, String owner) {
        this.context = ImmutableList.of(context);
        this.id = id;
        this.ver = ver;
        this.iss = iss;
        this.iat = iat;
        this.exp = exp;
        this.typ = ImmutableList.of(typ);
        this.sub = JacksonUtil.jsonStrToBean(sub, HealthyCredential.class);
    }
}
