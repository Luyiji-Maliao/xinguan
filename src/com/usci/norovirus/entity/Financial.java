package com.usci.norovirus.entity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * SampleJd entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "xg_financial")
public class Financial implements java.io.Serializable {

	private Integer id;
    private Integer xgappid;
    private String  outtradeno;
    private String  tuikuan;
    private String tuikuandate;
    private String tuikuanname;
    private String tuikuanyuanyin;
    private String tuikuanjine;
    private String shenqingdate;
    private String shijijine;
    private String shenqingname;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getXgappid() {
        return xgappid;
    }

    public void setXgappid(Integer xgappid) {
        this.xgappid = xgappid;
    }

    public String getOuttradeno() {
        return outtradeno;
    }

    public void setOuttradeno(String outtradeno) {
        this.outtradeno = outtradeno;
    }

    public String getTuikuan() {
        return tuikuan;
    }

    public void setTuikuan(String tuikuan) {
        this.tuikuan = tuikuan;
    }

    public String getTuikuandate() {
        return tuikuandate;
    }

    public void setTuikuandate(String tuikuandate) {
        this.tuikuandate = tuikuandate;
    }

    public String getTuikuanname() {
        return tuikuanname;
    }

    public void setTuikuanname(String tuikuanname) {
        this.tuikuanname = tuikuanname;
    }

    public String getTuikuanyuanyin() {
        return tuikuanyuanyin;
    }

    public void setTuikuanyuanyin(String tuikuanyuanyin) {
        this.tuikuanyuanyin = tuikuanyuanyin;
    }

    public String getTuikuanjine() {
        return tuikuanjine;
    }

    public void setTuikuanjine(String tuikuanjine) {
        this.tuikuanjine = tuikuanjine;
    }

    public String getShenqingdate() {
        return shenqingdate;
    }

    public void setShenqingdate(String shenqingdate) {
        this.shenqingdate = shenqingdate;
    }

    public String getShenqingname() {
        return shenqingname;
    }

    public void setShenqingname(String shenqingname) {
        this.shenqingname = shenqingname;
    }

    public String getShijijine() {
        return shijijine;
    }

    public void setShijijine(String shijijine) {
        this.shijijine = shijijine;
    }
}