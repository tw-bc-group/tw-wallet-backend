package com.thoughtworks.wallet.scheduler.eth.strategy;

import com.thoughtworks.wallet.scheduler.eth.pojo.Identity;
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
public class IdentityRegistered extends BaseEventStrategy {
    public final static String IDENTITY_REGISTERED = "IdentityRegistered";

    @Autowired
    private DBAdptor dbAdptor;

    @Override
    public Event getEventDefinition() {
        return new Event(
                IDENTITY_REGISTERED,
                Arrays.asList(
                        new TypeReference<org.web3j.abi.datatypes.Utf8String>() {
                        },
                        new TypeReference<org.web3j.abi.datatypes.Utf8String>() {
                        },
                        new TypeReference<org.web3j.abi.datatypes.Utf8String>() {
                        },
                        new TypeReference<org.web3j.abi.datatypes.Uint>() {
                        },
                        new TypeReference<org.web3j.abi.datatypes.Utf8String>() {
                        },
                        new TypeReference<Address>() {
                        },
                        new TypeReference<org.web3j.abi.datatypes.Utf8String>() {
                        }
                ));
    }



    //TODO: dappId 等类型没有存在数据库中，看积分那边是否需要用这个同步，需要的化可以增加保存字段。
    @Override
    protected void readBcLogs(TransactionReceipt receipt) {
        List<Log> logs = receipt.getLogs();
        Log logTx = logs.get(0);

        List<Type> results = FunctionReturnDecoder.decode(logTx.getData(), getEvent().getNonIndexedParameters());

        String name = (String) results.get(0).getValue();
        String did = (String) results.get(1).getValue();
        String dappId = (String) results.get(2).getValue();
        BigInteger index = (BigInteger) results.get(3).getValue();
        String extraInfo = (String) results.get(4).getValue();
        String operator = (String) results.get(5).getValue();
        String publicKey = (String) results.get(6).getValue();

        log.info("operator:{}, ownerAddress:{}, did:{}, publicKey:{}, name:{}",
                operator, operator, did, publicKey, name);

        dbAdptor.SaveDIDCreateTransaction(Identity.of(operator, operator, did, publicKey, name, receipt.getTransactionHash(), receipt.getBlockNumber(), receipt.getTransactionIndex(), IdentityRegistered.IDENTITY_REGISTERED));
    }

}
