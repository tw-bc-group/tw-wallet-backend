package com.thoughtworks.wallet.scheduler.base;

import com.thoughtworks.wallet.scheduler.util.ConfigSync;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.*;

import static java.util.stream.LongStream.range;

/**
 * BaseSync 实现了模板模式，每个同步区块链数据的工作都可以按照此模板执行
 */
@Slf4j
public abstract class BaseSync implements ISyncJob {

    @Autowired
    private ConfigSync configSync;

    protected final ForkJoinPool SYNC_EXECUTOR = new ForkJoinPool(4);

    @Autowired
    private ApplicationContext applicationContext;


    /**
     * 顺序解析块
     */
    @SneakyThrows
    @Override
    public void execute() {

        int oneBlockTryTime = 1;

        while (true) {
            long remoteBlockNum = geRemoteBlockNum();
            long localBlockNum = getLocalBlockNum();

            log.info("Sync Job Start- {}::execute localBlockNum:{}, remoteBlockNum:{}", this.getClass().getName(), localBlockNum, remoteBlockNum);

            //wait for generating block
            if (localBlockNum >= remoteBlockNum) {
                log.info("{} wait for block", this.getClass().getName());
                Thread.sleep(configSync.SYNC_BLOCK_INTERVAL);
                oneBlockTryTime++;
                if (oneBlockTryTime >= configSync.SYNC_BLOCK_STOP_INCREASE) {
                    switchNode();
                    oneBlockTryTime = 1;
                }
                continue;
            }


            range(localBlockNum, remoteBlockNum)
                    .forEach((blockNumber) -> {
                        try {
                            this.parseBlock(blockNumber);
                        } catch (Exception e) {
                            log.error("{}::parseBlock - exception: ", this.getClass().getName(), e);
                        }
                    });
        }
    }

    /**
     * 方便调试parseTx的函数
     */
    private void test() {

        long blockNumber = 15566;
        String txHashTWP = "0xac64a6426f5b56f25bcdc0c5c0ea1599c1ebff3bbafa588807af289a0be4ccdf";
        String txHashDID = "0x05841b5fd5a3dc4a4d88e6177cdcdd4e89cdfc4b14df0da266ed502e194fd51f";
        try {
//            this.parseBlock(blockNumber);
            this.parseTx(txHashDID);
        } catch (Exception e) {
            log.error("{}::parseBlock - exception", this.getClass().getName(), e);
        }
    }

    public abstract void switchNode();

    public abstract void parseTx(String hash);

    public abstract void parseBlock(Long height);

    protected long getLocalBlockNum() {
        return 0;
    }

    protected long geRemoteBlockNum() {
        return 0;
    }
}
