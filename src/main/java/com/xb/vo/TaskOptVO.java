package com.xb.vo;

public class TaskOptVO {

	private String rsWfId;
	private Integer instNum;
	
	private String comments;
	private String nextAssigners;
	private String nextTaskId;//当前选择的事务节点ID. Next Task ID
	private String optCode;//C:提交，V:否决，RJ:退回，RD:撤回
	
	private String gnmkId;
	
	/**********以上为处理事务时必填字段*************/
	
	private String currTaskId;//撤回操作时，必须加上
	private String prevInstHistId;
	private String currUserId;
	private boolean nextEndTaskFlag;
	
	private String wfId;//userd when create hist

	public String getWfId() {
		return wfId;
	}
	public void setWfId(String wfId) {
		this.wfId = wfId;
	}
	
	public String getCurrTaskId() {
		return currTaskId;
	}
	public void setCurrTaskId(String currTaskId) {
		this.currTaskId = currTaskId;
	}
	public String getCurrUserId() {
		return currUserId;
	}
	public void setCurrUserId(String currUserId) {
		this.currUserId = currUserId;
	}
	public boolean isNextEndTaskFlag() {
		return nextEndTaskFlag;
	}
	public void setNextEndTaskFlag(boolean nextEndTaskFlag) {
		this.nextEndTaskFlag = nextEndTaskFlag;
	}
	public String getPrevInstHistId() {
		return prevInstHistId;
	}
	public void setPrevInstHistId(String prevInstHistId) {
		this.prevInstHistId = prevInstHistId;
	}
	
	public String getOptCode() {
		return optCode;
	}
	public void setOptCode(String optCode) {
		this.optCode = optCode;
	}
	public String getRsWfId() {
		return rsWfId;
	}
	public void setRsWfId(String rsWfId) {
		this.rsWfId = rsWfId;
	}
	public Integer getInstNum() {
		return instNum;
	}
	public void setInstNum(Integer instNum) {
		this.instNum = instNum;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getNextAssigners() {
		return nextAssigners;
	}
	public void setNextAssigners(String nextAssigners) {
		this.nextAssigners = nextAssigners;
	}
	public String getNextTaskId() {
		return nextTaskId;
	}
	public void setNextTaskId(String nextTaskId) {
		this.nextTaskId = nextTaskId;
	}
	
	public String getGnmkId() {
		return gnmkId;
	}
	public void setGnmkId(String gnmkId) {
		this.gnmkId = gnmkId;
	}
	@Override
	public String toString() {
		return "TaskOptVO [rsWfId=" + rsWfId + ", instNum=" + instNum + ", comments=" + comments + ", nextAssigners="
				+ nextAssigners + ", nextTaskId=" + nextTaskId + ", optCode=" + optCode + ", gnmkId=" + gnmkId
				+ ", prevInstHistId=" + prevInstHistId + ", currUserId=" + currUserId + ", nextEndTaskFlag="
				+ nextEndTaskFlag + ", wfId=" + wfId + "]";
	}
	
}
