package com.thoughtworks.wallet.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static java.util.stream.LongStream.range;

@Slf4j
public abstract class BaseSync implements ISyncJob {

    //TODO: config parallelism
    protected static final ForkJoinPool SYNC_EXECUTOR = new ForkJoinPool(4);

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void execute() {

        log.info("\n\n------------------Eth Sync Job Start------------------\n\n");

        long remoteBlockNum = geRemoteBlockNum();
        long localBlockNum = getLocalBlockNum();

        log.info("{}::execute localBlockNum:{}, remoteBlockNum:{}", this.getClass().getName(), localBlockNum, remoteBlockNum);

        SYNC_EXECUTOR.submit(
                () -> range(localBlockNum, remoteBlockNum)
                        .parallel()
                        .forEach((blockNum) -> {
                            try {
                                this.parseBlock(blockNum);
                            } catch (Exception e) {
                                log.error("{}::parseBlock - exception: {}", this.getClass().getName(), e.getMessage());
                            }
                        }));
    }


    public abstract void parseBlock(Long height);

    protected long getLocalBlockNum() {
        return 0;
    }

    protected long geRemoteBlockNum() {
        return 0;
    }
}
