package com.thoughtworks.wallet.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static java.util.concurrent.ForkJoinPool.defaultForkJoinWorkerThreadFactory;
import static java.util.stream.LongStream.range;

@Slf4j
public abstract class BaseSync implements ISyncJob {

    protected final ForkJoinPool SYNC_EXECUTOR = new ForkJoinPool(4);

    @Autowired
    private ApplicationContext applicationContext;

    @SneakyThrows
    @Override
    public void execute() {

        long remoteBlockNum = geRemoteBlockNum();
        long localBlockNum = getLocalBlockNum();

        log.info("Eth Sync Job Start- {}::execute localBlockNum:{}, remoteBlockNum:{}", this.getClass().getName(), localBlockNum, remoteBlockNum);

//        this.test();

        range(localBlockNum, remoteBlockNum)
                .forEach((blockNumber) -> {
                    try {
                        this.parseBlock(blockNumber);
                    } catch (Exception e) {
                        log.error("{}::parseBlock - exception: {}", this.getClass().getName(), e.getMessage());
                    }
                });
    }

    private void test() {

        long blockNumber = 15566;
        String txHashTWP = "0xac64a6426f5b56f25bcdc0c5c0ea1599c1ebff3bbafa588807af289a0be4ccdf";
        String txHashDID = "0x05841b5fd5a3dc4a4d88e6177cdcdd4e89cdfc4b14df0da266ed502e194fd51f";
        try {
//            this.parseBlock(blockNumber);
            this.parseTx(txHashDID);
        } catch (Exception e) {
            log.error("{}::parseBlock - exception: {}", this.getClass().getName(), e.getMessage());
        }
    }

    public abstract void parseTx(String hash);

    public abstract void parseBlock(Long height);

    protected long getLocalBlockNum() {
        return 0;
    }

    protected long geRemoteBlockNum() {
        return 0;
    }
}
