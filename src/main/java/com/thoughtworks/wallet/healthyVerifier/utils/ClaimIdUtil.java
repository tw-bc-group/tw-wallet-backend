package com.thoughtworks.wallet.healthyVerifier.utils;

import com.thoughtworks.wallet.healthyVerifier.crypto.Base58;
import com.thoughtworks.wallet.healthyVerifier.exception.QuorumConnectionErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Hash;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


@Slf4j
@Component
public class ClaimIdUtil {
    private final Web3j web3j;

    public ClaimIdUtil(Web3j web3j) {
        this.web3j = web3j;
    }

    /**
     * 生成规则：(https://github.com/ontio/ontology-DID/blob/master/docs/cn/ONTID_protocol_spec_cn.md)
     * 1. 生成 32 字节的随机 nonce，计算 h = Hash160(nonce），data = VER || h；
     * 2. 对data计算两次 SHA256，并取结果哈希的前4字节作为校验，即 checksum = SHA256(SHA256(data))[0:4]；
     * 3. 令 idString = base58(data || checksum)；
     * 4. 将 "did:healthyClaim:" 与 idString 拼接，即 ID = "did:healthyClaim:" || idString；
     * 5. 输出 ID。
     * 上述过程中，|| 表示连接前后两个字节串，VER 是 1 个字节的标签位。
     *
     * @param did
     * @return
     * @throws Exception
     */
    public String generateClaimId(String did, String ver) {
        final String prefix = "did:healthyClaim:";
        final String address = getAddressFromDid(did);
        final BigInteger nonce = getNonce(address);
        final byte[] h = Hash.sha256hash160(nonce.toByteArray());
        final String data = ver.concat(Arrays.toString(h));
        final String checkSum = Arrays.toString(Hash.sha256(Hash.sha256(data.getBytes()))).substring(0, 4);
        final String idString = Base58.encode(checkSum.concat(data).getBytes()).substring(0, 64);

        return prefix.concat(idString);
    }

    BigInteger getNonce(String address) {
        EthGetTransactionCount ethGetTransactionCount;
        try {
            ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST)
                                          .sendAsync()
                                          .get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Quorum connection error!");
            throw new QuorumConnectionErrorException();
        }

        return Objects.requireNonNull(ethGetTransactionCount).getTransactionCount();
    }

    String getAddressFromDid(String did) {
        did = did.toLowerCase();
        return did.replace("did:tw:", "0x");
    }
}
