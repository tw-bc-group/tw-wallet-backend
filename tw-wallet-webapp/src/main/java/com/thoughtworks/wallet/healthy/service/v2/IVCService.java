package com.thoughtworks.wallet.healthy.service.v2;

import com.thoughtworks.wallet.healthy.dto.*;
import com.thoughtworks.wallet.healthy.dto.v2.CreateVCRequest;

import java.util.List;

public interface IVCService {

    /**
     * 创建健康声明
     *
     * @param createVCRequest
     * @return
     */
    JwtResponse createHealthVerification(CreateVCRequest createVCRequest);


    /**
     * 蛋白检测VC
     *
     * @param createVCRequest
     * @return
     */
    JwtResponse createImmunoglobulinDetectionVC(CreateVCRequest createVCRequest);

    /**
     * 创建护照
     *
     * @param createVCRequest
     * @return
     */
    JwtResponse createPassportVC(CreateVCRequest createVCRequest);

    /**
     * 获取健康声明
     *
     * @param ownerId
     * @return
     */
    List<String> getHealthVerification(String ownerId);

    /**
     * 验证token
     *
     * @param verifyJwtRequest
     * @return 通行证token
     */
    JwtResponse VerifyHealthVerification(VerifyJwtRequest verifyJwtRequest);
}
