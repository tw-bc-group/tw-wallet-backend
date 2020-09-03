package com.thoughtworks.wallet.scheduler.eth.strategy;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.datatypes.Event;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.List;
import java.util.Optional;

abstract public class BaseEventStrategy {

    private String encodedSignature = null;
    private Event event = null;

    public Event getEvent() {
        event = Optional.ofNullable(event).orElseGet(() -> getEventDefinition());
        return event;
    }

    public String getEncodedSignature() {
        return Optional.ofNullable(encodedSignature).orElseGet(() -> {
            encodedSignature = EventEncoder.encode(getEvent());
            return encodedSignature;
        });
    }

    /**
     * 定义区块链abi函数定义，需要派生类提供具体定义
     *
     * @return
     */
    abstract protected Event getEventDefinition();

    /**
     * 读取logs里面信息。需要派生类提供方法
     *
     * @param receipt
     */
    abstract protected void readBcLogs(TransactionReceipt receipt) throws Exception;

    /**
     * 执行策略
     *
     * @param receipt
     */
    public void execute(TransactionReceipt receipt) throws Exception {

        List<Log> logs = receipt.getLogs();
        Log logTx = logs.get(0);
        List<String> topics = logTx.getTopics();
        if (getEncodedSignature().equals(topics.get(0))) {
            readBcLogs(receipt);
        }
    }
}
