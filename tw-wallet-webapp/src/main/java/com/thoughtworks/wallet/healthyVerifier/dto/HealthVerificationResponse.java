package com.thoughtworks.wallet.healthyVerifier.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.thoughtworks.wallet.healthyVerifier.model.HealthyCredential;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HealthVerificationResponse {
    @JsonProperty(value = "@context")
    List<String> context;
    String            id;
    String            ver;
    String            iss;
    long              iat;
    long              exp;
    List<String>      typ;
    HealthyCredential sub;
}
