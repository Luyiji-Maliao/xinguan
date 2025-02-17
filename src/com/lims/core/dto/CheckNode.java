package com.lims.core.dto;
/**
 * 树节点的默认选中
 * @author 聂梦肖
 *
 */
public class CheckNode extends Node{
	private boolean checked;
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
