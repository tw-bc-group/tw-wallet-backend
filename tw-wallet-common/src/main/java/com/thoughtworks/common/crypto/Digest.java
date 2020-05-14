package com.thoughtworks.common.crypto;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class Digest {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] hash160(byte[] value) {
        return ripemd160(sha256(value));
    }

    public static byte[] hash256(byte[] value) {
        return sha256(sha256(value));
    }

    public static byte[] hash256(byte[] value, int offset, int length) {
        if (offset != 0 || length != value.length) {
            byte[] array = new byte[length];
            System.arraycopy(value, offset, array, 0, length);
            value = array;
        }
        return sha256(sha256(value));
    }

    public static byte[] ripemd160(byte[] value) {
        try {
            MessageDigest md = MessageDigest.getInstance("RipeMD160");
            return md.digest(value);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static byte[] sha256(byte[] value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(value);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static byte[] sha256(byte[] value, int offset, int length) {
        if (offset != 0 || length != value.length) {
            byte[] array = new byte[length];
            System.arraycopy(value, offset, array, 0, length);
            value = array;
        }
        return sha256(value);
    }

    public static byte[] hmacSha512(byte[] keyBytes, byte[] text) {
        HMac             hmac   = new HMac(new SHA512Digest());
        byte[]           resBuf = new byte[hmac.getMacSize()];
        CipherParameters pm     = new KeyParameter(keyBytes);
        hmac.init(pm);
        hmac.update(text, 0, text.length);
        hmac.doFinal(resBuf, 0);
        return resBuf;
    }

}
