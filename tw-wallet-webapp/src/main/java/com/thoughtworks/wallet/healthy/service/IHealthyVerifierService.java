package com.thoughtworks.wallet.healthy.service;

import com.thoughtworks.wallet.healthy.dto.*;

public interface IHealthyVerifierService {

    /**
     * 创建健康声明
     *
     * @param healthVerification
     * @return
     */
    JwtResponse createHealthVerification(HealthVerificationRequest healthVerification);

    /**
     * 获取健康声明
     *
     * @param ownerId
     * @return
     */
    HealthVerificationResponse getHealthVerification(String ownerId);

    /**
     * 改变健康声明状态，测试用途
     *
     * @param changeHealthVerificationRequest
     * @return
     */
    HealthVerificationResponse changeHealthVerification(ChangeHealthVerificationRequest changeHealthVerificationRequest);

//    VerifyJwtResponse VerifyHealthVerification(VerifyJwtRequest verifyJwtRequest);
}
