package com.thoughtworks.wallet.scheduler.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigSync {
    /**
     * the interval for waiting block generation
     */
    @Value("${sync.interval}")
    public int SYNC_BLOCK_INTERVAL;


    /**
     * the maximum time of each node for waiting for generating block
     */
    @Value("${sync.block-stop-increase}")
    public int SYNC_BLOCK_STOP_INCREASE;
}
