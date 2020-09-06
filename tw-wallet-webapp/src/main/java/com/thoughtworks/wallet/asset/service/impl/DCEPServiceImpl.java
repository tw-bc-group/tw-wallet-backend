package com.thoughtworks.wallet.asset.service.impl;

import com.thoughtworks.common.annotation.Node1PrivateKey;
import com.thoughtworks.common.annotation.QuorumRPCUrl;
import com.thoughtworks.common.exception.*;
import com.thoughtworks.common.util.JacksonUtil;
import com.thoughtworks.common.util.dcep.DCEPUtil;
import com.thoughtworks.common.util.dcep.StringBytesConvert;
import com.thoughtworks.common.wrapper.DCEPContract;
import com.thoughtworks.wallet.asset.repository.DECPRepository;
import com.thoughtworks.wallet.asset.request.DCEPMintRequest;
import com.thoughtworks.wallet.asset.response.*;
import com.thoughtworks.wallet.asset.service.IDCEPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

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
    public DCEPNFTInfoV2Response getDCEPBySerialNumber(String serialNumber) {
        return decpRepository.getDCEPBySerialNumber(serialNumber);
    }

    @Override
    public List<DCEPNFTInfoV2Response> getDCEPByAddress(String address, int limit, int offset) {
        return decpRepository.getDCEPByAddress(address, limit, offset);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public DCEPNFTInfoV2Response mint(DCEPMintRequest mintRequest) {

        log.info("mint - DCEPMintRequest: {}", mintRequest.toString());
        String serialNumberStr = "error";
        String bankSign = "error";
        LocalDateTime createTime = LocalDateTime.now();

        try {

            // 根据请求的money生成冠字号
            BigInteger serialNumber = DCEPUtil.serialNumber(mintRequest.getMoneyType());
            serialNumberStr = StringBytesConvert.hexToAscii(serialNumber);

            // 创建银行签名
            bankSign = DCEPUtil.sign(serialNumberStr, privateKey);

            // 把信息保存到数据库
            decpRepository.insert(serialNumberStr, mintRequest.getMoneyType(), mintRequest.getAddress(), bankSign, createTime);

            // 批量生产 NFT
            this.decp.mint(mintRequest.getAddress(), serialNumber).sendAsync();

        } catch (Exception e) {
            log.error("getMessage:{}, serialNumberStr:{}, getMoneyType:{}, getAddress:{}, bankSign:{}"
                    , e.getMessage(), serialNumberStr, mintRequest.getMoneyType(), mintRequest.getAddress(), bankSign);
            // 冠字号可能已经被用了，需要事后去检查钱在哪里，在更新数据库
            throw new MintException(rpcUrl);
        }
        // 返回给客户端
        return new DCEPNFTInfoV2Response(serialNumberStr, mintRequest.getAddress(), bankSign, mintRequest.getMoneyType(), createTime);
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
