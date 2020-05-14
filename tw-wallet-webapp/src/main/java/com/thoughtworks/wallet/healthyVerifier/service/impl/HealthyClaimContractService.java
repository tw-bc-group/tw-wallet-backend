package com.thoughtworks.wallet.healthyVerifier.service.impl;

import com.google.common.collect.ImmutableList;
import com.thoughtworks.wallet.healthyVerifier.exception.HealthyVerificationCreateFailedException;
import com.thoughtworks.wallet.healthyVerifier.model.HealthVerificationClaimContract;
import com.thoughtworks.wallet.healthyVerifier.utils.QuorumUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class HealthyClaimContractService {

    private static final BigInteger GAS_PRICE = BigInteger.ZERO;
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(3000000);

    private final QuorumUtils quorumUtils;
    private final HealthVerificationClaimContract healthVerificationClaimContract;

    public HealthyClaimContractService(QuorumUtils quorumUtils, HealthVerificationClaimContract healthVerificationClaimContract) {
        this.quorumUtils = quorumUtils;
        this.healthVerificationClaimContract = healthVerificationClaimContract;
    }

    public void createHealthVerification(String issuerAddress, String claimId, String ownerId, String issuerId) {
        Function createHealthVerificationFunction = createHealthVerificationFunction(new Address(issuerAddress), new Utf8String(claimId), new Utf8String(ownerId), new Utf8String(issuerId));

        String signedTx = generateSignedTx(createHealthVerificationFunction, healthVerificationClaimContract, issuerAddress);

        EthSendTransaction transactionResponse = quorumUtils.sendRawSignedTransaction(signedTx);

        final String transactionHash = transactionResponse.getTransactionHash();

        log.info("CreateHealthVerification transaction hash is {}.", transactionHash);

        TransactionReceipt transferTransactionReceipt = quorumUtils.waitForTransactionReceipt(transactionHash);
        List<Log> receiptLogs = Objects.requireNonNull(transferTransactionReceipt).getLogs();
        log.info("Receipt logs are: {}.", receiptLogs.toString());

        if (receiptLogs.isEmpty()) {
            throw new HealthyVerificationCreateFailedException(ownerId);
        }
    }

    private Function createHealthVerificationFunction(Address issuerAddress, Utf8String claimId, Utf8String ownerId, Utf8String issuerId) {
        return new Function(
            "createHealthVerification",
            ImmutableList.of(issuerAddress, claimId, ownerId, issuerId),
            Collections.singletonList(new TypeReference<Utf8String>() {
            }));
    }

    @NotNull
    private String generateSignedTx(Function function, HealthVerificationClaimContract verificationClaimContract, String issuerAddress) {
        final BigInteger nonce = quorumUtils.getNonce(issuerAddress);

        String encodedFunction = FunctionEncoder.encode(function);

        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, GAS_PRICE, GAS_LIMIT, verificationClaimContract.getAddress(), encodedFunction);

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, Credentials.create(verificationClaimContract.getIssuerPrivateKey()));

        return Numeric.toHexString(signedMessage);
    }
}
