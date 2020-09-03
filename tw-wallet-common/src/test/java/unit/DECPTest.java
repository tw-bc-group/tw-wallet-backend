package unit;

import com.thoughtworks.common.crypto.CryptoFacade;
import com.thoughtworks.common.crypto.Curve;
import com.thoughtworks.common.crypto.Digest;
import com.thoughtworks.common.crypto.SignatureScheme;
import com.thoughtworks.common.util.dcep.DCEPUtil;
import com.thoughtworks.common.util.dcep.MoneyType;
import com.thoughtworks.common.util.dcep.StringBytesConvert;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DECPTest {

    // 人命币的格式
    String rmb100 = "CB_100_00_1000000000000000000001";
    private static final String privateKey = "e6181caaffff94a09d7e332fc8da9884d99902c7874eb74354bdcadf411929f1";
    private static final String publicKey = "02d404fd50a69fc33003943b438cd63440bc65f7f10ea1a7f2cf0dd2bd620b80d6";


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
}
