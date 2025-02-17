package com.usci.report.entity;



import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

/**
 *  报告防伪二维码申请
 * @author 菅志平
 */
import javax.persistence.GeneratedValue;
@Entity
@Table(name = "qrcode_key")
public class QrCodeKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qrid", unique = true, nullable = false)
    private Integer qrid;
    @Column(length = 50,nullable = false)
    private String qrkey;
    @Column(length = 50,nullable = false)
    private String sampleNo;
    @Column(length = 50,nullable = false)
    private String userName;
    @Column(length = 50,nullable = false)
    private String imageUrl;
    @Column(length = 50,nullable = false)
    private String updateName;
    @Column(length = 50,nullable = false)
    private String updateTime;
    @Transient
    private String filepath;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Integer getQrid() {
        return qrid;
    }

    public void setQrid(Integer qrid) {
        this.qrid = qrid;
    }

    public String getQrkey() {
        return qrkey;
    }

    public void setQrkey(String qrkey) {
        this.qrkey = qrkey;
    }

    public String getSampleNo() {
        return sampleNo;
    }

    public void setSampleNo(String sampleNo) {
        this.sampleNo = sampleNo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "QrCodeKey{" +
                "qrid=" + qrid +
                ", qrkey='" + qrkey + '\'' +
                ", sampleNo='" + sampleNo + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", updateName='" + updateName + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", filepath='" + filepath + '\'' +
                '}';
    }
}
