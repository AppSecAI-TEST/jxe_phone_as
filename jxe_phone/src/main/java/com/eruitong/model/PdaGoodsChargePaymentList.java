package com.eruitong.model;

/**
 * Created by lyl on 2017/7/28.
 */

public class PdaGoodsChargePaymentList {

    // waybillNo
    public String waybillNo;
    // receiveAccount
    public String receiveAccount;
    // consignorContName
    public String consignorContName;
    // consignorPhone
    public String consignorPhone;
    // consignorMobile
    public String consignorMobile;
    // registerName
    public String registerName;
    // registerPhone
    public String registerPhone;
    // registerMobile
    public String registerMobile;
    // 登记人工号
    public String registerOperCode;
    // 登记网点
    public String registerDeptCode;
    // 登记操作时间
    public String registerTm;

    public String feeAmount;

    public String paymentState;

    public String bankAccountType;

    public String paymentPeriod;

    public String serviceFee;

    public String consignedTm;

    public PdaGoodsChargePaymentList() {
        waybillNo = "";
        receiveAccount = "";
        consignorContName = "";
        consignorPhone = "";
        consignorMobile = "";
        registerName = "";
        registerPhone = "";
        registerMobile = "";
        registerOperCode = "";
        registerDeptCode = "";
        registerTm = "";
        feeAmount = "";
        paymentState = "";
        bankAccountType = "";
        paymentPeriod = "";
        serviceFee = "";
        consignedTm = "";
    }
}
