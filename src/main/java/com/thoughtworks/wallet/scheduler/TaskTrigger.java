package com.thoughtworks.wallet.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@DependsOn("Engine")
public class TaskTrigger {
    private final ApplicationContext context;

    public TaskTrigger(ApplicationContext context) {
        this.context = context;
        log.info("TaskTrigger init - engine.run() start");
        Engine blockHandlerThread = context.getBean("Engine", Engine.class);
        blockHandlerThread.run();
    }
}
