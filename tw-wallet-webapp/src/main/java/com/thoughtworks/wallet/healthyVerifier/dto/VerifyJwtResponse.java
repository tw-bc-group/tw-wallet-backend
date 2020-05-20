package com.thoughtworks.wallet.healthyVerifier.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VerifyJwtResponse {
    VerifyResultEnum outdate;
    VerifyResultEnum revoked;
    VerifyResultEnum onchain;
    VerifyResultEnum verifyIssuer;
    VerifyResultEnum verifySignature;
    VerifyResultEnum verifyHolder;
}
