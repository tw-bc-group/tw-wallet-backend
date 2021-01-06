package com.thoughtworks.wallet.healthy.controller.v2;

import com.thoughtworks.wallet.healthy.dto.*;
import com.thoughtworks.wallet.healthy.dto.v2.*;
import com.thoughtworks.wallet.healthy.service.impl.v2.IssuerService;
import com.thoughtworks.wallet.healthy.service.impl.v2.VcTypeService;
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
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/v2/vc-market")
@Api(tags = "V2-VC大市场接口")
public class VCMarketController {

    private final IVCService healthyVerifierServiceV2;
    private final IssuerService issuerService;
    private final VcTypeService vcTypeService;
    private final VerifierService verifierService;

    public VCMarketController(IVCService healthyVerifierServiceV2, IssuerService issuerService, VcTypeService vcTypeService, VerifierService verifierService) {
        this.healthyVerifierServiceV2 = healthyVerifierServiceV2;
        this.issuerService = issuerService;
        this.vcTypeService = vcTypeService;
        this.verifierService = verifierService;
    }

    @PostMapping("vcs/health-certification")
    @ApiOperation(value = "添加健康认证")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtResponse createHealthVerification(@Valid @RequestBody CreateVCRequest createVCRequest) {
        return healthyVerifierServiceV2.createHealthVerification(createVCRequest);
    }

    @PostMapping("vcs/immunoglobulin-detection")
    @ApiOperation(value = "蛋白检测")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtResponse createImmunoglobulinDetection(@Valid @RequestBody CreateVCRequest createVCRequest) {
        return healthyVerifierServiceV2.createImmunoglobulinDetectionVC(createVCRequest);
    }

    @PostMapping("vcs/passport")
    @ApiOperation(value = "出境记录")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtResponse createPassportVC(@Valid @RequestBody CreateVCRequest createVCRequest) {
        return healthyVerifierServiceV2.createPassportVC(createVCRequest);
    }

    @GetMapping("vcs/{ownerId}")
    @ApiOperation(value = "根据 owner DID 获取 健康认证")
    public List<String> getHealthVerification(@PathVariable(value = "ownerId") String ownerId) {
        return healthyVerifierServiceV2.getHealthVerification(ownerId);
    }

    @PostMapping("vcs/vc-type")
    @ApiOperation(value = "添加VC类型")
    @ResponseStatus(HttpStatus.CREATED)
    public VcTypeResponse createVcType(@Valid @RequestBody VcTypeRequest vcTypeRequest) {
        return vcTypeService.createVcType(vcTypeRequest);
    }

    @PostMapping("/issuers")
    @ApiOperation(value = "添加认证机构")
    @ResponseStatus(HttpStatus.CREATED)
    public IssuerResponse createIssuer(@Valid @RequestBody IssuerRequest issuerRequest) {
        return issuerService.createIssuer(issuerRequest);
    }

    @GetMapping("/issuers")
    @ApiOperation(value = "获取所有认证机构及其VC类型")
    @ResponseStatus(HttpStatus.OK)
    public List<IssuerResponse> getAllIssuers() {
        return issuerService.getAllIssuers();
    }



    @GetMapping("/verifiers/vc-type/{id}")
    @ApiOperation(value = "获取VC类型")
    @ResponseStatus(HttpStatus.OK)
    public VcTypeResponse getVcTypeById(@PathVariable String id) {
        return vcTypeService.getVcTypeById(id);
    }

    @PostMapping("/verifiers")
    @ApiOperation(value = "添加验证者")
    @ResponseStatus(HttpStatus.CREATED)
    public VerifierResponse createVerifier(@Valid @RequestBody VerifierRequest verifierRequest) {
        return verifierService.createVerifier(verifierRequest);
    }

    @GetMapping("/verifiers/{id}")
    @ApiOperation(value = "获取验证者")
    @ResponseStatus(HttpStatus.OK)
    public VerifierResponse getVerifier(@PathVariable Integer id) {
        return verifierService.getVerifierById(id);
    }

    @PatchMapping("/verifiers/{id}")
    @ApiOperation(value = "更新验证者所需VC类型")
    @ResponseStatus(HttpStatus.OK)
    public VerifierResponse updateVerifierVcType(@PathVariable Integer id, @Valid @RequestBody VerifierVcTypesRequest request) {
        return verifierService.updateVerifierVcTypes(id, request);
    }


    @GetMapping("/verifiers/{id}/vc")
    @ApiOperation(value = "获取验证者所需VC类型(json-ld格式)")
    @ResponseStatus(HttpStatus.OK)
    public VerifierVcResponse getVerifierVc(@PathVariable Integer id) throws IOException {
        return verifierService.getVerifierVc(id);
    }
}
