package com.thoughtworks.common;

public enum RspCode {

    /**
     * success
     */
    SUCCESS(0, "SUCCESS", "成功");


    private int rspCode;
    private String descEN;
    private String descCN;

    RspCode(int rspCode, String descEN, String descCN) {
        this.rspCode = rspCode;
        this.descEN = descEN;
        this.descCN = descCN;
    }

    public int code() {
        return rspCode;
    }

    public String descEN() {
        return descEN;
    }

    public String descCN() {
        return descCN;
    }

}
