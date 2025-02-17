package com.usci.norovirus.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="xg_bukaifapiao")
public class XgBuKaiFaPiao {
	@Id
	@GeneratedValue
	private Integer id;
	
	//是否需要发票
	@Column(length=100)
	private String isfapiao;

	//税号
	@Column(length=50)
	private String shuihao;

	//发票抬头
	@Column(length=100)
	private String fapiaotaitou;

	//注册地址
	@Column(length=100)
	private String zhucedizhi;

	//注册电话
	@Column(length=100)
	private String zhucedianhua;

	//开户银行
	@Column(length=100)
	private String kaihuyinhang;

	//银行账号
	@Column(length=100)
	private String yinhangzhanghao;

	//发票类型（纸质发票、电子发票）
	@Column(length=100)
	private String fapiaotype;

	//发票收件人姓名
	@Column(length=100)
	private String accessname;

	//发票收件人电话
	@Column(length=100)
	private String accessphone;

	//发票收件人地址
	@Column(length=100)
	private String accessaddress;

	//发票收件人邮箱
	@Column(length=100)
	private String accessemail;



	//预约id
	@Column(length=11)
	private Integer yuyueid;
	
	//补开状态
	@Column(length=5)
	private String bukaistate;
	
	//确认补开人
	@Column(length=30)
	private String bukainame;
	
	//确认补开时间
	@Column(length=20)
	private String bukaitime;
	
	//开票时间
	@Column(length=20)
	private String kaipiaodate;
	
	//开票号
	@Column(length=50)
	private String kaipiaono;
	
	@Column(length=20)
	private String updatename;
	@Column(length=20)
	private String updatetime;
	@Column(length=100)
	private String inputtime;
	@Column(length=100)
	private String inputname;
	
	//预约号
	@Transient
	private String yuyuenum;
	//商户单号
	@Transient
	private String outtradeno; 
	//姓名
	@Transient
	private String name;
	//身份证号
	@Transient
	private String sfz;
	
	
	/*************导出财务Excel需要属性start**************/
	//norvious
	@Transient
	private Norovirus norovirus;
	//付款金额
	@Transient
	private String totalfee;
	//采样点
	@Transient
	private String caiyangdidian;
	//合作机构
	@Transient
	private String cooperationcompanyname;
	//分支机构
	@Transient
	private String cooperationsubcompanyname;
	//业务员
	@Transient
	private String businessmanager;
	//销售姓名
	@Transient
	private String salename;
	//样本编号
	@Transient
	private String sampleNo;
    //补开发票表样本编号
    @Column(length=20)
    private String samplenum;
	//送检人单位
	@Transient
	private String sendingPerson;
	//样本绑定时间
	@Transient
	private String samplebindtime;
	//接收时间
	@Transient
	private String receptionDate;
	//检测项目
	@Transient
	private String checkProject;
	//报告时间
	@Transient
	private String reportDate;
	//送检单位（优迅医学）
	@Transient
	private String inspection;
	
	
	
	/*************导出财务Excel需要属性end**************/

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIsfapiao() {
		return isfapiao;
	}

	public void setIsfapiao(String isfapiao) {
		this.isfapiao = isfapiao;
	}

	public String getShuihao() {
		return shuihao;
	}

	public void setShuihao(String shuihao) {
		this.shuihao = shuihao;
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

	public Integer getYuyueid() {
		return yuyueid;
	}

	public void setYuyueid(Integer yuyueid) {
		this.yuyueid = yuyueid;
	}

	public String getBukaistate() {
		return bukaistate;
	}

	public void setBukaistate(String bukaistate) {
		this.bukaistate = bukaistate;
	}

	public String getBukainame() {
		return bukainame;
	}

	public void setBukainame(String bukainame) {
		this.bukainame = bukainame;
	}

	public String getBukaitime() {
		return bukaitime;
	}

	public void setBukaitime(String bukaitime) {
		this.bukaitime = bukaitime;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSfz() {
		return sfz;
	}

	public void setSfz(String sfz) {
		this.sfz = sfz;
	}

	public Norovirus getNorovirus() {
		return norovirus;
	}

	public void setNorovirus(Norovirus norovirus) {
		this.norovirus = norovirus;
	}

	public String getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(String totalfee) {
		this.totalfee = totalfee;
	}

	public String getCaiyangdidian() {
		return caiyangdidian;
	}

	public void setCaiyangdidian(String caiyangdidian) {
		this.caiyangdidian = caiyangdidian;
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

	public String getSalename() {
		return salename;
	}

	public void setSalename(String salename) {
		this.salename = salename;
	}

	public String getSampleNo() {
		return sampleNo;
	}

	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
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

	public String getUpdatename() {
		return updatename;
	}

	public void setUpdatename(String updatename) {
		this.updatename = updatename;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getInputtime() {
		return inputtime;
	}

	public void setInputtime(String inputtime) {
		this.inputtime = inputtime;
	}

	public String getInputname() {
		return inputname;
	}

	public void setInputname(String inputname) {
		this.inputname = inputname;
	}

	public String getSendingPerson() {
		return sendingPerson;
	}

	public void setSendingPerson(String sendingPerson) {
		this.sendingPerson = sendingPerson;
	}

	public String getSamplebindtime() {
		return samplebindtime;
	}

	public void setSamplebindtime(String samplebindtime) {
		this.samplebindtime = samplebindtime;
	}

	public String getReceptionDate() {
		return receptionDate;
	}

	public void setReceptionDate(String receptionDate) {
		this.receptionDate = receptionDate;
	}

	public String getCheckProject() {
		return checkProject;
	}

	public void setCheckProject(String checkProject) {
		this.checkProject = checkProject;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getInspection() {
		return inspection;
	}

	public void setInspection(String inspection) {
		this.inspection = inspection;
	}

    public String getSamplenum() {
        return samplenum;
    }

    public void setSamplenum(String samplenum) {
        this.samplenum = samplenum;
    }
}
