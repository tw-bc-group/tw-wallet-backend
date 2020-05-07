package com.thoughtworks.wallet.healthyVerifier.annotation;


import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Value("${quorum.health-verification-claim.issuer-address}")
@Retention(RetentionPolicy.RUNTIME)
public @interface HealthVerificationClaimIssuerAddress {
}
