package com.usci.tool.entity;
import javax.persistence.*;
/**
 * 优馨安补录批量添加json数组转实体用
 * **/
public class PiliangBuYxa {
	//样本编号
	private String num;
	//受检材料
	private String blood;
	//抽血时间
	private String xtime;
	//送检医院
	private String hospital;
	//姓名
	private String name;
	//年龄
	private String age;
	//孕周
	private String haveweekw;
	//孕天
	private String haveweekd;
	//末次月经
	private String lastyj;
	//身份证号
	private String sfzh;
	//姓名拼音
	private String xname;
	//手机号
	private String telphone;
	//出生日期
	private String birthdate;
	//民族
	private String nation;
	//地址
	private String address;
	//重量
	private String weight;
	//临床诊断
	private String lczd;
	//简要病史
	private String jybs;
	//是否异体输血
	private String ytsxpd;
	//异体输血时间
	private String ytsx;
	//是否免疫治疗
	private String myzlpd;
	//免疫治疗时间
	private String myzl;
	//移植手术
	private String yzss;
	//干细胞治疗
	private String gxbzl;
	//B超
	private String bc;
	//B超异常
	private String bcyc;
	//筛查模式
	private String scms;
	//母血清筛查
	private String mxqsc;
	//21三体综合症
	private String santi21;
	//18三体综合症
	private String santi18;
	//ntd
	private String ntd;
	//年龄风险
	private String agedanger;
	//丈夫染色体核型
	private String husband;
	//丈夫染色体核型异常
	private String husbandyc;
	//孕妇染色体核型
	private String wife;
	//孕妇染色体核型异常
	private String wifeyc;
	//孕次
	private String yunc;
	//产次
	private String chanc;
	//人流次
	private String renc;
	//自然流产次
	private String zic;
	//顺产次
	private String shunc;
	//剖腹产次
	private String poc;
	//ivf次
	private String ivfc;
	//宫外孕次
	private String gwyc;
	//葡萄胎次
	private String pttc;
	//死胎次
	private String stc;
	//新生儿死亡
	private String xsesc;
	//畸形儿
	private String jxe;
	//其他说明
	private String qtsm;
	//孕妇签字
	private String yfqz;
	//送检医生
	private String sjys;
	//签字日期
	private String qzrq1;
	//孕妇同意
	private String yfty;
	//签字日期
	private String qzrq2;
	//备注
	private String bz;
	//接收样本时间
	private String receivedatetime;
	//送检单信息更正
	private String sjdgz;
	//身高
	private String stature;
	//超声测定NT孕周-周
	private String csyz;
	//超声测定NT孕周-天
	private String csyt;
	//NT测定值
	private String ntcd;
	//植入胚胎数量
	private String zrpt;
	//植入时间
	private String zrsj;
	//确定胚胎时间
	private String qddt;
	//不良孕产史
	private String blycs;
	//门诊号/住院号
	private String mzh;
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
	

	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getBlood() {
		return blood;
	}
	public void setBlood(String blood) {
		this.blood = blood;
	}
	public String getXtime() {
		return xtime;
	}
	public void setXtime(String xtime) {
		this.xtime = xtime;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getHaveweekw() {
		return haveweekw;
	}
	public void setHaveweekw(String haveweekw) {
		this.haveweekw = haveweekw;
	}
	public String getHaveweekd() {
		return haveweekd;
	}
	public void setHaveweekd(String haveweekd) {
		this.haveweekd = haveweekd;
	}
	public String getLastyj() {
		return lastyj;
	}
	public void setLastyj(String lastyj) {
		this.lastyj = lastyj;
	}
	public String getSfzh() {
		return sfzh;
	}
	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}
	public String getXname() {
		return xname;
	}
	public void setXname(String xname) {
		this.xname = xname;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getLczd() {
		return lczd;
	}
	public void setLczd(String lczd) {
		this.lczd = lczd;
	}
	public String getJybs() {
		return jybs;
	}
	public void setJybs(String jybs) {
		this.jybs = jybs;
	}
	public String getYtsx() {
		return ytsx;
	}
	public void setYtsx(String ytsx) {
		this.ytsx = ytsx;
	}
	public String getYtsxpd() {
		return ytsxpd;
	}
	public void setYtsxpd(String ytsxpd) {
		this.ytsxpd = ytsxpd;
	}
	public String getMyzlpd() {
		return myzlpd;
	}
	public void setMyzlpd(String myzlpd) {
		this.myzlpd = myzlpd;
	}
	public String getMyzl() {
		return myzl;
	}
	public void setMyzl(String myzl) {
		this.myzl = myzl;
	}
	public String getYzss() {
		return yzss;
	}
	public void setYzss(String yzss) {
		this.yzss = yzss;
	}
	public String getGxbzl() {
		return gxbzl;
	}
	public void setGxbzl(String gxbzl) {
		this.gxbzl = gxbzl;
	}
	public String getBc() {
		return bc;
	}
	public void setBc(String bc) {
		this.bc = bc;
	}
	public String getBcyc() {
		return bcyc;
	}
	public void setBcyc(String bcyc) {
		this.bcyc = bcyc;
	}
	public String getScms() {
		return scms;
	}
	public void setScms(String scms) {
		this.scms = scms;
	}
	public String getMxqsc() {
		return mxqsc;
	}
	public void setMxqsc(String mxqsc) {
		this.mxqsc = mxqsc;
	}
	public String getSanti21() {
		return santi21;
	}
	public void setSanti21(String santi21) {
		this.santi21 = santi21;
	}
	public String getSanti18() {
		return santi18;
	}
	public void setSanti18(String santi18) {
		this.santi18 = santi18;
	}
	public String getNtd() {
		return ntd;
	}
	public void setNtd(String ntd) {
		this.ntd = ntd;
	}
	public String getAgedanger() {
		return agedanger;
	}
	public void setAgedanger(String agedanger) {
		this.agedanger = agedanger;
	}
	public String getHusband() {
		return husband;
	}
	public void setHusband(String husband) {
		this.husband = husband;
	}
	public String getHusbandyc() {
		return husbandyc;
	}
	public void setHusbandyc(String husbandyc) {
		this.husbandyc = husbandyc;
	}
	public String getWife() {
		return wife;
	}
	public void setWife(String wife) {
		this.wife = wife;
	}
	public String getWifeyc() {
		return wifeyc;
	}
	public void setWifeyc(String wifeyc) {
		this.wifeyc = wifeyc;
	}
	public String getYunc() {
		return yunc;
	}
	public void setYunc(String yunc) {
		this.yunc = yunc;
	}
	public String getChanc() {
		return chanc;
	}
	public void setChanc(String chanc) {
		this.chanc = chanc;
	}
	public String getRenc() {
		return renc;
	}
	public void setRenc(String renc) {
		this.renc = renc;
	}
	public String getZic() {
		return zic;
	}
	public void setZic(String zic) {
		this.zic = zic;
	}
	public String getShunc() {
		return shunc;
	}
	public void setShunc(String shunc) {
		this.shunc = shunc;
	}
	public String getPoc() {
		return poc;
	}
	public void setPoc(String poc) {
		this.poc = poc;
	}
	public String getIvfc() {
		return ivfc;
	}
	public void setIvfc(String ivfc) {
		this.ivfc = ivfc;
	}
	public String getGwyc() {
		return gwyc;
	}
	public void setGwyc(String gwyc) {
		this.gwyc = gwyc;
	}
	public String getPttc() {
		return pttc;
	}
	public void setPttc(String pttc) {
		this.pttc = pttc;
	}
	public String getStc() {
		return stc;
	}
	public void setStc(String stc) {
		this.stc = stc;
	}
	public String getXsesc() {
		return xsesc;
	}
	public void setXsesc(String xsesc) {
		this.xsesc = xsesc;
	}
	public String getJxe() {
		return jxe;
	}
	public void setJxe(String jxe) {
		this.jxe = jxe;
	}
	public String getQtsm() {
		return qtsm;
	}
	public void setQtsm(String qtsm) {
		this.qtsm = qtsm;
	}
	public String getYfqz() {
		return yfqz;
	}
	public void setYfqz(String yfqz) {
		this.yfqz = yfqz;
	}
	public String getSjys() {
		return sjys;
	}
	public void setSjys(String sjys) {
		this.sjys = sjys;
	}
	public String getQzrq1() {
		return qzrq1;
	}
	public void setQzrq1(String qzrq1) {
		this.qzrq1 = qzrq1;
	}
	public String getYfty() {
		return yfty;
	}
	public void setYfty(String yfty) {
		this.yfty = yfty;
	}
	public String getQzrq2() {
		return qzrq2;
	}
	public void setQzrq2(String qzrq2) {
		this.qzrq2 = qzrq2;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getReceivedatetime() {
		return receivedatetime;
	}
	public void setReceivedatetime(String receivedatetime) {
		this.receivedatetime = receivedatetime;
	}
	public String getSjdgz() {
		return sjdgz;
	}
	public void setSjdgz(String sjdgz) {
		this.sjdgz = sjdgz;
	}
	public String getStature() {
		return stature;
	}
	public void setStature(String stature) {
		this.stature = stature;
	}
	public String getCsyz() {
		return csyz;
	}
	public void setCsyz(String csyz) {
		this.csyz = csyz;
	}
	public String getCsyt() {
		return csyt;
	}
	public void setCsyt(String csyt) {
		this.csyt = csyt;
	}
	public String getNtcd() {
		return ntcd;
	}
	public void setNtcd(String ntcd) {
		this.ntcd = ntcd;
	}
	public String getZrpt() {
		return zrpt;
	}
	public void setZrpt(String zrpt) {
		this.zrpt = zrpt;
	}
	public String getZrsj() {
		return zrsj;
	}
	public void setZrsj(String zrsj) {
		this.zrsj = zrsj;
	}
	public String getQddt() {
		return qddt;
	}
	public void setQddt(String qddt) {
		this.qddt = qddt;
	}
	public String getBlycs() {
		return blycs;
	}
	public void setBlycs(String blycs) {
		this.blycs = blycs;
	}
	public String getMzh() {
		return mzh;
	}
	public void setMzh(String mzh) {
		this.mzh = mzh;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
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

}
