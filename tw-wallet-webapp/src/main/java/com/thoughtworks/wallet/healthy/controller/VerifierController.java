package com.thoughtworks.wallet.healthy.controller;

import com.thoughtworks.wallet.healthy.dto.VcTypeRequest;
import com.thoughtworks.wallet.healthy.dto.VcTypeResponse;
import com.thoughtworks.wallet.healthy.dto.VerifierRequest;
import com.thoughtworks.wallet.healthy.dto.VerifierResponse;
import com.thoughtworks.wallet.healthy.service.impl.VerifierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/v2/verifier")
@Api(tags = "验证者服务")
public class VerifierController {

    private final VerifierService verifierService;

    public VerifierController(VerifierService verifierService) {
        this.verifierService = verifierService;
    }

    @PostMapping
    @ApiOperation(value = "添加验证者")
    @ResponseStatus(HttpStatus.CREATED)
    public VerifierResponse createVerifier(@Valid @RequestBody VerifierRequest verifierRequest) {
        return verifierService.createVerifier(verifierRequest);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取验证者")
    @ResponseStatus(HttpStatus.OK)
    public VerifierResponse getVerifier(@PathVariable Integer id) {
        return verifierService.getVerifierById(id);
    }
}
