package com.thoughtworks.wallet.scheduler;

import com.thoughtworks.wallet.scheduler.base.ISyncJob;
import com.thoughtworks.wallet.scheduler.base.SyncJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

/**
 * 需要解决以下问题：
 * 1. 某块同步的时候异常了，后面怎么再同步这个块，可能与部分tx已经同步好了
 * 2. 正在同步，又调用一次，会导致同一个块在两个job里面处理，如何避免（加个锁）
 * 3. 现在只同步交易信息，是否有其他信息需要同步，比如余额，合约里面的信息，一些交易的状态信息等等（看产品需求）
 * 4. 现在只同步Quorum，同步其他链如何增加（框架已经解决）
 * 5. 重试机制
 * 6. 如何结合调度
 * 7. 线程中的网络请求的超时时间需要设置
 * 8. 如果某个节点长时间不出块，需要切换节点
 * 9. w3j超时了，会在engine::run异常，如何切换节点
 */

@Service("Engine")
@Slf4j
public class Engine {
    private final ApplicationContext applicationContext;
    protected final ForkJoinPool SYNC_EXECUTOR = new ForkJoinPool(4);
    private static Map<String, ISyncJob> jobs = new HashMap<>();

    public static ISyncJob addJobHandler(String name, ISyncJob jobHandler) {
        log.info("Engine::addJobHandler - name:{}, jobHandler:{}", name, jobHandler);
        return jobs.put(name, jobHandler);
    }

    public static ISyncJob loadJobHandler(String name) {
        return jobs.get(name);
    }

    public Engine(ApplicationContext applicationContext) {
        // init job handler action
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(SyncJob.class);
        if (serviceBeanMap.size() > 0) {
            for (Object serviceBean : serviceBeanMap.values()) {
                if (serviceBean instanceof ISyncJob) {
                    String name = serviceBean.getClass().getName();
                    ISyncJob handler = (ISyncJob) serviceBean;
                    if (loadJobHandler(name) != null) {
                        throw new RuntimeException("Engine::onstructor - job handler[" + name + "] naming conflicts.");
                    }
                    addJobHandler(name, handler);
                }
            }
        }
        this.applicationContext = applicationContext;
    }

    public void run() {
        SYNC_EXECUTOR.submit(() ->
                jobs.entrySet().stream().parallel().forEach(syncJobEntry -> {
                    try {
                        syncJobEntry.getValue().execute();
                    } catch (Exception e) {
                        log.error("Engine::run - execute exception: {}", e.getMessage(), e);
                    }
                })
        );
    }
}
