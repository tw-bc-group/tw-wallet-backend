package com.thoughtworks.wallet.asset.controller;

import com.thoughtworks.wallet.asset.request.DCEPMintRequest;
import com.thoughtworks.wallet.asset.response.DCEPInfoV2Response;
import com.thoughtworks.wallet.asset.service.IDCEPService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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


    @PostMapping(value = "/mint")
    @ApiOperation(value = "DC/EP 奖励")
    public void transfer(@Valid @RequestBody DCEPMintRequest request) {
        log.info("DC/EP mint: " + request.toString());
        decpService.mint(request);
    }

    @GetMapping(value = "/info")
    @ApiOperation(value = "获取 NFT DC/EP 合约的相关信息")
    public DCEPInfoV2Response getDCEPContractInfo() {
        return decpService.getDCEPInfo();
    }
}


