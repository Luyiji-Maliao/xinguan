package com.lims.core.dto;


/**
 * ext 树节点
 * @author nimengxiao
 *
 */
public class Node1 {
	private String id;
	private String text;
	private boolean leaf;
	private String moduleUrl;
	private String iconCls;

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
    public String getModuleUrl() {
        return moduleUrl;
    }
    public void setModuleUrl(String moduleUrl) {
        this.moduleUrl = moduleUrl;
    }
    public String getIconCls() {
        return iconCls;
    }
    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    @Override
	public boolean equals(Object obj) {
		Node1 node = (Node1)obj;
		return this.getId().equals(node.getId());
	}
	
	
}
