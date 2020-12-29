package com.thoughtworks.wallet.healthy.controller.v2;

import com.thoughtworks.wallet.healthy.dto.v2.VerifierRequest;
import com.thoughtworks.wallet.healthy.dto.v2.VerifierResponse;
import com.thoughtworks.wallet.healthy.dto.v2.VerifierVcResponse;
import com.thoughtworks.wallet.healthy.dto.v2.VerifierVcTypesRequest;
import com.thoughtworks.wallet.healthy.service.impl.v2.VerifierService;
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
}
