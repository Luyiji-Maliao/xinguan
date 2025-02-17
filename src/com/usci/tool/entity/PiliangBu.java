package com.usci.tool.entity;
import javax.persistence.*;

import javax.persistence.Column;

/**
 * 肿瘤补录批量添加json数组转实体用
 * **/
public class PiliangBu{
	// 姓名
	private String name;
	// 性别
	private String sex;
	// 年龄
	private String age;
	// 身份证号
	private String sfzh;
	// 联系电话
	private String phone;
	// 邮箱
	private String email;
	// 通讯地址
	private String address;
	// 个人疾病史
	private String grjbs;
	// 肿瘤家族史
	private String zljzs;
	// 送检单位
	private String hospital;
	// 送检科室
	private String sjks;
	// 送检医师
	private String doctor;
	// 确诊年龄
	private String qznl;
	// 临床诊断
	private String lczd;
	// 临床分期
	private String lcfq;
	// 已有病理检测结果
	private String jcjg;
	// 治疗史
	private String zls;
	// 来源器官
	private String lyqg;
	// 原病发灶
	private String ybfz;
	// 淋巴结转移处
	private String lbjzyc;
	// 远处转移处
	private String yczyc;
	// 手术石蜡切片
	private String sslqp;
	// 手术石蜡切片张
	private String sslqpz;
	// 手术石蜡切片厚
	private String sslqph;
	// 手术新鲜组织
	private String sxxzz;
	// 手术新鲜组织个
	private String sxxzzg;
	// 穿刺石蜡切片
	private String cslqp;
	// 穿刺石蜡切片张
	private String cslqpz;
	// 穿刺石蜡切片厚
	private String cslqph;
	// 穿刺新鲜组织
	private String cxxzz;
	// 穿刺新鲜组织个
	private String cxxzzg;
	// 胸腹水
	private String xfs;
	// 胸腹水ml
	private String xfsml;
	// 外周血
	private String wzx;
	// 外周血ml
	private String wzxml;
	// 口腔拭子
	private String kqsz;
	// 其他
	private String qt;
	// 其他划线
	private String qtpd;
	// 样本采集日期
	private String ybdate;
	// 其他说明
	private String qtsm;
	
	// 优替检测样本编号
	private String yt;
	// 标准版(60基因)
	private String ytbz;
	// 高级版(549基因)
	private String ytgj;
	// 单项肿瘤
	private String ytdx;
	// 优替单项框
	private String ytdxpd;
	// 化疗用药
	private String ythl;
	// MSI(微卫星不稳定性)
	private String ytmsi;
	// MSI样本编号
	private String msi;
	// 乳腺癌21基因
	private String yt21;
	// 21基因样本编号
	private String jy21;
	// 其他
	private String ytqt;
	// 优替其他框
	private String ytqtpd;
	
	// 优旭ctDNA检测套餐
	private String yxct;
	// ctdna标准版
	private String yxctbz;
	// ctdna高级版
	private String yxctgj;
	// ctdna单项
	private String yxctdx;
	// ctdna单项框
	private String yxctdxpd;
	// ctdna其他
	private String yxqt;
	// ctdna其他框
	private String yxqtpd;
	// 优旭CTC检测套餐
	private String yxctc;
	// ctc结直肠癌
	private String yxctcjzca;
	// ctc其他
	private String yxctcqt;
	// ctc其他框
	private String yxctcqtpd;
	
	
	
	// 优逸检测套餐
	private String yy;
	// 优逸全套
	private String yyqu;
	// 优逸男性
	private String yymale;
	// 优逸女性
	private String yyfemale;
	// 优逸brca
	private String yybrca;
	// 优逸单项
	private String yydx;
	// 优逸单项框
	private String yydxpd;
	// 优逸其他
	private String yyqt;
	// 优逸其他框
	private String yyqtpd;
	
	
	// 受检者签名
	private String sjzqm;
	// 监护人签名
	private String jhrqm;
	// 与受检者关系
	private String relation;
	// 受检者签名日期
	private String sjzdate;
	// 送检医生签名
	private String doctorqm;
	// 送检医生签名日期
	private String doctordate;
	// 备注
	private String bz;
	// 收费情况
	private String sfqk;
	//送检单更正
	private String sjdgz;
	// 一录人
	private String ylr;
	// 一录时间
	private String yltime;
	// 二录人
	private String elr;
	// 二录时间
	private String eltime;
	// 一审人
	private String ysr;
	// 一审时间
	private String ystime;
	// 二审人
	private String esr;
	// 二审时间
	private String estime;
	// 图片上传时间
	private String uploadtime;
	// 图片名称(路径)
	private String img;
	
	//肿瘤临时补录 解读诊断
	private String readDiagnosis;
	
	
	public String getReadDiagnosis() {
		return readDiagnosis;
	}
	public void setReadDiagnosis(String readDiagnosis) {
		this.readDiagnosis = readDiagnosis;
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
	public String getSfzh() {
		return sfzh;
	}
	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGrjbs() {
		return grjbs;
	}
	public void setGrjbs(String grjbs) {
		this.grjbs = grjbs;
	}
	public String getZljzs() {
		return zljzs;
	}
	public void setZljzs(String zljzs) {
		this.zljzs = zljzs;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getSjks() {
		return sjks;
	}
	public void setSjks(String sjks) {
		this.sjks = sjks;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getQznl() {
		return qznl;
	}
	public void setQznl(String qznl) {
		this.qznl = qznl;
	}
	public String getLczd() {
		return lczd;
	}
	public void setLczd(String lczd) {
		this.lczd = lczd;
	}
	public String getLcfq() {
		return lcfq;
	}
	public void setLcfq(String lcfq) {
		this.lcfq = lcfq;
	}
	public String getJcjg() {
		return jcjg;
	}
	public void setJcjg(String jcjg) {
		this.jcjg = jcjg;
	}
	public String getZls() {
		return zls;
	}
	public void setZls(String zls) {
		this.zls = zls;
	}
	public String getLyqg() {
		return lyqg;
	}
	public void setLyqg(String lyqg) {
		this.lyqg = lyqg;
	}
	public String getYbfz() {
		return ybfz;
	}
	public void setYbfz(String ybfz) {
		this.ybfz = ybfz;
	}
	public String getLbjzyc() {
		return lbjzyc;
	}
	public void setLbjzyc(String lbjzyc) {
		this.lbjzyc = lbjzyc;
	}
	public String getYczyc() {
		return yczyc;
	}
	public void setYczyc(String yczyc) {
		this.yczyc = yczyc;
	}
	public String getSslqp() {
		return sslqp;
	}
	public void setSslqp(String sslqp) {
		this.sslqp = sslqp;
	}
	public String getSslqpz() {
		return sslqpz;
	}
	public void setSslqpz(String sslqpz) {
		this.sslqpz = sslqpz;
	}
	public String getSslqph() {
		return sslqph;
	}
	public void setSslqph(String sslqph) {
		this.sslqph = sslqph;
	}
	public String getSxxzz() {
		return sxxzz;
	}
	public void setSxxzz(String sxxzz) {
		this.sxxzz = sxxzz;
	}
	public String getSxxzzg() {
		return sxxzzg;
	}
	public void setSxxzzg(String sxxzzg) {
		this.sxxzzg = sxxzzg;
	}
	public String getCslqp() {
		return cslqp;
	}
	public void setCslqp(String cslqp) {
		this.cslqp = cslqp;
	}
	public String getCslqpz() {
		return cslqpz;
	}
	public void setCslqpz(String cslqpz) {
		this.cslqpz = cslqpz;
	}
	public String getCslqph() {
		return cslqph;
	}
	public void setCslqph(String cslqph) {
		this.cslqph = cslqph;
	}
	public String getCxxzz() {
		return cxxzz;
	}
	public void setCxxzz(String cxxzz) {
		this.cxxzz = cxxzz;
	}
	public String getCxxzzg() {
		return cxxzzg;
	}
	public void setCxxzzg(String cxxzzg) {
		this.cxxzzg = cxxzzg;
	}
	public String getXfs() {
		return xfs;
	}
	public void setXfs(String xfs) {
		this.xfs = xfs;
	}
	public String getXfsml() {
		return xfsml;
	}
	public void setXfsml(String xfsml) {
		this.xfsml = xfsml;
	}
	public String getWzx() {
		return wzx;
	}
	public void setWzx(String wzx) {
		this.wzx = wzx;
	}
	public String getWzxml() {
		return wzxml;
	}
	public void setWzxml(String wzxml) {
		this.wzxml = wzxml;
	}
	public String getKqsz() {
		return kqsz;
	}
	public void setKqsz(String kqsz) {
		this.kqsz = kqsz;
	}
	public String getQt() {
		return qt;
	}
	public void setQt(String qt) {
		this.qt = qt;
	}
	public String getQtpd() {
		return qtpd;
	}
	public void setQtpd(String qtpd) {
		this.qtpd = qtpd;
	}
	public String getYbdate() {
		return ybdate;
	}
	public void setYbdate(String ybdate) {
		this.ybdate = ybdate;
	}
	public String getQtsm() {
		return qtsm;
	}
	public void setQtsm(String qtsm) {
		this.qtsm = qtsm;
	}
	public String getYy() {
		return yy;
	}
	public void setYy(String yy) {
		this.yy = yy;
	}
	public String getYyqu() {
		return yyqu;
	}
	public void setYyqu(String yyqu) {
		this.yyqu = yyqu;
	}
	public String getYymale() {
		return yymale;
	}
	public void setYymale(String yymale) {
		this.yymale = yymale;
	}
	public String getYyfemale() {
		return yyfemale;
	}
	public void setYyfemale(String yyfemale) {
		this.yyfemale = yyfemale;
	}
	public String getSjzqm() {
		return sjzqm;
	}
	public void setSjzqm(String sjzqm) {
		this.sjzqm = sjzqm;
	}
	public String getJhrqm() {
		return jhrqm;
	}
	public void setJhrqm(String jhrqm) {
		this.jhrqm = jhrqm;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getSjzdate() {
		return sjzdate;
	}
	public void setSjzdate(String sjzdate) {
		this.sjzdate = sjzdate;
	}
	public String getDoctorqm() {
		return doctorqm;
	}
	public void setDoctorqm(String doctorqm) {
		this.doctorqm = doctorqm;
	}
	public String getDoctordate() {
		return doctordate;
	}
	public void setDoctordate(String doctordate) {
		this.doctordate = doctordate;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getSfqk() {
		return sfqk;
	}
	public void setSfqk(String sfqk) {
		this.sfqk = sfqk;
	}
	public String getSjdgz() {
		return sjdgz;
	}
	public void setSjdgz(String sjdgz) {
		this.sjdgz = sjdgz;
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
	public String getUploadtime() {
		return uploadtime;
	}
	public void setUploadtime(String uploadtime) {
		this.uploadtime = uploadtime;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getYt() {
		return yt;
	}
	public void setYt(String yt) {
		this.yt = yt;
	}
	public String getYtbz() {
		return ytbz;
	}
	public void setYtbz(String ytbz) {
		this.ytbz = ytbz;
	}
	public String getYtgj() {
		return ytgj;
	}
	public void setYtgj(String ytgj) {
		this.ytgj = ytgj;
	}
	public String getYtdx() {
		return ytdx;
	}
	public void setYtdx(String ytdx) {
		this.ytdx = ytdx;
	}
	public String getYtdxpd() {
		return ytdxpd;
	}
	public void setYtdxpd(String ytdxpd) {
		this.ytdxpd = ytdxpd;
	}
	public String getYthl() {
		return ythl;
	}
	public void setYthl(String ythl) {
		this.ythl = ythl;
	}
	public String getYtmsi() {
		return ytmsi;
	}
	public void setYtmsi(String ytmsi) {
		this.ytmsi = ytmsi;
	}
	public String getMsi() {
		return msi;
	}
	public void setMsi(String msi) {
		this.msi = msi;
	}
	public String getYt21() {
		return yt21;
	}
	public void setYt21(String yt21) {
		this.yt21 = yt21;
	}
	public String getJy21() {
		return jy21;
	}
	public void setJy21(String jy21) {
		this.jy21 = jy21;
	}
	public String getYtqt() {
		return ytqt;
	}
	public void setYtqt(String ytqt) {
		this.ytqt = ytqt;
	}
	public String getYtqtpd() {
		return ytqtpd;
	}
	public void setYtqtpd(String ytqtpd) {
		this.ytqtpd = ytqtpd;
	}
	public String getYxct() {
		return yxct;
	}
	public void setYxct(String yxct) {
		this.yxct = yxct;
	}
	public String getYxctbz() {
		return yxctbz;
	}
	public void setYxctbz(String yxctbz) {
		this.yxctbz = yxctbz;
	}
	public String getYxctgj() {
		return yxctgj;
	}
	public void setYxctgj(String yxctgj) {
		this.yxctgj = yxctgj;
	}
	public String getYxctdx() {
		return yxctdx;
	}
	public void setYxctdx(String yxctdx) {
		this.yxctdx = yxctdx;
	}
	public String getYxctdxpd() {
		return yxctdxpd;
	}
	public void setYxctdxpd(String yxctdxpd) {
		this.yxctdxpd = yxctdxpd;
	}
	public String getYxqt() {
		return yxqt;
	}
	public void setYxqt(String yxqt) {
		this.yxqt = yxqt;
	}
	public String getYxqtpd() {
		return yxqtpd;
	}
	public void setYxqtpd(String yxqtpd) {
		this.yxqtpd = yxqtpd;
	}
	public String getYxctc() {
		return yxctc;
	}
	public void setYxctc(String yxctc) {
		this.yxctc = yxctc;
	}
	public String getYxctcjzca() {
		return yxctcjzca;
	}
	public void setYxctcjzca(String yxctcjzca) {
		this.yxctcjzca = yxctcjzca;
	}
	public String getYxctcqt() {
		return yxctcqt;
	}
	public void setYxctcqt(String yxctcqt) {
		this.yxctcqt = yxctcqt;
	}
	public String getYxctcqtpd() {
		return yxctcqtpd;
	}
	public void setYxctcqtpd(String yxctcqtpd) {
		this.yxctcqtpd = yxctcqtpd;
	}
	public String getYybrca() {
		return yybrca;
	}
	public void setYybrca(String yybrca) {
		this.yybrca = yybrca;
	}
	public String getYydx() {
		return yydx;
	}
	public void setYydx(String yydx) {
		this.yydx = yydx;
	}
	public String getYydxpd() {
		return yydxpd;
	}
	public void setYydxpd(String yydxpd) {
		this.yydxpd = yydxpd;
	}
	public String getYyqt() {
		return yyqt;
	}
	public void setYyqt(String yyqt) {
		this.yyqt = yyqt;
	}
	public String getYyqtpd() {
		return yyqtpd;
	}
	public void setYyqtpd(String yyqtpd) {
		this.yyqtpd = yyqtpd;
	}
	
}