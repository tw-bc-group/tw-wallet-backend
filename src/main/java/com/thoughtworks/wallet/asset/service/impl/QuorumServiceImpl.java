package com.thoughtworks.wallet.asset.service.impl;

import com.thoughtworks.wallet.asset.annotation.QuorumRPCUrl;
import com.thoughtworks.wallet.asset.exception.ErrorIdentityCreationException;
import com.thoughtworks.wallet.asset.exception.InvalidAddressErrorException;
import com.thoughtworks.wallet.asset.exception.QuorumConnectionErrorException;
import com.thoughtworks.wallet.asset.model.TWPoint;
import com.thoughtworks.wallet.asset.response.TWPointBalanceResponse;
import com.thoughtworks.wallet.asset.response.TransactionResponse;
import com.thoughtworks.wallet.asset.service.IBlockchainService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.SignedRawTransaction;
import org.web3j.crypto.TransactionDecoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuorumServiceImpl implements IBlockchainService {

    private final Web3j web3j;
    private final ERC20 twPoint;
    private final ModelMapper modelMapper = new ModelMapper();

    @QuorumRPCUrl
    private String rpcUrl;

    @Autowired
    public QuorumServiceImpl(Web3j web3j, ERC20 erc20) {
        this.web3j = web3j;
        this.twPoint = erc20;
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
    public void createIdentity(String signedTransactionData, String address, String messageHash) {
        org.web3j.protocol.core.methods.response.EthSendTransaction transactionResponse;

        RawTransaction tx = TransactionDecoder.decode(signedTransactionData);

        if (!(tx instanceof SignedRawTransaction)) {
            throw new ErrorIdentityCreationException("Wrong raw transaction data");
        }

        final Sign.SignatureData signatureData = ((SignedRawTransaction) tx).getSignatureData();

        if (!verifySignature(address, messageHash, signatureData)) {
            throw new ErrorIdentityCreationException("Can not verify your signed transaction.");
        }

        try {
            transactionResponse = web3j.ethSendRawTransaction(signedTransactionData).send();
            if (transactionResponse.hasError()) {
                throw new ErrorIdentityCreationException(transactionResponse.getError().getMessage());
            }
        } catch (IOException e) {
            throw new ErrorIdentityCreationException(e.getMessage());
        }

        String transactionHash = transactionResponse.getTransactionHash();
        log.info("Transaction hash: " + transactionHash);
    }


    public boolean verifySignature(String address, String messageHash, Sign.SignatureData signatureData) {
        byte[] messageHashBytes = Numeric.hexStringToByteArray(messageHash);
        boolean match = false;

        // Iterate for each possible key to recover
        for (int i = 0; i < 4; i++) {
            BigInteger publicKey = Sign.recoverFromSignature(
                (byte) i,
                new ECDSASignature(new BigInteger(1, signatureData.getR()), new BigInteger(1, signatureData.getS())), messageHashBytes);

            if (publicKey != null) {
                String addressRecovered = "0x" + Keys.getAddress(publicKey);

                if (addressRecovered.equalsIgnoreCase(address)) {
                    match = true;
                    System.out.println("index = " + i);
                    break;
                }
            }
        }
        return match;
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

}