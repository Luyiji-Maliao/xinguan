package com.usci.system.entity;
import javax.persistence.*;

import com.google.common.collect.Lists;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.List;

import javax.persistence.GeneratedValue;
@Entity
@Table(name = "wx_loginuser")
@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class WxLoginUser {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 微信ID
     */
    @Column(length = 30, unique = true, nullable = false)
    private String openid;
    /**
     * 登陆账号
     */
    @Column(length = 30, unique = true, nullable = false)
    private String username;
    /**
     * 密码
     */
    @Column(length = 32, nullable = false)
    private String password;
    /**
     * 权限
     */
    @Column(length = 200)
    private String perstr;

    @Transient
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerstr() {
        return perstr;
    }

    public void setPerstr(String perstr) {
        this.perstr = perstr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}