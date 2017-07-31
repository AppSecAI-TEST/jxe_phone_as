package com.eruitong.model.net;

import com.eruitong.model.DbDatafInfo;

import java.util.List;

public class I_SyncBaseData {
	private boolean success;// 成功标识：true –成功 false-失败
	private String error;// 失败描述内容
	private String lastTime;// 最后更新时间
	private List<DbDatafInfo> dataList;// 数据集合(格式请查看数据字典)

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

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public List<DbDatafInfo> getDataList() {
		return dataList;
	}

	public void setDataList(List<DbDatafInfo> dataList) {
		this.dataList = dataList;
	}

}
