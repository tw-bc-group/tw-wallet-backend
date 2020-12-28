package com.thoughtworks.wallet.healthy.controller;

import com.thoughtworks.wallet.healthy.dto.*;
import com.thoughtworks.wallet.healthy.dto.V2.HealthVerificationRequestV2;
import com.thoughtworks.wallet.healthy.service.IHealthyClaimServiceV2;
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
@RequestMapping(value = "/v2/health-certifications")
@Api(tags = "健康认证服务V2")
public class HealthyVerifierControllerV2 {

    private final IHealthyClaimServiceV2 healthyVerifierServiceV2;

    public HealthyVerifierControllerV2(IHealthyClaimServiceV2 healthyVerifierService) {
        this.healthyVerifierServiceV2 = healthyVerifierService;
    }

    @PostMapping
    @ApiOperation(value = "添加健康认证")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtResponse createHealthVerification(@Valid @RequestBody HealthVerificationRequestV2 healthVerification) {
        return healthyVerifierServiceV2.createHealthVerification(healthVerification);
    }

    @GetMapping("/{ownerId}")
    @ApiOperation(value = "根据 owner DID 获取 健康认证")
    public List<String> getHealthVerification(@PathVariable(value = "ownerId") String ownerId) {
        return healthyVerifierServiceV2.getHealthVerification(ownerId);
    }

    @PostMapping(value = "/verify")
    @ApiOperation(value = "验证健康认证")
    @ResponseStatus(HttpStatus.OK)
    public VerifyJwtResponse verifyHealthVerification(@Valid @RequestBody VerifyJwtRequest verifyJwtRequest) {
        return healthyVerifierServiceV2.VerifyHealthVerification(verifyJwtRequest);
    }

}
