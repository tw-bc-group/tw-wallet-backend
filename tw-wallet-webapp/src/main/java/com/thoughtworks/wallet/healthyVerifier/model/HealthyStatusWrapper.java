package com.thoughtworks.wallet.healthyVerifier.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HealthyStatusWrapper {
    String typ;
    String val;

    public static HealthyStatusWrapper of(String val) {
        return new HealthyStatusWrapper("HealthyStatus", val);
    }
}
