package com.eruitong.model.net;

public class O_PdaUploadData {
	// 请求JSON:
	// [{"operTypeCode":"02","deliverEmpCode":"000019","waybillNo":"02900001292121","operTm":"2013-11-30 19:40:11","inputType":"0"},{"operTypeCode":"02","deliverEmpCode":"000019","waybillNo":"001993388837","operTm":"2013-11-30 19:40:11","inputType":"1"},{"operTypeCode":"02","deliverEmpCode":"000019","waybillNo":"02900001292121","operTm":"2013-11-30 19:40:11","inputType":"1"},{"operTypeCode":"02","deliverEmpCode":"000019","waybillNo":"02900001292121","operTm":"2013-11-30 19:40:11","inputType":"1"}]

	private String operTypeCode;// 操作代码: 02 - 上门收件; 13 – 派件签收

	private String deliverEmpCode;// 收派员
	private String waybillNo;// 运单号
	private String operTm;// 操作日期: 格式：yyyy-MM-dd HH:mm:ss
	private String inputType;// 输入方式，取值:0-扫描,1-手工输入
	private String carCode;// 车标号
	private String carNo;// 车牌号
	private String lineCode;// 线路编码
	private String stayWayCode;// 原因代码
	private String lockedCarCode;// 封车码
	private String srcCarNo;// 源车标
	private String bagCageNo;// 包笼号
	private String deptCode;// 操作点部代码
	private String operEmpCode;// 操作人
	private String macNo;// 机器编号
	private String uploadEmpCode;// 上传人
	private String acquisitionInfo;// 扩展属性

	public String getOperTypeCode() {
		return operTypeCode;
	}

	public void setOperTypeCode(String operTypeCode) {
		this.operTypeCode = operTypeCode;
	}

	public String getDeliverEmpCode() {
		return deliverEmpCode;
	}

	public void setDeliverEmpCode(String deliverEmpCode) {
		this.deliverEmpCode = deliverEmpCode;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getOperTm() {
		return operTm;
	}

	public void setOperTm(String operTm) {
		this.operTm = operTm;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getCarCode() {
		return carCode;
	}

	public void setCarCode(String carCode) {
		this.carCode = carCode;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getLineCode() {
		return lineCode;
	}

	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}

	public String getStayWayCode() {
		return stayWayCode;
	}

	public void setStayWayCode(String stayWayCode) {
		this.stayWayCode = stayWayCode;
	}

	public String getLockedCarCode() {
		return lockedCarCode;
	}

	public void setLockedCarCode(String lockedCarCode) {
		this.lockedCarCode = lockedCarCode;
	}

	public String getSrcCarNo() {
		return srcCarNo;
	}

	public void setSrcCarNo(String srcCarNo) {
		this.srcCarNo = srcCarNo;
	}

	public String getBagCageNo() {
		return bagCageNo;
	}

	public void setBagCageNo(String bagCageNo) {
		this.bagCageNo = bagCageNo;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getOperEmpCode() {
		return operEmpCode;
	}

	public void setOperEmpCode(String operEmpCode) {
		this.operEmpCode = operEmpCode;
	}

	public String getMacNo() {
		return macNo;
	}

	public void setMacNo(String macNo) {
		this.macNo = macNo;
	}

	public String getUploadEmpCode() {
		return uploadEmpCode;
	}

	public void setUploadEmpCode(String uploadEmpCode) {
		this.uploadEmpCode = uploadEmpCode;
	}

	public String getAcquisitionInfo() {
		return acquisitionInfo;
	}

	public void setAcquisitionInfo(String acquisitionInfo) {
		this.acquisitionInfo = acquisitionInfo;
	}

}
