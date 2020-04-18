package com.thoughtworks.wallet.util;

import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;

import java.util.Arrays;
import java.util.Objects;

import static java.lang.String.format;

public class Identity {

    private static final String VALID_ADDRESS_FORMAT = "^0x[0-9a-fA-F]{40}$";

    public static boolean verifySignature(ECDSASignature sig, String address, byte[] messageHash) {
        return Arrays.stream(new Byte[]{0, 1, 2, 3})
            .map(recoverId -> Sign.recoverFromSignature(recoverId, sig, messageHash))
            .filter(Objects::nonNull)
            .map(publicKey -> format("0x%s", Keys.getAddress(publicKey)))
            .anyMatch(addressRecovered -> addressRecovered.equalsIgnoreCase(address));
    }

    public static boolean isValidAddress(String addr) {
        return addr.matches(VALID_ADDRESS_FORMAT);
    }
}
