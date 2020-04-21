package com.thoughtworks.wallet.scheduler;

import com.thoughtworks.wallet.asset.response.TransactionResponse;
import com.thoughtworks.wallet.asset.service.IAddressService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/v1/scheduler")
public class SchedulerController {
    Engine engine;

    @Autowired
    public SchedulerController(Engine e) {
        engine = e;
    }

    @ApiOperation(value = "Start Sync Block")
    @GetMapping
    public boolean run() {
        log.info("engine.run() start");
        engine.run();
        log.info("engine.run() end");
        return true;
    }
}
