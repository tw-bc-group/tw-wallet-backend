package com.thoughtworks.wallet.healthy.service.v2;

import com.thoughtworks.wallet.healthy.dto.v2.VcTypeRequest;
import com.thoughtworks.wallet.healthy.dto.v2.VcTypeResponse;

public interface IVcTypeService {
    VcTypeResponse createVcType(VcTypeRequest vcTypeRequest);
    VcTypeResponse getVcTypeById(String id);
}
