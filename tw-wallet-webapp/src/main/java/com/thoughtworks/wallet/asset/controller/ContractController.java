package com.thoughtworks.wallet.asset.controller;

import com.thoughtworks.wallet.asset.response.DCEPInfoV2Response;
import com.thoughtworks.wallet.asset.response.DECPInfoResponse;
import com.thoughtworks.wallet.asset.response.IdentitiesContractInfoRepresentation;
import com.thoughtworks.wallet.asset.response.IdentityRegistryInfoResponse;
import com.thoughtworks.wallet.asset.service.IBlockchainService;
import com.thoughtworks.wallet.asset.service.IDCEPService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final IDCEPService decpService;

    @Autowired
    public ContractController(IBlockchainService quorumService, IDCEPService decpService) {
        this.quorumService = quorumService;
        this.decpService = decpService;
    }

    @GetMapping(value = "/identity-registry")
    @ApiOperation(value = "获取 IdentityRegistry 合约的相关信息")
    public IdentityRegistryInfoResponse getIdentityRegistryContractInfo() {
        return quorumService.getIdentityRegistryInfo();
    }

    @GetMapping(value = "/token")
    @ApiOperation(value = "获取 DC/EP 合约的相关信息")
    public DECPInfoResponse getDCEPContractInfo() {
        return quorumService.getDCEPInfo();
    }


    @GetMapping(value = "/nft-dcep")
    @ApiOperation(value = "获取 NFT DC/EP 合约的相关信息")
    public DCEPInfoV2Response getDCEPContractInfoNew() {
        return decpService.getDCEPInfo();
    }

    @GetMapping(value = "/identities")
    @ApiOperation(value = "Fetch identities contract info")
    public ResponseEntity<IdentitiesContractInfoRepresentation> getIdentitiesContractInfo() {
        return new ResponseEntity<>(quorumService.getIdentityInfo(), HttpStatus.OK);
    }
}
