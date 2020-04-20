package com.thoughtworks.wallet.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

@Service
@Slf4j
public class Engine {
    private final ApplicationContext applicationContext;

    ForkJoinPool customThreadPool;

    private static Map<String, ISyncJob> jobHandlerRepository = new HashMap<>();

    public static ISyncJob addJobHandler(String name, ISyncJob jobHandler) {
        log.info("Engine::addJobHandler - name:{}, jobHandler:{}", name, jobHandler);
        return jobHandlerRepository.put(name, jobHandler);
    }

    public static ISyncJob loadJobHandler(String name) {
        return jobHandlerRepository.get(name);
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
        jobHandlerRepository.entrySet().stream().parallel().forEach(syncJobEntry -> {
            try {
                syncJobEntry.getValue().execute();
            } catch (Exception e) {
                log.error("Engine::run - execute exception: {}", e.getMessage());
            }
        });
    }
}
