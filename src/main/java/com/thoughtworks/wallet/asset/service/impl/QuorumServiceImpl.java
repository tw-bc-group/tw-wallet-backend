package com.thoughtworks.wallet.asset.service.impl;

import com.thoughtworks.wallet.asset.annotation.Node1PrivateKey;
import com.thoughtworks.wallet.asset.annotation.QuorumRPCUrl;
import com.thoughtworks.wallet.asset.annotation.TWPointContractAddress;
import com.thoughtworks.wallet.asset.exception.InvalidAddressErrorException;
import com.thoughtworks.wallet.asset.exception.QuorumConnectionErrorException;
import com.thoughtworks.wallet.asset.model.TWPoint;
import com.thoughtworks.wallet.asset.response.TWPointBalanceResponse;
import com.thoughtworks.wallet.asset.response.TransactionResponse;
import com.thoughtworks.wallet.asset.service.IBlockchainService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.tx.gas.DefaultGasProvider;

@Slf4j
@Service
public class QuorumServiceImpl implements IBlockchainService {

    private final Web3j web3j;
    private final ERC20 twPoint;
    private final ModelMapper modelMapper = new ModelMapper();

    @QuorumRPCUrl
    private String rpcUrl;

    @Node1PrivateKey
    private String privateKey;

    @TWPointContractAddress
    private String TWPointContractAddress;

    @Autowired
    public QuorumServiceImpl(Web3j web3j) {
        this.web3j = web3j;
        this.twPoint = getErc20(web3j);
    }

    public static boolean isValidAddress(String addr) {
        String regex = "^0x[0-9a-fA-F]{40}$";

        return addr.matches(regex);
    }

    @Override
    public TWPointBalanceResponse getTWPointBalanceBy(String address)
        throws InvalidAddressErrorException, QuorumConnectionErrorException {
        log.info("Thd address of this request is " + address);

        boolean isAddress = isValidAddress(address);
        if (!isAddress) {
            throw new InvalidAddressErrorException(address);
        }

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
            log.error(e.getMessage());
            throw new QuorumConnectionErrorException(rpcUrl);
        }

        return TWPointBalanceResponse
            .of(address, TWPoint.create(twPointName, twPointSymbol, twPointDecimal),
                new BigDecimal(twPointBalance));
    }

    @SneakyThrows
    public List<TransactionResponse> getTransactionsBy(String address, int limit) {
        log.info("getTransactionsBy:" + address);
        int blockLimit = 1000;
        List<TransactionResponse> responses = new ArrayList<>();

        if (!isValidAddress(address)) {
            throw new InvalidAddressErrorException(address);
        }

        BigInteger ethBlockNumber = web3j.ethBlockNumber().sendAsync().get().getBlockNumber();

        for (int i = 0; i < blockLimit; i++) {
            if (countBlockTransactions(ethBlockNumber).compareTo(BigInteger.ZERO) > 0) {
                final List<Transaction> transactions = fetchBlockTransactions(address,
                    ethBlockNumber);

                for (int j = 0; j < Math.max(limit, transactions.size()); j++) {
                    responses.add(modelMapper.map(transactions.get(j), TransactionResponse.class));
                    if (responses.size() >= limit) {
                        return responses;
                    }
                }

            }
            ethBlockNumber = ethBlockNumber.subtract(BigInteger.ONE);
        }

        return responses;
    }

    private BigInteger countBlockTransactions(BigInteger ethBlockNumber)
        throws ExecutionException, InterruptedException {
        return web3j
            .ethGetBlockTransactionCountByNumber(DefaultBlockParameter.valueOf(ethBlockNumber))
            .sendAsync().get().getTransactionCount();
    }

    private List<Transaction> fetchBlockTransactions(String address, BigInteger ethBlockNumber)
        throws InterruptedException, ExecutionException {
        return web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(ethBlockNumber), true)
            .sendAsync()
            .get()
            .getBlock()
            .getTransactions()
            .stream()
            .map(transactionResult -> (Transaction) transactionResult.get())
            .filter(tx -> filterTransactions(tx, address))
            .collect(Collectors.toList());
    }


    private boolean filterTransactions(Transaction tx, String address) {
        return address.equals(tx.getFrom()) || address.equals(tx.getTo());
    }

    private ERC20 getErc20(Web3j web3j) {
        Web3ClientVersion web3ClientVersion;
        try {
            web3ClientVersion = web3j.web3ClientVersion().sendAsync().get();
            log.info("Connected to Quorum client with version: " + web3ClientVersion
                .getWeb3ClientVersion());
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            web3j.shutdown();
            throw new QuorumConnectionErrorException(rpcUrl);
        }

        return ERC20.load(TWPointContractAddress, web3j, Credentials.create(privateKey),
            new DefaultGasProvider());
    }

}