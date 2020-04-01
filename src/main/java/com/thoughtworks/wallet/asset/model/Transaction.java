package com.thoughtworks.wallet.asset.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thoughtworks.wallet.util.CoinSerializer;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Transaction {

    private String hash;

    private String txType;

    @JsonSerialize(using = CoinSerializer.class)
    private BigDecimal amount;

    private LocalDateTime createTime;

    private LocalDateTime confirmTime;

    private String fromAddress;

    private String fromAddressName;

    private String toAddress;

    private String toAddressName;
}
