package unit;

import com.thoughtworks.common.crypto.ByteHelper;
import com.thoughtworks.common.crypto.CryptoFacade;
import com.thoughtworks.common.crypto.Digest;
import com.thoughtworks.common.crypto.SignatureScheme;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SignDataTest {


    @Test
    void givenEthSignature_WhenVerify_ThenReturnSuccess() throws Exception {
        //generate privateKey and publicKey by web3.js and ethers.js
        String       publicKey     = "038773a46bc5a2bb1c5687de4788a7d58df3f27483687c8df81d07350753161e05";
        String       privateKey    = "4762e04d10832808a0aebdaa79c12de54afbe006bfffd228b3abcc494fe986f9";
        String       reqJson       = "{\"msg_id\":\"111\",\"data\":{\"coin_type\":\"ETH\",\"address\":\"123566777\"}}";
        byte[]       hashData      = Digest.hash256(reqJson.getBytes());
        String       hash          = ByteHelper.toHexString(hashData);
        CryptoFacade sign          = new CryptoFacade(privateKey, SignatureScheme.SHA256WITHECDSA);
        String       publicKeySign = ByteHelper.toHexString(sign.serializePublicKey());
        String       signature     = sign.generateSignature(hashData);
        CryptoFacade sign1         = new CryptoFacade(publicKey);
        CryptoFacade sign2         = new CryptoFacade(publicKeySign);
        boolean      b             = sign.verifySignature(hash, signature);
        boolean      b1            = sign1.verifySignature(hash, signature);
        boolean      b2            = sign2.verifySignature(hash, signature);
        assertThat(new boolean[]{b, b1, b2}).doesNotContain(false);
    }
}
