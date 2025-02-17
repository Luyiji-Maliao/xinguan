package com.usci.tool.entity;
import javax.persistence.*;

public class PlusTj {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9145219021988880751L;

	//样本编号
	private String sampleNo;
	
	//受检材料
	private String sjcl;
	
	//抽血时间
	private String bloodTime;
	
	//送检医院
	private String testedHospital;
	
	//孕妇姓名
	private String mamaName;
	
	//姓名拼音
	private String pinyin;
	
	//门诊号/住院号
	private String patientNo;
	
	//年龄
	private String age;
	//出生日期
	private String bornDate;
	//民族
	private String nation;
	//孕周周
	private String preWeek;
	//孕周天
	private String preWeekd;
	//身高
	private String high;
	//体重
	private String weight;
	//末经日期
	private String moJingDate;
	//通讯地址
	private String address;
	
	//电话
	private String phone;
	
	//孕产史孕
	private String yunTime;
	//孕产史产
	private String chanTime;
	//孕产史自然流产
	private String liuTime;
	
	//人流
	private String rl;
	
	//顺产
	private String sc;
	
	//刨宫产
	private String pgc;
	
	//不良孕史无
	private String noHistory;
	
	//宫外孕
	private String exfetation;
	//葡萄胎
	private String puFetus;
	//死胎
	private String dieFetus;
	//新生儿死亡
	private String dieBaby;
	//畸形儿
	private String oaf;
	//简要病史(家族史)
	private String sickHistory;
	
	//夫妻双方染色体核型
	private String ranSeTi;
	
	//孕妇染色体核型
	private String yfrsthx;
	
	//丈夫染色体核型
	private String zfrsthx;
	
	
	//临床诊断
	private String diagnose;
	
	//产前检查
	//妊娠情况
	private String renshenCase;
	

	//试管婴儿 无
	private String tubeBaby;
	//试管婴儿 是
	//胚胎数量
	private String peitaiNum;
	//植入时间
	private String peitaiTime;
	//确定单胎时间
	private String confirmTime;
	//超声检查
	private String chaoTest;
	
	//超声检查 做了 未见异常  NT值
	private String NTValue;

	//筛查模式
	private String scms;
	
	//血清筛查
	private String bloodTest;
	//血清筛查 做了 风险值 21三体
	private String riskValueErYi;
	
	//血清筛查 做了 风险值18三体
	private String riskValueYiBa;
	//血清筛查 做了 风险值NTD
	private String riskValueNTD;

	//预约介入性刺穿手术
	private String noOpretion;
	
	//预约介入性刺穿手术 时间
	private String opretionTime;
	
	//其他信息
	//干细胞治疗 无
	private String noGan;
	
	//细胞治疗
	private String noCellTherapy;
	
	
	//移植手术
	private String noYizhiOperation;
	
	//肿瘤患者
	private String notCancerPatients;
	
	//异体输血
	private String noBloodTransfusion;
	
	//免疫治疗
	private String myzl;
	
	//孕妇签字
	private String sign;
	//身份证号
	private String IDCardNo;
	
	//签字日期
	private String signTime;
	//孕妇同意
	private String agree;
	//签字日期
	private String agreeTime;
	
	private String bz;
	
	/**--------------------主表信息------------------**/	
	//样本编号
	//孕妇姓名
	//一录人
	private String ylr;
	//一录时间
	private String yltime;
	//二录人
	private String elr;
	//二录时间
	private String eltime;
	//一审人
	private String ysr;
	//一审时间
	private String ystime;
	//二审人
	private String esr;
	//二审时间
	private String estime;
	//上传时间
	private String sctime;
	//更新时间
	private String updatetime;
	//图片修改时间
	//图片名称
	private String img;
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String getRiskValueYiBa() {
		return riskValueYiBa;
	}
	public void setRiskValueYiBa(String riskValueYiBa) {
		this.riskValueYiBa = riskValueYiBa;
	}
	public String getRiskValueNTD() {
		return riskValueNTD;
	}
	public void setRiskValueNTD(String riskValueNTD) {
		this.riskValueNTD = riskValueNTD;
	}
	public String getSampleNo() {
		return sampleNo;
	}
	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}
	public String getSjcl() {
		return sjcl;
	}
	public void setSjcl(String sjcl) {
		this.sjcl = sjcl;
	}
	public String getBloodTime() {
		return bloodTime;
	}
	public void setBloodTime(String bloodTime) {
		this.bloodTime = bloodTime;
	}
	public String getTestedHospital() {
		return testedHospital;
	}
	public void setTestedHospital(String testedHospital) {
		this.testedHospital = testedHospital;
	}
	public String getMamaName() {
		return mamaName;
	}
	public void setMamaName(String mamaName) {
		this.mamaName = mamaName;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getPatientNo() {
		return patientNo;
	}
	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getBornDate() {
		return bornDate;
	}
	public void setBornDate(String bornDate) {
		this.bornDate = bornDate;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getPreWeek() {
		return preWeek;
	}
	public void setPreWeek(String preWeek) {
		this.preWeek = preWeek;
	}
	public String getPreWeekd() {
		return preWeekd;
	}
	public void setPreWeekd(String preWeekd) {
		this.preWeekd = preWeekd;
	}
	public String getHigh() {
		return high;
	}
	public void setHigh(String high) {
		this.high = high;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getMoJingDate() {
		return moJingDate;
	}
	public void setMoJingDate(String moJingDate) {
		this.moJingDate = moJingDate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getYunTime() {
		return yunTime;
	}
	public void setYunTime(String yunTime) {
		this.yunTime = yunTime;
	}
	public String getChanTime() {
		return chanTime;
	}
	public void setChanTime(String chanTime) {
		this.chanTime = chanTime;
	}
	public String getLiuTime() {
		return liuTime;
	}
	public void setLiuTime(String liuTime) {
		this.liuTime = liuTime;
	}
	public String getRl() {
		return rl;
	}
	public void setRl(String rl) {
		this.rl = rl;
	}
	public String getSc() {
		return sc;
	}
	public void setSc(String sc) {
		this.sc = sc;
	}
	public String getPgc() {
		return pgc;
	}
	public void setPgc(String pgc) {
		this.pgc = pgc;
	}
	public String getNoHistory() {
		return noHistory;
	}
	public void setNoHistory(String noHistory) {
		this.noHistory = noHistory;
	}
	public String getExfetation() {
		return exfetation;
	}
	public void setExfetation(String exfetation) {
		this.exfetation = exfetation;
	}
	public String getPuFetus() {
		return puFetus;
	}
	public void setPuFetus(String puFetus) {
		this.puFetus = puFetus;
	}
	public String getDieFetus() {
		return dieFetus;
	}
	public void setDieFetus(String dieFetus) {
		this.dieFetus = dieFetus;
	}
	public String getDieBaby() {
		return dieBaby;
	}
	public void setDieBaby(String dieBaby) {
		this.dieBaby = dieBaby;
	}
	public String getOaf() {
		return oaf;
	}
	public void setOaf(String oaf) {
		this.oaf = oaf;
	}
	public String getSickHistory() {
		return sickHistory;
	}
	public void setSickHistory(String sickHistory) {
		this.sickHistory = sickHistory;
	}
	public String getRanSeTi() {
		return ranSeTi;
	}
	public void setRanSeTi(String ranSeTi) {
		this.ranSeTi = ranSeTi;
	}
	public String getYfrsthx() {
		return yfrsthx;
	}
	public void setYfrsthx(String yfrsthx) {
		this.yfrsthx = yfrsthx;
	}
	public String getZfrsthx() {
		return zfrsthx;
	}
	public void setZfrsthx(String zfrsthx) {
		this.zfrsthx = zfrsthx;
	}
	public String getDiagnose() {
		return diagnose;
	}
	public void setDiagnose(String diagnose) {
		this.diagnose = diagnose;
	}
	public String getRenshenCase() {
		return renshenCase;
	}
	public void setRenshenCase(String renshenCase) {
		this.renshenCase = renshenCase;
	}
	public String getTubeBaby() {
		return tubeBaby;
	}
	public void setTubeBaby(String tubeBaby) {
		this.tubeBaby = tubeBaby;
	}
	public String getPeitaiNum() {
		return peitaiNum;
	}
	public void setPeitaiNum(String peitaiNum) {
		this.peitaiNum = peitaiNum;
	}
	public String getPeitaiTime() {
		return peitaiTime;
	}
	public void setPeitaiTime(String peitaiTime) {
		this.peitaiTime = peitaiTime;
	}
	public String getConfirmTime() {
		return confirmTime;
	}
	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}
	public String getChaoTest() {
		return chaoTest;
	}
	public void setChaoTest(String chaoTest) {
		this.chaoTest = chaoTest;
	}
	public String getNTValue() {
		return NTValue;
	}
	public void setNTValue(String nTValue) {
		NTValue = nTValue;
	}
	public String getBloodTest() {
		return bloodTest;
	}
	public void setBloodTest(String bloodTest) {
		this.bloodTest = bloodTest;
	}
	public String getRiskValueErYi() {
		return riskValueErYi;
	}
	public void setRiskValueErYi(String riskValueErYi) {
		this.riskValueErYi = riskValueErYi;
	}
	public String getNoOpretion() {
		return noOpretion;
	}
	public void setNoOpretion(String noOpretion) {
		this.noOpretion = noOpretion;
	}
	public String getOpretionTime() {
		return opretionTime;
	}
	public void setOpretionTime(String opretionTime) {
		this.opretionTime = opretionTime;
	}
	public String getNoGan() {
		return noGan;
	}
	public void setNoGan(String noGan) {
		this.noGan = noGan;
	}
	public String getNoCellTherapy() {
		return noCellTherapy;
	}
	public void setNoCellTherapy(String noCellTherapy) {
		this.noCellTherapy = noCellTherapy;
	}
	public String getNoYizhiOperation() {
		return noYizhiOperation;
	}
	public void setNoYizhiOperation(String noYizhiOperation) {
		this.noYizhiOperation = noYizhiOperation;
	}
	public String getNotCancerPatients() {
		return notCancerPatients;
	}
	public void setNotCancerPatients(String notCancerPatients) {
		this.notCancerPatients = notCancerPatients;
	}
	public String getNoBloodTransfusion() {
		return noBloodTransfusion;
	}
	public void setNoBloodTransfusion(String noBloodTransfusion) {
		this.noBloodTransfusion = noBloodTransfusion;
	}
	public String getMyzl() {
		return myzl;
	}
	public void setMyzl(String myzl) {
		this.myzl = myzl;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getIDCardNo() {
		return IDCardNo;
	}
	public void setIDCardNo(String iDCardNo) {
		IDCardNo = iDCardNo;
	}
	public String getSignTime() {
		return signTime;
	}
	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}
	public String getAgree() {
		return agree;
	}
	public void setAgree(String agree) {
		this.agree = agree;
	}
	public String getAgreeTime() {
		return agreeTime;
	}
	public void setAgreeTime(String agreeTime) {
		this.agreeTime = agreeTime;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getYlr() {
		return ylr;
	}
	public void setYlr(String ylr) {
		this.ylr = ylr;
	}
	public String getYltime() {
		return yltime;
	}
	public void setYltime(String yltime) {
		this.yltime = yltime;
	}
	public String getElr() {
		return elr;
	}
	public void setElr(String elr) {
		this.elr = elr;
	}
	public String getEltime() {
		return eltime;
	}
	public void setEltime(String eltime) {
		this.eltime = eltime;
	}
	public String getYsr() {
		return ysr;
	}
	public void setYsr(String ysr) {
		this.ysr = ysr;
	}
	public String getYstime() {
		return ystime;
	}
	public void setYstime(String ystime) {
		this.ystime = ystime;
	}
	public String getEsr() {
		return esr;
	}
	public void setEsr(String esr) {
		this.esr = esr;
	}
	public String getEstime() {
		return estime;
	}
	public void setEstime(String estime) {
		this.estime = estime;
	}
	public String getSctime() {
		return sctime;
	}
	public void setSctime(String sctime) {
		this.sctime = sctime;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getScms() {
		return scms;
	}
	public void setScms(String scms) {
		this.scms = scms;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
