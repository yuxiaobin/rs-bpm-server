package com.xb.persistent;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.IdType;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 *
 * 
 *
 */
@TableName("tbl_plus_demo")
public class TblPlusDemo implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "DEMO_ID", type = IdType.UUID)
	private String demoId;

	/**  */
	@TableField(value = "DEMO_NAME")
	private String demoName;

	/**  */
	@TableField(value = "POS_TOP")
	private Double posTop;

	/**  */
	private Double LEFT;

	public String getDemoId() {
		return this.demoId;
	}

	public void setDemoId(String demoId) {
		this.demoId = demoId;
	}

	public String getDemoName() {
		return this.demoName;
	}

	public void setDemoName(String demoName) {
		this.demoName = demoName;
	}

	public Double getPosTop() {
		return this.posTop;
	}

	public void setPosTop(Double posTop) {
		this.posTop = posTop;
	}

	public Double getLEFT() {
		return this.LEFT;
	}

	public void setLEFT(Double LEFT) {
		this.LEFT = LEFT;
	}

}
