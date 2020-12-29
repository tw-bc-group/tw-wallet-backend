package com.thoughtworks.wallet.healthy.service;

import com.thoughtworks.wallet.healthy.dto.*;
import com.thoughtworks.wallet.healthy.dto.V2.HealthVerificationClaimV2;
import com.thoughtworks.wallet.healthy.dto.V2.HealthVerificationRequestV2;

import java.util.List;

public interface IHealthyClaimServiceV2 {

    /**
     * 创建健康声明
     *
     * @param healthVerification
     * @return
     */
    JwtResponse createHealthVerification(HealthVerificationRequestV2 healthVerification);


    /**
     * 蛋白检测VC
     * @param healthVerification
     * @return
     */
    JwtResponse createImmunoglobulinDetectionVC(HealthVerificationRequestV2 healthVerification);


    /**
     * 获取健康声明
     *
     * @param ownerId
     * @return
     */
    List<String> getHealthVerification(String ownerId);

    /**
     * 改变健康声明状态，测试用途
     * 另外也可以直接修改黑名单来改变状态
     *
     * @param changeHealthVerificationRequest
     * @return
     */
//    HealthVerificationResponse changeHealthVerification(ChangeHealthVerificationRequest changeHealthVerificationRequest);

    /**
     * 验证token
     *
     * @param verifyJwtRequest
     * @return
     */
    VerifyJwtResponse VerifyHealthVerification(VerifyJwtRequest verifyJwtRequest);
}
