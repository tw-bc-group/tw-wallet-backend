package com.thoughtworks.wallet.healthy.controller.v2;

import com.thoughtworks.wallet.healthy.dto.JwtResponse;
import com.thoughtworks.wallet.healthy.dto.VerifyJwtRequest;
import com.thoughtworks.wallet.healthy.dto.VerifyJwtResponse;
import com.thoughtworks.wallet.healthy.dto.v2.*;
import com.thoughtworks.wallet.healthy.service.impl.v2.VerifierService;
import com.thoughtworks.wallet.healthy.service.v2.IVCService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/v2/verifier")
@Api(tags = "V2-验证者服务")
public class VerifierController {

    private final IVCService healthyVerifierServiceV2;
    private final VerifierService verifierService;

    public VerifierController(IVCService healthyVerifierServiceV2, VerifierService verifierService) {
        this.healthyVerifierServiceV2 = healthyVerifierServiceV2;
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

    @PatchMapping("/{id}")
    @ApiOperation(value = "更新验证者所需VC类型")
    @ResponseStatus(HttpStatus.OK)
    public VerifierResponse updateVerifierVcType(@PathVariable Integer id, @Valid @RequestBody VerifierVcTypesRequest request) {
        return verifierService.updateVerifierVcTypes(id, request);
    }


    @GetMapping("/{id}/vc")
    @ApiOperation(value = "获取验证者所需VC类型(json-ld格式)")
    @ResponseStatus(HttpStatus.OK)
    public VerifierVcResponse getVerifierVc(@PathVariable Integer id) throws IOException {
        return verifierService.getVerifierVc(id);
    }

    @PostMapping(value = "/health-certification/verify")
    @ApiOperation(value = "验证健康认证")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse verifyHealthVerification(@Valid @RequestBody VerifyJwtTokensRequest verifyJwtRequest) {
        return healthyVerifierServiceV2.VerifyHealthVerification(verifyJwtRequest);
    }

    @PostMapping(value = "/travel-badge/verify")
    @ApiOperation(value = "验证通行证")
    @ResponseStatus(HttpStatus.OK)
    public VerifyJwtResponse verifyravelBadgeVerification(@Valid @RequestBody VerifyJwtRequest verifyJwtRequest) {
        return healthyVerifierServiceV2.VerifyTravelBadgeVC(verifyJwtRequest);
    }
}
