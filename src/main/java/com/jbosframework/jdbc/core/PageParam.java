package com.jbosframework.jdbc.core;
/**
 * 分面查询参数类
 * @author youfu.wang
 * @version 1.0
 */
public class PageParam {
	private String primary="id";//表主键
	private Integer firstResult=0;//起始记录行
	private Integer maxResult=0;//最大记录行
	private Integer pageSize=10;//每页大小
	private Integer currentPage=0;//当面页号
	private Integer maxPage=0;//最大页号
	private Integer maxRowNum=0;//最大行号
	
	public int getMaxRowNum() {
		return maxRowNum;
	}
	public void setMaxRowNum(Integer maxRowNum) {
		this.maxRowNum = maxRowNum;
	}
	public PageParam(){
		
	}	
	/**
	 * 返回起始记录行
	 * @return
	 */
	public Integer getFirstResult() {
		if(this.getCurrentPage()==1)
			return 1;
		else
			return this.getPageSize()*(this.getCurrentPage()-1)+1;
	}
	/**
	 * 设置起始记录行
	 * @param firstResult
	 */
	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}
	/**
	 * 返回最大记录行
	 * @return
	 */
	public Integer getMaxResult() {		
		return this.getPageSize()*this.getCurrentPage();
	}
	/**
	 * 设置最大记录行
	 * @param maxResult
	 */
	public void setMaxResult(Integer maxResult) {
		this.maxResult = maxResult;
	}
	/**
	 * 返回页面大小
	 * @return
	 */
	public Integer getPageSize() {
		if(this.pageSize==null)
			return 10;
		return pageSize;
	}
	/**
	 * 设置页面大小 
	 * @param pageSize
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * 返回当前页号
	 * @return
	 */
	public Integer getCurrentPage() {
		if(this.currentPage==null)
			return 1;
		return currentPage;
	}
	/**
	 * 设置当前页号
	 * @param currentPage
	 */
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	/**
	 * 返回最大页号
	 * @return
	 */
	public Integer getMaxPage() {
		return maxPage;
	}
	/**
	 * 设置最大页号
	 * @param maxPage
	 */
	public void setMaxPage(Integer maxPage) {		
		this.maxPage = maxPage;
	}
	/**
	 * 返回表主键
	 * @return
	 */
	public String getPrimary() {
		return primary;
	}
	/**
	 * 设置表主键
	 * @param primary
	 */
	public void setPrimary(String primary) {
		this.primary = primary;
	}
}
