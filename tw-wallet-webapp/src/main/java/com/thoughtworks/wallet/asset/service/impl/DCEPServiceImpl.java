package com.thoughtworks.wallet.asset.service.impl;

import com.thoughtworks.common.annotation.Node1PrivateKey;
import com.thoughtworks.common.annotation.QuorumRPCUrl;
import com.thoughtworks.common.exception.*;
import com.thoughtworks.common.util.Identity;
import com.thoughtworks.common.util.JacksonUtil;
import com.thoughtworks.common.util.dcep.DCEPUtil;
import com.thoughtworks.common.util.dcep.StringBytesConvert;
import com.thoughtworks.wallet.asset.repository.DECPRepository;
import com.thoughtworks.wallet.asset.request.DCEPMintRequest;
import com.thoughtworks.wallet.asset.response.*;
import com.thoughtworks.wallet.asset.service.IDCEPService;
import com.thoughtworks.wallet.wrapper.DCEPContract;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;

import java.io.IOException;
import java.math.BigInteger;

@Slf4j
@Service
public class DCEPServiceImpl implements IDCEPService {

    private static final String RMB_CONTRACT_PATH = "/contracts/RMB.json";
    private final Web3j web3j;
    private final DCEPContract decp;
    private final JacksonUtil jacksonUtil;

    @Node1PrivateKey
    private String privateKey;

    // TODO：做成可以自动切换节点，有重试机制的请求模块。这里只是为了打印错误好调试
    @QuorumRPCUrl
    private String rpcUrl;

    private DECPRepository decpRepository;

    @Autowired
    public DCEPServiceImpl(Web3j web3j, DCEPContract decp, JacksonUtil jacksonUtil, DECPRepository decpRepository) {
        this.web3j = web3j;
        this.decp = decp;
        this.jacksonUtil = jacksonUtil;
        this.decpRepository = decpRepository;
    }


    @Override
    public DCEPNFTInfoV2Response getDCEPInfo(String id) {
        return null;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public DCEPNFTInfoV2Response mint(DCEPMintRequest mintRequest) {

        log.info("mint - DCEPMintRequest: {}", mintRequest.toString());
        String serialNumberStr = "error";
        String bankSign = "error";
        try {

            // 根据请求的money生成冠字号
            BigInteger serialNumber = DCEPUtil.serialNumber(mintRequest.getMoneyType());
            serialNumberStr = StringBytesConvert.hexToAscii(serialNumber);

            // 创建银行签名
            bankSign = DCEPUtil.getBankSign(serialNumberStr, privateKey);

            // 把信息保存到数据库
            decpRepository.insert(serialNumberStr, mintRequest.getMoneyType(), mintRequest.getAddress(), bankSign);

            // 批量生产 NFT
            this.decp.mint(mintRequest.getAddress(), serialNumber).send();

        } catch (Exception e) {
            log.error("getMessage:{}, serialNumberStr:{}, getMoneyType:{}, getAddress:{}, bankSign:{}"
                    , e.getMessage(), serialNumberStr, mintRequest.getMoneyType(), mintRequest.getAddress(), bankSign);
            // 冠字号可能已经被用了，需要事后去检查钱在哪里，在更新数据库
            throw new MintException(rpcUrl);
        }
        // 返回给客户端
        return DCEPNFTInfoV2Response.of(serialNumberStr, mintRequest.getMoneyType(), bankSign);
    }

    @Override
    public DCEPInfoV2Response getDCEPInfo() {
        final String decpSymbol = "￥";
        final String decpName = "DC/EP";
        final BigInteger decpDecimal = new BigInteger("2");


        final String decpContractPath = "/contracts/RMB.json";
        final String jsonString;
        String abi;
        try {
            jsonString = jacksonUtil.readJsonFile(decpContractPath);
            abi = jacksonUtil.parsePropertyFromJson(jsonString, "abi");
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ReadFileErrorException(decpContractPath);
        }

        return DCEPInfoV2Response.of(decp.getContractAddress(), decpName, decpSymbol, decpDecimal, abi);
    }
}
