
package com.thoughtworks.common.crypto;

import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.spec.SM2ParameterSpec;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.ECPointUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.util.Strings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Arrays;


public class CryptoFacade {
    private KeyType         keyType;
    private Object[]        curveParams;
    private PrivateKey      privateKey;
    private PublicKey       publicKey;
    private SignatureScheme signatureScheme;
    private Curve           curve;

    /**
     * 用静态函数创建
     */
    private CryptoFacade(boolean fromPrivateKey, String key, SignatureScheme scheme, Curve curve) throws Exception {
        if (scheme == SignatureScheme.SM3WITHSM2) {
            this.keyType = KeyType.SM2;
            this.curveParams = new Object[]{curve.toString()};
        } else if (scheme == SignatureScheme.SHA256WITHECDSA) {
            this.keyType = KeyType.ECDSA;
            this.curveParams = new Object[]{curve.toString()};
        }
        this.signatureScheme = scheme;
        this.curve = curve;

        if (fromPrivateKey) {
            byte[] privateKey = ByteHelper.hexToBytes(key);
            Security.addProvider(new BouncyCastleProvider());

            switch (scheme) {
                case SHA256WITHECDSA:
                case SM3WITHSM2:
                    BigInteger d = new BigInteger(1, privateKey);

                    ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec((String) this.curveParams[0]);
                    ECParameterSpec paramSpec = new ECNamedCurveSpec(spec.getName(), spec.getCurve(), spec.getG(), spec.getN());
                    ECPrivateKeySpec priSpec = new ECPrivateKeySpec(d, paramSpec);
                    KeyFactory kf = KeyFactory.getInstance("EC", "BC");
                    this.privateKey = kf.generatePrivate(priSpec);

                    org.bouncycastle.math.ec.ECPoint Q = spec.getG().multiply(d).normalize();
                    ECPublicKeySpec pubSpec = new ECPublicKeySpec(
                            new ECPoint(Q.getAffineXCoord().toBigInteger(), Q.getAffineYCoord().toBigInteger()),
                            paramSpec);
                    this.publicKey = kf.generatePublic(pubSpec);
                    break;
                default:
                    throw new CryptoException(CryptoError.UnsupportedKeyType);
            }
        } else {
            byte[] publicKeyBytes = ByteHelper.hexToBytes(key);
            Security.addProvider(new BouncyCastleProvider());
            parsePublicKey(publicKeyBytes);
        }
    }

    /**
     * 公钥验证
     *
     * @param publicKey
     * @throws Exception
     */
    static public CryptoFacade fromPublicKey(String publicKey, SignatureScheme scheme, Curve curve) throws Exception {
        return new CryptoFacade(false, publicKey, scheme, curve);
    }

    /**
     * 私钥生成公私钥对，可以签名
     *
     * @param privateKeyString
     * @param scheme
     * @param curve
     * @return
     * @throws Exception
     */
    static public CryptoFacade fromPrivateKey(String privateKeyString, SignatureScheme scheme, Curve curve) throws Exception {
        return new CryptoFacade(true, privateKeyString, scheme, curve);
    }

    private void parsePublicKey(byte[] data) throws CryptoException, InvalidKeySpecException, NoSuchProviderException, NoSuchAlgorithmException {
        if (data == null) {
            throw new CryptoException(CryptoError.NullInput);
        } else if (data.length < 2) {
            throw new CryptoException(CryptoError.InvalidData);
        } else {

            this.privateKey = null;
            this.publicKey = null;
            switch (this.keyType) {
                case ECDSA:
                case SM2:
                    this.keyType = KeyType.ECDSA;
                    this.curveParams = new Object[]{curve.toString()};
                    ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec(curve.toString());
                    ECParameterSpec param = new ECNamedCurveSpec(spec.getName(), spec.getCurve(), spec.getG(), spec.getN());
                    ECPublicKeySpec pubSpec = new ECPublicKeySpec(ECPointUtil.decodePoint(param.getCurve(), Arrays.copyOfRange(data, 0, data.length)), param);
                    KeyFactory kf = KeyFactory.getInstance("EC", "BC");
                    this.publicKey = kf.generatePublic(pubSpec);
                    break;
                default:
                    throw new CryptoException(CryptoError.UnknownKeyType);
            }

        }
    }


    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }


    public String generateSignature(String msg) throws Exception {
        return generateSignature(msg.getBytes(StandardCharsets.UTF_8), null);
    }

    public String generateSignature(byte[] msg, Object sm2param) throws Exception {
        if (msg == null || msg.length == 0) {
            throw new CryptoException(CryptoError.InvalidMessage);
        }
        if (this.privateKey == null) {
            throw new CryptoException(CryptoError.WithoutPrivate);
        }

        SignatureHandler       ctx       = new SignatureHandler(keyType, signatureScheme);
        AlgorithmParameterSpec paramSpec = null;
        if (signatureScheme == SignatureScheme.SM3WITHSM2) {
            if (sm2param instanceof String) {
                paramSpec = new SM2ParameterSpec(Strings.toByteArray((String) sm2param));
            } else if (sm2param == null) {
                paramSpec = new SM2ParameterSpec("1011111111111111".getBytes());
            } else {
                throw new CryptoException(CryptoError.InvalidSM2Signature);
            }
        }
        byte[] signature = new Signature(signatureScheme, paramSpec, ctx.generateSignature(privateKey, msg, paramSpec)).toBytes();
        return ByteHelper.toHexString(signature);
    }

    public boolean verifySignature(String msg, String signature) throws Exception {

        byte[] msgBytes       = msg.getBytes(StandardCharsets.UTF_8);
        byte[] signatureBytes = ByteHelper.hexToBytes(signature);

        if (msgBytes == null || signatureBytes == null || msgBytes.length == 0 || signatureBytes.length == 0) {
            throw new CryptoException(CryptoError.InvalidParams);
        }
        if (this.publicKey == null) {
            throw new CryptoException(CryptoError.WithoutPublicKey);
        }
        Signature        sig = new Signature(signatureBytes);
        SignatureHandler ctx = new SignatureHandler(keyType, sig.getScheme());
        return ctx.verifySignature(publicKey, msgBytes, sig.getValue());
    }

    public byte[] serializePublicKey() throws CryptoException, IOException {
        ByteArrayOutputStream bs  = new ByteArrayOutputStream();
        BCECPublicKey         pub = (BCECPublicKey) publicKey;
        switch (this.keyType) {
            case ECDSA:
            case SM2:
                bs.write(pub.getQ().getEncoded(true));
                break;
            default:
                // Should not reach here
                throw new CryptoException(CryptoError.UnknownKeyType);
        }

        return bs.toByteArray();
    }
}
