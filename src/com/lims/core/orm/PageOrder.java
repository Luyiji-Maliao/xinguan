package com.lims.core.orm;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

/**
 * 分页
 * @author 聂梦肖
 *
 * @param <T>
 */
public class PageOrder<T> {

	public static final String ASC = "asc";
	public static final String DESC = "desc";

	
	protected int pageNo = 1;
	protected int pageSize = -1;
	protected String orderBy = null;
	protected String order = null;
	protected boolean autoCount = true;
	protected String pageorder=null;

	
	
	protected List<T> result = Lists.newArrayList();
	protected Long totalCount = -1L;
	
	//--子权限（当前页面的操作权限）--//
	protected String roleright;
	public String getRoleright() {
		return roleright;
	}

	public void setRoleright(String roleright) {
		this.roleright = roleright;
	}

	
	public String getPageorder() {
		return pageorder;
	}

	public void setPageorder(String pageorder) {
		this.pageorder = pageorder;
	}

	public PageOrder() {
	}

	public PageOrder(int pageNo,int pageSize){
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}
	
	public PageOrder(int pageSize) {
		this.pageSize = pageSize;
	}

	
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(final int pageNo) {
		this.pageNo = pageNo;

		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}

	
	public PageOrder<T> pageNo(final int thePageNo) {
		setPageNo(thePageNo);
		return this;
	}

	
	public int getPageSize() {
		return pageSize;
	}

	
	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}

	
	public PageOrder<T> pageSize(final int thePageSize) {
		setPageSize(thePageSize);
		return this;
	}

	
	public int getFirst() {
		return ((pageNo - 1) * pageSize) + 1;
	}

	
	public String getOrderBy() {
		return orderBy;
	}

	
	public void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}

	
	public PageOrder<T> orderBy(final String theOrderBy) {
		setOrderBy(theOrderBy);
		return this;
	}

	
	public String getOrder() {
		return order;
	}

	
	public void setOrder(final String order) {
		String lowcaseOrder = StringUtils.lowerCase(order);

		//检查order字符串的合法值
		String[] orders = StringUtils.split(lowcaseOrder, ',');
		for (String orderStr : orders) {
			if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr)) {
				throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
			}
		}

		this.order = lowcaseOrder;
	}

	
	public PageOrder<T> order(final String theOrder) {
		setOrder(theOrder);
		return this;
	}

	
	public boolean isOrderBySetted() {
		return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order));
	}

	
	public boolean isAutoCount() {
		return autoCount;
	}

	
	public void setAutoCount(final boolean autoCount) {
		this.autoCount = autoCount;
	}

	
	public PageOrder<T> autoCount(final boolean theAutoCount) {
		setAutoCount(theAutoCount);
		return this;
	}

	public List<T> getResult() {
		return result;
	}

	
	public void setResult(final List<T> result) {
		this.result = result;
	}

	
	public Long getTotalCount() {
		return totalCount;
	}

	
	public void setTotalCount(final Long totalCount) {
		this.totalCount = totalCount;
	}

	
	public Long getTotalPages() {
		if (totalCount < 0) {
			return -1L;
		}

		Long count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	
	public boolean isHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}

	
	public int getNextPage() {
		if (isHasNext()) {
			return pageNo + 1;
		} else {
			return pageNo;
		}
	}

	
	public boolean isHasPre() {
		return (pageNo - 1 >= 1);
	}

	public int getPrePage() {
		if (isHasPre()) {
			return pageNo - 1;
		} else {
			return pageNo;
		}
	}
}
