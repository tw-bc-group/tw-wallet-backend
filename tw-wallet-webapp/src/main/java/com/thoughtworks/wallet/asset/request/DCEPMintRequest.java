package com.thoughtworks.wallet.asset.request;

import com.thoughtworks.common.util.dcep.MoneyType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class DCEPMintRequest {
    @NotBlank(message = "Address can not be blank")
    String address;

    MoneyType moneyType;
}
