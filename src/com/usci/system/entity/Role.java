package com.usci.system.entity;
import javax.persistence.*;

import java.util.List;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.common.collect.Lists;

import javax.persistence.GeneratedValue;
@Entity
@Table(name = "sys_role")
public class Role  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //@ManyToMany(cascade=CascadeType.ALL)
    @Transient
    private List<Right> rights = Lists.newArrayList();
    /**
     * 角色名称
     */
    @Column(length = 30, unique = true, nullable = false)
    private String roleName;
    /**
     * 描述
     */
    @Column(length = 80, nullable = false)
    private String description;
    /**
     * 是否是系统定义角色(0 系统定义，1 自定义)
     */
    @Column(nullable = false,length=2)
    private Integer isSysNeeded;
    @Column(length = 30)
    private String updateTime;
    
    @Transient
    private String roleRights;
    @Transient
    private String roleUser;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public List<Right> getRights() {
        return rights;
    }
    public void setRights(List<Right> rights) {
        this.rights = rights;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
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
	public Integer getIsSysNeeded() {
		return isSysNeeded;
	}
	public void setIsSysNeeded(Integer isSysNeeded) {
		this.isSysNeeded = isSysNeeded;
	}
	public String getRoleRights() {
		return roleRights;
	}
	public void setRoleRights(String roleRights) {
		this.roleRights = roleRights;
	}
	public String getRoleUser() {
		return roleUser;
	}
	public void setRoleUser(String roleUser) {
		this.roleUser = roleUser;
	}

}