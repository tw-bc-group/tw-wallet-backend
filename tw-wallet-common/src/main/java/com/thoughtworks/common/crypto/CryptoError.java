package com.thoughtworks.common.crypto;

import com.thoughtworks.common.util.JacksonUtil;

import java.util.HashMap;
import java.util.Map;

public class CryptoError {

    public static String getError(int code, String msg) {
        Map map = new HashMap();
        map.put("Code", code);
        map.put("Message", msg);
        return JacksonUtil.beanToJSonStr(map);
    }

    public static String ChecksumNotValidate        = getError(10000, "Base58 Error,Checksum does not validate");
    public static String InputTooShort              = getError(10001, "Base58 Error,Input too short");
    public static String UnknownCurve               = getError(10002, "Curve Error,unknown curve");
    public static String UnknownCurveLabel          = getError(10003, "Curve Error,unknown curve label");
    public static String UnknownAsymmetricKeyType   = getError(10003, "keyType Error,unknown asymmetric key type");
    public static String InvalidSignatureData       = getError(10005, "Signature Error,invalid signature data: missing the ID parameter for SM3withSM2");
    public static String InvalidSignatureDataLen    = getError(10006, "Signature Error,invalid signature data length");
    public static String MalformedSignature         = getError(10007, "Signature Error,malformed signature");
    public static String UnsupportedSignatureScheme = getError(10008, "Signature Error,unsupported signature scheme:");
    public static String ParamError                 = getError(10009, "Param error,");
    public static String UnknownKeyType             = getError(10010, "Unknown key type");
    public static String InvalidParams              = getError(10011, "Invalid params");
    public static String UnsupportedKeyType         = getError(10012, "Unsupported key type");
    public static String InvalidMessage             = getError(10013, "Invalid message");
    public static String WithoutPrivate             = getError(10014, "Without private key cannot generate signature");
    public static String InvalidSM2Signature        = getError(10015, "Invalid SM2 signature parameter, ID (String) excepted");
    public static String WithoutPublicKey           = getError(10016, "Without public key cannot verify signature");
    public static String NullInput                  = getError(10017, "Null input");
    public static String InvalidData                = getError(10018, "Invalid data");
}
