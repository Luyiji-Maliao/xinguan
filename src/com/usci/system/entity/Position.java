package com.usci.system.entity;
import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;
@Entity
@Table(name = "sys_position")
@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
  
    /**
     * 职位名称
     */
    @Column(length = 30)
    private String posName;
    /**
     * 职位描述
     */
    @Column(length = 45, nullable = false)
     private String description;
    /**
     * 用户管理（部门，职位级联查询条件）
     */
    private Integer deptId;
    /**
     *部门
     */
    @ManyToOne
    @JoinColumn(name="departmentId",nullable=false)
    private Department department;
    @Column(length = 30)
    private String updateTime;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
   
    public String getPosName() {
        return posName;
    }
    public void setPosName(String posName) {
        this.posName = posName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
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
	public Integer getDeptId() {
		return deptId;
	}
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	
    
}