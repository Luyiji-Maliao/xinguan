package com.lims.core.dto;

/**
 * ext 树节点
 * @author nimengxiao
 *
 */
public class Node {
	private String id;
	private Integer ids;
	private String text;
	private boolean leaf;
	private String url;
	private String iconCls;
	//角色所拥有页面的操作权限
	private String roleright;
	//页面的操作权限
	private String right_web_num;
	//jsp页面
	private String jspPage;

	public Integer getIds() {
		return ids;
	}

	public void setIds(Integer ids) {
		this.ids = ids;
	}

	public String getRoleright() {
		return roleright;
	}
	public void setRoleright(String roleright) {
		this.roleright = roleright;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	
	public String getRight_web_num() {
		return right_web_num;
	}
	public void setRight_web_num(String rightWebNum) {
		right_web_num = rightWebNum;
	}
	@Override
	public boolean equals(Object obj) {
		Node node = (Node)obj;
		return this.getId().equals(node.getId());
	}
	public String getJspPage() {
		return jspPage;
	}
	public void setJspPage(String jspPage) {
		this.jspPage = jspPage;
	}
	
	
}
