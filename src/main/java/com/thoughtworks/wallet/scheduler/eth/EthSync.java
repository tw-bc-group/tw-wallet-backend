package com.thoughtworks.wallet.scheduler.eth;


import com.thoughtworks.wallet.scheduler.BaseSync;
import com.thoughtworks.wallet.scheduler.DBAdptor;
import com.thoughtworks.wallet.scheduler.SyncJob;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SyncJob
public class EthSync extends BaseSync {

    final static String TRANSFER = "Transfer";
    final static String IDENTITY_CREATED = "IdentityCreated";


    @Autowired
    private EthClientAdaptor ethClientAdaptor;

    @Autowired
    private DBAdptor dbAdptor;

    @Override
    protected long geRemoteBlockNum() {
        return ethClientAdaptor.getRemoteBlockNum();
    }

    @Override
    protected long getLocalBlockNum() {
        return dbAdptor.getLocalBlockHeight();
    }

    @SneakyThrows
    @Override
    public void parseTx(String txHash) {
        // save to transaction table
        TransactionReceipt receipt = ethClientAdaptor.getTransactionReceipt(txHash);
        List<Log> logs = receipt.getLogs();
        Log logTx = logs.get(0);
        List<String> topics = logTx.getTopics();

        Event transferEvent = transferEvent();
        String encodedTransferEventSignature = EventEncoder.encode(transferEvent);

        Event createIdentityEvent = createIdentityEvent();
        String encodedCreateIdentityEventSignature = EventEncoder.encode(createIdentityEvent);

        if (topics.get(0).equals(encodedTransferEventSignature)) {

            Address fromAddr = new Address(topics.get(1));
            Address toAddr = new Address(topics.get(2));

            // verify qty transferred
            List<Type> results = FunctionReturnDecoder.decode(logTx.getData(), transferEvent.getNonIndexedParameters());

            BigInteger value = (BigInteger) results.get(0).getValue();

            log.info("fromAddr:{}, toAddr:{}, value:{}", fromAddr.getValue(), toAddr.getValue(), value);

            dbAdptor.SaveTWPTransaction(TWPoint.of(fromAddr.getValue(), toAddr.getValue(), receipt.getTransactionHash(), receipt.getBlockNumber(), receipt.getTransactionIndex(), value, TRANSFER));
        } else if (topics.get(0).equals(encodedCreateIdentityEventSignature)) {
            // verify qty transferred
            List<Type> results = FunctionReturnDecoder.decode(logTx.getData(), createIdentityEvent.getParameters());

            String initiator = (String) results.get(0).getValue();
            String ownerAddress = (String) results.get(1).getValue();
            String did = (String) results.get(2).getValue();
            String publicKey = (String) results.get(3).getValue();
            String name = (String) results.get(4).getValue();

            log.info("initiator:{}, ownerAddress:{}, did:{}, publicKey:{}, name:{}",
                    initiator, ownerAddress, did, publicKey, name);

            dbAdptor.SaveDIDCreateTransaction(Identity.of(initiator, ownerAddress, did, publicKey, name, receipt.getTransactionHash(), receipt.getBlockNumber(), receipt.getTransactionIndex(), IDENTITY_CREATED));
        }

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
                    () -> transactionObjects.stream().parallel().forEach(tx -> this.parseTx(tx.getHash()))
            );
        }
        // save block
        dbAdptor.SaveBlock(height, block.getHash());
    }

    private Event transferEvent() {
        return new Event(
                TRANSFER,
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
                IDENTITY_CREATED,
                Arrays.asList(
                        new TypeReference<Address>(true) {
                        },
                        new TypeReference<Address>(false) {
                        },
                        new TypeReference<org.web3j.abi.datatypes.Utf8String>(false) {
                        },
                        new TypeReference<org.web3j.abi.datatypes.Utf8String>(false) {
                        },
                        new TypeReference<org.web3j.abi.datatypes.Utf8String>(false) {
                        }
                ));
    }


}
