package com.thoughtworks.wallet.healthy.service;

import com.thoughtworks.wallet.healthy.dto.VcTypeRequest;
import com.thoughtworks.wallet.healthy.dto.VcTypeResponse;
import com.thoughtworks.wallet.healthy.model.VcType;

public interface IVcTypeService {
    VcTypeResponse createVcType(VcTypeRequest vcTypeRequest);
    VcTypeResponse getVcTypeById(String id);
}
