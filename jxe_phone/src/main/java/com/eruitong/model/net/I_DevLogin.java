package com.eruitong.model.net;

import java.util.List;

public class I_DevLogin {
	private String empName;// 当前登陆员工姓名
	private String deptCode;// 当前登陆网点编码
	private List<String> authList;// 登陆后主界面操作权限：String List
	// (请列出所有的权限，每个权限所对应的功能列表)
	private String isUpdate;// 是否需要更新
	private boolean success;
	private String error;

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public List<String> getAuthList() {
		return authList;
	}

	public void setAuthList(List<String> authList) {
		this.authList = authList;
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

	public String getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}

	@Override
	public String toString() {
		return "I_DevLogin [empName=" + empName + ", deptCode=" + deptCode
				+ ",isUpdate=" + isUpdate + ",  authList=" + authList
				+ ", success=" + success + ", error=" + error + "]";
	}

}
