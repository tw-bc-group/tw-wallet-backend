package com.thoughtworks.wallet.healthyVerifier.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data
public class SuspectedPatientInfo {
    private String phone;
}
