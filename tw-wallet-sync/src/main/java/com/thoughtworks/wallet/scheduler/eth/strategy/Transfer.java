package com.thoughtworks.wallet.scheduler.eth.strategy;

import com.thoughtworks.wallet.scheduler.eth.pojo.TWPoint;
import com.thoughtworks.wallet.scheduler.util.DBAdptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Slf4j
@BcEvent
public class Transfer extends BaseEventStrategy {
    public final static String TRANSFER = "Transfer";

    @Autowired
    private DBAdptor dbAdptor;

    @Override
    public Event getEventDefinition() {
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

    @Override
    protected void readBcLogs(TransactionReceipt receipt) {
        List<Log> logs = receipt.getLogs();
        Log logTx = logs.get(0);
        List<String> topics = logTx.getTopics();
        Address fromAddr = new Address(topics.get(1));
        Address toAddr = new Address(topics.get(2));

        // verify qty transferred
        List<Type> results = FunctionReturnDecoder.decode(logTx.getData(), getEvent().getNonIndexedParameters());

        BigInteger value = (BigInteger) results.get(0).getValue();

        log.info("fromAddr:{}, toAddr:{}, value:{}", fromAddr.getValue(), toAddr.getValue(), value);
        dbAdptor.SaveTWPTransaction(TWPoint.of(fromAddr.getValue(), toAddr.getValue(), receipt.getTransactionHash(), receipt.getBlockNumber(), receipt.getTransactionIndex(), value, Transfer.TRANSFER));
    }
}
