package com.thoughtworks.wallet.asset.service;

import com.thoughtworks.wallet.asset.request.DCEPMintRequest;
import com.thoughtworks.wallet.asset.response.*;

public interface IDCEPService {


    /**
     * get DC/EP Infos
     *
     * @return
     */
    DCEPNFTInfoV2Response getDCEPInfo(String id);


    /**
     * mint DC/EP to address
     */
    DCEPNFTInfoV2Response mint(DCEPMintRequest DCEPMintRequest);

    /**
     * 获取合约地址和abi
     * @return
     */
    DCEPInfoV2Response getDCEPInfo();

}
