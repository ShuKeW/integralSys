package com.skw.integralsys.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/29 11:49
 * @类描述 一句话描述 你的类
 */

public class SingleThreadPoolExecutor extends ThreadPoolExecutor {
    public SingleThreadPoolExecutor() {
        super(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }
}
