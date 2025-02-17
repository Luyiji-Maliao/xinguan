package com.usci.norovirus.entity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * SampleJd entity. @author MyEclipse Persistence Tools
 * 预约表
 */
@Entity
@Table(name = "xg_tuanti")
public class Tuanti implements java.io.Serializable {
    private Integer id;
    private String tuanname;
    private String yuyuedate;
    private String yuyuetime;
    private String caiyangdidian;
    private String isfapiao;
    private String fapiaotaitou;
    private String zhucedizhi;
    private String zhucedianhua;
    private String kaihuyinhang;
    private String yinhangzhanghao;
    private String fapiaotype;
    private String accessname;
    private String accessphone;
    private String accessaddress;
    private String accessemail;
    private String xgreason;
    private String subjecttype;
    private String cooperationstr;
    private String qudao;
    private String sampleType;
    private String sending;
    private String checkProject;
    private String inspection;
    private String inputTime;
    private String inputName;
    private String updateTime;
    private String updateName;
    private String imgpath;
    private String shuihao;
    private String isjiesuan;
    private String code;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTuanname() {
        return tuanname;
    }

    public void setTuanname(String tuanname) {
        this.tuanname = tuanname;
    }

    public String getYuyuedate() {
        return yuyuedate;
    }

    public void setYuyuedate(String yuyuedate) {
        this.yuyuedate = yuyuedate;
    }

    public String getYuyuetime() {
        return yuyuetime;
    }

    public void setYuyuetime(String yuyuetime) {
        this.yuyuetime = yuyuetime;
    }

    public String getCaiyangdidian() {
        return caiyangdidian;
    }

    public void setCaiyangdidian(String caiyangdidian) {
        this.caiyangdidian = caiyangdidian;
    }

    public String getIsfapiao() {
        return isfapiao;
    }

    public void setIsfapiao(String isfapiao) {
        this.isfapiao = isfapiao;
    }

    public String getFapiaotaitou() {
        return fapiaotaitou;
    }

    public void setFapiaotaitou(String fapiaotaitou) {
        this.fapiaotaitou = fapiaotaitou;
    }

    public String getZhucedizhi() {
        return zhucedizhi;
    }

    public void setZhucedizhi(String zhucedizhi) {
        this.zhucedizhi = zhucedizhi;
    }

    public String getZhucedianhua() {
        return zhucedianhua;
    }

    public void setZhucedianhua(String zhucedianhua) {
        this.zhucedianhua = zhucedianhua;
    }

    public String getKaihuyinhang() {
        return kaihuyinhang;
    }

    public void setKaihuyinhang(String kaihuyinhang) {
        this.kaihuyinhang = kaihuyinhang;
    }

    public String getYinhangzhanghao() {
        return yinhangzhanghao;
    }

    public void setYinhangzhanghao(String yinhangzhanghao) {
        this.yinhangzhanghao = yinhangzhanghao;
    }

    public String getFapiaotype() {
        return fapiaotype;
    }

    public void setFapiaotype(String fapiaotype) {
        this.fapiaotype = fapiaotype;
    }

    public String getAccessname() {
        return accessname;
    }

    public void setAccessname(String accessname) {
        this.accessname = accessname;
    }

    public String getAccessphone() {
        return accessphone;
    }

    public void setAccessphone(String accessphone) {
        this.accessphone = accessphone;
    }

    public String getAccessaddress() {
        return accessaddress;
    }

    public void setAccessaddress(String accessaddress) {
        this.accessaddress = accessaddress;
    }

    public String getAccessemail() {
        return accessemail;
    }

    public void setAccessemail(String accessemail) {
        this.accessemail = accessemail;
    }

    public String getXgreason() {
        return xgreason;
    }

    public void setXgreason(String xgreason) {
        this.xgreason = xgreason;
    }

    public String getCooperationstr() {
        return cooperationstr;
    }

    public void setCooperationstr(String cooperationstr) {
        this.cooperationstr = cooperationstr;
    }

    public String getQudao() {
        return qudao;
    }

    public void setQudao(String qudao) {
        this.qudao = qudao;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getSending() {
        return sending;
    }

    public void setSending(String sending) {
        this.sending = sending;
    }

    public String getCheckProject() {
        return checkProject;
    }

    public void setCheckProject(String checkProject) {
        this.checkProject = checkProject;
    }

    public String getInspection() {
        return inspection;
    }

    public void setInspection(String inspection) {
        this.inspection = inspection;
    }

    public String getInputTime() {
        return inputTime;
    }

    public void setInputTime(String inputTime) {
        this.inputTime = inputTime;
    }

    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getShuihao() {
        return shuihao;
    }

    public void setShuihao(String shuihao) {
        this.shuihao = shuihao;
    }

    public String getIsjiesuan() {
        return isjiesuan;
    }

    public void setIsjiesuan(String isjiesuan) {
        this.isjiesuan = isjiesuan;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSubjecttype() {
        return subjecttype;
    }

    public void setSubjecttype(String subjecttype) {
        this.subjecttype = subjecttype;
    }
}