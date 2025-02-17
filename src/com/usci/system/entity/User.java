package com.usci.system.entity;
import javax.persistence.*;

import java.util.List;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.common.collect.Lists;

import javax.persistence.GeneratedValue;
@Entity
@Table(name = "sys_user")
@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class User{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 部门id
     */
    @Column(nullable = false)
    private Integer departmentId;
    /**
     * 职位id
     */
    @Column(nullable = false)
    private Integer positionId;
    @Transient
    private Department department;
    @Transient
    private Position position;

    @Transient//@ManyToMany(fetch=FetchType.LAZY)
    private List<Role> roles = Lists.newArrayList();
    /**
     * 登陆账号
     */
    @Column(length = 30, unique = true, nullable = false)
    private String username;
    /**
     * 密码
     */
    @Column(length = 32, nullable = false)
    private String password;
   /**
    * 登陆状态
    */
    private Integer loginStatus;
    /**
     *真实姓名
     */
    @Column(length = 30,unique = true, nullable = false)
    private String name;
    /**
     * 性别
     */
    @Column(length = 2)
    private String sex;
    /**
     * 邮箱
     */
    @Column(length = 30, unique = true, nullable = false)
    private String email;
    /**
     * 手机
     */
    @Column(length = 11)
    private String mobile;
    /**
     * 办公电话
     */
    @Column(length = 20)
    private String phone;
    /**
     * 住址
     */
    @Column(length = 80)
    private String address;
    /**
     * 生日
     */
    @Column(length = 20)
    private String birthday;
    /**
     * 身份证
     */
    @Column(length = 25, unique = true, nullable = false)
    private String idNumber;
    /**
     * 紧急联系人
     */
    @Column(length = 30)
    private String emePerson;
    /**
     * 紧急联系方式
     */
    @Column(length = 30)
    private String emePhone;
    /**
     * 毕业院校
     */
    @Column(length = 30)
    private String school;
    /**
     * 专业
     */
    @Column(length = 30)
    private String userProfession;
    /**
     * 入职时间
     */
    @Column(length = 10)
    private String entryDate;
    /**
     * 转正时间
     */
    @Column(length = 10)
    private String regularDate;
    /**
     * 合同到期时间
     */
    @Column(length = 10)
    private String contractDate;
    /**
     * 离职时间
     */
    @Column(length = 10)
    private String leaveDate;
    @Column(length = 15)
    private String qq;
    /**
     * 工号
     */
    @Column(length = 25)
    private String jobNumber;
    /**
     * 工作性质（全职，兼职，实习）
     */
    @Column(length = 10)
    private String jobType;
    /**
     * 民族
     */
    @Column(length = 10)
    private String nation;
    /**
     * 政治面貌
     */
    @Column(length = 10)
    private String politicsStatus;
    /**
     * 婚姻状况（已婚，未婚）
     */
    @Column(length = 4)
    private String isMarried;
    /**
     * 户口所在地
     */
    @Column(length = 45)
    private String residence;
    /**
     * 籍贯
     */
    @Column(length = 10)
    private String nativePlace;
    /**
     * 第一次参加工作时间
     */
    @Column(length = 10)
    private String firstEntryDate;
    /**
     * 是否签订劳动合同
     */
    @Column(length = 2)
    private String isSignedLc;
    /**
     * 是否签订知识产权及保密协议
     */
    @Column(length = 2)
    private String isSignedIcca;
    /**
     * 户口性质（农村，城市）
     */
    @Column(length = 4)
    private String residenceType;
    /**
     * 是否在本市缴纳过保险
     */
    @Column(length = 2)
    private String isPaidInsur;
    /**
     * 常驻工作地
     */
    @Column(length = 30)
    private String jobLocation;
    /**
     * 学历
     */
    @Column(length = 10)
    private String education;
    /**
     * 备注
     */
    @Column(length = 100)
    private String remark;
    @Column(length = 30)
    private String updateTime;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
    public Integer getPositionId() {
        return positionId;
    }
    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }
    public List<Role> getRoles() {
        return roles;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    @JsonIgnore
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Integer getLoginStatus() {
        return loginStatus;
    }
    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getIdNumber() {
        return idNumber;
    }
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
    public String getEmePerson() {
        return emePerson;
    }
    public void setEmePerson(String emePerson) {
        this.emePerson = emePerson;
    }
    public String getEmePhone() {
        return emePhone;
    }
    public void setEmePhone(String emePhone) {
        this.emePhone = emePhone;
    }
    public String getSchool() {
        return school;
    }
    public void setSchool(String school) {
        this.school = school;
    }
    public String getEntryDate() {
        return entryDate;
    }
    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }
    public String getLeaveDate() {
        return leaveDate;
    }
    public void setLeaveDate(String leaveDate) {
        this.leaveDate = leaveDate;
    }
    public String getQq() {
        return qq;
    }
    public void setQq(String qq) {
        this.qq = qq;
    }
    public String getJobNumber() {
        return jobNumber;
    }
    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }
    public String getJobType() {
        return jobType;
    }
    public void setJobType(String jobType) {
        this.jobType = jobType;
    }
    public String getNation() {
        return nation;
    }
    public void setNation(String nation) {
        this.nation = nation;
    }
    public String getPoliticsStatus() {
        return politicsStatus;
    }
    public void setPoliticsStatus(String politicsStatus) {
        this.politicsStatus = politicsStatus;
    }
    public String getIsMarried() {
        return isMarried;
    }
    public void setIsMarried(String isMarried) {
        this.isMarried = isMarried;
    }
    public String getResidence() {
        return residence;
    }
    public void setResidence(String residence) {
        this.residence = residence;
    }
    public String getNativePlace() {
        return nativePlace;
    }
    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }
    public String getFirstEntryDate() {
        return firstEntryDate;
    }
    public void setFirstEntryDate(String firstEntryDate) {
        this.firstEntryDate = firstEntryDate;
    }
    public String getIsSignedLc() {
        return isSignedLc;
    }
    public void setIsSignedLc(String isSignedLc) {
        this.isSignedLc = isSignedLc;
    }
    public String getIsSignedIcca() {
        return isSignedIcca;
    }
    public void setIsSignedIcca(String isSignedIcca) {
        this.isSignedIcca = isSignedIcca;
    }
    public String getResidenceType() {
        return residenceType;
    }
    public void setResidenceType(String residenceType) {
        this.residenceType = residenceType;
    }
    public String getIsPaidInsur() {
        return isPaidInsur;
    }
    public void setIsPaidInsur(String isPaidInsur) {
        this.isPaidInsur = isPaidInsur;
    }
    public String getJobLocation() {
        return jobLocation;
    }
    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getRegularDate() {
		return regularDate;
	}
	public void setRegularDate(String regularDate) {
		this.regularDate = regularDate;
	}
	public String getContractDate() {
		return contractDate;
	}
	public void setContractDate(String contractDate) {
		this.contractDate = contractDate;
	}
	public String getUserProfession() {
		return userProfession;
	}
	public void setUserProfession(String userProfession) {
		this.userProfession = userProfession;
	}
	
    
    
}