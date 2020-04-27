package com.thoughtworks.wallet.asset.request;

import com.thoughtworks.wallet.asset.exception.InvalidHealthyStatusException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HealthVerificationRequestTest {
    @Test
    void should_throw_exception_when_request_with_wrong_healthy_status() {
        final String invalidHealthyStatus = "invalid";

        Assertions.assertThrows(InvalidHealthyStatusException.class, () -> {
            new HealthVerificationRequest("13098989898", invalidHealthyStatus);
        });
    }

    @Test
    void should_throw_exception_when_request_with_healthy_status() {
        final String healthyStatus = "healthy";

        Assertions.assertDoesNotThrow(() -> {
            new HealthVerificationRequest("13098989898", healthyStatus);
        });
    }
}