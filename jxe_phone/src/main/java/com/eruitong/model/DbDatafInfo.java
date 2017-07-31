package com.eruitong.model;

import java.util.List;

public class DbDatafInfo {
	// 行政区域
	private String typeCode;
	private String distName;
	private String distCode;
	private String parentDistCode;
	private String cityCode;
	private String provinceCode;
	private String id;

	// 网点部门
	private String typeLevel;
	private String deptName;
	private String districtCode;
	private String parentCode;
	private String codeSuffix;
	private String outFeeRate;
	private String inFeeRate;
	private String deptAddress;
	private String outsiteName;
	private String partitionName;
	private String transferFee;

	// 员工
	private String billEmployeeid;
	private String empCode;
	private String empName;
	private String deptCode;

	// 线路
	private String srczonecode;
	private String destzonecode;
	private String linecode;
	private String linename;

	// 价格信息
	private String sourceZoneCode;
	private String destZoneCode;
	private String serviceCode;
	private String cargoTypeCode;
	private String limitTypeCode;
	private String expressTypeCode;
	private List<nextWeightPrices> nextWeightPrices;

	// 地区间关系
	private String sourcezonecode;
	private String distancetypecode;

	// 增值服务
	private String currencyCode;
	private String feeTypeCode;
	private String priceDesc;
	// private String serviceCode;
	private List<serviceProdProps> serviceProdProps;

	// 原因代码
	private String causecode;
	private String causecont;

	// 上门费
	private String dropinFeeId;
	private String baseWeightQty;
	private String baseDropInFee;
	private String weightDropInFeeQty;
	private String priceRoundTypeCode;
	private String weightRoundTypeCode;
	
	//特殊品类
	private String categoryPriceId;
	private String categoryId;
	private String categoryCode;
	private String categoryName;
	private String categoryDesc;
	private String unitName;
	private String standardWeight;
	private String validFlg;
	private String calculateSign;
	private String updateExpressFlag;
	
	private String price;
	private String lowerPrice;
	private String  commissionAmt;
	private String  serviceTypeCode;

	public String getDropinFeeId() {
		return dropinFeeId;
	}

	public void setDropinFeeId(String dropinFeeId) {
		this.dropinFeeId = dropinFeeId;
	}

	public String getBaseWeightQty() {
		return baseWeightQty;
	}

	public void setBaseWeightQty(String baseWeightQty) {
		this.baseWeightQty = baseWeightQty;
	}

	public String getBaseDropInFee() {
		return baseDropInFee;
	}

	public void setBaseDropInFee(String baseDropInFee) {
		this.baseDropInFee = baseDropInFee;
	}

	public String getWeightDropInFeeQty() {
		return weightDropInFeeQty;
	}

	public void setWeightDropInFeeQty(String weightDropInFeeQty) {
		this.weightDropInFeeQty = weightDropInFeeQty;
	}

	public String getPriceRoundTypeCode() {
		return priceRoundTypeCode;
	}

	public void setPriceRoundTypeCode(String priceRoundTypeCode) {
		this.priceRoundTypeCode = priceRoundTypeCode;
	}

	public String getWeightRoundTypeCode() {
		return weightRoundTypeCode;
	}

	public void setWeightRoundTypeCode(String weightRoundTypeCode) {
		this.weightRoundTypeCode = weightRoundTypeCode;
	}

	public String getPartitionName() {
		return partitionName;
	}

	public void setPartitionName(String partitionName) {
		this.partitionName = partitionName;
	}

	public String getDeptAddress() {
		return deptAddress;
	}

	public void setDeptAddress(String deptAddress) {
		this.deptAddress = deptAddress;
	}

	public String getOutsiteName() {
		return outsiteName;
	}

	public void setOutsiteName(String outsiteName) {
		this.outsiteName = outsiteName;
	}

	public List<nextWeightPrices> getNextWeightPrices() {
		return nextWeightPrices;
	}

	public void setNextWeightPrices(List<nextWeightPrices> nextWeightPrices) {
		this.nextWeightPrices = nextWeightPrices;
	}

	public List<serviceProdProps> getServiceProdProps() {
		return serviceProdProps;
	}

	public void setServiceProdProps(List<serviceProdProps> serviceProdProps) {
		this.serviceProdProps = serviceProdProps;
	}

	public String getCausecode() {
		return causecode;
	}

	public void setCausecode(String causecode) {
		this.causecode = causecode;
	}

	public String getCausecont() {
		return causecont;
	}

	public void setCausecont(String causecont) {
		this.causecont = causecont;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getFeeTypeCode() {
		return feeTypeCode;
	}

	public void setFeeTypeCode(String feeTypeCode) {
		this.feeTypeCode = feeTypeCode;
	}

	public String getPriceDesc() {
		return priceDesc;
	}

	public void setPriceDesc(String priceDesc) {
		this.priceDesc = priceDesc;
	}

	public String getSourcezonecode() {
		return sourcezonecode;
	}

	public void setSourcezonecode(String sourcezonecode) {
		this.sourcezonecode = sourcezonecode;
	}

	public String getDistancetypecode() {
		return distancetypecode;
	}

	public void setDistancetypecode(String distancetypecode) {
		this.distancetypecode = distancetypecode;
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

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getCargoTypeCode() {
		return cargoTypeCode;
	}

	public void setCargoTypeCode(String cargoTypeCode) {
		this.cargoTypeCode = cargoTypeCode;
	}

	public String getLimitTypeCode() {
		return limitTypeCode;
	}

	public void setLimitTypeCode(String limitTypeCode) {
		this.limitTypeCode = limitTypeCode;
	}

	public String getExpressTypeCode() {
		return expressTypeCode;
	}

	public void setExpressTypeCode(String expressTypeCode) {
		this.expressTypeCode = expressTypeCode;
	}

	public String getSrczonecode() {
		return srczonecode;
	}

	public void setSrczonecode(String srczonecode) {
		this.srczonecode = srczonecode;
	}

	public String getDestzonecode() {
		return destzonecode;
	}

	public void setDestzonecode(String destzonecode) {
		this.destzonecode = destzonecode;
	}

	public String getLinecode() {
		return linecode;
	}

	public void setLinecode(String linecode) {
		this.linecode = linecode;
	}

	public String getLinename() {
		return linename;
	}

	public void setLinename(String linename) {
		this.linename = linename;
	}

	public String getTypeLevel() {
		return typeLevel;
	}

	public void setTypeLevel(String typeLevel) {
		this.typeLevel = typeLevel;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getCodeSuffix() {
		return codeSuffix;
	}

	public void setCodeSuffix(String codeSuffix) {
		this.codeSuffix = codeSuffix;
	}

	public String getOutFeeRate() {
		return outFeeRate;
	}

	public void setOutFeeRate(String outFeeRate) {
		this.outFeeRate = outFeeRate;
	}

	public String getInFeeRate() {
		return inFeeRate;
	}

	public void setInFeeRate(String inFeeRate) {
		this.inFeeRate = inFeeRate;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getDistName() {
		return distName;
	}

	public void setDistName(String distName) {
		this.distName = distName;
	}

	public String getDistCode() {
		return distCode;
	}

	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	public String getParentDistCode() {
		return parentDistCode;
	}

	public void setParentDistCode(String parentDistCode) {
		this.parentDistCode = parentDistCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBillEmployeeid() {
		return billEmployeeid;
	}

	public void setBillEmployeeid(String billEmployeeid) {
		this.billEmployeeid = billEmployeeid;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

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

	public String getTransferFee() {
		return transferFee;
	}

	public void setTransferFee(String transferFee) {
		this.transferFee = transferFee;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getStandardWeight() {
		return standardWeight;
	}

	public void setStandardWeight(String standardWeight) {
		this.standardWeight = standardWeight;
	}

	public String getValidFlg() {
		return validFlg;
	}

	public void setValidFlg(String validFlg) {
		this.validFlg = validFlg;
	}

	public String getCalculateSign() {
		return calculateSign;
	}

	public void setCalculateSign(String calculateSign) {
		this.calculateSign = calculateSign;
	}

	public String getUpdateExpressFlag() {
		return updateExpressFlag;
	}

	public void setUpdateExpressFlag(String updateExpressFlag) {
		this.updateExpressFlag = updateExpressFlag;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getLowerPrice() {
		return lowerPrice;
	}

	public void setLowerPrice(String lowerPrice) {
		this.lowerPrice = lowerPrice;
	}

	public String getCommissionAmt() {
		return commissionAmt;
	}

	public void setCommissionAmt(String commissionAmt) {
		this.commissionAmt = commissionAmt;
	}

	public String getServiceTypeCode() {
		return serviceTypeCode;
	}

	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}

	public String getCategoryPriceId() {
		return categoryPriceId;
	}

	public void setCategoryPriceId(String categoryPriceId) {
		this.categoryPriceId = categoryPriceId;
	}

}
