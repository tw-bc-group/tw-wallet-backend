
package com.thoughtworks.common.crypto;

import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
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
import java.security.*;
import java.security.spec.*;
import java.util.Arrays;


public class CryptoFacade {
    private KeyType         keyType;
    private Object[]        curveParams;
    private PrivateKey      privateKey;
    private PublicKey       publicKey;
    private SignatureScheme signatureScheme;

    /**
     * 公钥验证走这边
     *
     * @param publicKey
     * @throws Exception
     */
    public CryptoFacade(String publicKey) throws CryptoException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
        byte[] publicKeyBytes = ByteHelper.hexToBytes(publicKey);

        Security.addProvider(new BouncyCastleProvider());
        parsePublicKey(publicKeyBytes);
    }

    //    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
//    ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
    private void parsePublicKey(byte[] data) throws CryptoException, InvalidKeySpecException, NoSuchProviderException, NoSuchAlgorithmException {
        if (data == null) {
            throw new CryptoException(CryptoError.NullInput);
        } else if (data.length < 2) {
            throw new CryptoException(CryptoError.InvalidData);
        } else {
            if (data.length == 33) {
                this.keyType = KeyType.ECDSA;
            } else if (data.length == 35) {
                this.keyType = KeyType.fromLabel(data[0]);
            }

            this.privateKey = null;
            this.publicKey = null;
            switch (this.keyType) {
                case ECDSA:
                    this.keyType = KeyType.ECDSA;
                    this.curveParams = new Object[]{Curve.SECP256K1.toString()};
                    ECNamedCurveParameterSpec spec0 = ECNamedCurveTable.getParameterSpec(Curve.SECP256K1.toString());
                    ECParameterSpec param0 = new ECNamedCurveSpec(spec0.getName(), spec0.getCurve(), spec0.getG(), spec0.getN());
                    ECPublicKeySpec pubSpec0 = new ECPublicKeySpec(ECPointUtil.decodePoint(param0.getCurve(), Arrays.copyOfRange(data, 0, data.length)), param0);
                    KeyFactory kf0 = KeyFactory.getInstance("ECDSA", "BC");
                    this.publicKey = kf0.generatePublic(pubSpec0);
                    break;
                case SM2:
                    Curve c = Curve.fromLabel(data[1]);
                    this.curveParams = new Object[]{c.toString()};
                    ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec(c.toString());
                    ECParameterSpec param = new ECNamedCurveSpec(spec.getName(), spec.getCurve(), spec.getG(), spec.getN());
                    ECPublicKeySpec pubSpec = new ECPublicKeySpec(ECPointUtil.decodePoint(param.getCurve(), Arrays.copyOfRange(data, 2, data.length)), param);
                    KeyFactory kf = KeyFactory.getInstance("EC", "BC");
                    this.publicKey = kf.generatePublic(pubSpec);
                    break;
                default:
                    throw new CryptoException(CryptoError.UnknownKeyType);
            }

        }
    }

    public CryptoFacade(String privateKeyString, SignatureScheme scheme) throws CryptoException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateKey = ByteHelper.hexToBytes(privateKeyString);
        Security.addProvider(new BouncyCastleProvider());
        signatureScheme = scheme;

        if (scheme == SignatureScheme.SM3WITHSM2) {
            this.keyType = KeyType.SM2;
            this.curveParams = new Object[]{Curve.SM2P256V1.toString()};
        } else if (scheme == SignatureScheme.SHA256WITHECDSA) {
            this.keyType = KeyType.ECDSA;
            this.curveParams = new Object[]{Curve.SECP256K1.toString()};
        }


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
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }


    public String generateSignature(byte[] msg) throws CryptoException, InvalidKeyException, NoSuchAlgorithmException, IOException, SignatureException, NoSuchProviderException, InvalidAlgorithmParameterException {
        return generateSignature(msg, null);
    }

    public String generateSignature(byte[] msg, Object sm2param) throws CryptoException, IOException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException, NoSuchProviderException, NoSuchAlgorithmException {
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

    public boolean verifySignature(String msg, String signature) throws CryptoException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, IOException, SignatureException {

        byte[] msgBytes       = ByteHelper.hexToBytes(msg);
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
                bs.write(pub.getQ().getEncoded(true));
                break;
            case SM2:
                bs.write(this.keyType.getLabel());
                bs.write(Curve.valueOf(pub.getParameters().getCurve()).getLabel());
                bs.write(pub.getQ().getEncoded(true));
                break;
            default:
                // Should not reach here
                throw new CryptoException(CryptoError.UnknownKeyType);
        }

        return bs.toByteArray();
    }
}
