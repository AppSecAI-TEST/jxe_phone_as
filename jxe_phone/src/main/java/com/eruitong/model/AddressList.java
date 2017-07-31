package com.eruitong.model;

public class AddressList {
	public String address;// 地址
	public String deptCode;// 网点编码
	public String deliverAreaFlag;// 派送范围：0-否 1-是
	public String teamCode;// 派送组编号

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeliverAreaFlag() {
		return deliverAreaFlag;
	}

	public void setDeliverAreaFlag(String deliverAreaFlag) {
		this.deliverAreaFlag = deliverAreaFlag;
	}

	public String getTeamCode() {
		if (teamCode == null) {
			return "";
		} else {
			if (teamCode.length() >= 2) {
				return teamCode.substring(teamCode.length() - 2,
						teamCode.length());
			} else {
				return teamCode;
			}
		}
	}

	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}

}
