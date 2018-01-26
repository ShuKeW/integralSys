package com.skw.integralsys.eventbus;

import com.skw.integralsys.db.Members;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/23 10:18
 * @类描述 一句话描述 你的类
 */

public class EditMemberEvent {
    public final Members members;

    public EditMemberEvent(Members members) {
        this.members = members;
    }
}
