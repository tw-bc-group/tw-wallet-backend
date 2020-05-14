package com.thoughtworks.wallet.healthyVerifier.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.thoughtworks.wallet.gen.tables.records.TblHealthyVerificationClaimRecord;
import com.thoughtworks.wallet.healthyVerifier.crypto.Base58;
import com.thoughtworks.wallet.healthyVerifier.model.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Hash;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@Component
@NoArgsConstructor
public class ClaimUtil {
    public final static String                didSchema      = "DID:TW:";
    private final       String                version        = "0.1";
    final               ImmutableList<String> context        = ImmutableList.of("https://blockchain.thoughtworks.cn/credentials/v1/");
    final               ImmutableList<String> credentialType = ImmutableList.of("HealthyCredential");
    // TODO 目前假设 claim 5 mins 过期
    Duration expiredDuration = Duration.ofMinutes(5);


    /**
     * 生成规则：
     * 1. 根据用户的地址，计算 h = sha256hash160(address），data = version || h；
     * 2. 对 data 计算两次 sha256，并取结果哈希的前 4 字节作为校验，即 checksum = sha256(sha256(data))[0:4]；
     * 3. 令 idString = base58(data || checksum)[0, 40]；
     * 4. 将 "did:tw:" 与 idString 拼接，即 ID = "did:tw:" || idString；
     * 5. 输出 ID。
     * 上述过程中，|| 表示连接前后两个字节串，version 是当前版本。
     *
     * @param did
     * @return claim id
     * @throws Exception
     */
    public String generateClaimId(String did, String version) {
        final String address  = getAddressFromDid(did);
        final byte[] h        = Hash.sha256hash160(address.getBytes());
        final String data     = version.concat(Arrays.toString(h));
        final String checkSum = Arrays.toString(Hash.sha256(Hash.sha256(data.getBytes()))).substring(0, 4);
        final String idString = Base58.encode(checkSum.concat(data).getBytes()).substring(0, 40);

        return didSchema.concat(idString);
    }

    @NotNull
    public String generateIssuerDid(String issuerAddress) {
        return didSchema + issuerAddress.substring(2);
    }


    public HealthyStatusWrapper generateHealthyStatus(String phone) {
        // TODO: mock healthy status: last number of phone is odd number, then it's UNHEALTHY, Otherwise its HEALTHY
        Map<Integer, String> healthyStatus = ImmutableMap.of(0, HealthyStatus.HEALTHY.getStatus(), 1, HealthyStatus.UNHEALTHY.getStatus());
        String               lastChar      = phone.substring(phone.length() - 1);
        final int            lastNumber    = Integer.parseInt(lastChar);
        int                  no            = lastNumber % 2;
        return HealthyStatusWrapper.of(healthyStatus.get(no));
    }

    public HealthVerificationClaim generateHealthyVerificationClaim(String did, String phone, String issuerAddress) {
        final String claimId = generateClaimId(did, version);
        log.info("Claim Id of did:{} is {}.", did, claimId);
        String issuerDid = generateIssuerDid(issuerAddress);

        final Instant now         = Instant.now();
        final long    currentTime = now.getEpochSecond();
        final long    expiredTime = now.plus(expiredDuration).getEpochSecond();

        final HealthyStatusWrapper healthyStatus = generateHealthyStatus(phone);

        return HealthVerificationClaim.builder()
                .context(context)
                .id(claimId)
                .ver(version)
                .iss(issuerDid)
                .iat(currentTime)
                .exp(expiredTime)
                .typ(credentialType)
                .sub(HealthyCredential.of(did, phone, healthyStatus))
                .build();
    }

    public HealthVerificationClaim changeHealthyVerificationClaim(TblHealthyVerificationClaimRecord claimRecord, String status) {
        HealthVerificationClaim claim = new HealthVerificationClaim(claimRecord);
        claim.getSub().getHealthyStatus().setVal(status);
        final Instant now         = Instant.now();
        final long    expiredTime = now.plus(expiredDuration).getEpochSecond();
        claim.setExp(expiredTime);
        return claim;
    }

    private String getAddressFromDid(String did) {
        did = did.toLowerCase();
        return did.replace("did:tw:", "0x");
    }


}
