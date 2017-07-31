package com.eruitong.model.net;

import java.util.List;

public class I_SaveWaybill {
	/*
	 * {"success":true,"wrongWaybillNoList":["0290099323223","9288099323223",
	 * "9884099323223","0290099354523"]}
	 */
	private boolean success;
	private String error;
	private List<String> wrongWaybillNoList;

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

	public List<String> getWrongWaybillNoList() {
		return wrongWaybillNoList;
	}

	public void setWrongWaybillNoList(List<String> wrongWaybillNoList) {
		this.wrongWaybillNoList = wrongWaybillNoList;
	}

}
