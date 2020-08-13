package com.thoughtworks.wallet.asset.service.impl;

import com.thoughtworks.common.annotation.QuorumRPCUrl;
import com.thoughtworks.common.exception.*;
import com.thoughtworks.common.util.Identity;
import com.thoughtworks.common.util.JacksonUtil;
import com.thoughtworks.wallet.asset.annotation.IdentitiesContractAddress;
import com.thoughtworks.wallet.asset.model.DECP;
import com.thoughtworks.wallet.asset.response.*;
import com.thoughtworks.wallet.asset.service.IBlockchainService;
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

    private static final String IDENTITIES_CONTRACT_PATH = "/contracts/Identities.json";
    private final static int autoTransferValue = 10;
    private final Web3j web3j;
    private final ERC20 decp;
    private final ModelMapper modelMapper = new ModelMapper();
    private final JacksonUtil jacksonUtil;
    @QuorumRPCUrl
    private String rpcUrl;

    @IdentitiesContractAddress
    private String identityRegistryContractAddress;

    @Autowired
    public QuorumServiceImpl(Web3j web3j, ERC20 erc20, JacksonUtil jacksonUtil) {
        this.web3j = web3j;
        this.decp = erc20;
        this.jacksonUtil = jacksonUtil;
    }

    @Override
    public DECPBalanceResponse getDCEPBalanceBy(String address)
            throws InvalidAddressErrorException, QuorumConnectionErrorException {
        log.info("The address of this request is " + address);

        if (!Identity.isValidAddress(address)) {
            throw new InvalidAddressErrorException(address);
        }

        final String decpSymbol;
        final String decpName;
        final BigInteger decpDecimal;
        final BigInteger decpBalance;
        try {
            decpSymbol = decp.symbol().sendAsync().get();
            decpName = decp.name().sendAsync().get();
            decpDecimal = decp.decimals().sendAsync().get();
            decpBalance = decp.balanceOf(address).sendAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new QuorumConnectionErrorException(rpcUrl);
        }

        return DECPBalanceResponse
                .of(address, DECP.create(decpName, decpSymbol, decpDecimal), new BigDecimal(decpBalance));
    }

    @Override
    public DECPInfoResponse getDCEPInfo() {
        final String decpSymbol;
        final String decpName;
        final BigInteger decpDecimal;
        try {
            decpSymbol = decp.symbol().sendAsync().get();
            decpName = decp.name().sendAsync().get();
            decpDecimal = decp.decimals().sendAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new QuorumConnectionErrorException(rpcUrl);
        }

        final String decpContractPath = "/contracts/DC_EP_ERC20.json";
        final String jsonString;
        String abi;
        try {
            jsonString = jacksonUtil.readJsonFile(decpContractPath);
            abi = jacksonUtil.parsePropertyFromJson(jsonString, "abi");
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ReadFileErrorException(decpContractPath);
        }

        return DECPInfoResponse.of(decp.getContractAddress(), decpName, decpSymbol, decpDecimal, abi);
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

    @Override
    public void assignInitPoint(String address, int amount) {
        log.info("assignInitPoint - address: {}", address);
        try {
            BigInteger decpDecimal = decp.decimals().sendAsync().get();
            BigInteger money =
                    BigInteger.valueOf(autoTransferValue).multiply(BigInteger.valueOf(amount).pow(decpDecimal.intValue()));
            this.decp.transfer(address, money).sendAsync();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new TransferException(rpcUrl);
        }
    }

    @Override
    public IdentitiesContractInfoRepresentation getIdentityInfo() {
        try {
            String jsonString = jacksonUtil.readJsonFile(IDENTITIES_CONTRACT_PATH);
            String name = jacksonUtil.parsePropertyFromJson(jsonString, "contractName");
            String abi = jacksonUtil.parsePropertyFromJson(jsonString, "abi");
            return IdentitiesContractInfoRepresentation.of(name, identityRegistryContractAddress, abi);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ReadFileErrorException(IDENTITIES_CONTRACT_PATH);
        }
    }

    @Override
    public List<TransactionResponse> getTransactionsBy(String address, int limit) {
        log.info("getTransactionsBy:" + address);
        int blockLimit = 1000;
        List<TransactionResponse> responses = new ArrayList<>();

        if (!Identity.isValidAddress(address)) {
            throw new InvalidAddressErrorException(address);
        }
        try {
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
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new QuorumConnectionErrorException(rpcUrl);
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
