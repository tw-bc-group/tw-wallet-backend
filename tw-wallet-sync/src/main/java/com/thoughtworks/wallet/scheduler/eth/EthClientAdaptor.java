package com.thoughtworks.wallet.scheduler.eth;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.*;

import java.io.IOException;
import java.math.BigInteger;

@Slf4j
@Service
public class EthClientAdaptor {

    private Web3j w3j;

    public EthClientAdaptor(Web3j web3j) {
        w3j = web3j;
    }

    @SneakyThrows
    public long getRemoteBlockNum() {
        EthBlockNumber ethBlockNumber = w3j.ethBlockNumber().send();
        return ethBlockNumber.getBlockNumber().longValue();
    }

    public EthBlock.Block getBlockByNumber(long blockNum, boolean returnFullTransactionObjects) throws IOException {
        EthBlock ethBlock = w3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNum)), returnFullTransactionObjects).send();
        return ethBlock.getBlock();
    }

    public Transaction getTransactionByHash(String txHash) throws IOException {
        EthTransaction ethTransaction = w3j.ethGetTransactionByHash(txHash).send();
        return ethTransaction.getTransaction().orElse(null);
    }

    public TransactionReceipt getTransactionReceipt(String trxHash) throws IOException {
        EthGetTransactionReceipt transactionReceipt = w3j.ethGetTransactionReceipt(trxHash).send();
        return transactionReceipt.getResult();

    }

    public void switchNode() {
        log.info("switchNode , create web3j with another url");
    }
}
