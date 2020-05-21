package com.thoughtworks.wallet.healthy.utils;

import com.thoughtworks.common.exception.ErrorSendTransactionException;
import com.thoughtworks.wallet.healthy.exception.QuorumConnectionErrorException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class QuorumUtils {
    private final Web3j web3j;
    private static final int SLEEP_DURATION = 15000;
    private static final int ATTEMPTS = 40;

    public QuorumUtils(Web3j web3j) {
        this.web3j = web3j;
    }

    public BigInteger getNonce(String address) {
        EthGetTransactionCount ethGetTransactionCount;
        try {
            ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST)
                                          .sendAsync()
                                          .get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Quorum connection error!");
            throw new QuorumConnectionErrorException();
        }

        return Objects.requireNonNull(ethGetTransactionCount).getTransactionCount();
    }

    public TransactionReceipt waitForTransactionReceipt(String transactionHash) {
        Optional<TransactionReceipt> transactionReceiptOptional;
        try {
            transactionReceiptOptional = getTransactionReceipt(transactionHash, SLEEP_DURATION, ATTEMPTS);
        } catch (Exception e) {
            log.error("Transaction receipt not generated after " + ATTEMPTS + " attempts");
            throw new ErrorSendTransactionException(e.getLocalizedMessage());
        }

        if (!transactionReceiptOptional.isPresent()) {
            log.error("Transaction receipt not generated after " + ATTEMPTS + " attempts");
            throw new ErrorSendTransactionException("Transaction receipt not generated.");
        }

        return transactionReceiptOptional.get();
    }

    @NotNull
    public EthSendTransaction sendRawSignedTransaction(String rawTx) {
        EthSendTransaction transactionResponse;
        try {
            transactionResponse = web3j.ethSendRawTransaction(rawTx).send();
            if (transactionResponse.hasError()) {
                throw new ErrorSendTransactionException(transactionResponse.getError().getMessage());
            }
        } catch (IOException e) {
            log.error("Cannot send transaction", e);
            throw new ErrorSendTransactionException(e.getMessage());
        }
        return transactionResponse;
    }

    private Optional<TransactionReceipt> getTransactionReceipt(
        String transactionHash, int sleepDuration, int attempts) throws Exception {

        Optional<TransactionReceipt> receiptOptional =
            sendTransactionReceiptRequest(transactionHash);
        for (int i = 0; i < attempts; i++) {
            if (!receiptOptional.isPresent()) {
                Thread.sleep(sleepDuration);
                receiptOptional = sendTransactionReceiptRequest(transactionHash);
            } else {
                break;
            }
        }

        return receiptOptional;
    }

    private Optional<TransactionReceipt> sendTransactionReceiptRequest(String transactionHash)
        throws Exception {
        EthGetTransactionReceipt transactionReceipt =
            web3j.ethGetTransactionReceipt(transactionHash).sendAsync().get();

        return transactionReceipt.getTransactionReceipt();
    }
}
