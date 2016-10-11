package com.xb.persistent;

import com.rshare.service.wf.annotations.FuncVar;

public class SampleBuzEntity {

	private String id;
	
	private String name;
	
	@FuncVar(value="数量", dbColunm = "amount")
	private Integer amount;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "SampleBuzEntity [id=" + id + ", name=" + name + ", amount=" + amount + "]";
	}
	
}
