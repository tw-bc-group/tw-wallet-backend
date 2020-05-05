package com.thoughtworks.wallet.asset.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class IdentityRegistryRequest {
    @NotBlank(message = "Signed Transaction should not be blank.")
    String signedTransactionRawData;

    @NotBlank(message = "Address can not be blank")
    String address;

    @NotBlank(message = "DID can not be blank")
    String did;

    @NotBlank(message = "Public key can not be blank")
    String publicKey;

    @NotBlank(message = "Name can not be blank")
    String name;

    @JsonCreator
    public IdentityRegistryRequest(@JsonProperty("signedTransactionRawData") String signedTransactionRawData,
                                   @JsonProperty("address") String address,
                                   @JsonProperty("did") String did,
                                   @JsonProperty("publicKey") String publicKey,
                                   @JsonProperty("name") String name) {
        this.signedTransactionRawData = signedTransactionRawData;
        this.address = address;
        this.did = did;
        this.publicKey = publicKey;
        this.name = name;
    }
}
