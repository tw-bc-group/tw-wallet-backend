package com.thoughtworks.wallet.retry;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RetryHelper {

    /**
     * 构造方法私有化
     */
    private RetryHelper(){

    }

    public static final ThreadLocal<Thread> INVOKE_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 是否业务重试调用
     * @return
     */
    public static boolean isRetry(){
        return null != INVOKE_THREAD_LOCAL.get();
    }

    /**
     * 业务重试前
     */
    public static void preRetrey(){
        INVOKE_THREAD_LOCAL.set(Thread.currentThread());
    }

    /**
     * 业务重试后
     */
    public static void postRetry(){
        INVOKE_THREAD_LOCAL.remove();
    }

}
