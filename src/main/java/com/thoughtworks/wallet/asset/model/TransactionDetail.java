package com.thoughtworks.wallet.asset.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thoughtworks.wallet.util.CoinSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper=true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TransactionDetail extends Transaction {

    @JsonSerialize(using = CoinSerializer.class)
    private BigDecimal currentBalance;

    private String fromAddress;

    private String fromAddressName;

    private String toAddress;

    private String toAddressName;

    private String txHash;

    @JsonSerialize(using = CoinSerializer.class)
    private BigDecimal fee;

    private String note = "";

    private String rejectReason = "";

    public TransactionDetail() {
        super();
    }
}
