package com.thoughtworks.wallet.healthy.controller;

import com.thoughtworks.wallet.healthy.dto.IssuerRequest;
import com.thoughtworks.wallet.healthy.dto.IssuerResponse;
import com.thoughtworks.wallet.healthy.dto.VcTypeRequest;
import com.thoughtworks.wallet.healthy.dto.VcTypeResponse;
import com.thoughtworks.wallet.healthy.model.VcType;
import com.thoughtworks.wallet.healthy.service.impl.VcTypeService;
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
@RequestMapping(value = "/v2/vctypes")
@Api(tags = "VC类型")
public class VcTypeController {

    private final VcTypeService vcTypeService;

    public VcTypeController(VcTypeService vcTypeService) {
        this.vcTypeService = vcTypeService;
    }

    @PostMapping
    @ApiOperation(value = "添加VC类型")
    @ResponseStatus(HttpStatus.CREATED)
    public VcTypeResponse createVcType(@Valid @RequestBody VcTypeRequest vcTypeRequest) {
        return vcTypeService.createVcType(vcTypeRequest);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取VC类型")
    @ResponseStatus(HttpStatus.OK)
    public VcTypeResponse getVcTypeById(@PathVariable Integer id) {
        return vcTypeService.getVcTypeById(id);
    }
}
