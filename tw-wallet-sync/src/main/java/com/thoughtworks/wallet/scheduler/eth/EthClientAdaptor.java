package com.thoughtworks.wallet.scheduler.eth;

import com.thoughtworks.wallet.QuorumRPCUrls;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class EthClientAdaptor {

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        configureLogging(builder);
        configureTimeouts(builder);
        return builder.build();
    }

    private void configureTimeouts(OkHttpClient.Builder builder) {
        Long tos = 60L * 3;
        if (tos != null) {
            builder.connectTimeout(tos, TimeUnit.SECONDS);
            builder.readTimeout(tos, TimeUnit.SECONDS);  // Sets the socket timeout too
            builder.writeTimeout(tos, TimeUnit.SECONDS);
        }
    }

    private static void configureLogging(OkHttpClient.Builder builder) {
        if (log.isDebugEnabled()) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(log::debug);
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
    }

    Web3j quorum(String rpcUrl) {
        String nodeEndpoint = rpcUrl;
        Web3jService web3jService;

        if (nodeEndpoint == null || nodeEndpoint.equals("")) {
            web3jService = new HttpService(createOkHttpClient());
        } else if (nodeEndpoint.startsWith("http")) {
            web3jService = new HttpService(nodeEndpoint, createOkHttpClient(), false);
        } else if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            web3jService = new WindowsIpcService(nodeEndpoint);
        } else {
            web3jService = new UnixIpcService(nodeEndpoint);
        }

        return Web3j.build(web3jService);
    }

    private List<Web3j> w3js = new LinkedList<>();
    private Web3j w3j;
    private int index=0;

    public EthClientAdaptor(QuorumRPCUrls urls) {
        urls.getUrls().forEach(rpcUrl -> {
            w3js.add(quorum(rpcUrl));
        });
        w3j=w3js.get(index);
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
        log.info("switchNode , create web3j with another url, current index: {}", index);
        index = (index + 1) % w3js.size();
        w3j= w3js.get(index);
        log.info("switchNode ,  next¬ index: {}", index);
    }
}
