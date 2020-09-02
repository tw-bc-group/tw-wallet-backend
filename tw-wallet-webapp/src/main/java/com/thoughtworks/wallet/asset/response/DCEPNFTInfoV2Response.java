package com.thoughtworks.wallet.asset.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.thoughtworks.common.util.dcep.MoneyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Uint256;

@Data
@AllArgsConstructor(staticName = "of")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DCEPNFTInfoV2Response {
    private String serialNumber;
    private MoneyType moneyType;
    private String bankSign;
}
