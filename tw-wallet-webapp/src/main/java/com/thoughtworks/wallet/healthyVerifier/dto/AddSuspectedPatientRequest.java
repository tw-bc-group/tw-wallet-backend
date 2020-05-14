package com.thoughtworks.wallet.healthyVerifier.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AddSuspectedPatientRequest {
    @Length(min = 11, max = 11, message = "Phone number should only have 11 digits.")
    private String phone;
}
