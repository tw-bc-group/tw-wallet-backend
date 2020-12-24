package com.thoughtworks.wallet.healthy.controller;

import com.thoughtworks.wallet.healthy.dto.IssuerRequest;
import com.thoughtworks.wallet.healthy.dto.IssuerResponse;
import com.thoughtworks.wallet.healthy.service.impl.IssuerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/v2/issuers")
@Api(tags = "认证机构")
public class IssuerController {

    private final IssuerService issuerService;

    public IssuerController(IssuerService issuerService) {
        this.issuerService = issuerService;
    }

    @PostMapping
    @ApiOperation(value = "添加认证机构")
    @ResponseStatus(HttpStatus.CREATED)
    public IssuerResponse createIssuer(@Valid @RequestBody IssuerRequest issuerRequest) {
        return issuerService.createIssuer(issuerRequest);
    }

    @GetMapping
    @ApiOperation(value = "获取所有认证机构及其VC")
    @ResponseStatus(HttpStatus.CREATED)
    public List<IssuerResponse> getAllIssuers() {
        return issuerService.getAllIssuers();
    }
}
