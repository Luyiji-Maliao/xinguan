package com.usci.xgcollectinfo.entity;

import javax.persistence.*;

@Entity
@Table(name = "xg_reserve")
public class Xgreserve implements java.io.Serializable{
    private Integer id;
    private String name;
    private String certNumber;
    private String phone;
    private String inputname;
    private String inputtime;
    private String updatename;
    private String updatetime;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name = "name", length = 55)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "certNumber", length = 55)
    public String getCertNumber() {
        return certNumber;
    }

    public void setCertNumber(String certNumber) {
        this.certNumber = certNumber;
    }
    @Column(name = "phone", length = 55)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    @Column(name = "inputname", length = 55)
    public String getInputname() {
        return inputname;
    }

    public void setInputname(String inputname) {
        this.inputname = inputname;
    }
    @Column(name = "inputtime", length = 55)
    public String getInputtime() {
        return inputtime;
    }

    public void setInputtime(String inputtime) {
        this.inputtime = inputtime;
    }
    @Column(name = "updatename", length = 55)
    public String getUpdatename() {
        return updatename;
    }

    public void setUpdatename(String updatename) {
        this.updatename = updatename;
    }
    @Column(name = "updatetime", length = 55)
    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
}
