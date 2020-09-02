package com.thoughtworks.common.util.dcep;

import lombok.Getter;
import lombok.Setter;

public enum MoneyType {
    RMB100("100_00"),
    RMB050("050_00"),
    RMB010("010_00"),
    RMB005("005_00"),
    RMB001("001_00"),
    RMB000_50("000_50"),
    RMB000_10("000_10");

    @Getter
    @Setter
    private String moneyTypeString;

    MoneyType(String moneyTypeString) {
        this.moneyTypeString = moneyTypeString;
    }
}
