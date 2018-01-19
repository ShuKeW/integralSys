package com.skw.integralsys.db;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/15 17:32
 * @类描述 一句话描述 你的类
 */

@Entity
public class Integral {
    @Id
    private long id;

    private ToOne<Members> membersToOne;

    /**
     * 本次加油的L数
     */
    private float LNumber;
    /**
     * 本次积分
     */
    private float integral;

    private Date createTime;
    private Date updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ToOne<Members> getMembersToOne() {
        return membersToOne;
    }

    public void setMembersToOne(ToOne<Members> membersToOne) {
        this.membersToOne = membersToOne;
    }

    public float getLNumber() {
        return LNumber;
    }

    public void setLNumber(float LNumber) {
        this.LNumber = LNumber;
    }

    public float getIntegral() {
        return integral;
    }

    public void setIntegral(float integral) {
        this.integral = integral;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
