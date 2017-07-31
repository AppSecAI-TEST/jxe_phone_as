// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.eruitong.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eruitong.model.InputDataInfo;

// Referenced classes of package com.dingzhou.db:
//            DBHelper

public class AcceptInputDBWrapper {

    private static AcceptInputDBWrapper sInstance;
    private SQLiteDatabase mDb;

    private AcceptInputDBWrapper(Context context) {
        mDb = (new DBHelper(context, "order.db", null, 1)).getWritableDatabase();
    }

    public static AcceptInputDBWrapper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new AcceptInputDBWrapper(context);
        }

        return sInstance;

    }

    public Cursor SeaQueryRank(String s, int i) {
        s = (new StringBuilder("select * from acceptvisit where waybillNo like '%")).append(s).append("%'").append(" " + "and ").append("isUpload").append(" =\"").append(i).append("\"").toString();
        return mDb.rawQuery(s, null);
    }

    public void TMdeleteRank(String s) {
        mDb.delete("acceptvisit", "consignedTm=?", new String[]{s});
    }

    public Cursor dataQueryRank(String s, int i) {
        s = (new StringBuilder("select * from acceptvisit where consignedTm like '%")).append(s).append("%'").append
				(" and ").append("isUpload").append(" =\"").append(i).append("\"").toString();
        return mDb.rawQuery(s, null);
    }

    public void deleteRank() {
        mDb.delete("acceptvisit", null, null);
    }

    public void deleteRank(String s) {
        mDb.delete("acceptvisit", "waybillNo=?", new String[]{s});
    }

    public void deleteRank(String s, int i) {
        mDb.delete("acceptvisit", "waybillNo=? and isUpload=?", new String[]{s, (new StringBuilder(String.valueOf(i))
		).toString()});
    }

    public void insertRank(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7,
						   String s8, String s9, String s10, String s11, String s12, String s13, String s14, String
								   s15, String s16, String s17, String s18, String s19, String s20, String s21, String
								   s22, String s23, String s24, String s25, String s26, String s27, String s28, String
								   s29, String s30, String s31, String s32, String s33, String s34, String s35, String
								   s36, String empCode, String s38, String empName, String opCode, String inputType,
						   int isUpload, String s42, String s43, String s44, String s45, String s46, String s47,
						   String s48, String s49, String s50, String s51, String s52, String custIdentityCard, Double
								   fuelServiceFee, String consType) {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("waybillNo", s);
        contentvalues.put("sourceZoneCode", s1);
        contentvalues.put("destZoneCode", s2);
        contentvalues.put("custCode", s3);
        contentvalues.put("consignorCompName", s4);
        contentvalues.put("consignorAddr", s5);
        contentvalues.put("consignorPhone", s6);
        contentvalues.put("consignorContName", s7);
        contentvalues.put("consignorMobile", s8);
        contentvalues.put("addresseeCompName", s9);
        contentvalues.put("addresseeAddr", s10);
        contentvalues.put("addresseePhone", s11);
        contentvalues.put("addresseeContName", s12);
        contentvalues.put("addresseeMobile", s13);
        contentvalues.put("consName", s14);
        contentvalues.put("realWeightQty", s17);
        contentvalues.put("meterageWeightQty", s18);
        contentvalues.put("quantity", s15);
        contentvalues.put("volume", s16);
        contentvalues.put("consigneeEmpCode", s19);
        contentvalues.put("consignedTm", s20);
        contentvalues.put("paymentTypeCode", s21);
        contentvalues.put("settlementTypeCode", s22);
        contentvalues.put("waybillFee", s23);
        contentvalues.put("goodsChargeFee", s24);
        contentvalues.put("chargeAgentFee", s25);
        contentvalues.put("bankNo", s26);
        contentvalues.put("bankType", s27);
        contentvalues.put("transferDays", s28);
        contentvalues.put("insuranceAmount", s29);
        contentvalues.put("insuranceFee", s30);
        contentvalues.put("deboursFee", s31);
        contentvalues.put("deboursFlag", s32);
        contentvalues.put("productTypeCode", s33);
        contentvalues.put("orderNo", s34);
        contentvalues.put("teamCode", s35);
        contentvalues.put("discountExpressFee", s36);
        contentvalues.put("distanceTypeCode", s38);
        contentvalues.put("signBackFee", s42);
        contentvalues.put("signBackNo", s43);
        contentvalues.put("signBackCount", s44);
        contentvalues.put("signBackSize", s45);
        contentvalues.put("signBackReceipt", s46);
        contentvalues.put("signBackSeal", s47);
        contentvalues.put("signBackIdentity", s48);
        contentvalues.put("waitNotifyFee", s49);
        contentvalues.put("deliverFee", s50);
        contentvalues.put("waybillRemk", s51);
        contentvalues.put("serviceTypeCode", s52);
        contentvalues.put("fuelServiceFee", fuelServiceFee);


        contentvalues.put("inputerEmpCode", empCode);
        contentvalues.put("empName", empName);
        contentvalues.put("opCode", opCode);
        contentvalues.put("inputType", inputType);
        contentvalues.put("isUpload", Integer.valueOf(isUpload));
        contentvalues.put("custIdentityCard", custIdentityCard);
        contentvalues.put("consType", consType);
        mDb.insert("acceptvisit", "", contentvalues);
    }

    public void insertRank(InputDataInfo info, String empCode, String empName, String opCode, String inputType, int
			isUpload, String custIdentityCard, String consType) {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("waybillNo", info.waybillNo);
        contentvalues.put("sourceZoneCode", info.sourceZoneCode);
        contentvalues.put("destZoneCode", info.destZoneCode);
        contentvalues.put("custCode", info.custCode);
        contentvalues.put("consignorCompName", info.consignorCompName);
        contentvalues.put("consignorAddr", info.consignorAddr);
        contentvalues.put("consignorPhone", info.consignorPhone);
        contentvalues.put("consignorContName", info.consignorContName);
        contentvalues.put("consignorMobile", info.consignorMobile);
        contentvalues.put("addresseeCompName", info.addresseeCompName);
        contentvalues.put("addresseeAddr", info.addresseeAddr);
        contentvalues.put("addresseePhone", info.addresseePhone);
        contentvalues.put("addresseeContName", info.addresseeContName);
        contentvalues.put("addresseeMobile", info.addresseeMobile);
        contentvalues.put("consName", info.consName);
        contentvalues.put("quantity", info.quantity);
        contentvalues.put("volume", info.volume);
        contentvalues.put("realWeightQty", info.realWeightQty);
        contentvalues.put("meterageWeightQty", info.meterageWeightQty);
        contentvalues.put("consigneeEmpCode", info.consigneeEmpCode);
        contentvalues.put("consignedTm", info.consignedTm);
        contentvalues.put("paymentTypeCode", info.paymentTypeCode);
        contentvalues.put("settlementTypeCode", info.settlementTypeCode);
        contentvalues.put("waybillFee", info.waybillFee);
        contentvalues.put("goodsChargeFee", info.goodsChargeFee);
        contentvalues.put("chargeAgentFee", info.chargeAgentFee);
        contentvalues.put("bankNo", info.bankNo);
        contentvalues.put("bankType", info.bankType);
        contentvalues.put("transferDays", info.transferDays);
        contentvalues.put("insuranceAmount", info.insuranceAmount);
        contentvalues.put("insuranceFee", info.insuranceFee);
        contentvalues.put("deboursFee", info.deboursFee);
        contentvalues.put("deboursFlag", info.deboursFlag);
        contentvalues.put("productTypeCode", info.productTypeCode);
        contentvalues.put("orderNo", info.orderNo);
        contentvalues.put("teamCode", info.teamCode);
        contentvalues.put("discountExpressFee", info.discountExpressFee);
        contentvalues.put("distanceTypeCode", info.distanceTypeCode);
        contentvalues.put("signBackFee", info.signBackFee);
        contentvalues.put("signBackNo", info.signBackNo);
        contentvalues.put("signBackCount", info.signBackCount);
        contentvalues.put("signBackSize", info.signBackSize);
        contentvalues.put("signBackReceipt", info.signBackReceipt);
        contentvalues.put("signBackSeal", info.signBackSeal);
        contentvalues.put("signBackIdentity", info.signBackIdentity);
        contentvalues.put("waitNotifyFee", info.waitNotifyFee);
        contentvalues.put("deliverFee", info.deliverFee);
        contentvalues.put("waybillRemk", info.waybillRemk);
        contentvalues.put("serviceTypeCode", info.serviceTypeCode);
        contentvalues.put("fuelServiceFee", info.fuelServiceFee);


        contentvalues.put("inputerEmpCode", empCode);
        contentvalues.put("empName", empName);
        contentvalues.put("opCode", opCode);
        contentvalues.put("inputType", inputType);
        contentvalues.put("isUpload", Integer.valueOf(isUpload));
        contentvalues.put("custIdentityCard", custIdentityCard);
        contentvalues.put("consType", consType);
        mDb.insert("acceptvisit", "", contentvalues);
    }

    public Cursor rawQueryRank() {
        return mDb.rawQuery("select * from acceptvisit ASC", null);
    }

    public Cursor rawQueryRank(int i) {
        String s = (new StringBuilder("select * from acceptvisit where isUpload =\"")).append(i).append("\"")
				.toString();
        return mDb.rawQuery(s, null);
    }

    public Cursor rawQueryRank(String s) {
        s = (new StringBuilder("select * from acceptvisit where waybillNo =\"")).append(s).append("\"").toString();
        return mDb.rawQuery(s, null);
    }

    public Cursor rawQueryRank(String s, int i) {
        s = (new StringBuilder("select * from acceptvisit where waybillNo =\"")).append(s).append("\"").append(" and " +
				"").append("isUpload").append(" =\"").append(i).append("\"").toString();
        return mDb.rawQuery(s, null);
    }

    public void updateRank(String s, int i) {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("isUpload", Integer.valueOf(i));
        mDb.update("acceptvisit", contentvalues, "waybillNo=?", new String[]{s});
    }

    public void updateRank(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7,
						   String s8, String s9, String s10) {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("signBackFee", s1);
        contentvalues.put("signBackNo", s2);
        contentvalues.put("signBackCount", s3);
        contentvalues.put("signBackSize", s4);
        contentvalues.put("signBackReceipt", s5);
        contentvalues.put("signBackSeal", s6);
        contentvalues.put("signBackIdentity", s7);
        contentvalues.put("waitNotifyFee", s8);
        contentvalues.put("deliverFee", s9);
        contentvalues.put("waybillRemk", s10);
        mDb.update("acceptvisit", contentvalues, "waybillNo=?", new String[]{s});
    }
}
