package com.usci.xgcollectinfo.entity;

import javax.persistence.*;

@Entity
@Table(name = "xg_collectinfo")
public class xgcollectinfo implements java.io.Serializable {
    private Integer id;
    private String samplenum;
    private String phone;
    private String bornDay;
    private String certAddress;
    private String certNumber;
    private String certOrg;
    private String certType;
    private String chineseName;
    private String effDate;
    private String expDate;
    private String gender;
    private String identityPic;
    private String nation;
    private String partyName;
    private String passportNo;
    private String signNum;
    private String inputtime;
    private String inputname;
    private String updatetime;
    private String updatename;
    private int isshow;
    private String inputAddress;
    private String fiveone;
    private String isruku;
    @Column(name = "fiveone", length = 55)
    public String getFiveone() {
        return fiveone;
    }

    public void setFiveone(String fiveone) {
        this.fiveone = fiveone;
    }
    @Column(name = "isruku", length = 55)
    public String getIsruku() {
        return isruku;
    }

    public void setIsruku(String isruku) {
        this.isruku = isruku;
    }

    public String getInputAddress() {
        return inputAddress;
    }

    public void setInputAddress(String inputAddress) {
        this.inputAddress = inputAddress;
    }

    public int getIsshow() {
        return isshow;
    }

    public void setIsshow(int isshow) {
        this.isshow = isshow;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "samplenum", length = 55)
    public String getSamplenum() {
        return samplenum;
    }

    public void setSamplenum(String samplenum) {
        this.samplenum = samplenum;
    }
    @Column(name = "phone", length = 55)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    @Column(name = "bornDay", length = 55)
    public String getBornDay() {
        return bornDay;
    }

    public void setBornDay(String bornDay) {
        this.bornDay = bornDay;
    }
    @Column(name = "certAddress", length = 55)
    public String getCertAddress() {
        return certAddress;
    }

    public void setCertAddress(String certAddress) {
        this.certAddress = certAddress;
    }
    @Column(name = "certNumber", length = 55)
    public String getCertNumber() {
        return certNumber;
    }

    public void setCertNumber(String certNumber) {
        this.certNumber = certNumber;
    }
    @Column(name = "certOrg", length = 55)
    public String getCertOrg() {
        return certOrg;
    }

    public void setCertOrg(String certOrg) {
        this.certOrg = certOrg;
    }
    @Column(name = "certType", length = 55)
    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }
    @Column(name = "chineseName", length = 55)
    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }
    @Column(name = "effDate", length = 55)
    public String getEffDate() {
        return effDate;
    }

    public void setEffDate(String effDate) {
        this.effDate = effDate;
    }
    @Column(name = "expDate", length = 55)
    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }
    @Column(name = "gender", length = 55)
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    @Column(name = "identityPic", length = 55)
    public String getIdentityPic() {
        return identityPic;
    }

    public void setIdentityPic(String identityPic) {
        this.identityPic = identityPic;
    }
    @Column(name = "nation", length = 55)
    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }
    @Column(name = "partyName", length = 55)
    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }
    @Column(name = "passportNo", length = 55)
    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }
    @Column(name = "signNum", length = 55)
    public String getSignNum() {
        return signNum;
    }

    public void setSignNum(String signNum) {
        this.signNum = signNum;
    }
    @Column(name = "inputtime", length = 55)
    public String getInputtime() {
        return inputtime;
    }

    public void setInputtime(String inputtime) {
        this.inputtime = inputtime;
    }
    @Column(name = "inputname", length = 55)
    public String getInputname() {
        return inputname;
    }

    public void setInputname(String inputname) {
        this.inputname = inputname;
    }
    @Column(name = "updatetime", length = 55)
    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
    @Column(name = "updatename", length = 55)
    public String getUpdatename() {
        return updatename;
    }

    public void setUpdatename(String updatename) {
        this.updatename = updatename;
    }


}
