package com.thoughtworks.wallet.healthyVerifier.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@Getter
public class HealthyCredential {
    String id;
    String phone;
    HealthyStatusWrapper healthyStatus;
}
