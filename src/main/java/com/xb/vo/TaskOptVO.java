package com.xb.vo;

public class TaskOptVO {

	private String rsWfId;
	private Integer instNum;
	
	private String comments;
	private String nextAssigners;
	private String nextTaskId;//当前选择的事务节点ID. Next Task ID
	private String optType;//C:提交，V:否决，RJ:退回，RD:撤回
	
	
	private String currTaskId;
	private String instId;
	private String prevInstHistId;
	
	private boolean nextEndTaskFlag;
	
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
	public String getOptType() {
		return optType;
	}
	public void setOptType(String optType) {
		this.optType = optType;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getCurrTaskId() {
		return currTaskId;
	}
	public void setCurrTaskId(String currTaskId) {
		this.currTaskId = currTaskId;
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
	@Override
	public String toString() {
		return "TaskOptVO [rsWfId=" + rsWfId + ", instNum=" + instNum + ", comments=" + comments + ", nextAssigners="
				+ nextAssigners + ", nextTaskId=" + nextTaskId + ", optType=" + optType + ", currTaskId=" + currTaskId
				+ ", instId=" + instId + "]";
	}
	
	
}
