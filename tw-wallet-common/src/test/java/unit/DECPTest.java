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
    private static final String privateKey = "4762e04d10832808a0aebdaa79c12de54afbe006bfffd228b3abcc494fe986f9";

    @Test
    void should_generate_serial_number_success() throws Exception {
        BigInteger serialNumber = DCEPUtil.serialNumber(MoneyType.RMB100);
        assertThat(StringBytesConvert.hexToAscii(serialNumber.toString(16)).startsWith("CB_100_00_"));
    }

    @Test
    void should_get_bank_signature_success() throws Exception {
        String serialNumberSign = DCEPUtil.getBankSign(rmb100, privateKey);
        CryptoFacade sign = CryptoFacade.fromPrivateKey(privateKey, SignatureScheme.SHA256WITHECDSA, Curve.SECP256K1);
        byte[] bytes = Digest.hash256(rmb100.getBytes(StandardCharsets.UTF_8));
        boolean verifySignature = sign.verifySignature(bytes, serialNumberSign);
        assertThat(verifySignature).isTrue();
    }
}
