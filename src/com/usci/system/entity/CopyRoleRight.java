package com.usci.system.entity;
import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

//复制t_right_t_role,增加当前角色所拥有权限的当前操作（增删改查......）

@Entity
@Table(name="sys_copyroleright")
public class CopyRoleRight  {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Integer id;
  @Column
  private Integer roleid;//角色id
  @Column
  private Integer rightid;//权限id
  @Column(length=30)
  private String roleHaveRight;//角色拥有当前权限的操作
  @Column(length=30)
  private String updateTime;
  
    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getRoleid() {
        return roleid;
    }
    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }
    public Integer getRightid() {
        return rightid;
    }
    public void setRightid(Integer rightid) {
        this.rightid = rightid;
    }
    public String getRoleHaveRight() {
        return roleHaveRight;
    }
    public void setRoleHaveRight(String roleHaveRight) {
        this.roleHaveRight = roleHaveRight;
    }

  
}
