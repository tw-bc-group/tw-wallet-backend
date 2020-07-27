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
 * 手写的引擎，后面可以换成 xxl-job
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
