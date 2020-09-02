package com.thoughtworks.wallet.asset.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thoughtworks.common.util.CoinSerializer;
import com.thoughtworks.wallet.asset.model.DECP;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DECPBalanceResponse {
    private String address;

    // TODO: DCEP
    private DECP DECP;

    @JsonSerialize(using = CoinSerializer.class)
    private BigDecimal balance;
}
