package com.thoughtworks.wallet.scheduler.eth;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EthUpdaterJob {

    @Autowired
    private EthSync ethSync;

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("ethUpdaterJob")
    public void ethUpdaterJobJobHandler() throws Exception {
        log.info("-----ethUpdaterJob start-----");
        ethSync.execute();
    }
}