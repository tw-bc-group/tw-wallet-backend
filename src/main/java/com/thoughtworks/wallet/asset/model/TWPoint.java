package com.thoughtworks.wallet.asset.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thoughtworks.wallet.util.CoinSerializer;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TWPoint {
    private String address;

    @JsonSerialize(using = CoinSerializer.class)
    private BigDecimal balance;
}
