package com.thoughtworks.wallet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(scanBasePackages = {"com.thoughtworks.common", "com.thoughtworks.wallet"})
public class TWWalletSync {

    public static void main(String[] args) {
        SpringApplication.run(TWWalletSync.class, args);
    }
}
