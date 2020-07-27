package com.thoughtworks.wallet.scheduler.eth.strategy;

import com.thoughtworks.wallet.scheduler.eth.pojo.Identity;
import com.thoughtworks.wallet.scheduler.eth.pojo.TWPoint;
import com.thoughtworks.wallet.scheduler.util.DBAdptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Slf4j
@BcEvent
public class IdentityCreated extends BaseEventStrategy {
    public final static String IDENTITY_CREATED = "IdentityCreated";

    @Autowired
    private DBAdptor dbAdptor;

    @Override
    public Event getEventDefinition() {
        return new Event(
                IDENTITY_CREATED,
                Arrays.asList(
                        new TypeReference<Address>(true) {
                        },
                        new TypeReference<Address>() {
                        },
                        new TypeReference<org.web3j.abi.datatypes.Utf8String>() {
                        },
                        new TypeReference<org.web3j.abi.datatypes.Utf8String>() {
                        },
                        new TypeReference<org.web3j.abi.datatypes.Utf8String>() {
                        }
                ));
    }

    @Override
    protected void readBcLogs(TransactionReceipt receipt) {
        List<Log> logs = receipt.getLogs();
        Log logTx = logs.get(0);

        List<Type> resultsIndexed = FunctionReturnDecoder.decode(logTx.getData(), getEvent().getIndexedParameters());
        List<Type> results = FunctionReturnDecoder.decode(logTx.getData(), getEvent().getNonIndexedParameters());

        String initiator = (String) resultsIndexed.get(0).getValue();
        String ownerAddress = (String) results.get(0).getValue();
        String did = (String) results.get(1).getValue();
        String publicKey = (String) results.get(2).getValue();
        String name = (String) results.get(3).getValue();

        log.info("initiator:{}, ownerAddress:{}, did:{}, publicKey:{}, name:{}",
                initiator, ownerAddress, did, publicKey, name);

        dbAdptor.SaveDIDCreateTransaction(Identity.of(initiator, ownerAddress, did, publicKey, name, receipt.getTransactionHash(), receipt.getBlockNumber(), receipt.getTransactionIndex(), IdentityCreated.IDENTITY_CREATED));
    }

}
