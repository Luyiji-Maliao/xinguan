package com.usci.norovirus.entity;

import java.math.BigInteger;

/**
 * 新冠销售分销码
 */
public class Cooperationinformation {

    private int id;
    private String salename;
    private String cooperationcompanyname;
    private String cooperationsubcompanyname;
    private String businessmanager;
    private String cooperationstr;
    private BigInteger renshu;
    private String permissionperson;


    public String getPermissionperson() {
        return permissionperson;
    }

    public void setPermissionperson(String permissionperson) {
        this.permissionperson = permissionperson;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSalename() {
        return salename;
    }

    public void setSalename(String salename) {
        this.salename = salename;
    }

    public String getCooperationcompanyname() {
        return cooperationcompanyname;
    }

    public void setCooperationcompanyname(String cooperationcompanyname) {
        this.cooperationcompanyname = cooperationcompanyname;
    }

    public String getCooperationsubcompanyname() {
        return cooperationsubcompanyname;
    }

    public void setCooperationsubcompanyname(String cooperationsubcompanyname) {
        this.cooperationsubcompanyname = cooperationsubcompanyname;
    }

    public String getBusinessmanager() {
        return businessmanager;
    }

    public void setBusinessmanager(String businessmanager) {
        this.businessmanager = businessmanager;
    }

    public String getCooperationstr() {
        return cooperationstr;
    }

    public void setCooperationstr(String cooperationstr) {
        this.cooperationstr = cooperationstr;
    }

    public BigInteger getRenshu() {
        return renshu;
    }

    public void setRenshu(BigInteger renshu) {
        this.renshu = renshu;
    }
}
