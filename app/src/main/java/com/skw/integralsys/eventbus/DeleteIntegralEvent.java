package com.skw.integralsys.eventbus;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/23 10:38
 * @类描述 一句话描述 你的类
 */

public class DeleteIntegralEvent {
    public final long integralId;

    public DeleteIntegralEvent(long integralId) {
        this.integralId = integralId;
    }
}
