package com.thoughtworks.wallet.healthy.controller.v2;

import com.thoughtworks.wallet.healthy.dto.JwtResponse;
import com.thoughtworks.wallet.healthy.dto.v2.CreateVCRequest;
import com.thoughtworks.wallet.healthy.dto.VerifyJwtRequest;
import com.thoughtworks.wallet.healthy.dto.VerifyJwtResponse;
import com.thoughtworks.wallet.healthy.service.v2.IVCService;
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
@RequestMapping(value = "/v2/vc-market")
@Api(tags = "VC大市场接口")
public class VCMarketController {

    private final IVCService healthyVerifierServiceV2;

    public VCMarketController(IVCService healthyVerifierService) {
        this.healthyVerifierServiceV2 = healthyVerifierService;
    }

    @PostMapping("/health-certification")
    @ApiOperation(value = "添加健康认证")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtResponse createHealthVerification(@Valid @RequestBody CreateVCRequest createVCRequest) {
        return healthyVerifierServiceV2.createHealthVerification(createVCRequest);
    }

    @PostMapping("/immunoglobulin-detection")
    @ApiOperation(value = "蛋白检测")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtResponse createImmunoglobulinDetection(@Valid @RequestBody CreateVCRequest createVCRequest) {
        return healthyVerifierServiceV2.createImmunoglobulinDetectionVC(createVCRequest);
    }

    @PostMapping("/passport")
    @ApiOperation(value = "蛋白检测")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtResponse createPassportVC(@Valid @RequestBody CreateVCRequest createVCRequest) {
        return healthyVerifierServiceV2.createPassportVC(createVCRequest);
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
