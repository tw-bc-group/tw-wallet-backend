package com.thoughtworks.wallet.asset.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thoughtworks.wallet.asset.model.TWPoint;
import com.thoughtworks.wallet.util.CoinSerializer;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TWPointBalanceResponse {
    private String address;

    private TWPoint TWPoint;

    @JsonSerialize(using = CoinSerializer.class)
    private BigDecimal balance;

    private TWPointBalanceResponse(String address, TWPoint twPoint, BigDecimal balance) {
        this.address = address;
        this.TWPoint = twPoint;
        this.balance = balance;
    }

    public static TWPointBalanceResponse of(String address, TWPoint twPoint, BigDecimal balance) {
        return new TWPointBalanceResponse(address, twPoint, balance);
    }
}
