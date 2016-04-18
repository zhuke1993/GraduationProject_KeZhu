package com.zhuke.svmclassifier.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

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
    @Column(length = 1024)
    private String action;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "user_id")
    private Long userId;

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

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
