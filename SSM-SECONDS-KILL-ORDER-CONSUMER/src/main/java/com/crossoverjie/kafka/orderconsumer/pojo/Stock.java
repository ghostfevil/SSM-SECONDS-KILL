package com.crossoverjie.kafka.orderconsumer.pojo;

import java.io.Serializable;

import com.crossoverjie.kafka.orderconsumer.common.entity.BaseEntity;

public class Stock extends BaseEntity implements Serializable{

//    private Integer id  与父类冲突;

    /**
     * 名称
     */
    private String name;

    /**
     * 库存
     */
    private Integer count;

    /**
     * 已售
     */
    private Integer sale;

    /**
     * 乐观锁，版本号
     */
//    private Integer version 与父类冲突;


    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取库存
     *
     * @return count - 库存
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 设置库存
     *
     * @param count 库存
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 获取已售
     *
     * @return sale - 已售
     */
    public Integer getSale() {
        return sale;
    }

    /**
     * 设置已售
     *
     * @param sale 已售
     */
    public void setSale(Integer sale) {
        this.sale = sale;
    }


}