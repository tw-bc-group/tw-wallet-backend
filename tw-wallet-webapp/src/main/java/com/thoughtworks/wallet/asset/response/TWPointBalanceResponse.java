package com.thoughtworks.wallet.asset.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thoughtworks.common.util.CoinSerializer;
import com.thoughtworks.wallet.asset.model.TWPoint;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TWPointBalanceResponse {
    private String address;

    private TWPoint TWPoint;

    @JsonSerialize(using = CoinSerializer.class)
    private BigDecimal balance;
}
