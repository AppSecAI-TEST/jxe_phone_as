package com.eruitong.model.net;

import java.util.List;

public class I_PdaUploadData {
	private boolean success;
	private String error;
	private String gatherState;
	private List<String> wrongWaybillNoList;

	public List<String> getWrongWaybillNoList() {
		return wrongWaybillNoList;
	}

	public void setWrongWaybillNoList(List<String> wrongWaybillNoList) {
		this.wrongWaybillNoList = wrongWaybillNoList;
	}

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

	public String getGatherState() {
		return gatherState;
	}

	public void setGatherState(String gatherState) {
		this.gatherState = gatherState;
	}
	

}
