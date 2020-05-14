package com.thoughtworks.wallet.unit;

import org.junit.jupiter.api.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.security.MessageDigest;

import static com.thoughtworks.common.util.Identity.verifySignature;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SignDataTest {

    @Test
    void test_decode_tx() {
        String privateKey1 = "4762e04d10832808a0aebdaa79c12de54afbe006bfffd228b3abcc494fe986f9";
        Credentials credentials = Credentials.create(privateKey1);
        String testString = "TEST";//"\u0019Ethereum Signed Message:\n" + "TEST".length() + "TEST";

//        byte[] encodedTransaction = encode(testString);
//        Sign.SignatureData signatureData = Sign.signMessage(
//                encodedTransaction, credentials.getEcKeyPair());
//         encode(rawTransaction, signatureData);
//
//        //Sign.SignatureData signatureData = Sign.signMessage(testString.getBytes(), credentials.getEcKeyPair());
//        System.out.println("R: " + Numeric.toHexString(signatureData.getR()));
//        System.out.println("S: " + Numeric.toHexString(signatureData.getS()));
//        System.out.println("V: " + signatureData.getV());
//
//        assertThat(verifySignature(rawTx, address)).isTrue();
    }
}
