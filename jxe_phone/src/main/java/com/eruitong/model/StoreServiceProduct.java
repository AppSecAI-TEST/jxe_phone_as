package com.eruitong.model;


import java.util.Date;

/**********************************************
 * 增值服务之保价仓管费
 * 
 * Copyright JXE..
 * All rights reserved. 
 * 
 *********************************************
 */
public class StoreServiceProduct {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5899374073111001191L;
	
	/**
	 * 1小时常量
	 */
	private static final int ONE_HOUR = 60*60*1000;
	
	//起步金额
	private double baseWeight;
	//起步重量（首重）
	private double basePrice;
	//续重单价
	private double nextPrice;
	//最高倍数
	private double maxMultiple;
	//N小时之后开始计收
	private double exceptHours;

	public double getBaseWeight() {
		return baseWeight;
	}

	public void setBaseWeight(double baseWeight) {
		this.baseWeight = baseWeight;
	}
	
	public double getNextPrice() {
		return nextPrice;
	}

	public void setNextPrice(double nextPrice) {
		this.nextPrice = nextPrice;
	}
	public double getBasePrice() {
		return basePrice;
	}
	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}
	
	public double getMaxMultiple() {
		return maxMultiple;
	}
	
	public void setMaxMultiple(double maxMultiple) {
		this.maxMultiple = maxMultiple;
	}
	
	/**
	 * 计算时差：单位(小时)
	 * @param d1
	 * @param d2
	 * @return
	 */
	private static int getHours(Date d1, Date d2){
		if (d1 == null || d2 == null){
			return 0;
		}
		int hours = (int)((d2.getTime() - d1.getTime()) / ONE_HOUR);
		return hours;
	}
	public double getExceptHours() {
		return exceptHours;
	}
	public void setExceptHours(double exceptHours) {
		this.exceptHours = exceptHours;
	}
}