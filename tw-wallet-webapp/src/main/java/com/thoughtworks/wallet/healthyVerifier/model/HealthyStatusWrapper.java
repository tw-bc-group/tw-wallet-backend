package com.thoughtworks.wallet.healthyVerifier.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HealthyStatusWrapper {
    String typ;
    String val;

    public static HealthyStatusWrapper of(String val) {
        return new HealthyStatusWrapper("HealthyStatus", val);
    }
}
