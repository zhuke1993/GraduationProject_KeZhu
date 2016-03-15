package com.zhuke.smart_home.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by ZHUKE on 2016/3/14.
 */
@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserInfo(Long id, String userName, String password, String email) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public UserInfo() {
    }
}
