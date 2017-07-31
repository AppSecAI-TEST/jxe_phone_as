package com.eruitong.model.net;

import com.eruitong.model.OrderList;

import java.util.List;

public class I_GetOrder {

	private boolean success;
	private String error;
	private List<OrderList> dataList;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<OrderList> getDataList() {
		return dataList;
	}

	public void setDataList(List<OrderList> dataList) {
		this.dataList = dataList;
	}

}
