package com.thoughtworks.wallet.healthyVerifier;

import com.thoughtworks.wallet.healthyVerifier.service.IHealthyVerifierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping
    @ApiOperation(value = "根据手机号码获取健康认证状态")
    public HealthVerificationResponse getHealthVerificationByPhone(@Valid @RequestParam(value = "phone") @Length(min = 11, max = 11, message = "Phone number should only have 11 digits.") String phone) {
        return healthyVerifierService.getHealthVerificationByPhone(phone);
    }

    @PostMapping
    @ApiOperation(value = "添加健康认证")
    public void createHealthVerification(@Valid @RequestBody HealthVerificationRequest healthVerification) {
        healthyVerifierService.createHealthVerification(healthVerification);
    }
}
