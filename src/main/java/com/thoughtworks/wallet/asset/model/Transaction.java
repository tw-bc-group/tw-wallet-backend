package com.thoughtworks.wallet.asset.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thoughtworks.wallet.util.CoinSerializer;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Transaction {

    @Column(name = "tx_id")
    private String txId;

    @Column(name = "coin_type")
    private String coinType;

    @Column(name = "address")
    private String address;


    @Column(name = "tx_amount")
    @JsonSerialize(using = CoinSerializer.class)
    private BigDecimal amount;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "confirm_time")
    private Date confirmTime;

    @Column(name = "current_balance")
    @JsonSerialize(using = CoinSerializer.class)
    private BigDecimal currentBalance;

    @Column(name = "tx_hash")
    private String txHash;

    @JsonIgnore
    @Column(name = "cust_id")
    private Integer custId;

    @Transient
    private String status;

    @Transient
    private String txType;

    @Override
    public String toString() {
        return "Transaction{" +
                "txId='" + txId + '\'' +
                ", coinType='" + coinType + '\'' +
                ", address='" + address + '\'' +
                ", amount=" + amount +
                ", createTime=" + createTime +
                ", confirmTime=" + confirmTime +
                ", currentBalance=" + currentBalance +
                ", txHash='" + txHash + '\'' +
                ", status='" + status + '\'' +
                ", txType='" + txType + '\'' +
                '}';
    }
}
