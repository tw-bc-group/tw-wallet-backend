package com.thoughtworks.wallet.asset.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Value
public class TWPRewardRequest {
    @NotBlank(message = "Address can not be blank")
    String address;

    @Positive(message = "Address can not be blank")
    int amount;

    @JsonCreator
    public TWPRewardRequest(@JsonProperty("address") String address,
                            @JsonProperty("amount") int amount) {
        this.address = address;
        this.amount = amount;
    }
}
