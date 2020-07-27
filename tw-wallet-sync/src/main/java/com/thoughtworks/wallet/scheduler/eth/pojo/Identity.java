package com.thoughtworks.wallet.scheduler.eth.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor(staticName = "of")
public class Identity {
    private final String initiator;
    private final String ownerAddress;
    private final String did;
    private final String publicKey;
    private final String name;
    private final String hash;
    private final BigInteger height;
    private final BigInteger txIndex;
    private final String txType;

}
