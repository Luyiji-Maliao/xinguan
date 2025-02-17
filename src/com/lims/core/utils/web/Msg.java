package com.lims.core.utils.web;

import java.util.List;

/**
 * Ajax 请求返回消息封装类
 * @author LIMS
 *
 */
public class Msg {
	private boolean success = true;
	private String msg;
	private Object o;
	private List<Object> l;
	
	public List<Object> getL() {
		return l;
	}
	public void setL(List<Object> l) {
		this.l = l;
	}
	public Object getO() {
		return o;
	}
	public void setO(Object o) {
		this.o = o;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
