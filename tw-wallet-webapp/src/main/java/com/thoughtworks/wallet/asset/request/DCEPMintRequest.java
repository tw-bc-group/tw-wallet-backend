package com.thoughtworks.wallet.asset.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigInteger;
import java.util.List;

@Data
public class DCEPMintRequest {
    @NotBlank(message = "Address can not be blank")
    String address;

    @NotEmpty(message = "money can not s blank")
    List<BigInteger> moneys;
}
