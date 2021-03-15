package com.thoughtworks.wallet.retry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: atoken-team
 * @date: 2019/12/25 19:36
 **/
@Slf4j
@Component
public class RetryJob {

    @Autowired
    private RetryHandler retryHandler;

    @XxlJob("retryJob")
    public void execute() throws Exception {
        log.info("-----ethUpdaterJob start-----");
        retryHandler.execute();
    }
}