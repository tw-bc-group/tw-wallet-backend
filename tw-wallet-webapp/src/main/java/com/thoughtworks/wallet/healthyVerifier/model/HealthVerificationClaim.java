package com.thoughtworks.wallet.healthyVerifier.model;

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
}
