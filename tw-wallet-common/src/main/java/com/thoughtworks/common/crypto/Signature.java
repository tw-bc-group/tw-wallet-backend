package com.thoughtworks.common.crypto;

import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.jcajce.spec.SM2ParameterSpec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

public class Signature {
    private SignatureScheme        scheme;
    private AlgorithmParameterSpec param;
    private byte[]                 value;

    public Signature(SignatureScheme scheme, AlgorithmParameterSpec param, byte[] signature) {
        this.scheme = scheme;
        this.param = param;
        this.value = signature;
    }

    public Signature(byte[] data) throws CryptoException {
        if (data == null) {
            throw new CryptoException(CryptoError.ParamError);
        }

        if (data.length < 2) {
            throw new CryptoException(CryptoError.InvalidSignatureDataLen);
        }

        this.scheme = SignatureScheme.values()[data[0]];
        this.value = Arrays.copyOfRange(data, 1, data.length);
    }

    public byte[] toBytes() {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        try {
            bs.write((byte) scheme.ordinal());
            if (scheme == SignatureScheme.SM3WITHSM2) {
                // adding the ID
                bs.write(((SM2ParameterSpec) param).getID());
                // padding a 0 as the terminator
                bs.write((byte) 0);
            }
            bs.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bs.toByteArray();
    }

    public SignatureScheme getScheme() {
        return scheme;
    }

    public byte[] getValue() {
        return this.value;
    }
}
