package com.thoughtworks.wallet.asset.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class TWPRewardRequest {
    @NotBlank(message = "Address can not be blank")
    String address;

    @JsonCreator
    public TWPRewardRequest(@JsonProperty("fromAddress") String address) {
        this.address = address;
    }
}
