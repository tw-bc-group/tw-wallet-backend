package com.thoughtworks.wallet.scheduler.eth;

import com.thoughtworks.wallet.scheduler.ISyncJob;
import com.thoughtworks.wallet.scheduler.SyncJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@SyncJob
public class EthSyncJob implements ISyncJob {

    private final EthSync ethSync;

    public EthSyncJob(EthSync ethSync) {
        this.ethSync = ethSync;
    }

    @Override
    public void execute() {
        log.info("\n\n------------------Eth Sync Job Start------------------\n\n");
        ethSync.run();
    }
}
