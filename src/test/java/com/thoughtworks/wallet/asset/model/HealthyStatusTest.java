package com.thoughtworks.wallet.asset.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HealthyStatusTest {
    @Test
    void should_return_true_when_healthy_status_is_healthy() {
        final boolean isValid = HealthyStatus.isValid("healthy");
        Assertions.assertTrue(isValid);
    }

    @Test
    void should_return_true_when_healthy_status_is_unhealthy() {
        final boolean isValid = HealthyStatus.isValid("unhealthy");
        Assertions.assertTrue(isValid);
    }

    @Test
    void should_return_true_when_healthy_status_is_invalid() {
        final boolean isValid = HealthyStatus.isValid("invalid");
        Assertions.assertFalse(isValid);
    }

    @Test
    void should_return_true_when_healthy_status_is_empty() {
        final boolean isValid = HealthyStatus.isValid("");
        Assertions.assertFalse(isValid);
    }
}