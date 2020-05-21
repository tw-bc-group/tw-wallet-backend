package com.thoughtworks.wallet.healthy.model;

import com.thoughtworks.wallet.healthy.annotation.HealthVerificationClaimContractAddress;
import com.thoughtworks.wallet.healthy.annotation.HealthVerificationClaimIssuerAddress;
import com.thoughtworks.wallet.healthy.annotation.HealthVerificationClaimIssuerPrivateKey;
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
