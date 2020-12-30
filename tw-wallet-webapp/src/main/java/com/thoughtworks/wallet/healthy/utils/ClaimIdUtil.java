package com.thoughtworks.wallet.healthy.utils;

import com.thoughtworks.wallet.healthy.crypto.Base58;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Hash;

import java.util.Arrays;

import static com.thoughtworks.wallet.healthy.service.impl.HealthyClaimService.didSchema;


@Slf4j
@Component
@NoArgsConstructor
public class ClaimIdUtil {

    /**
     * DID 生成规则：(https://github.com/ontio/ontology-DID/blob/master/docs/cn/ONTID_protocol_spec_cn.md)
     * 1. 根据用户的地址，计算 h = sha256hash160(address），data = version || h；
     * 2. 对 data 计算两次 sha256，并取结果哈希的前 4 字节作为校验，即 checksum = sha256(sha256(data))[0:4]；
     * 3. 令 idString = base58(data || checksum)[0, 40]；
     * 4. 将 "did:tw:" 与 idString 拼接，即 ID = "did:tw:" || idString；
     * 5. 输出 ID。
     * 上述过程中，|| 表示连接前后两个字节串，version 是当前版本。
     *
     * @param holderDid
     * @return claim id
     * @throws Exception
     */
    public String generateClaimId(String version) {
        final String address = getAddressFromDid(RandomStringUtils.randomAlphanumeric(100));
        final byte[] h = Hash.sha256hash160(address.getBytes());
        final String data = version.concat(Arrays.toString(h));
        final String checkSum = Arrays.toString(Hash.sha256(Hash.sha256(data.getBytes()))).substring(0, 4);
        final String idString = Base58.encode(checkSum.concat(data).getBytes()).substring(0, 40);

        return didSchema.concat(idString);
    }

    String getAddressFromDid(String did) {
        did = did.toLowerCase();
        return did.replace("did:tw:", "0x");
    }
}
