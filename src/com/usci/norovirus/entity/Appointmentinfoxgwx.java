package com.usci.norovirus.entity;

import javax.persistence.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * SampleJd entity. @author MyEclipse Persistence Tools
 * 预约表
 */
@Entity
@Table(name = "xg_appointmentinfo")
public class Appointmentinfoxgwx implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
	private Integer id;
	private String openid;
	private String name;
	private String sex;
	private String age;
	private String sending;
	private String sfz;
	private String phone;
	private String isgeli;
	private String comebjtime;
	private String comebjreason;
	private String yuyuedate;
	private String yuyuetime;
	private String shuihao;
	private String zhucedizhi;
	private String zhucedianhua;
	private String kaihuyinhang;
	private String yinhangzhanghao;
	private String fapiaotype;
	private String accessname;
	private String yuyuenum; //'预约号'
	private String outtradeno; // '商户单号'
	private String totalfee; //'付款金额'
	private String isfapiao; //'是否要发票'
	private String fapiaotaitou; //'发票抬头'
	private String caiyangdidian; //'采样点'
	private String accessphone;
	private String accessaddress;
	private String accessemail;
	private String isagree;
	private String issuccessed;
	private String xgreason;
	private String subjecttype;
	private String cooperationstr;
	private String sampleNo;
	private String qudao;
	private String shicaiyangdian;
	private String checkProject;
	private String duihuanma;
	private String sampleType;
	private String daoyangdate;
	private String inspection;
	private String inputTime;
	private String inputName;
	private String updateTime;
	private String  updateName;
	private String customerbindtime;
	private String samplebindtime;
	@Column(length = 55)
	private String reportdate;
	private String englishName;
	private String passport;
	private String englishreport;

	/**
	 * 退款信息
	 */
    @Transient
	private String tuikuan;
    @Transient
	private String tuikuandate;
    @Transient
	private String tuikuanname;
	@Transient
	private String tuikuanyuanyin;
	@Transient
	private String tuikuanjine;
	@Transient
	private String shenqingdate;
	@Transient
	private String shenqingname;
	@Transient
	private String shijijine;

    @Transient
	private String caiyangdian;

    /**
     * 销售信息、销售姓名、合作机构名称、子机构名称、客户人数、权限字段等信息
     */
    @Transient
    private String salename;
    @Transient
	private String cooperationcompanyname;
    @Transient
	private String cooperationsubcompanyname;
    @Transient
	private String businessmanager;
    @Transient
	private BigInteger renshu;
    @Transient
	private String permissionperson;

    @Transient
    private String startdate;
    @Transient
    private String enddate;

	/**
	 * 发票信息
	 */
	@Transient
	private String fapiaostate;
	@Transient
	private String kaipiaodate;
	@Transient
	private String kaipiaono;
	//开票状态
	@Transient
	private String kaipiaostate;
	//手续费
	@Transient
	private String shouxufei;
	//进账金额
	@Transient
	private String jinzhangmoney;
	//汇款金额
	@Transient
	private String collectionmethod;
	//开票金额
	@Transient
	private String kaipiaomoney;
	//回款状态
	@Transient
	private String huikuanstate;
    @Transient
    private Object shicaicount;
    //现场
    @Transient
    private BigDecimal xianchang;
    //线上
    @Transient
    private BigDecimal xianshang;
    //合计
    @Transient
    private BigInteger heji;
    //实际
    @Transient
    private BigInteger shiji;
    @Transient
    private String tuanname;
    /*团体预约人数*/
	@Transient
	private BigDecimal tuanti;
	/*京东预约人数*/
	@Transient
	private BigDecimal jingdong;
	/*华大渠道预约人数*/
	@Transient
	private BigDecimal huada;


	@Transient
	private Norovirus norovirus;
    @Transient
    private String reportState;


	public BigDecimal getTuanti() {
		return tuanti;
	}

	public void setTuanti(BigDecimal tuanti) {
		this.tuanti = tuanti;
	}

	public BigDecimal getJingdong() {
		return jingdong;
	}

	public void setJingdong(BigDecimal jingdong) {
		this.jingdong = jingdong;
	}

	public BigDecimal getHuada() {
		return huada;
	}

	public void setHuada(BigDecimal huada) {
		this.huada = huada;
	}

	public Object getShicaicount() {
        return shicaicount;
    }

    public String getTuanname() {
        return tuanname;
    }

    public void setTuanname(String tuanname) {
        this.tuanname = tuanname;
    }

    public BigDecimal getXianchang() {
        return xianchang;
    }

    public void setXianchang(BigDecimal xianchang) {
        this.xianchang = xianchang;
    }

    public BigDecimal getXianshang() {
        return xianshang;
    }

    public void setXianshang(BigDecimal xianshang) {
        this.xianshang = xianshang;
    }

    public BigInteger getHeji() {
        return heji;
    }

    public void setHeji(BigInteger heji) {
        this.heji = heji;
    }

    public BigInteger getShiji() {
        return shiji;
    }

    public void setShiji(BigInteger shiji) {
        this.shiji = shiji;
    }
    public void setShicaicount(Object shicaicount) {
        this.shicaicount = shicaicount;
    }
    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSending() {
		return sending;
	}

	public void setSending(String sending) {
		this.sending = sending;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSfz() {
		return sfz;
	}

	public void setSfz(String sfz) {
		this.sfz = sfz;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIsgeli() {
		return isgeli;
	}

	public void setIsgeli(String isgeli) {
		this.isgeli = isgeli;
	}

	public String getComebjtime() {
		return comebjtime;
	}

	public void setComebjtime(String comebjtime) {
		this.comebjtime = comebjtime;
	}

	public String getComebjreason() {
		return comebjreason;
	}

	public void setComebjreason(String comebjreason) {
		this.comebjreason = comebjreason;
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

	public String getShuihao() {
		return shuihao;
	}

	public void setShuihao(String shuihao) {
		this.shuihao = shuihao;
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

	public String getYuyuenum() {
		return yuyuenum;
	}

	public void setYuyuenum(String yuyuenum) {
		this.yuyuenum = yuyuenum;
	}


	public String getOuttradeno() {
		return outtradeno;
	}

	public void setOuttradeno(String outtradeno) {
		this.outtradeno = outtradeno;
	}

	public String getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(String totalfee) {
		this.totalfee = totalfee;
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

	public String getCaiyangdidian() {
		return caiyangdidian;
	}

	public void setCaiyangdidian(String caiyangdidian) {
		this.caiyangdidian = caiyangdidian;
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

	public String getIsagree() {
		return isagree;
	}

	public void setIsagree(String isagree) {
		this.isagree = isagree;
	}

	public String getIssuccessed() {
		return issuccessed;
	}

	public void setIssuccessed(String issuccessed) {
		this.issuccessed = issuccessed;
	}

	public String getXgreason() {
		return xgreason;
	}

	public void setXgreason(String xgreason) {
		this.xgreason = xgreason;
	}

	public String getSubjecttype() {
		return subjecttype;
	}

	public void setSubjecttype(String subjecttype) {
		this.subjecttype = subjecttype;
	}

	public String getCooperationstr() {
		return cooperationstr;
	}

	public void setCooperationstr(String cooperationstr) {
		this.cooperationstr = cooperationstr;
	}

	public String getSampleNo() {
		return sampleNo;
	}

	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}

	public String getQudao() {
		return qudao;
	}

	public void setQudao(String qudao) {
		this.qudao = qudao;
	}

	public String getShicaiyangdian() {
		return shicaiyangdian;
	}

	public void setShicaiyangdian(String shicaiyangdian) {
		this.shicaiyangdian = shicaiyangdian;
	}

	public String getCheckProject() {
		return checkProject;
	}

	public void setCheckProject(String checkProject) {
		this.checkProject = checkProject;
	}

	public String getDuihuanma() {
		return duihuanma;
	}

	public void setDuihuanma(String duihuanma) {
		this.duihuanma = duihuanma;
	}

	public String getSampleType() {
		return sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}

	public String getDaoyangdate() {
		return daoyangdate;
	}

	public void setDaoyangdate(String daoyangdate) {
		this.daoyangdate = daoyangdate;
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

	public String getInspection() {
		return inspection;
	}

	public void setInspection(String inspection) {
		this.inspection = inspection;
	}
	public String getCustomerbindtime() {
		return customerbindtime;
	}

	public void setCustomerbindtime(String customerbindtime) {
		this.customerbindtime = customerbindtime;
	}

	public String getSamplebindtime() {
		return samplebindtime;
	}

	public void setSamplebindtime(String samplebindtime) {
		this.samplebindtime = samplebindtime;
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

    public String getCaiyangdian() {
        return caiyangdian;
    }

    public void setCaiyangdian(String caiyangdian) {
        this.caiyangdian = caiyangdian;
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

    public BigInteger getRenshu() {
        return renshu;
    }

    public void setRenshu(BigInteger renshu) {
        this.renshu = renshu;
    }

    public String getPermissionperson() {
        return permissionperson;
    }

    public void setPermissionperson(String permissionperson) {
        this.permissionperson = permissionperson;
    }

    public Norovirus getNorovirus() {
        return norovirus;
    }

    public void setNorovirus(Norovirus norovirus) {
        this.norovirus = norovirus;
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


	public String getFapiaostate() {
		return fapiaostate;
	}

	public void setFapiaostate(String fapiaostate) {
		this.fapiaostate = fapiaostate;
	}

	public String getKaipiaodate() {
		return kaipiaodate;
	}

	public void setKaipiaodate(String kaipiaodate) {
		this.kaipiaodate = kaipiaodate;
	}

	public String getKaipiaono() {
		return kaipiaono;
	}

	public void setKaipiaono(String kaipiaono) {
		this.kaipiaono = kaipiaono;
	}

	public String getKaipiaostate() {
		return kaipiaostate;
	}

	public void setKaipiaostate(String kaipiaostate) {
		this.kaipiaostate = kaipiaostate;
	}

	public String getShouxufei() {
		return shouxufei;
	}

	public void setShouxufei(String shouxufei) {
		this.shouxufei = shouxufei;
	}

	public String getJinzhangmoney() {
		return jinzhangmoney;
	}

	public void setJinzhangmoney(String jinzhangmoney) {
		this.jinzhangmoney = jinzhangmoney;
	}

	public String getCollectionmethod() {
		return collectionmethod;
	}

	public void setCollectionmethod(String collectionmethod) {
		this.collectionmethod = collectionmethod;
	}

	public String getKaipiaomoney() {
		return kaipiaomoney;
	}

	public void setKaipiaomoney(String kaipiaomoney) {
		this.kaipiaomoney = kaipiaomoney;
	}

	public String getHuikuanstate() {
		return huikuanstate;
	}

	public void setHuikuanstate(String huikuanstate) {
		this.huikuanstate = huikuanstate;
	}

	public String getReportdate() {
		return reportdate;
	}

	public void setReportdate(String reportdate) {
		this.reportdate = reportdate;
	}

	public String getReportState() {
		return reportState;
	}

	public void setReportState(String reportState) {
		this.reportState = reportState;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getEnglishreport() {
		return englishreport;
	}

	public void setEnglishreport(String englishreport) {
		this.englishreport = englishreport;
	}

	@Override
	public String toString() {
		return "Appointmentinfoxgwx{" +
				"tuikuan='" + tuikuan + '\'' +
				'}';
	}
}