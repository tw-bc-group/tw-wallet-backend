package com.thoughtworks.wallet.asset.controller;


import com.thoughtworks.wallet.asset.request.IdentityRegistryRequest;
import com.thoughtworks.wallet.asset.service.IBlockchainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/v1/identities")
@Api(tags = "身份相关操作")
public class IdentityController {

    private final IBlockchainService quorumService;

    @Autowired
    public IdentityController(IBlockchainService quorumService) {
        this.quorumService = quorumService;
    }

    @PostMapping
    @ApiOperation(value = "注册身份")
    @ResponseStatus(HttpStatus.CREATED)
    public void identityRegistry(@Valid @RequestBody IdentityRegistryRequest request) {
        log.info("Create Identity: " + request.toString());
        quorumService.sendRawTransaction(request.getSignedTransactionRawData(), request.getAddress());
    }
}
