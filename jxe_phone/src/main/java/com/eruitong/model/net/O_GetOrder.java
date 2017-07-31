package com.eruitong.model.net;

public class O_GetOrder {
	private String areaCode;// 001 – 子单号
	// 123 - 签回单单号
	// 当前登陆网点区域代码 – 主单号
	// 网点属性字段’ districtCode’

	private Integer applyCount;// 申请数量

	private String empCode;

	private String devId;

	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Integer getApplyCount() {
		return applyCount;
	}

	public void setApplyCount(Integer applyCount) {
		this.applyCount = applyCount;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

}
