package com.thoughtworks.wallet.healthy.service.impl;

import com.thoughtworks.wallet.healthy.dto.VcTypeRequest;
import com.thoughtworks.wallet.healthy.dto.VcTypeResponse;
import com.thoughtworks.wallet.healthy.model.VcType;
import com.thoughtworks.wallet.healthy.repository.VcTypeDAO;
import com.thoughtworks.wallet.healthy.service.IVcTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class VcTypeService implements IVcTypeService {
    private final VcTypeDAO vcTypeDAO;

    public VcTypeService(VcTypeDAO vcTypeDAO) {
        this.vcTypeDAO = vcTypeDAO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public VcTypeResponse createVcType(VcTypeRequest vcTypeRequest) {
        String id = vcTypeDAO.insertVcType(VcType.builder()
                .id(vcTypeRequest.getId())
                .name(vcTypeRequest.getName())
                .issuerId(vcTypeRequest.getIssuerId())
                .content(vcTypeRequest.getContent())
                .url(vcTypeRequest.getUrl())
                .build());
        VcType vcType = vcTypeDAO.getVcTypeById(id);
        return VcTypeResponse.builder()
                .id(vcType.getId())
                .name(vcType.getName())
                .issuerId(vcType.getIssuerId())
                .content(vcType.getContent())
                .url(vcType.getUrl())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public VcTypeResponse getVcTypeById(String id) {
        VcType vcType = vcTypeDAO.getVcTypeById(id);
        return VcTypeResponse.builder()
                .id(vcType.getId())
                .name(vcType.getName())
                .issuerId(vcType.getIssuerId())
                .content(vcType.getContent())
                .url(vcType.getUrl())
                .build();
    }
}
