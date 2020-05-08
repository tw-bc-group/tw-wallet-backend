package com.thoughtworks.wallet.healthyVerifier.service;

import com.google.common.collect.ImmutableList;
import com.thoughtworks.wallet.healthyVerifier.model.HealthVerificationClaimContract;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;

import java.util.Collections;

@Slf4j
@Service
public class HealthyClaimContractService {

    private final Web3j web3j;

    private final HealthVerificationClaimContract healthVerificationClaimContract;

    public HealthyClaimContractService(Web3j web3j, HealthVerificationClaimContract healthVerificationClaimContract) {
        this.web3j = web3j;
        this.healthVerificationClaimContract = healthVerificationClaimContract;
    }

    public void createHealthVerification(String issuerAddress, String claimId, String ownerId, String issuerId) {
        Function createHealthVerificationFunction = createHealthVerificationFunction(new Address(issuerAddress), new Utf8String(claimId), new Utf8String(ownerId), new Utf8String(issuerId));

        String responseValue = null;
        try {
            responseValue = callSmartContractFunction(createHealthVerificationFunction, healthVerificationClaimContract.getAddress(), issuerAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("CreateHealthVerification with response value: {}.", responseValue);
    }

    private Function createHealthVerificationFunction(Address issuerAddress, Utf8String claimId, Utf8String ownerId, Utf8String issuerId) {
        return new Function(
            "createHealthVerification",
            ImmutableList.of(issuerAddress, claimId, ownerId, issuerId),
            Collections.singletonList(new TypeReference<Utf8String>() {
            }));
    }

    private String callSmartContractFunction(Function function, String contractAddress, String issuerAddress) throws Exception {
        String encodedFunction = FunctionEncoder.encode(function);

        org.web3j.protocol.core.methods.response.EthCall response =
            web3j.ethCall(
                Transaction.createEthCallTransaction(issuerAddress, contractAddress, encodedFunction),
                DefaultBlockParameterName.LATEST)
                 .sendAsync()
                 .get();

        return response.getValue();
    }
}
