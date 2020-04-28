package com.thoughtworks.wallet.asset.request;

import com.thoughtworks.wallet.healthyVerifier.HealthVerificationRequest;
import com.thoughtworks.wallet.healthyVerifier.exception.InvalidHealthyStatusException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class HealthVerificationRequestTest {
    @ParameterizedTest
    @ValueSource(strings = {"healthy", "unhealthy"})
    void should_throw_exception_when_request_with_valid_healthy_status(String healthyStatus) {
        Assertions.assertDoesNotThrow(() -> {
            new HealthVerificationRequest("13098989898", healthyStatus);
        });
    }

    @Test
    void should_throw_exception_when_request_with_wrong_healthy_status() {
        final String invalidHealthyStatus = "invalid";

        Assertions.assertThrows(InvalidHealthyStatusException.class, () -> {
            new HealthVerificationRequest("13098989898", invalidHealthyStatus);
        });
    }
}