package com.thoughtworks.wallet.scheduler.eth;

import com.thoughtworks.wallet.annotation.QuorumRPCUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.ipc.UnixIpcService;
import org.web3j.protocol.ipc.WindowsIpcService;

import java.io.IOException;
import java.math.BigInteger;

@Slf4j
@Service
public class EthClientAdaptor {
    private Web3j w3j;

    @QuorumRPCUrl
    private String rpcUrl;

    public EthClientAdaptor() {

        String nodeEndpoint = rpcUrl;
        Web3jService web3jService;

        if (nodeEndpoint == null || nodeEndpoint.equals("")) {
            web3jService = new HttpService();
        } else if (nodeEndpoint.startsWith("http")) {
            web3jService = new HttpService(nodeEndpoint);
        } else if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            web3jService = new WindowsIpcService(nodeEndpoint);
        } else {
            web3jService = new UnixIpcService(nodeEndpoint);
        }

        w3j = Web3j.build(web3jService);
    }

    public BigInteger getLatestBlockNum() throws IOException {
        EthBlockNumber ethBlockNumber = w3j.ethBlockNumber().send();
        return ethBlockNumber.getBlockNumber();
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
}
