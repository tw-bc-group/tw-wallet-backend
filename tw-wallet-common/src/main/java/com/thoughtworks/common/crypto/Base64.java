package com.thoughtworks.common.crypto;

import java.io.UnsupportedEncodingException;

/**
 * Base64工具
 */
public class Base64 {

    public static String encode(String str) throws UnsupportedEncodingException {
        byte[] encodeBytes = java.util.Base64.getEncoder().encode(str.getBytes("utf-8"));
        return new String(encodeBytes);
    }

    public static String encode(byte[] src) throws UnsupportedEncodingException {
        byte[] encodeBytes = java.util.Base64.getEncoder().encode(src);
        return new String(encodeBytes);
    }

    public static String decode(String str) throws UnsupportedEncodingException {
        byte[] decodeBytes = java.util.Base64.getDecoder().decode(str.getBytes("utf-8"));
        return new String(decodeBytes);
    }

    public static byte[] decodeAndReturnBytes(String str) throws UnsupportedEncodingException {
        byte[] decodeBytes = java.util.Base64.getDecoder().decode(str.getBytes("utf-8"));
        return decodeBytes;
    }

}
