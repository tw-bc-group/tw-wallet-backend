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

        if (!SYNC_EXECUTOR.awaitQuiescence(1, TimeUnit.SECONDS)) {
            log.info("------------------Eth Sync Job is Running------------------ \n\n");
            return;
        }

//        long remoteBlockNum = geRemoteBlockNum();
//        long localBlockNum = getLocalBlockNum();

        long remoteBlockNum = 1;
        long localBlockNum = 0;

        log.info("Eth Sync Job Start- {}::execute localBlockNum:{}, remoteBlockNum:{}", this.getClass().getName(), localBlockNum, remoteBlockNum);

        SYNC_EXECUTOR.submit(
                () -> range(localBlockNum, remoteBlockNum)
                        .parallel()
                        .forEach((blockNumber) -> {
                            try {
                                this.test();
//                                this.parseBlock(blockNumber);
                            } catch (Exception e) {
                                log.error("{}::parseBlock - exception: {}", this.getClass().getName(), e.getMessage());
                            }
                        }));
    }

    private void test() {

        long blockNumber = 15566;
        String txHash = "0xac64a6426f5b56f25bcdc0c5c0ea1599c1ebff3bbafa588807af289a0be4ccdf";

        try {
//            this.parseBlock(blockNumber);
            this.parseTx(txHash);

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
