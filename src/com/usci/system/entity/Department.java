package com.usci.system.entity;
import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


import javax.persistence.GeneratedValue;
@Entity
@Table(name = "sys_department")
@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class Department  {
  
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
   /**
    * 部门名称
    */
    @Column(length = 20, unique = true, nullable = false)
    private String deptName;
     /**
      * 是否是叶子节点
      */
    @Transient
	private boolean leaf;
    /**
     * 父级id
     */
	@Transient
	private Integer parentid;
	/**
	 * 描述
	 */
    @Column(length = 45)
    private String description;
    /**
     * 父节点
     */
	   @ManyToOne(fetch=FetchType.LAZY)
	   @NotFound(action=NotFoundAction.IGNORE)//找不到引用的外键数据时忽略
	   @JoinColumn(name="parentid")
	   private Department parent;
   
    @Column(length = 30)
    private String updateTime;
    
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
    
    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public Department getParent() {
		return parent;
	}
	public void setParent(Department parent) {
		this.parent = parent;
	}
	public Integer getParentid() {
		return parentid;
	}
	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}
	
    
}