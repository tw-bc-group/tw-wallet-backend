package com.thoughtworks.common.util.dcep;

import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Collections;

public class StringBytesConvert {
    // String to 64 length HexString (equivalent to 32 Hex lenght)
    public static String asciiToHex(String asciiValue) {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        return hex.toString() + "".join("", Collections.nCopies(32 - (hex.length() / 2), "00"));
    }

    public static Bytes32 HextoBytes32(String hexValue) {
        byte[] myStringInByte = Numeric.hexStringToByteArray(hexValue);
        Bytes32 myStringInBytes32 = new Bytes32(myStringInByte);
        return myStringInBytes32;
    }

    public static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }

    public static String hexToAscii(BigInteger hex) {
        return StringBytesConvert.hexToAscii(hex.toString(16));
    }

    public static Bytes32 StringToBytes32(String asciiValue) {
        return HextoBytes32(asciiToHex(asciiValue));
    }
}
