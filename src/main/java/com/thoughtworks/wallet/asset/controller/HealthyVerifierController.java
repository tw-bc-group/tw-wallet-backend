package com.thoughtworks.wallet.asset.controller;

import com.thoughtworks.wallet.asset.response.HealthVerificationResponse;
import com.thoughtworks.wallet.asset.service.IHealthyVerifierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/phone/{phone}")
    @ApiOperation(value = "根据手机号码获取健康认证状态")
    public HealthVerificationResponse getHealthVerificationByPhone(@Valid @PathVariable(value = "phone") @Length(min = 11, max = 11, message = "Phone number should only have 11 digits.") String phone) {
        return healthyVerifierService.getHealthVerificationByPhone(phone);
    }
}
