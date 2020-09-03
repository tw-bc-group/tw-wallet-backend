package com.thoughtworks.wallet.asset.service;

import com.thoughtworks.wallet.asset.request.DCEPMintRequest;
import com.thoughtworks.wallet.asset.response.*;

public interface IDCEPService {


    /**
     * mint DC/EP to address
     */
    DCEPNFTInfoV2Response mint(DCEPMintRequest DCEPMintRequest);

    /**
     * 获取合约地址和abi
     * @return
     */
    DCEPInfoV2Response getDCEPInfo();

    /**
     * 根据冠字号获取 DC/EP 信息
     * @return
     */
    DCEPNFTInfoV2Response getDCEPBySerialNumber(String serialNumber);
}
