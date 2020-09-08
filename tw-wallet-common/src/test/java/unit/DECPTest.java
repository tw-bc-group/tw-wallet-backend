package unit;

import com.thoughtworks.common.crypto.*;
import com.thoughtworks.common.util.dcep.DCEPUtil;
import com.thoughtworks.common.util.dcep.MoneyType;
import com.thoughtworks.common.util.dcep.StringBytesConvert;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DECPTest {

    // 人命币的格式
    String rmb100 = "CB_100_00_1000000000000000000001";
    String hello = "hello world";
    private static final String privateKey = "e5e2a5f8e8f786b61a08af8770afe9a3f5bc3fa7cce000ec932372b6732fe018";
    private static final String publicKey = "03e0e8fe3eecbaf06def783231626ae80f43131e39b5bccab02698afd387909ee2";


//    ➜ tw-eth-cli inspect -k 0xe5e2a5f8e8f786b61a08af8770afe9a3f5bc3fa7cce000ec932372b6732fe018
//    {
//        "publicKey": "0x04e0e8fe3eecbaf06def783231626ae80f43131e39b5bccab02698afd387909ee224cccb30e916d7432b966a8e9ec5e4074df1f3c02da89c78d4761d12854600dd",
//        "compressedPublicKey": "0x03e0e8fe3eecbaf06def783231626ae80f43131e39b5bccab02698afd387909ee2",
//        "address": "0xa06AfBa6A830E5ba4f377bA7364b6ffec86e6AB7"
//    }

    @Test
    void should_generate_serial_number_success() throws Exception {
        BigInteger serialNumber = DCEPUtil.serialNumber(MoneyType.RMB000_10);
        assertThat(StringBytesConvert.hexToAscii(serialNumber.toString(16)).startsWith("CB_100_00_"));
    }

    @Test
    void should_get_bank_signature_success() throws Exception {
        String serialNumberSign = DCEPUtil.getBankSign(rmb100, privateKey);
        CryptoFacade sign = CryptoFacade.fromPublicKey(publicKey, SignatureScheme.SHA256WITHECDSA, Curve.SECP256K1);
        byte[] bytes = Digest.hash256(rmb100.getBytes(StandardCharsets.UTF_8));
        boolean verifySignature = sign.verifySignature(bytes, serialNumberSign);
        assertThat(verifySignature).isTrue();
    }

    // TODO: 张诚的方法测试通不过
    @Test
    void should_get_bank_signature_with_web3j_success() throws Exception {
        String data = "hello world";
        byte[] sha3 = Hash.sha3(data.getBytes(StandardCharsets.UTF_8));
        assertThat(Numeric.toHexString(sha3)).isEqualTo("0x47173285a8d7341e5e972fc677286384f802f8ef42a5ec5f03bbfa254cb01fad");
        String serialNumberSign = DCEPUtil.getBankSignWithWeb3j(data, privateKey);
        assertThat(serialNumberSign).isNotEqualTo("0xf843a05726cd86dab097cfe32d2369fa5571def6809eeaf46d536d173ef4d128e1e1afa02b3d1bf67a581ce2c5040b9d3d824c65fae1aab890a2de06ee9bea882ba5f1b31c");
    }


    @Test
    void should_get_bank_signature_with_web3j_without_rlp_success() throws Exception {
        byte[] ethereumMessageHash = DCEPUtil.getEthereumMessageHash(hello.getBytes(StandardCharsets.UTF_8));
        assertThat(Numeric.toHexString(ethereumMessageHash)).isEqualTo("0xd9eba16ed0ecae432b71fe008c98cc872bb4cc214d3220a36f365326cf807d68");
        String sign = DCEPUtil.sign(hello, privateKey);
        // signature is generate by `tw-eth-cli sign -m "hello world" -k e5e2a5f8e8f786b61a08af8770afe9a3f5bc3fa7cce000ec932372b6732fe018`
        assertThat(sign).isEqualTo("0x03325eb8d7617e10a6fa106a65284c438bf6529016c64e84af158a62aac285295e810f0823a094ec65cd4ada8bb85e605c59de327ba408bf8f33fce5b3faf9bd1b");
    }

    @Test
    void should_get_bank_signature_with_rsa() throws Exception {

        String b64PrivatekeyRSA = "MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAjlJenDVJKwzcU3jclUqfiBv6gZd+MvusIOW6rJ0DIdny9XvJKqLygzUKgdEL1TZJPsiV3J2xD7hyML3F/N7HFwIDAQABAkBpj3yu/fLcJHjSzMVHUgcLrDzcm+G1rOeZqusPlpG5K9PdSP2epVpcQOlL+rUpSq9ZIHq80kj9sEuWzjAaoXWJAiEAzS4GV9yuY7HXnXoncn0M3IucKiNoUyyFk6HwmPR1/5UCIQCxkqmnwx6dwXgbwfXMqUsUXEGA3Ja602h/L5xC0Oxw+wIgKMmRcT3pXOApoKB73wKE4ALQ1H+daYtcMnDqxz0xh9ECIHUPDP5F13IBry8FPwdK9u6WHqxXPposcW+esDEvzx5vAiAjhMh+ld+hVzQNfc64etsCBX9PZPUoBUSis7xq9kMnVA==";
        //public key with base64 encoded
        String b64pubkey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAI5SXpw1SSsM3FN43JVKn4gb+oGXfjL7rCDluqydAyHZ8vV7ySqi8oM1CoHRC9U2ST7IldydsQ+4cjC9xfzexxcCAwEAAQ==";
        String ethereumMessageHash = DCEPUtil.signWithSHA256RSA(hello, b64PrivatekeyRSA);
        boolean verify = RSA.verify(hello, ethereumMessageHash, b64pubkey);
        assertThat(verify).isTrue();
    }
}
