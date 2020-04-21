package com.thoughtworks.wallet.scheduler.eth;


import com.thoughtworks.wallet.scheduler.BaseSync;
import com.thoughtworks.wallet.scheduler.DBAdptor;
import com.thoughtworks.wallet.scheduler.SyncJob;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.LongStream.range;

@Slf4j
@SyncJob
public class EthSync extends BaseSync {

    @Autowired
    private EthClientAdaptor ethClientAdaptor;

    @Autowired
    private DBAdptor dbAdptor;

    @Override
    protected long geRemoteBlockNum() {
        return ethClientAdaptor.getLatestBlockNum();
    }

    @Override
    protected long getLocalBlockNum() {
        return dbAdptor.getLocalBlockHeight();
    }

    @SneakyThrows
    @Override
    public void parseTx(String hash) {
        this.saveTransaction(hash);
    }

    @SneakyThrows
    @Override
    public void parseBlock(Long height) {
        EthBlock.Block block = ethClientAdaptor.getBlockByNumber(height, true);
        List<EthBlock.TransactionObject> transactionObjects = block.getTransactions().stream()
                .map(txResult -> (EthBlock.TransactionObject) txResult.get())
                .collect(Collectors.toList());

        boolean hasTx = CollectionUtils.isNotEmpty(transactionObjects);
        log.info("parseBlock - height:{}, has Txs: {}", height, hasTx);

        if (hasTx) {
            SYNC_EXECUTOR.submit(
                    () -> transactionObjects.stream().parallel().forEach(tx -> this.saveTransaction(tx.getHash()))
            );
        }
    }

    private Event transferEvent() {
        return new Event(
                "Transfer",
                Arrays.asList(
                        new TypeReference<Address>(true) {
                        },
                        new TypeReference<Address>(true) {
                        },
                        new TypeReference<Uint256>() {
                        }));
    }

    private Event createIdentityEvent() {
        return new Event(
                "createIdentity",
                Arrays.asList(
                        new TypeReference<Address>(true) {
                        },
                        new TypeReference<Address>(true) {
                        },
                        new TypeReference<Uint256>() {
                        }));
    }

    @SneakyThrows
    public void saveTransaction(String txHash) {
        // save to transaction table
        TransactionReceipt receipt = ethClientAdaptor.getTransactionReceipt(txHash);
        List<Log> logs = receipt.getLogs();
        Event transferEvent = transferEvent();
        Log logTx = logs.get(0);

        // verify the event was called with the function parameters
        List<String> topics = logTx.getTopics();

        // check function signature - we only have a single topic our event signature,
        // there are no indexed parameters in this example
        String encodedEventSignature = EventEncoder.encode(transferEvent);

        Address fromAddr = new Address(topics.get(1));
        Address toAddr = new Address(topics.get(2));

        if (!topics.get(0).equals(encodedEventSignature)) {
            log.error("saveTransaction - topic is not transfer - topics.get(0): {}", topics.get(0));
        }

        // verify qty transferred
        List<Type> results = FunctionReturnDecoder.decode(logTx.getData(), transferEvent.getNonIndexedParameters());

        BigInteger value = (BigInteger) results.get(0).getValue();

        log.info("fromAddr:{}, toAddr:{}, value:{}", fromAddr, toAddr, value);
    }

}
