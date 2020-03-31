package com.thoughtworks.wallet.asset.service.impl;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.wallet.asset.annotation.Node1PrivateKey;
import com.thoughtworks.wallet.asset.annotation.QuorumRPCUrl;
import com.thoughtworks.wallet.asset.annotation.TWPointContractAddress;
import com.thoughtworks.wallet.asset.model.TWPoint;
import com.thoughtworks.wallet.asset.response.TWPointBalanceResponse;
import com.thoughtworks.wallet.asset.service.IQuorumService;
import com.thoughtworks.wallet.common.exception.AppException;
import com.thoughtworks.wallet.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class QuorumServiceImpl implements IQuorumService {

    @QuorumRPCUrl
    private String rpcUrl;

    @Node1PrivateKey
    private String privateKey;

    @TWPointContractAddress
    private String TWPointContractAddress;

    @Override
    public TWPointBalanceResponse getTWPointBalanceBy(String address) {

        Web3j web3j = Web3j.build(new HttpService(rpcUrl));

        Web3ClientVersion web3ClientVersion;
        try {
            web3ClientVersion = web3j.web3ClientVersion().sendAsync().get();
            log.info("Connected to Quorum client with version: " + web3ClientVersion.getWeb3ClientVersion());
        } catch (InterruptedException | ExecutionException e) {
            throw new QuorumConnectionErrorException(rpcUrl);
        }

        final ERC20 twPoint = ERC20.load(TWPointContractAddress, web3j, Credentials.create(privateKey), new DefaultGasProvider());

        final CompletableFuture<String> twPointSymbol = twPoint.symbol().sendAsync();
        final CompletableFuture<String> twPointName = twPoint.name().sendAsync();
        final CompletableFuture<BigInteger> twPointDecimal = twPoint.decimals().sendAsync();
        final CompletableFuture<BigInteger> twPointBalance = twPoint.balanceOf(address).sendAsync();

        web3j.shutdown();
        try {
            return TWPointBalanceResponse.of(address, TWPoint.create(twPointName.get(), twPointSymbol.get(), twPointDecimal.get()), new BigDecimal(twPointBalance.get()));
        } catch (InterruptedException | ExecutionException e) {
            throw new QuorumConnectionErrorException(rpcUrl);
        }
    }

    private static class QuorumConnectionErrorException extends AppException {
        public QuorumConnectionErrorException(String rpcUrl) {
            super(ErrorCode.QUORUM_CONNECTION_ERROR, ImmutableMap.of("QuorumRpcUrl", rpcUrl));
        }
    }
}
