package com.thoughtworks.wallet.asset.controller;


import com.thoughtworks.wallet.asset.request.IdentityRegistryRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/v1/identities")
@Api(tags = "身份相关操作")
public class IdentityController {
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "注册身份")
    public void identityRegistry(@Valid @RequestBody IdentityRegistryRequest request) {
        // TODO call blockchain identity registry smart contract
        log.info(request.toString());
    }
}
