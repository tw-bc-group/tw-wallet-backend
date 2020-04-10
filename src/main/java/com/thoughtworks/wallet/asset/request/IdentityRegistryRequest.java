package com.thoughtworks.wallet.asset.request;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class IdentityRegistryRequest {
    @NotBlank(message = "DID can not be blank")
    String did;

    @NotBlank(message = "Address can not be blank")
    String address;

    @NotBlank(message = "Public key can not be blank")
    String publicKey;
}
