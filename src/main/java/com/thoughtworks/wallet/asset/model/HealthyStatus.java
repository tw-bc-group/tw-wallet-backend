package com.thoughtworks.wallet.asset.model;

public enum HealthyStatus {
    HEALTHY("healthy"),
    UN_HEALTHY("unhealthy");

    private String status;

    HealthyStatus(String status) {
        this.status = status;
    }

    public static boolean isValid(String status) {
        return HEALTHY.status.equals(status) || UN_HEALTHY.status.equals(status);
    }
}
