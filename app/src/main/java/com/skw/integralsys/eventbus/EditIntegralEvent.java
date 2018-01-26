package com.skw.integralsys.eventbus;

import com.skw.integralsys.db.Integral;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/23 11:17
 * @类描述 一句话描述 你的类
 */

public class EditIntegralEvent {
    public final Integral integral;

    public EditIntegralEvent(Integral integral) {
        this.integral = integral;
    }
}
