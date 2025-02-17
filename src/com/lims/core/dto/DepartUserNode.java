package com.lims.core.dto;

import javax.persistence.Column;





public class DepartUserNode  {
  

    private Integer id;
   /**
    * 部门名称
    */
    private String deptName;
     /**
      * 是否是叶子节点
      */
	private boolean leaf;
    /**
     * 父级id
     */
	private Integer parentid;
	/**
	 * 描述
	 */
    private String description;
    /**
     * 部门职位
     */
    private String posName;
    /**
     * 邮箱
     * 
     */
    private String userEmail;
    /**
     * 手机
     */
    private String userMobile;
    /**
     * 性别
     */
    private String sex;
    /**
     * 身份证
     */
    private String idNumber;
    /**
     * 籍贯
     */
    private String nativePlace;
    /**
     * 入职时间
     */
    private String entryDate;
    /**
     * 转正时间
     */
    private String regularDate;
    /**
     * 合同到期时间
     */
    private String contractDate;
    
    /**
     * 学校
     */
    private String school;
    /**
     * 专业
     */
    
    private String userProfession;
    private String education;
    private String address;
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserMobile() {
		return userMobile;
	}
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	public String getPosName() {
		return posName;
	}
	public void setPosName(String posName) {
		this.posName = posName;
	}
	public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getDeptName() {
        return deptName;
    }
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
 
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public Integer getParentid() {
		return parentid;
	}
	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getNativePlace() {
		return nativePlace;
	}
	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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