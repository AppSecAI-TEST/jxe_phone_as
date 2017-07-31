package com.eruitong.model.net;

public class O_SaveWaybill {
	private String waybillNo;// 主运单号（本地缓存自动获取）（通过哪个接口获取运单号）
	private String sourceZoneCode;// 原寄地(当前员工所属网点)
	private String destZoneCode;// 目的地(下拉列表：网点缓存)
	private String custCode;// 寄方客户编码(可为空)
	private String consignorCompName;// 寄件公司（可为空）
	private String consignorAddr;// 寄件地址(必填)
	private String consignorPhone;// 寄件电话（与手机号必填一个）
	private String consignorContName;// 寄件人(必填)
	private String consignorMobile;// 寄件手机（与电话必填一个）
	private String addresseeCompName;// 收件客户公司(可为空)
	private String addresseeAddr;// 收件地址(必填)
	private String addresseePhone;// 收件电话（与手机号必填一个）
	private String addresseeContName;// 收件人(必填)
	private String addresseeMobile;// 收件手机（与电话必填一个）
	private Double realWeightQty;// 重量(必填)
	private Double meterageWeightQty;// 计费重量(根据重量和体积取大值)
	private Integer Quantity;// 件数(必填)，多件时，自动获取(件数- 1)的子件单号 （这个不明白啥意思）
	private Double Volume;// 体积(可为空)
	private String consigneeEmpCode;// 收件员工(当前登录员工)
	private String consignedTm;// 寄件时间,格式：yyyy-MM-dd HH:mm:ss
	private String waybillRemk;// 备注(可为空)
	private String consName;// 托寄物（必填）
	private String inputType;// 终端类型(4-PDA上传)
	private String paymentTypeCode;// 付款方式(下拉):1-寄付 2-到付
	private String settlementTypeCode;// 结算方式(下拉):1-现结 2-月结
	private String serviceTypeCode;// 服务类型:1-门到门;2-港到门;3-港到港
	// 不为空;默认为2-港到门;
	private String waybillFee;// 运费（自动计算）（计算公式是啥？）
	private String goodsChargeFee;// 代收款（可为空）
	private String chargeAgentFee;// 代收服务费(根据代收款计算) （计算公式是啥？）
	private String bankNo;// 银行账户(可填) 查询客户资料带出
	private String bankType;// 卡号类型(下拉：基础数据加载) 查询客户资料带出
	private Integer transferDays;// 转账周期
	private String insuranceAmount;// 保价金额(不能为空,大于等于1000元)
	private Double insuranceFee;// 保价费(根据规则计算)
	private String signBackNo;// 签回单号(单号123开头,在本地获取)
	private String signBackCount;// 返单份数(选取签回单业务时,份数必须填)
	private String signBackSize;// 返单张数(选取签回单业务时,张数必须填)
	private String signBackReceipt;// 签收条(0-未选中;1-选取)
	private String signBackSeal;// 签字盖章(0-未选中;1-选取)
	private String signBackIdentity;// 身份证号(0-未选中;1-选取)选取签回单业务时,收条、盖章、身份证至少选一个
	private Double waitNotifyFee;// 等通知费(可为空,选取时,服务费10元)
	private Double signBackFee;// 签回单费
	private Double deboursFee;// 垫付费(可为空,选取时,手工输入大于0,小于等于300)
	private String deboursFlag;// 是否垫结(0-未垫结、1-已垫结)
	private Double deliverFee;// 派送费(可为空,选取时,手工填写)
	private String productTypeCode;// 产品类型(B1-快递;B2-普货,默认为B2)不能为空;
	private String orderNo;// 系统外订单号 可以为空
	private String childNoList;// List 子单号（件数大于1自动本地获取）
	private String teamCode;// 派送组编号
	private Double discountExpressFee;// 折扣运费
	private String inputerEmpCode;// 录单员
	private String distanceTypeCode;// 区域类型

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getSourceZoneCode() {
		return sourceZoneCode;
	}

	public void setSourceZoneCode(String sourceZoneCode) {
		this.sourceZoneCode = sourceZoneCode;
	}

	public String getDestZoneCode() {
		return destZoneCode;
	}

	public void setDestZoneCode(String destZoneCode) {
		this.destZoneCode = destZoneCode;
	}

	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public String getConsignorCompName() {
		return consignorCompName;
	}

	public void setConsignorCompName(String consignorCompName) {
		this.consignorCompName = consignorCompName;
	}

	public String getConsignorAddr() {
		return consignorAddr;
	}

	public void setConsignorAddr(String consignorAddr) {
		this.consignorAddr = consignorAddr;
	}

	public String getConsignorPhone() {
		return consignorPhone;
	}

	public void setConsignorPhone(String consignorPhone) {
		this.consignorPhone = consignorPhone;
	}

	public String getConsignorContName() {
		return consignorContName;
	}

	public void setConsignorContName(String consignorContName) {
		this.consignorContName = consignorContName;
	}

	public String getConsignorMobile() {
		return consignorMobile;
	}

	public void setConsignorMobile(String consignorMobile) {
		this.consignorMobile = consignorMobile;
	}

	public String getAddresseeCompName() {
		return addresseeCompName;
	}

	public void setAddresseeCompName(String addresseeCompName) {
		this.addresseeCompName = addresseeCompName;
	}

	public String getAddresseeAddr() {
		return addresseeAddr;
	}

	public void setAddresseeAddr(String addresseeAddr) {
		this.addresseeAddr = addresseeAddr;
	}

	public String getAddresseePhone() {
		return addresseePhone;
	}

	public void setAddresseePhone(String addresseePhone) {
		this.addresseePhone = addresseePhone;
	}

	public String getAddresseeContName() {
		return addresseeContName;
	}

	public void setAddresseeContName(String addresseeContName) {
		this.addresseeContName = addresseeContName;
	}

	public String getAddresseeMobile() {
		return addresseeMobile;
	}

	public void setAddresseeMobile(String addresseeMobile) {
		this.addresseeMobile = addresseeMobile;
	}

	public Double getRealWeightQty() {
		return realWeightQty;
	}

	public void setRealWeightQty(Double realWeightQty) {
		this.realWeightQty = realWeightQty;
	}

	public Double getMeterageWeightQty() {
		return meterageWeightQty;
	}

	public void setMeterageWeightQty(Double meterageWeightQty) {
		this.meterageWeightQty = meterageWeightQty;
	}

	public Integer getQuantity() {
		return Quantity;
	}

	public void setQuantity(Integer quantity) {
		Quantity = quantity;
	}

	public Double getVolume() {
		return Volume;
	}

	public void setVolume(Double volume) {
		Volume = volume;
	}

	public String getConsigneeEmpCode() {
		return consigneeEmpCode;
	}

	public void setConsigneeEmpCode(String consigneeEmpCode) {
		this.consigneeEmpCode = consigneeEmpCode;
	}

	public String getConsignedTm() {
		return consignedTm;
	}

	public void setConsignedTm(String consignedTm) {
		this.consignedTm = consignedTm;
	}

	public String getWaybillRemk() {
		return waybillRemk;
	}

	public void setWaybillRemk(String waybillRemk) {
		this.waybillRemk = waybillRemk;
	}

	public String getConsName() {
		return consName;
	}

	public void setConsName(String consName) {
		this.consName = consName;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getPaymentTypeCode() {
		return paymentTypeCode;
	}

	public void setPaymentTypeCode(String paymentTypeCode) {
		this.paymentTypeCode = paymentTypeCode;
	}

	public String getSettlementTypeCode() {
		return settlementTypeCode;
	}

	public void setSettlementTypeCode(String settlementTypeCode) {
		this.settlementTypeCode = settlementTypeCode;
	}

	public String getServiceTypeCode() {
		return serviceTypeCode;
	}

	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}

	public String getWaybillFee() {
		return waybillFee;
	}

	public void setWaybillFee(String waybillFee) {
		this.waybillFee = waybillFee;
	}

	public String getGoodsChargeFee() {
		return goodsChargeFee;
	}

	public void setGoodsChargeFee(String goodsChargeFee) {
		this.goodsChargeFee = goodsChargeFee;
	}

	public String getChargeAgentFee() {
		return chargeAgentFee;
	}

	public void setChargeAgentFee(String chargeAgentFee) {
		this.chargeAgentFee = chargeAgentFee;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public Integer getTransferDays() {
		return transferDays;
	}

	public void setTransferDays(Integer transferDays) {
		this.transferDays = transferDays;
	}

	public String getInsuranceAmount() {
		return insuranceAmount;
	}

	public void setInsuranceAmount(String insuranceAmount) {
		this.insuranceAmount = insuranceAmount;
	}

	public Double getInsuranceFee() {
		return insuranceFee;
	}

	public void setInsuranceFee(Double insuranceFee) {
		this.insuranceFee = insuranceFee;
	}

	public String getSignBackNo() {
		return signBackNo;
	}

	public void setSignBackNo(String signBackNo) {
		this.signBackNo = signBackNo;
	}

	public String getSignBackCount() {
		return signBackCount;
	}

	public void setSignBackCount(String signBackCount) {
		this.signBackCount = signBackCount;
	}

	public String getSignBackSize() {
		return signBackSize;
	}

	public void setSignBackSize(String signBackSize) {
		this.signBackSize = signBackSize;
	}

	public String getSignBackReceipt() {
		return signBackReceipt;
	}

	public void setSignBackReceipt(String signBackReceipt) {
		this.signBackReceipt = signBackReceipt;
	}

	public String getSignBackSeal() {
		return signBackSeal;
	}

	public void setSignBackSeal(String signBackSeal) {
		this.signBackSeal = signBackSeal;
	}

	public String getSignBackIdentity() {
		return signBackIdentity;
	}

	public void setSignBackIdentity(String signBackIdentity) {
		this.signBackIdentity = signBackIdentity;
	}

	public Double getWaitNotifyFee() {
		return waitNotifyFee;
	}

	public void setWaitNotifyFee(Double waitNotifyFee) {
		this.waitNotifyFee = waitNotifyFee;
	}

	public Double getSignBackFee() {
		return signBackFee;
	}

	public void setSignBackFee(Double signBackFee) {
		this.signBackFee = signBackFee;
	}

	public Double getDeboursFee() {
		return deboursFee;
	}

	public void setDeboursFee(Double deboursFee) {
		this.deboursFee = deboursFee;
	}

	public String getDeboursFlag() {
		return deboursFlag;
	}

	public void setDeboursFlag(String deboursFlag) {
		this.deboursFlag = deboursFlag;
	}

	public Double getDeliverFee() {
		return deliverFee;
	}

	public void setDeliverFee(Double deliverFee) {
		this.deliverFee = deliverFee;
	}

	public String getProductTypeCode() {
		return productTypeCode;
	}

	public void setProductTypeCode(String productTypeCode) {
		this.productTypeCode = productTypeCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getChildNoList() {
		return childNoList;
	}

	public void setChildNoList(String childNoList) {
		this.childNoList = childNoList;
	}

	public String getTeamCode() {
		return teamCode;
	}

	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}

	public Double getDiscountExpressFee() {
		return discountExpressFee;
	}

	public void setDiscountExpressFee(Double discountExpressFee) {
		this.discountExpressFee = discountExpressFee;
	}

	public String getInputerEmpCode() {
		return inputerEmpCode;
	}

	public void setInputerEmpCode(String inputerEmpCode) {
		this.inputerEmpCode = inputerEmpCode;
	}

	public String getDistanceTypeCode() {
		return distanceTypeCode;
	}

	public void setDistanceTypeCode(String distanceTypeCode) {
		this.distanceTypeCode = distanceTypeCode;
	}

}
