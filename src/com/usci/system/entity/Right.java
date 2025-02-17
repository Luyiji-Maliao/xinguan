package com.usci.system.entity;
import javax.persistence.*;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import com.google.common.collect.Lists;

import javax.persistence.GeneratedValue;
@Entity
@Table(name = "sys_right")
public class Right {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 父节点
     */
    @ManyToOne(fetch=FetchType.LAZY)
    private Right parent;
    /**
     * 子节点
     */
    @OneToMany(mappedBy="parent",fetch=FetchType.LAZY)
    private List<Right> children = Lists.newArrayList();
    /**
     * 模块名字
     */
    @Column(length = 20, unique = true, nullable = false)
    private String moduleName;
    /**
     * jsp页面
     */
    @Column(length = 30, unique = true)
    private String jspPage;
    /**
     * url
     */
    @Column(length = 45, unique = true)
    private String moduleUrl;
    /**
     * 拥有的操作权限
     */
    @Column(length = 45)
    private String rights;
    /**
     * 是否是叶子节点
     */
    @Column(nullable = false)
    private boolean isLeaf;
    @Column(length = 30)
    private String updateTime;
    @Column(length = 11)
    private Integer rightSort;
    
    public Integer getRightSort() {
		return rightSort;
	}
	public void setRightSort(Integer rightSort) {
		this.rightSort = rightSort;
	}
	public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Right getParent() {
        return parent;
    }
    public void setParent(Right parent) {
        this.parent = parent;
    }
    public List<Right> getChildren() {
        return children;
    }
    public void setChildren(List<Right> children) {
        this.children = children;
    }
    public String getModuleName() {
        return moduleName;
    }
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
    public String getModuleUrl() {
        return moduleUrl;
    }
    public void setModuleUrl(String moduleUrl) {
        this.moduleUrl = moduleUrl;
    }
    public String getRights() {
        return rights;
    }
    public void setRights(String rights) {
        this.rights = rights;
    }
    public boolean isLeaf() {
        return isLeaf;
    }
    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }
    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
	public String getJspPage() {
		return jspPage;
	}
	public void setJspPage(String jspPage) {
		this.jspPage = jspPage;
	}
    
}