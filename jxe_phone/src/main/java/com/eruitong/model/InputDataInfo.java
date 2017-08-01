package com.eruitong.model;

import java.util.List;

public class InputDataInfo {

    public String pollCount;
    public String inputCommission;
    public String deliverEmpCode;
    public String piaoCount;
    public String expressFee;
    public String goodsChargeAgent;
    public String countFee;

    // SendPiecesSubscribeActivity 需要
    public String calledSign;
    public boolean isCall;

    // SendPiecesActivity 需要
    public String downDeliverFee;

    // SendPiecesSignActivity
    public String warehouseFee;
    public String auditedFlg;
    public String backCargoFee;
    public String transferFee;
    public String deliverCommission;
    public String imagePath;

    public String addresseeAddr;
    public String addresseeCompName;
    public String addresseeContName;
    public String addresseeCustCode;
    public String addresseeDistName;
    public String addresseeDistrictCode;
    public String addresseeMobile;
    public String addresseePhone;
    public String bankNo;
    public String bankType;
    public String chargeAgentFee;
    public String consName;
    public String consignedTm;
    public String consigneeEmpCode;
    public String consignorAddr;
    public String consignorCompName;
    public String consignorContName;
    public String consignorDistName;
    public String consignorDistrictCode;
    public String consignorMobile;
    public String consignorPhone;
    public String custCode;
    public String deboursFee;
    public String deboursFlag;
    public String deliverFee;
    public String destZoneCode;
    public String discountExpressFee;
    public String distanceTypeCode;
    public String empName;
    public String goodsChargeFee;
    public String inputType;
    public String inputerEmpCode;
    public String insuranceAmount;
    public String insuranceFee;
    public String isUpload;
    public String meterageWeightQty;
    public String opCode;
    public String opName;
    public String orderNo;
    public String outsiteName;
    public String paymentTypeCode;
    public String productTypeCode;
    public int quantity;
    public String realWeightQty;
    public String serviceTypeCode;
    public String settlementTypeCode;
    public String signBackCount;
    public String signBackFee;
    public String signBackIdentity;
    public String signBackNo;
    public String signBackReceipt;
    public String signBackSeal;
    public String signBackSize;
    public String sourceZoneCode;
    public String teamCode;
    public Double totalFee;
    public String transferDays;
    public String volume;
    public String waitNotifyFee;
    public String waybillFee;
    public String waybillNo;
    public String waybillRemk;
    public String partitionName;
    public Double FeeCount;
    public String WarehouseFee;
    public String childNoList;
    public List childNoLists;
    public String sourceZoneAddress;
    public String destZoneAddress;
    public String destZoneName;
    public String insurance;
    public String setterMethod;
    public String strConsignedTm;
    public String sourcedistrictCode;
    public String sourceZoneName;
    public Double fuelServiceFee = 0.0;
    public String deliverWaybillFee;
    public String provinceCode;
    //public String deliverFeeCommissio;

    public InputDataInfo() {
        FeeCount = Double.valueOf(0.0D);
        productTypeCode = "";
        consignedTm = "";
        inputType = "";
        waitNotifyFee = "";
        signBackFee = "0.0";
        waitNotifyFee = "0.0";
        insuranceAmount = "0.0";
        inputerEmpCode = "";
        auditedFlg = "";
        fuelServiceFee = 0.0;
        consigneeEmpCode = "";
        sourceZoneCode = "";
        destZoneCode = "";
        waybillNo = "";
        quantity = 0;
        consignorPhone = "";
        consignorContName = "";
        consignorAddr = "";
        addresseePhone = "";
        addresseeContName = "";
        downDeliverFee = "";
        addresseeAddr = "";
        teamCode = "";
        deliverFee = "0.0";
        destZoneName = "";
        consName = "";
        discountExpressFee = "0";
        volume = "0";
        waybillFee = "0.0";
        deboursFee = "0.0";
        goodsChargeFee = "0.0";
        chargeAgentFee = "0.0";
        insuranceFee = "0.0";
        bankType = "";
        bankNo = "";
        transferDays = "0";
        insurance = "";
        deboursFlag = "";
        custCode = "";
        consignorMobile = "";
        consignorCompName = "";
        addresseeCompName = "";
        addresseeMobile = "";
        meterageWeightQty = "0";
        signBackNo = "";
        signBackCount = "0";
        signBackSize = "0";
        signBackReceipt = "0";
        signBackSeal = "0";
        signBackIdentity = "0";
        warehouseFee = "0";
        strConsignedTm = "";
        backCargoFee = "";
        transferFee = "0";
        // deliverCommission = "";
        imagePath = "";
    }
}
