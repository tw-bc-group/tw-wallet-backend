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
public abstract class BaseSync {


    //TODO: config parallelism
    protected static final ForkJoinPool SYNC_EXECUTOR = new ForkJoinPool(4,
            ForkJoinPool.defaultForkJoinWorkerThreadFactory,
            (t, e) -> log.error("BaseSync::run - ForkJoinPool uncaughtException - id: {}, name: {}, exception: {}", t.getId(), t.getName(), e.getMessage()),
            false);

    @Autowired
    private ApplicationContext applicationContext;

    public void run() {

        long remoteBlockNum = geRemoteBlockNum();
        long localBlockNum = getLocalBlockNum();

        log.info("localBlockNum:{}, remoteBlockNum:{}", localBlockNum, remoteBlockNum);

        SYNC_EXECUTOR.submit(
                () -> range(localBlockNum, remoteBlockNum)
                        .parallel()
                        .forEach((blockNum) -> {
                            this.parseBlock(blockNum);
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
