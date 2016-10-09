package com.xb.persistent;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.IdType;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 *
 * 
 *
 */
@TableName("wf_cust_vars")
public class WfCustVars implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "CUST_VARS_ID", type = IdType.UUID)
	private String custVarsId;

	/**  */
	@TableField(value = "WF_ID")
	private String wfId;

	/**  */
	@TableField(value = "VAR_TYPE")
	private String varType;

	/**  */
	@TableField(value = "var_code")
	private String varCode;

	/**  */
	@TableField(value = "var_descp")
	private String varDescp;

	/**  */
	@TableField(value = "var_expression")
	private String varExpression;

	/**  */
	@TableField(value = "CREATED_BY")
	private String createdBy;

	/**  */
	@TableField(value = "CREATED_DT")
	private Date createdDt;

	/**  */
	@TableField(value = "UPDATED_BY")
	private String updatedBy;

	/**  */
	@TableField(value = "UPDATED_DT")
	private Date updatedDt;

	public String getCustVarsId() {
		return this.custVarsId;
	}

	public void setCustVarsId(String custVarsId) {
		this.custVarsId = custVarsId;
	}

	public String getWfId() {
		return this.wfId;
	}

	public void setWfId(String wfId) {
		this.wfId = wfId;
	}

	public String getVarType() {
		return this.varType;
	}

	public void setVarType(String varType) {
		this.varType = varType;
	}

	public String getVarCode() {
		return this.varCode;
	}

	public void setVarCode(String varCode) {
		this.varCode = varCode;
	}

	public String getVarDescp() {
		return this.varDescp;
	}

	public void setVarDescp(String varDescp) {
		this.varDescp = varDescp;
	}

	public String getVarExpression() {
		return this.varExpression;
	}

	public void setVarExpression(String varExpression) {
		this.varExpression = varExpression;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

}
