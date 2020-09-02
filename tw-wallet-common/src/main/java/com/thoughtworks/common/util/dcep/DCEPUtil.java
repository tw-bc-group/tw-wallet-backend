package com.thoughtworks.common.util.dcep;

import com.thoughtworks.common.crypto.CryptoFacade;
import com.thoughtworks.common.crypto.Curve;
import com.thoughtworks.common.crypto.Digest;
import com.thoughtworks.common.crypto.SignatureScheme;
import org.web3j.abi.datatypes.generated.Bytes32;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Random;
import org.apache.commons.lang3.RandomUtils;
public class DCEPUtil {
    private static final BigInteger range = (new BigInteger("10")).pow(21);

    public static BigInteger serialNumber(MoneyType moneyType) {
        long next = RandomUtils.nextLong(1,Long.MAX_VALUE);
        BigInteger indexRange = range.add(BigInteger.valueOf(next));
            String name = String.format("CB_%s_%d", moneyType.getMoneyTypeString(), indexRange);
        Bytes32 bytes32 = StringBytesConvert.StringToBytes32(name);
        BigInteger hexBig = new BigInteger(bytes32.getValue());
        return hexBig;
    }

    public static String getBankSign(String serialNumber, String privateKey) throws Exception {
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2, privateKey.length());
        }
        byte[] bytes = Digest.hash256(serialNumber.getBytes(StandardCharsets.UTF_8));
        CryptoFacade sign = CryptoFacade.fromPrivateKey(privateKey, SignatureScheme.SHA256WITHECDSA, Curve.SECP256K1);
        String signature = sign.generateSignature(bytes, null);
        return signature;
    }
}
