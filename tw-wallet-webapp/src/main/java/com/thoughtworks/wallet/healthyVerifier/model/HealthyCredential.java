package com.thoughtworks.wallet.healthyVerifier.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
@Setter
public class HealthyCredential {
    String id;
    String phone;
    HealthyStatusWrapper healthyStatus;
}
