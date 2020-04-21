package com.thoughtworks.wallet.asset.controller;

import com.thoughtworks.wallet.asset.response.IdentityRegistryInfoResponse;
import com.thoughtworks.wallet.asset.response.TWPointInfoResponse;
import com.thoughtworks.wallet.asset.service.IBlockchainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/v1/contracts")
@Api(tags = "合约相关接口")
public class ContractController {
    private final IBlockchainService quorumService;

    @Autowired
    public ContractController(IBlockchainService quorumService) {
        this.quorumService = quorumService;
    }

    @GetMapping(value = "/identity-registry")
    @ApiOperation(value = "获取 IdentityRegistry 合约的相关信息")
    public IdentityRegistryInfoResponse getIdentityRegistryContractInfo() {
        return quorumService.getIdentityRegistryInfo();
    }

    @GetMapping(value = "/tw-point")
    @ApiOperation(value = "获取 TWPoint 合约的相关信息")
    public TWPointInfoResponse getTWPointContractInfo() {
        return quorumService.getTWPointInfo();
    }
}
