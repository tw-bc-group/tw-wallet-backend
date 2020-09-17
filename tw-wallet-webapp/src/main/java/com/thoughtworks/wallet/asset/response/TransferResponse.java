package com.thoughtworks.wallet.asset.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
@Data
@AllArgsConstructor(staticName = "of")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TransferResponse {
    String hash;
}
