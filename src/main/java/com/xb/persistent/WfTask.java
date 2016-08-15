package com.xb.persistent;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotations.IdType;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.toolkit.CUBaseTO;

/**
 *
 * 
 *
 */
@TableName("wf_task")
public class WfTask extends CUBaseTO implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "TASK_ID", type = IdType.UUID)
	private String taskId;

	/**  */
	@TableField(value = "WF_ID")
	private String wfId;

	/**  */
	@TableField(value = "TASK_PG_ID")
	private String taskPgId;

	/**  */
	@TableField(value = "TASK_TYPE")
	private String taskType;

	/**  */
	@TableField(value = "TASK_DESCP")
	private String taskDescp;

	/**  */
	@TableField(value = "POS_TOP")
	private Double posTop;

	/**  */
	@TableField(value = "POS_LEFT")
	private Double posLeft;


/**  */
	@TableField(value = "TX_CODE")
	private String txCode;

	/**  */
	@TableField(value = "TX_TYPE")
	private String txType;

	/**  */
	@TableField(value = "BUZ_STATUS")
	private String buzStatus;

	/**  */
	@TableField(value = "TIME_LIMIT")
	private Integer timeLimit;

	/**  */
	@TableField(value = "TIME_LIMIT_TP")
	private String timeLimitTp;

	/**  */
	@TableField(value = "ALARM_TIME")
	private Integer alarmTime;

	/**  */
	@TableField(value = "ALARM_TIME_TP")
	private String alarmTimeTp;

	/**  */
	@TableField(value = "MODULE_ID")
	private String moduleId;

	/**  */
	@TableField(value = "Run_Param")
	private String runParam;

	/**  */
	@TableField(value = "TASK_DESCP_DISP")
	private String taskDescpDisp;

	/**  */
	@TableField(value = "TX_CHOICES")
	private String txChoices;

	/**  */
	@TableField(value = "TX_PR_CHOICES")
	private String txPrChoices;

	/**  */
	@TableField(value = "TX_BK_CHOICES")
	private String txBkChoices;

	/**  */
	@TableField(value = "SIGN_CHOICES")
	private String signChoices;
	@TableField(exist = false)
	private List<WfTaskAssign> assignerList;
	/**
	 * 用来标识是否该task被执行过
	 */
	@TableField(exist = false)
	private String processedFlag;
	/**
	 * 用来标识是否当前task是WF正在pending的那个task
	 */
	@TableField(exist = false)
	private String currTaskId;
	
	public String getProcessedFlag() {
		return processedFlag;
	}

	public void setProcessedFlag(String processedFlag) {
		this.processedFlag = processedFlag;
	}

	public String getCurrTaskId() {
		return currTaskId;
	}

	public void setCurrTaskId(String currTaskId) {
		this.currTaskId = currTaskId;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getWfId() {
		return this.wfId;
	}

	public void setWfId(String wfId) {
		this.wfId = wfId;
	}

	public String getTaskPgId() {
		return this.taskPgId;
	}

	public void setTaskPgId(String taskPgId) {
		this.taskPgId = taskPgId;
	}

	public String getTaskType() {
		return this.taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskDescp() {
		return this.taskDescp;
	}

	public void setTaskDescp(String taskDescp) {
		this.taskDescp = taskDescp;
	}

	public Double getPosTop() {
		return this.posTop;
	}

	public void setPosTop(Double posTop) {
		this.posTop = posTop;
	}

	public Double getPosLeft() {
		return this.posLeft;
	}

	public void setPosLeft(Double posLeft) {
		this.posLeft = posLeft;
	}

	public List<WfTaskAssign> getAssignerList() {
		return assignerList;
	}

	public void setAssignerList(List<WfTaskAssign> assignerList) {
		this.assignerList = assignerList;
	}

	public String getTxCode() {
		return txCode;
	}

	public void setTxCode(String txCode) {
		this.txCode = txCode;
	}

	public String getTxType() {
		return txType;
	}

	public void setTxType(String txType) {
		this.txType = txType;
	}

	public String getBuzStatus() {
		return buzStatus;
	}

	public void setBuzStatus(String buzStatus) {
		this.buzStatus = buzStatus;
	}

	public Integer getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}

	public String getTimeLimitTp() {
		return timeLimitTp;
	}

	public void setTimeLimitTp(String timeLimitTp) {
		this.timeLimitTp = timeLimitTp;
	}

	public Integer getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(Integer alarmTime) {
		this.alarmTime = alarmTime;
	}

	public String getAlarmTimeTp() {
		return alarmTimeTp;
	}

	public void setAlarmTimeTp(String alarmTimeTp) {
		this.alarmTimeTp = alarmTimeTp;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getRunParam() {
		return runParam;
	}

	public void setRunParam(String runParam) {
		this.runParam = runParam;
	}

	public String getTaskDescpDisp() {
		return taskDescpDisp;
	}

	public void setTaskDescpDisp(String taskDescpDisp) {
		this.taskDescpDisp = taskDescpDisp;
	}

	public String getTxChoices() {
		return txChoices;
	}

	public void setTxChoices(String txChoices) {
		this.txChoices = txChoices;
	}

	public String getTxPrChoices() {
		return txPrChoices;
	}

	public void setTxPrChoices(String txPrChoices) {
		this.txPrChoices = txPrChoices;
	}

	public String getTxBkChoices() {
		return txBkChoices;
	}

	public void setTxBkChoices(String txBkChoices) {
		this.txBkChoices = txBkChoices;
	}

	public String getSignChoices() {
		return signChoices;
	}

	public void setSignChoices(String signChoices) {
		this.signChoices = signChoices;
	}

	@Override
	public String toString() {
		return "WfTask [taskId=" + taskId + ", wfId=" + wfId + ", taskPgId=" + taskPgId + ", taskType=" + taskType
				+ ", taskDescp=" + taskDescp + ", posTop=" + posTop + ", posLeft=" + posLeft + ", txCode=" + txCode
				+ ", txType=" + txType + ", buzStatus=" + buzStatus + ", timeLimit=" + timeLimit + ", timeLimitTp="
				+ timeLimitTp + ", alarmTime=" + alarmTime + ", alarmTimeTp=" + alarmTimeTp + ", moduleId=" + moduleId
				+ ", runParam=" + runParam + ", taskDescpDisp=" + taskDescpDisp + ", txChoices=" + txChoices
				+ ", txPrChoices=" + txPrChoices + ", txBkChoices=" + txBkChoices + ", signChoices=" + signChoices
				+ ", processedFlag=" + processedFlag + ", currTaskId=" + currTaskId + "]";
	}
	
	
}
