package com.zhuke.svmclassifier.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 动作数据
 * Created by ZHUKE on 2016/3/31.
 */
@Entity
@Table(name = "action_record")
public class ActionRecord {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    /**
     * 动作数据，数据格式为 <lable>空格<attr1>:<value1>空格<attr2>:<value2>
     */
    @Column
    private String action;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
