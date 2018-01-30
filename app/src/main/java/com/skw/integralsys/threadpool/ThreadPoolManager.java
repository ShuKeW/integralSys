package com.skw.integralsys.threadpool;

import java.util.concurrent.ExecutorService;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/29 14:06
 * @类描述 一句话描述 你的类
 */

public class ThreadPoolManager {
    /**
     * 线程服务-并行工作线程池
     **/
    private static WorkThreadPoolExecutor workThreadPoolExecutor;

    /**
     * 线程服务-串行工作线程池
     **/
    private static SingleThreadPoolExecutor singleThreadPoolExecutor;

    public static synchronized ExecutorService getSingleExecutorService() {
        if (singleThreadPoolExecutor == null) {
            singleThreadPoolExecutor = new SingleThreadPoolExecutor();
        }
        return singleThreadPoolExecutor;
    }

    public static synchronized ExecutorService getWorkExecutorService() {
        if (workThreadPoolExecutor == null) {
            workThreadPoolExecutor = new WorkThreadPoolExecutor();
        }
        return workThreadPoolExecutor;
    }
}
