package com.thoughtworks.wallet.retry;

import org.springframework.stereotype.Service;

@Service
public class RetryHandler {
    public void execute() {
        // 从数据库里面读取需要重试的方法和参数，重试
    }
}
