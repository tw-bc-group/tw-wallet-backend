package com.thoughtworks.wallet.healthyVerifier.model;

import com.thoughtworks.wallet.healthyVerifier.annotation.HealthVerificationClaimContractAddress;
import com.thoughtworks.wallet.healthyVerifier.annotation.HealthVerificationClaimIssuerAddress;
import com.thoughtworks.wallet.healthyVerifier.annotation.HealthVerificationClaimIssuerPrivateKey;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class HealthVerificationClaimContract {
    @HealthVerificationClaimIssuerAddress
    private String issuerAddress;

    @HealthVerificationClaimIssuerPrivateKey
    private String issuerPrivateKey;

    @HealthVerificationClaimContractAddress
    private String address;
}
