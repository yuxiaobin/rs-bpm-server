package com.xb.vo;

import com.xb.persistent.WfAwt;
import com.xb.persistent.WfInstHist;

public class TaskVO {
	private WfAwt awt;//待办事宜
	
	
	private WfInstHist instHist;//上一条历史记录
	public WfInstHist getInstHist() {
		return instHist;
	}
	public void setInstHist(WfInstHist instHist) {
		this.instHist = instHist;
	}
	public WfAwt getAwt() {
		return awt;
	}
	public void setAwt(WfAwt awt) {
		this.awt = awt;
	}
	
}
