package com.thoughtworks.wallet.asset.controller;

import com.thoughtworks.wallet.asset.request.TWPRewardRequest;
import com.thoughtworks.wallet.asset.request.TWPTransferRequest;
import com.thoughtworks.wallet.asset.response.DECPBalanceResponse;
import com.thoughtworks.wallet.asset.response.TransferResponse;
import com.thoughtworks.wallet.asset.service.IBlockchainService;
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
@RequestMapping(value = "/v1/token")
@Api(tags = "DC/EP 相关操作")
public class TokenController {

    private final IBlockchainService quorumService;

    @Autowired
    public TokenController(IBlockchainService quorumService) {
        this.quorumService = quorumService;
    }

    @GetMapping("/{address}")
    @ApiOperation(value = "根据用户的 quorum 地址获取 DC/EP 余额")
    public DECPBalanceResponse getBalanceByAddress(@PathVariable("address") String address) {
        log.info("DC/EP getBalanceByAddress: " + address);
        return quorumService.getDCEPBalanceBy(address);
    }

    @PostMapping(value = "/transfer")
    @ApiOperation(value = "DC/EP 转账")
    public TransferResponse transfer(@Valid @RequestBody TWPTransferRequest request) {
        log.info("DC/EP transfer: " + request.toString());
        String hash = quorumService.sendRawTransaction(request.getSignedTransactionRawData(), request.getFromAddress());
        return TransferResponse.of(hash);
    }

    @PostMapping(value = "/reward")
    @ApiOperation(value = "DC/EP 奖励")
    public void transfer(@Valid @RequestBody TWPRewardRequest request) {
        log.info("DC/EP reward: " + request.toString());
        quorumService.assignInitPoint(request.getAddress(), request.getAmount());
    }
}
