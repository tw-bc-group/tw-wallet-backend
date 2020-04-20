package com.thoughtworks.wallet.asset.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class TWPTransferRequest {
    @NotBlank(message = "Signed Transaction should not be blank.")
    String signedTransactionRawData;

    @NotBlank(message = "Address can not be blank")
    String fromAddress;

    @NotBlank(message = "Public key can not be blank")
    String fromPublicKey;

    @JsonCreator
    public TWPTransferRequest(@JsonProperty("signedTransactionRawData") String signedTransactionRawData,
                              @JsonProperty("fromAddress") String fromAddress,
                              @JsonProperty("fromPublicKey") String fromPublicKey
    ) {
        this.signedTransactionRawData = signedTransactionRawData;
        this.fromAddress = fromAddress;
        this.fromPublicKey = fromPublicKey;
    }
}
