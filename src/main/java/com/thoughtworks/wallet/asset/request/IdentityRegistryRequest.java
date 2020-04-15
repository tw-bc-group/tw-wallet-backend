package com.thoughtworks.wallet.asset.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class IdentityRegistryRequest {
    @NotBlank(message = "Signed Transaction should not be blank.")
    String signedTransactionData;

    @JsonCreator
    public IdentityRegistryRequest(@JsonProperty("signedTransactionData") String signedTransactionData) {
        this.signedTransactionData = signedTransactionData;
    }
}
