package com.thoughtworks.common.crypto;

import lombok.extern.slf4j.Slf4j;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Slf4j
public class RSA {

    /**
     * Create base64 encoded signature using SHA256/RSA.
     * @param input
     * @param strPk private key with base64 encoded
     * @return
     * @throws Exception
     */
    public static String signSHA256RSA(String input, String strPk) throws Exception {

        //decode the private key from base64
        byte[] b1 = Base64.decodeAndReturnBytes(strPk);
        //get PrivateKey Object from byte[]
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b1);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey priv = kf.generatePrivate(spec);
        //generate signature
        //choose the algorithm
        Signature signature = Signature.getInstance("SHA256withRSA");
        //init the private key
        signature.initSign(priv);
        //update the data which is going to be signed
        signature.update(input.getBytes());
        //sign the data
        byte[] s = signature.sign();
        //return the signature with base64 encodeed
        return Base64.encode(s);
    }

    /**
     * verify signature
     * @param data original data
     * @param b64sig Signature with baes64 encoded
     * @param b64pubkey public key with base64 encoded
     * @return
     */
    public static boolean verify(String data, String b64sig, String b64pubkey) {
//        //original data
//        String data = "hello world";
//        //Signature with baes64 encoded
//        String b64sig = "Oebbp754ozaX+/539cRYomsZSOVhZ8L7NcNbLIw+hUWn0HMLvYjmK/B78ixMMQD+vk1zB6RRo2kyYeqoWka+FQ==";
//        //public key with base64 encoded
//        String b64pubkey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAI5SXpw1SSsM3FN43JVKn4gb+oGXfjL7rCDluqydAyHZ8vV7ySqi8oM1CoHRC9U2ST7IldydsQ+4cjC9xfzexxcCAwEAAQ==";
        boolean ret = false;
        try {

            //transform the base64 string to byte[]
            byte[] pk = Base64.decodeAndReturnBytes(b64pubkey);
            byte[] sig = Base64.decodeAndReturnBytes(b64sig);
            //get PublicKey Object from byte[]
            X509EncodedKeySpec spec = new X509EncodedKeySpec(pk);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey pubKey = null;
            pubKey = kf.generatePublic(spec);

            //verify the Signature
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(pubKey);
            signature.update(data.getBytes());
            ret = signature.verify(sig);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ret;
    }
}
