package com.skw.integralsys.threadpool;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/29 11:51
 * @类描述 一句话描述 你的类
 */

public class WorkThreadPoolExecutor extends ThreadPoolExecutor {

    public WorkThreadPoolExecutor() {
        super(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }
}
