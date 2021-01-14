package com.thoughtworks.wallet.healthy.dto.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class VerifyJwtTokensRequest {
    @NotBlank
    @Getter
    @Size(min = 5, max = 100, message = "The length of id is between 5 and 100")
    String verifierId;
    @NotEmpty
    @Getter
    List<String> tokens;
}
