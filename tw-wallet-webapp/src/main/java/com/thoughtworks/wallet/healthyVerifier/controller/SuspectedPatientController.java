package com.thoughtworks.wallet.healthyVerifier.controller;

import com.thoughtworks.wallet.healthyVerifier.dto.AddSuspectedPatientRequest;
import com.thoughtworks.wallet.healthyVerifier.model.SuspectedPatientInfo;
import com.thoughtworks.wallet.healthyVerifier.service.ISuspectedPatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping(value = "/v1/suspected-patients")
@Api(tags = "疑似不健康用户名单")
public class SuspectedPatientController {

    private final ISuspectedPatientService suspectedPatientService;

    public SuspectedPatientController(ISuspectedPatientService suspectedPatientService) {
        this.suspectedPatientService = suspectedPatientService;
    }

    @PostMapping
    @ApiOperation(value = "根据 phone 添加疑似不健康用户")
    public SuspectedPatientInfo addSuspectedPatient(@Valid @RequestBody AddSuspectedPatientRequest request) {
        return suspectedPatientService.addSuspectedPatient(request.getPhone());
    }

    @GetMapping
    @ApiOperation(value = "查询某个 phone 对应的用户是否疑似不健康")
    public boolean checkIsSuspectedPatient(@Valid @Length(min = 11, max = 11, message = "Phone number should only have 11 digits.") @RequestParam(value = "phone") String phone) {
        return suspectedPatientService.isSuspectedPatient(phone);
    }

    @DeleteMapping
    @ApiOperation(value = "根据 phone 从疑似不健康用户列表中删除某用户")
    public SuspectedPatientInfo deleteSuspectedPatient(@Valid @Length(min = 11, max = 11, message = "Phone number should only have 11 digits.") @RequestParam(value = "phone") String phone) {
        return suspectedPatientService.deleteSuspectedPatient(phone);
    }
}

