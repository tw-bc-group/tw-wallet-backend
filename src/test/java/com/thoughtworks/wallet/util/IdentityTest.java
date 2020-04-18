package com.thoughtworks.wallet.util;

import org.junit.jupiter.api.Test;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Sign;

import java.math.BigInteger;

import static com.thoughtworks.wallet.util.Identity.verifySignature;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.web3j.utils.Numeric.hexStringToByteArray;

class IdentityTest {
    @Test
    void test_verify_signatures() {
        Sign.SignatureData signatureData = new Sign.SignatureData((byte) 0x01,
            hexStringToByteArray("0xe263a5f24a079e173dfc93646abd9de5e17d5c63abd033951e3f4bd2fc8bc6b3"),
            hexStringToByteArray("0x7e0bce6bea1d27f826d1e3b495579d4267d84124dbb8412b66ebe9f0185b7699"));

        ECDSASignature sig = new ECDSASignature(
            new BigInteger(1, signatureData.getR()),
            new BigInteger(1, signatureData.getS()));
        String address = "0xcA843569e3427144cEad5e4d5999a3D0cCF92B8e";
        byte[] messageHash = hexStringToByteArray("0x89be3a61d10284335a3cd17552ca5eea618b7f8975479f30156fb807244af5ca");

        assertThat(verifySignature(sig, address, messageHash)).isTrue();
    }
}