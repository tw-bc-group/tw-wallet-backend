package com.thoughtworks.wallet.scheduler.eth.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor(staticName = "of")
public class TWPoint {
    private final String fromAddr;
    private final String toAddr;
    private final String txHash;
    private final BigInteger height;
    private final BigInteger index;
    private final BigInteger value;
    private final String txType;
}
