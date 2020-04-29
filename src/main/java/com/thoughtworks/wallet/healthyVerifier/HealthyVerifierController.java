package com.thoughtworks.wallet.healthyVerifier;

import com.thoughtworks.wallet.healthyVerifier.service.IHealthyVerifierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/v1/health-certifications")
@Api(tags = "健康认证服务")
public class HealthyVerifierController {

    private final IHealthyVerifierService healthyVerifierService;

    public HealthyVerifierController(IHealthyVerifierService healthyVerifierService) {
        this.healthyVerifierService = healthyVerifierService;
    }

    @PostMapping
    @ApiOperation(value = "添加健康认证")
    public HealthVerificationResponse createHealthVerification(@Valid @RequestBody HealthVerificationRequest healthVerification) {
        return healthyVerifierService.createHealthVerification(healthVerification);
    }
}
