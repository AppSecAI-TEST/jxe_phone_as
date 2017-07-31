package com.eruitong.model;

import java.util.List;

public class CustList {
	public String custName;// 客户姓名
	public String custCode;// 客户编码
	public String custType;// 客户类型: 0-散户 1-月结 2-欠款 3-黑名单
	public String custTel;// 客户电话
	public String custMobile;// 客户电话
	public String custPhone;// 客户手机号
	public String bankType;// 银行类型
	public String bankAccount;// 银行账号
	public String custCompany;// 公司名称
	public String custRate;// 折扣费率
	public String goodsAgentPeriod;// 转款周期
	public List<AddressList> addressList;// 地址集合

	public String getCustPhone() {
		return custPhone;
	}

	public void setCustPhone(String custPhone) {
		this.custPhone = custPhone;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public String getCustType() {
		return custType;
	}

	public void setCustType(String custType) {
		this.custType = custType;
	}

	public String getCustTel() {
		return custTel;
	}

	public void setCustTel(String custTel) {
		this.custTel = custTel;
	}

	public String getCustMobile() {
		return custMobile;
	}

	public void setCustMobile(String custMobile) {
		this.custMobile = custMobile;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getCustCompany() {
		return custCompany;
	}

	public void setCustCompany(String custCompany) {
		this.custCompany = custCompany;
	}

	public String getCustRate() {
		return custRate;
	}

	public void setCustRate(String custRate) {
		this.custRate = custRate;
	}

	public String getGoodsAgentPeriod() {
		return goodsAgentPeriod;
	}

	public void setGoodsAgentPeriod(String goodsAgentPeriod) {
		this.goodsAgentPeriod = goodsAgentPeriod;
	}

	public List<AddressList> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<AddressList> addressList) {
		this.addressList = addressList;
	}

}
