package com.thoughtworks.wallet.asset.model;

import lombok.Data;

import java.math.BigInteger;

@Data
public class TWPoint {
    private String name;
    private String symbol;
    private BigInteger decimal;

    private TWPoint(String name, String symbol, BigInteger decimal) {
        this.name = name;
        this.symbol = symbol;
        this.decimal = decimal;
    }

    public static TWPoint create(String name, String symbol, BigInteger decimal) {
        return new TWPoint(name, symbol, decimal);
    }
}
