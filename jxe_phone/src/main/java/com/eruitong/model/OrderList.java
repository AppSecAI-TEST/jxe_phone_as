package com.eruitong.model;

import java.util.List;

public class OrderList {
	public Integer applyCount;
	public String areaCode;
	public List<String> waybillNosList;

	public Integer getApplyCount() {
		return applyCount;
	}

	public void setApplyCount(Integer applyCount) {
		this.applyCount = applyCount;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public List<String> getWaybillNosList() {
		return waybillNosList;
	}

	public void setWaybillNosList(List<String> waybillNosList) {
		this.waybillNosList = waybillNosList;
	}

}