package com.thoughtworks.wallet.unit;

import com.google.common.collect.ImmutableList;
import com.thoughtworks.common.crypto.Base64;
import com.thoughtworks.common.crypto.CryptoFacade;
import com.thoughtworks.common.crypto.Curve;
import com.thoughtworks.common.crypto.SignatureScheme;
import com.thoughtworks.common.util.JacksonUtil;
import com.thoughtworks.wallet.healthy.dto.HealthVerificationResponse;
import com.thoughtworks.common.util.Jwt;
import com.thoughtworks.wallet.healthy.model.HealthyCredential;
import com.thoughtworks.wallet.healthy.model.HealthyStatus;
import com.thoughtworks.wallet.healthy.model.HealthyStatusWrapper;
import com.thoughtworks.wallet.healthy.model.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * jwt do not support SECP256K1, so we should sign and verify by ourselves
 */
public class SignClaimTest {
    // generate by ethers.js
    String publicKey     = "038773a46bc5a2bb1c5687de4788a7d58df3f27483687c8df81d07350753161e05";
    String privateKey    = "4762e04d10832808a0aebdaa79c12de54afbe006bfffd228b3abcc494fe986f9";
    Jwt    jwt;
    String headerPayload = ""; // 签名的对象
    String alg           = "ES256";
    String type          = "JWT";
    String claimId       = "DID:TW:claimId";

    @BeforeEach
    public void init() {
        try {

            final Instant now         = Instant.now();
            final long    currentTime = now.getEpochSecond();
            final long    expiredTime = now.plus(Duration.ofMinutes(5)).getEpochSecond();
            HealthVerificationResponse healthVerificationResponse = HealthVerificationResponse.builder()
                    .context(ImmutableList.of("https://blockchain.thoughtworks.cn/credentials/v1/"))
                    .id(claimId)
                    .ver("v0.0.1")
                    .iss("DID:TW:iss")
                    .iat(currentTime)
                    .exp(expiredTime)
                    .typ(ImmutableList.of("HealthyCredential"))
                    .sub(HealthyCredential.of("DID:TW:sub",
                            "phone",
                            36,
                            Result.NO,
                            Result.NO,
                            HealthyStatusWrapper.of(HealthyStatus.HEALTHY.getStatus())))
                    .build();

            jwt = Jwt.builder()
                    .header(new Jwt.Header("ES256", "JWT"))
                    .payLoad(healthVerificationResponse)
                    .sign(CryptoFacade.fromPrivateKey(privateKey, SignatureScheme.SHA256WITHECDSA, Curve.SECP256K1))
                    .verify(CryptoFacade.fromPublicKey(publicKey, SignatureScheme.SHA256WITHECDSA, Curve.SECP256K1))
                    .build();

            String header  = Base64.encode(JacksonUtil.beanToJSonStr(jwt.getHeader()));
            String payload = Base64.encode(JacksonUtil.beanToJSonStr(jwt.getPayLoad()));
            headerPayload = String.format("%s.%s", header, payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    void givenQRCode_WhenVerifyWithSECP256K1_ThenSuccess() throws Exception {
        String token;

        // 生成签名
        {
            token = jwt.genJwtString();
            System.out.println(token);
        }

        //https://cli.im/ 生成
        String[]                   strings         = token.split("\\.");
        Jwt.Header                 header          = JacksonUtil.jsonStrToBean(Base64.decode(strings[0]), Jwt.Header.class);
        HealthVerificationResponse payload         = JacksonUtil.jsonStrToBean(Base64.decode(strings[1]), HealthVerificationResponse.class);
        String                     signature       = Base64.decode(strings[2]);
        boolean                    verifySignature = jwt.getVerify().verifySignature(headerPayload, signature);
        assertThat(verifySignature).isTrue();
        assertThat(header.getAlg()).isEqualTo(alg);
        assertThat(header.getTyp()).isEqualTo(type);
        assertThat(payload.getId()).isEqualTo(claimId);
    }
}
