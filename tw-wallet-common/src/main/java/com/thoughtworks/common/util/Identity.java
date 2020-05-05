package com.thoughtworks.common.util;

import com.thoughtworks.common.exception.ErrorSendTransactionException;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.SignedRawTransaction;
import org.web3j.crypto.TransactionDecoder;

import java.security.SignatureException;

public class Identity {

    private static final String VALID_ADDRESS_FORMAT = "^0x[0-9a-fA-F]{40}$";

    public static boolean verifySignature(String signedTransactionData, String address) {

        RawTransaction tx = TransactionDecoder.decode(signedTransactionData);

        if (!(tx instanceof SignedRawTransaction)) {
            throw new ErrorSendTransactionException("Wrong raw transaction data");
        }

        try {
            final String fromAddress = ((SignedRawTransaction) tx).getFrom();
            return address.equalsIgnoreCase(fromAddress);
        } catch (SignatureException e) {
            throw new ErrorSendTransactionException("Can not verify your signature.");
        }
    }

    public static boolean isValidAddress(String addr) {
        return addr.matches(VALID_ADDRESS_FORMAT);
    }
}
