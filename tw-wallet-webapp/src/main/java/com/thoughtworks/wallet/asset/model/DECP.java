package com.thoughtworks.wallet.asset.model;

import lombok.Data;

import java.math.BigInteger;

@Data
public class DECP {
    private String name;
    private String symbol;
    private BigInteger decimal;

    private DECP(String name, String symbol, BigInteger decimal) {
        this.name = name;
        this.symbol = symbol;
        this.decimal = decimal;
    }

    public static DECP create(String name, String symbol, BigInteger decimal) {
        return new DECP(name, symbol, decimal);
    }
}
