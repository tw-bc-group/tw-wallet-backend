package com.thoughtworks.wallet.asset.service.impl;

import com.thoughtworks.wallet.asset.annotation.IdentityRegistryContractAddress;
import com.thoughtworks.wallet.asset.annotation.QuorumRPCUrl;
import com.thoughtworks.wallet.asset.exception.ErrorSendTransactionException;
import com.thoughtworks.wallet.asset.exception.InvalidAddressErrorException;
import com.thoughtworks.wallet.asset.exception.QuorumConnectionErrorException;
import com.thoughtworks.wallet.asset.exception.ReadFileErrorException;
import com.thoughtworks.wallet.asset.model.TWPoint;
import com.thoughtworks.wallet.asset.response.IdentityRegistryInfoResponse;
import com.thoughtworks.wallet.asset.response.TWPointBalanceResponse;
import com.thoughtworks.wallet.asset.response.TWPointInfoResponse;
import com.thoughtworks.wallet.asset.response.TransactionResponse;
import com.thoughtworks.wallet.asset.service.IBlockchainService;
import com.thoughtworks.wallet.util.Identity;
import com.thoughtworks.wallet.util.JacksonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class QuorumServiceImpl implements IBlockchainService {

    private final Web3j web3j;
    private final ERC20 twPoint;
    private final ModelMapper modelMapper = new ModelMapper();
    private final JacksonUtil jacksonUtil;

    @QuorumRPCUrl
    private String rpcUrl;

    @IdentityRegistryContractAddress
    private String identityRegistryContractAddress;

    @Autowired
    public QuorumServiceImpl(Web3j web3j, ERC20 erc20, JacksonUtil jacksonUtil) {
        this.web3j = web3j;
        this.twPoint = erc20;
        this.jacksonUtil = jacksonUtil;
    }

    @Override
    public TWPointBalanceResponse getTWPointBalanceBy(String address)
        throws InvalidAddressErrorException, QuorumConnectionErrorException {
        log.info("The address of this request is " + address);

        if (!Identity.isValidAddress(address)) {
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
            .of(address, TWPoint.create(twPointName, twPointSymbol, twPointDecimal), new BigDecimal(twPointBalance));
    }

    @Override
    public TWPointInfoResponse getTWPointInfo() {
        final String twPointSymbol;
        final String twPointName;
        final BigInteger twPointDecimal;
        try {
            twPointSymbol = twPoint.symbol().sendAsync().get();
            twPointName = twPoint.name().sendAsync().get();
            twPointDecimal = twPoint.decimals().sendAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new QuorumConnectionErrorException(rpcUrl);
        }

        final String TWPointContractPath = "/contracts/TWPointERC20.json";
        final String jsonString;
        String abi;
        try {
            jsonString = jacksonUtil.readJsonFile(TWPointContractPath);
            abi = jacksonUtil.parsePropertyFromJson(jsonString, "abi");
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ReadFileErrorException(TWPointContractPath);
        }

        return TWPointInfoResponse.of(twPoint.getContractAddress(), twPointName, twPointSymbol, twPointDecimal, abi);
    }

    @Override
    public IdentityRegistryInfoResponse getIdentityRegistryInfo() {
        final String identityRegistryContractPath = "/contracts/IdentityRegistry.json";

        final String name;
        final String abi;
        final String jsonString;
        try {
            jsonString = jacksonUtil.readJsonFile(identityRegistryContractPath);
            name = jacksonUtil.parsePropertyFromJson(jsonString, "contractName");
            abi = jacksonUtil.parsePropertyFromJson(jsonString, "abi");
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ReadFileErrorException(identityRegistryContractPath);
        }

        return IdentityRegistryInfoResponse.of(name, identityRegistryContractAddress, abi);
    }

    @SneakyThrows
    public List<TransactionResponse> getTransactionsBy(String address, int limit) {
        log.info("getTransactionsBy:" + address);
        int blockLimit = 1000;
        List<TransactionResponse> responses = new ArrayList<>();

        if (!Identity.isValidAddress(address)) {
            throw new InvalidAddressErrorException(address);
        }

        BigInteger ethBlockNumber = web3j.ethBlockNumber().sendAsync().get().getBlockNumber();

        for (int i = 0; i < blockLimit; i++) {
            log.debug("fetching block number: {}", ethBlockNumber);
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

    @Override
    public void sendRawTransaction(String signedTransactionData, String address) {

        if (!Identity.verifySignature(signedTransactionData, address)) {
            throw new ErrorSendTransactionException("Can not verify your signed transaction.");
        }

        org.web3j.protocol.core.methods.response.EthSendTransaction transactionResponse;
        try {
            transactionResponse = web3j.ethSendRawTransaction(signedTransactionData).send();
            if (transactionResponse.hasError()) {
                throw new ErrorSendTransactionException(transactionResponse.getError().getMessage());
            }
        } catch (IOException e) {
            log.error("Cannot send transaction", e);
            throw new ErrorSendTransactionException(e.getMessage());
        }

        log.info("Transaction hash: {}", transactionResponse.getTransactionHash());
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
            .filter(tx -> relatedTo(tx, address))
            .collect(toList());
    }

    private boolean relatedTo(Transaction tx, String address) {
        return address.equals(tx.getFrom()) || address.equals(tx.getTo());
    }
}