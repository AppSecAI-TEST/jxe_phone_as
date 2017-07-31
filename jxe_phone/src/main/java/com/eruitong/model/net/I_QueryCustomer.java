package com.eruitong.model.net;

import com.eruitong.model.CustList;

import java.util.List;

public class I_QueryCustomer {

	// 返回数据JSON:
	// {"custList":[{"addressList":[{"address":"大","addressId":"4565115","countryCode":"912","customer":null,"deptCode":"912D","provinceCode":"shangxisheng"},{"address":"大柳塔","addressId":"4594501","countryCode":"912","customer":null,"deptCode":"912D","provinceCode":"shangxisheng"}],"auditedFlag":"0","bankType":"0","compCode":"XA0200","createdEmpCode":"000015","createdTm":"2012-06-05T20:02:48","creditLimit":0.0,"custCode":"0004565114","custMobile":"13149129589","custName":"孙凯","custRate":1.0,"custType":"0","deptCode":"912D","goodsAgentPeriod":0,"goodsAgentRatio":1.0,"id":"4565114","inputType":"2","transferType":"2"}],"success":true}
	private boolean success;
	private String error;
	private List<CustList> custList;

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

	public List<CustList> getCustList() {
		return custList;
	}

	public void setCustList(List<CustList> custList) {
		this.custList = custList;
	}

}
