package com.thoughtworks.wallet.healthy.model;

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
    float temperature;
    Result contact;
    Result symptoms;
    HealthyStatusWrapper healthyStatus;
}
