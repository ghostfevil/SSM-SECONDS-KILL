package com.crossoverJie.seconds.kill.pojo;

import java.io.Serializable;
import java.util.Date;

import com.crossoverJie.seconds.kill.common.entity.BaseEntity;

public class StockOrder extends BaseEntity implements Serializable {

    private Integer sid;

    private String name;

    private Date createTime;

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}