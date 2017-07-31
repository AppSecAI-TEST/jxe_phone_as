package com.eruitong.utils;

import java.util.Date;

public class ProvisionTemplate  {

	/*
	 * 巴组角色
	 */
    private String groupRole;
    /*巴组人数 */
	private Long groupNo;
	/*巴组数量*/
	private Long groupQty;
    /*外阜到外阜运费计提*/
    private Double outProvision;
    /*外阜到西安运费计提*/
    private Double outXianProvision;
    /*票数计提*/
    private Double pollProvision;
    /*重量计提*/
	private Double weightQtyProvision;
	/*代收款计提*/
	private Double goodsChageProvision;
	/*保价计提*/
	private Double insuranceProvision;
	/**
	 * 快递派件运费计提
	 */
	private Double expressDeliverProsion;

	private Double groupProvision;

	/**
	 * 个人利润计提
	 */
	private Double personalProvision;


	private String deptCode;
	public String getGroupRole() {
		return groupRole;
	}
	public void setGroupRole(String groupRole) {
		this.groupRole = groupRole;
	}
	public Long getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(Long groupNo) {
		this.groupNo = groupNo;
	}
	public Long getGroupQty() {
		return groupQty;
	}
	public void setGroupQty(Long groupQty) {
		this.groupQty = groupQty;
	}
	public Double getOutProvision() {
		return outProvision;
	}
	public void setOutProvision(Double outProvision) {
		this.outProvision = outProvision;
	}
	public Double getOutXianProvision() {
		return outXianProvision;
	}
	public void setOutXianProvision(Double outXianProvision) {
		this.outXianProvision = outXianProvision;
	}
	public Double getPollProvision() {
		return pollProvision;
	}
	public void setPollProvision(Double pollProvision) {
		this.pollProvision = pollProvision;
	}
	public Double getWeightQtyProvision() {
		return weightQtyProvision;
	}
	public void setWeightQtyProvision(Double weightQtyProvision) {
		this.weightQtyProvision = weightQtyProvision;
	}
	public Double getGoodsChageProvision() {
		return goodsChageProvision;
	}
	public void setGoodsChageProvision(Double goodsChageProvision) {
		this.goodsChageProvision = goodsChageProvision;
	}
	public Double getInsuranceProvision() {
		return insuranceProvision;
	}
	public void setInsuranceProvision(Double insuranceProvision) {
		this.insuranceProvision = insuranceProvision;
	}
	public Double getExpressDeliverProsion() {
		return expressDeliverProsion;
	}
	public void setExpressDeliverProsion(Double expressDeliverProsion) {
		this.expressDeliverProsion = expressDeliverProsion;
	}
	public Double getPersonalProvision() {
		return personalProvision;
	}
	public void setPersonalProvision(Double personalProvision) {
		this.personalProvision = personalProvision;
	}
	public Double getGroupProvision() {
		return groupProvision;
	}
	public void setGroupProvision(Double groupProvision) {
		this.groupProvision = groupProvision;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}



}
