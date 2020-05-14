
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

    public static final X9ECParameters     CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1");
    static final        ECDomainParameters CURVE        =
            new ECDomainParameters(
                    CURVE_PARAMS.getCurve(),
                    CURVE_PARAMS.getG(),
                    CURVE_PARAMS.getN(),
                    CURVE_PARAMS.getH());

//    static X9ECParametersHolder secp256k1 = new X9ECParametersHolder()
//    {
//        protected X9ECParameters createParameters()
//        {
//            byte[] S = null;
//            GLVTypeBParameters glv = new GLVTypeBParameters(
//                    new BigInteger("7ae96a2b657c07106e64479eac3434e99cf0497512f58995c1396c28719501ee", 16),
//                    new BigInteger("5363ad4cc05c30e0a5261c028812645a122e22ea20816678df02967c1b23bd72", 16),
//                    new ScalarSplitParameters(
//                            new BigInteger[]{
//                                    new BigInteger("3086d221a7d46bcde86c90e49284eb15", 16),
//                                    new BigInteger("-e4437ed6010e88286f547fa90abfe4c3", 16) },
//                            new BigInteger[]{
//                                    new BigInteger("114ca50f7a8e2f3f657c1108d9d44cfd8", 16),
//                                    new BigInteger("3086d221a7d46bcde86c90e49284eb15", 16) },
//                            new BigInteger("3086d221a7d46bcde86c90e49284eb153dab", 16),
//                            new BigInteger("e4437ed6010e88286f547fa90abfe4c42212", 16),
//                            272));
//            ECCurve curve = configureCurveGLV(new SecP256K1Curve(), glv);
//            X9ECPoint G = configureBasepoint(curve,
//                    "0479BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8");
//            return new X9ECParameters(curve, G, curve.getOrder(), curve.getCofactor(), S);
//        }
//    };

//    private static ECNamedCurveParameterSpec getParameterSpec(String curveName, boolean custom) {
//        if (custom) {
//            X9ECParameters ecP = CustomNamedCurves.getByName(curveName);
//            ECParameterSpec ecParameterSpec = new ECParameterSpec(
//                    ecP.getCurve(),
//                    ecP.getG(),
//                    ecP.getN(),
//                    ecP.getH(),
//                    ecP.getSeed());
//            return ecParameterSpec;
//
//        }
//        return ECNamedCurveTable.getParameterSpec(curveName);
//    }

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
