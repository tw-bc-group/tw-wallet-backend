package unit;

import com.thoughtworks.common.crypto.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SignDataTest {

    String longPublicKey = "0x8773a46bc5a2bb1c5687de4788a7d58df3f27483687c8df81d07350753161e05741eafdcfa7f0e1728afc8d4c383aff5b69f886921d13e2c1be77c694fc3362f";
    String publicKey     = "038773a46bc5a2bb1c5687de4788a7d58df3f27483687c8df81d07350753161e05";
    String privateKey    = "4762e04d10832808a0aebdaa79c12de54afbe006bfffd228b3abcc494fe986f9";
    String reqJson       = "{\"msg_id\":\"111\",\"data\":{\"coin_type\":\"ETH\",\"address\":\"123566777\"}}";

    /**
     * 1. generate privateKey and publicKey by web3.js and ethers.js.
     * 2. use CryptoFacade to sign any data.
     * 3. use publicKey of Eth to verify.
     *
     * @throws Exception
     */
    @Test
    void givenEthSignature_WhenVerify_ThenReturnSuccess() throws Exception {
        CryptoFacade sign          = CryptoFacade.fromPrivateKey(privateKey, SignatureScheme.SHA256WITHECDSA, Curve.SECP256K1);
        String       publicKeySign = ByteHelper.toHexString(sign.serializePublicKey());
        String       signature     = sign.generateSignature(reqJson);
        CryptoFacade sign1         = CryptoFacade.fromPublicKey(publicKey, SignatureScheme.SHA256WITHECDSA, Curve.SECP256K1);
        CryptoFacade sign2         = CryptoFacade.fromPublicKey(publicKeySign, SignatureScheme.SHA256WITHECDSA, Curve.SECP256K1);
        boolean      b             = sign.verifySignature(reqJson, signature);
        boolean      b1            = sign1.verifySignature(reqJson, signature);
        boolean      b2            = sign2.verifySignature(reqJson, signature);
        assertThat(new boolean[]{b, b1, b2}).doesNotContain(false);
        assertThat(publicKey).isEqualTo(publicKeySign);
    }

    @Test
    void givenP256Signature_WhenVerify_ThenReturnSuccess() throws Exception {
        CryptoFacade sign            = CryptoFacade.fromPrivateKey(privateKey, SignatureScheme.SHA256WITHECDSA, Curve.P256);
        String       publicKeySign   = ByteHelper.toHexString(sign.serializePublicKey());
        String       signature       = sign.generateSignature(reqJson);
        CryptoFacade sign1           = CryptoFacade.fromPublicKey(publicKey, SignatureScheme.SHA256WITHECDSA, Curve.P256);
        CryptoFacade sign2           = CryptoFacade.fromPublicKey(publicKeySign, SignatureScheme.SHA256WITHECDSA, Curve.P256);
        boolean      signOfFacade    = sign.verifySignature(reqJson, signature);
        boolean      signOfEth       = sign1.verifySignature(reqJson, signature);
        boolean      signOfSerialize = sign2.verifySignature(reqJson, signature);
        assertThat(new boolean[]{signOfFacade, signOfSerialize}).doesNotContain(false);

        // signOfEth use eth public key, but we use P256 here
        assertThat(signOfEth).isFalse();
        assertThat(publicKey).isNotEqualTo(publicKeySign);
    }
}
