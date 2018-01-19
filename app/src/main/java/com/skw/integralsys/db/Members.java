package com.skw.integralsys.db;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * @创建人 weishukai
 * @创建时间 2018/1/15 15:13
 * @类描述 一句话描述 你的类
 */

@Entity
public class Members {
    /**
     * id
     */
    @Id
    private long id;
    /**
     * 卡号
     */
    private String cardId;
    /**
     * 名字
     */
    private String name;
    /**
     * 电话号码
     */
    private String phoneNumber;
    /**
     * 车牌号
     */
    private String carNumber;
    /**
     * 加油的总共升数
     */
    private float LTotalNumber;
    /**
     * 总积分
     */
    private float totalIntegral;

    private Date createTime;
    private Date updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public float getLTotalNumber() {
        return LTotalNumber;
    }

    public void setLTotalNumber(float LTotalNumber) {
        this.LTotalNumber = LTotalNumber;
    }

    public float getTotalIntegral() {
        return totalIntegral;
    }

    public void setTotalIntegral(float totalIntegral) {
        this.totalIntegral = totalIntegral;
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
