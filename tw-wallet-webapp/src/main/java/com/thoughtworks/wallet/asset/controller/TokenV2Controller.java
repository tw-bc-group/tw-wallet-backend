package com.thoughtworks.wallet.asset.controller;

import com.thoughtworks.common.exception.InvalidAddressErrorException;
import com.thoughtworks.common.util.Identity;
import com.thoughtworks.wallet.asset.request.DCEPMintRequest;
import com.thoughtworks.wallet.asset.request.TWPTransferRequest;
import com.thoughtworks.wallet.asset.response.DCEPInfoV2Response;
import com.thoughtworks.wallet.asset.response.DCEPNFTInfoV2Response;
import com.thoughtworks.wallet.asset.response.DECPBalanceResponse;
import com.thoughtworks.wallet.asset.service.IDCEPService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/v2/token")
@Api(tags = "DC/EP 相关操作")
public class TokenV2Controller {

    private final IDCEPService decpService;

    @Autowired
    public TokenV2Controller(IDCEPService decpService) {
        this.decpService = decpService;
    }


    // TODO: swagger 增加枚举的解析 https://juejin.im/post/6844903845697421319
    @PostMapping(value = "/mint")
    @ApiOperation(value = "发行 DC/EP")
    public DCEPNFTInfoV2Response mint(@Valid @RequestBody DCEPMintRequest request) {
        log.info("DC/EP mint: " + request.toString());
        if (!Identity.isValidAddress(request.getAddress())) {
            throw new InvalidAddressErrorException(request.getAddress());
        }
        return decpService.mint(request);
    }

    @GetMapping(value = "/info")
    @ApiOperation(value = "获取 NFT DC/EP 合约的相关信息")
    public DCEPInfoV2Response getDCEPContractInfo() {
        log.info("DC/EP getDCEPContractInfo");
        return decpService.getDCEPInfo();
    }


    @GetMapping("/{serial_number}")
    @ApiOperation(value = "根据冠字号地址获取 DC/EP 信息")
    public DCEPNFTInfoV2Response getDCEPBySerialNumber(@PathVariable("serial_number") String serialNumber) {
        log.info("DC/EP getDCEPBySerialNumber: " + serialNumber);
        DCEPNFTInfoV2Response dcepBySerialNumber = decpService.getDCEPBySerialNumber(serialNumber);
        return dcepBySerialNumber;
    }

    @GetMapping
    @ApiOperation(value = "查询某个 address 持有的 DC/EP")
    public List<DCEPNFTInfoV2Response> getDCEPByAddress(
            @RequestParam(value = "address") String address,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "offset", defaultValue = "0") int offset
    ) {
        return decpService.getDCEPByAddress(address, limit, offset);
    }

    @PostMapping(value = "/transfer")
    @ApiOperation(value = "DC/EP 转账")
    public void transfer(@Valid @RequestBody TWPTransferRequest request) {
        log.info("DC/EP transfer: " + request.toString());
        decpService.sendRawTransaction(request.getSignedTransactionRawData(), request.getFromAddress());
    }
}


