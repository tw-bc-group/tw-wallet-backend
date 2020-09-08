package com.thoughtworks.wallet.asset.service;

import com.thoughtworks.wallet.asset.request.DCEPMintRequest;
import com.thoughtworks.wallet.asset.response.*;

import java.util.List;

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

    /**
     * 根据 address 获取 DC/EP 信息
     * @param address
     * @param limit
     * @param offset
     * @return
     */
    List<DCEPNFTInfoV2Response> getDCEPByAddress(String address, int limit, int offset);

    void sendRawTransaction(String signedTransactionRawData, String fromAddress);
}
