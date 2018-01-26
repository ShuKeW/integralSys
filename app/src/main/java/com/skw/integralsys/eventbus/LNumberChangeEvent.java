package com.skw.integralsys.eventbus;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/23 16:31
 * @类描述 一句话描述 你的类
 */

public class LNumberChangeEvent {
    public final long MemberId;
    /**
     * 这个值是在总值的基础上要加上的值，可能为负数，表示删除了或者减少了
     */
    public final float LNumberTotal;
    public final float integralNumberTotal;


    public LNumberChangeEvent(long memberId, float lNumberTotal, float integralNumberTotal) {
        MemberId = memberId;
        LNumberTotal = lNumberTotal;
        this.integralNumberTotal = integralNumberTotal;
    }
}
