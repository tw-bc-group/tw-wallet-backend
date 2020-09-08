package com.thoughtworks.common.util.dcep;

import com.thoughtworks.common.crypto.*;
import org.apache.commons.lang3.RandomUtils;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Bytes;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DCEPUtil {
    private static final BigInteger range = (new BigInteger("10")).pow(21);

    public static BigInteger serialNumber(MoneyType moneyType) {
        long next = RandomUtils.nextLong(1, Long.MAX_VALUE);
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


    public static String getBankSignWithWeb3j(String serialNumber, String privateKey) throws Exception {

        String hash = Hash.sha3(Numeric.toHexStringNoPrefix(serialNumber.getBytes(StandardCharsets.UTF_8)));
        byte[] data = hash.getBytes();
        // Sign the Transaction
        Sign.SignatureData signedMessage = Sign.signMessage(
                data,
                Credentials.create(privateKey).getEcKeyPair()
        );
        byte[] encode = encode(serialNumber, signedMessage);
        String hexValue = Numeric.toHexString(encode);
        return hexValue;
    }

    public static final String MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    public static byte[] getEthereumMessagePrefix(int messageLength) {
        return MESSAGE_PREFIX.concat(String.valueOf(messageLength)).getBytes();
    }

    public static byte[] getEthereumMessageHash(byte[] message) {
        byte[] prefix = getEthereumMessagePrefix(message.length);

        byte[] result = new byte[prefix.length + message.length];
        System.arraycopy(prefix, 0, result, 0, prefix.length);
        System.arraycopy(message, 0, result, prefix.length, message.length);

        return Hash.sha3(result);
    }

    /**
     * 和web3js兼容的签名算法
     *
     * @param message
     * @param privateKey
     * @return
     */
    public static String sign(String message, String privateKey) {
        byte[] hash = message.getBytes(StandardCharsets.UTF_8);
        Sign.SignatureData signature = Sign.signPrefixedMessage(hash, Credentials.create(privateKey).getEcKeyPair());
        String r = Numeric.toHexString(signature.getR());
        String s = Numeric.toHexString(signature.getS()).substring(2);
        String v = Numeric.toHexString(signature.getV()).substring(2);
        return new StringBuilder(r)
                .append(s)
                .append(v)
                .toString();
    }

    /**
     * Create base64 encoded signature using SHA256/RSA.
     *
     * @param message
     * @param privateKey private key with base64 encoded
     * @return
     * @throws Exception
     */
    public static String signWithSHA256RSA(String message, String privateKey) throws Exception {
        return RSA.signSHA256RSA(message, privateKey);
    }

    private static byte[] encode(String serialNumber, Sign.SignatureData signatureData) {
        List<RlpType> values = asRlpValues(serialNumber, signatureData, false);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }

    private static List<RlpType> asRlpValues(
            String serialNumber, Sign.SignatureData signatureData, boolean withMessage) {
        List<RlpType> result = new ArrayList<>();

        if (withMessage)
            result.add(RlpString.create(serialNumber));

        if (signatureData != null) {
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getV())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getR())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getS())));
        }

        return result;
    }


}
