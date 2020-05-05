package com.thoughtworks.wallet.asset.controller;

import com.thoughtworks.wallet.asset.request.TWPTransferRequest;
import com.thoughtworks.wallet.asset.response.TWPointBalanceResponse;
import com.thoughtworks.wallet.asset.service.IBlockchainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/v1/tw-points")
@Api(tags = "TW Point 相关操作")
public class TWPointController {

    private final IBlockchainService quorumService;

    @Autowired
    public TWPointController(IBlockchainService quorumService) {
        this.quorumService = quorumService;
    }

    @GetMapping("/{address}")
    @ApiOperation(value = "根据用户的 quorum 地址获取 TW Point 余额")
    public TWPointBalanceResponse getBalanceByAddress(@PathVariable("address") String address) {
        return quorumService.getTWPointBalanceBy(address);
    }

    @PostMapping(value = "/transfer")
    @ApiOperation(value = "TWP 转账")
    public void transfer(@Valid @RequestBody TWPTransferRequest request) {
        log.info("TWPoint transfer: " + request.toString());
        quorumService.sendRawTransaction(request.getSignedTransactionRawData(), request.getFromAddress());
    }
}
