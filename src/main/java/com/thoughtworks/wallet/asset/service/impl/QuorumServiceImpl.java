package com.thoughtworks.wallet.asset.service.impl;

import com.thoughtworks.wallet.asset.annotation.Node1PrivateKey;
import com.thoughtworks.wallet.asset.annotation.QuorumRPCUrl;
import com.thoughtworks.wallet.asset.annotation.TWPointContractAddress;
import com.thoughtworks.wallet.asset.exception.InvalidAddressErrorException;
import com.thoughtworks.wallet.asset.exception.QuorumConnectionErrorException;
import com.thoughtworks.wallet.asset.model.TWPoint;
import com.thoughtworks.wallet.asset.response.TWPointBalanceResponse;
import com.thoughtworks.wallet.asset.service.IBlockchainService;
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
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class QuorumServiceImpl implements IBlockchainService {

    @QuorumRPCUrl
    private String rpcUrl;

    @Node1PrivateKey
    private String privateKey;

    @TWPointContractAddress
    private String TWPointContractAddress;

    @Override
    public TWPointBalanceResponse getTWPointBalanceBy(String address) throws InvalidAddressErrorException, QuorumConnectionErrorException {
        log.info("!!! For test in QuorumServiceImpl !!!");

        boolean isAddress = isValidAddress(address);
        if (!isAddress) {
            throw new InvalidAddressErrorException(address);
        }

        Web3j web3j = Web3j.build(new HttpService(rpcUrl));

        Web3ClientVersion web3ClientVersion;
        try {
            web3ClientVersion = web3j.web3ClientVersion().sendAsync().get();
            log.info("Connected to Quorum client with version: " + web3ClientVersion.getWeb3ClientVersion());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new QuorumConnectionErrorException(rpcUrl);
        }

        final ERC20 twPoint = ERC20.load(TWPointContractAddress, web3j, Credentials.create(privateKey), new DefaultGasProvider());

        final String twPointSymbol;
        final String twPointName;
        final BigInteger twPointDecimal;
        final BigInteger twPointBalance;
        try {
            twPointSymbol = twPoint.symbol().sendAsync().get();
            twPointName = twPoint.name().sendAsync().get();
            twPointDecimal = twPoint.decimals().sendAsync().get();
            twPointBalance = twPoint.balanceOf(address).sendAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new QuorumConnectionErrorException(rpcUrl);
        } finally {
            web3j.shutdown();
        }

        return TWPointBalanceResponse.of(address, TWPoint.create(twPointName, twPointSymbol, twPointDecimal), new BigDecimal(twPointBalance));
    }

    public static boolean isValidAddress(String addr) {
        String regex = "^0x[0-9a-fA-F]{40}$";

        return addr.matches(regex);
    }
}