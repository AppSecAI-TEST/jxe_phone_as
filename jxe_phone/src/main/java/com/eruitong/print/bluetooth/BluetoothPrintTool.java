// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.eruitong.print.bluetooth;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.text.TextUtils;

import com.eruitong.db.DepartmentDBWrapper;
import com.eruitong.db.DistrictDBWrapper;
import com.eruitong.model.InputDataInfo;
import com.eruitong.model.PdaGoodsChargePaymentList;
import com.eruitong.utils.MyUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zpSDK.zpSDK.zpSDK;

public class BluetoothPrintTool {

    public BluetoothPrintTool() {
    }

    /**
     * 打印客户联
     *
     * @param info
     * @return
     */
    public static boolean kehuLian(InputDataInfo info) {
        if (MyUtils.isEmpty(info.goodsChargeFee)) {
            info.goodsChargeFee = "0";
        }

        Double double1;
        double1 = Double.valueOf(0.0D);
        Double fuelServiceFee = 0.0;
        if (info.fuelServiceFee == null) {
            fuelServiceFee = 0.0;
        } else {
            fuelServiceFee = info.fuelServiceFee;
        }
        double d = Double.valueOf(Double.parseDouble(info.waybillFee) + Double.parseDouble(info.signBackFee) + Double
                .parseDouble(info.waitNotifyFee) + Double.parseDouble(info.deboursFee) + Double.parseDouble(info
                .insuranceFee) + Double.parseDouble(info.deliverFee)) + fuelServiceFee;
        double d1 = Double.parseDouble(info.goodsChargeFee);
        // double d2 =
        // Double.parseDouble(inputdatainfo.deboursFee)+(inputdatainfo.fuelServiceFee==null?0.0:inputdatainfo
        // .fuelServiceFee);
        double1 = Double.valueOf(d + d1);

        if (!zpSDK.zp_page_create(80D, 61D)) {
            return false;
        }
        zpSDK.TextPosWinStyle = false;
        zpSDK.zp_draw_rect(0.0D, 0D, 72D, 53D, 3);
        zpSDK.zp_draw_line(0.0D, 4D, 72D, 4D, 3);
        zpSDK.zp_draw_line(0.0D, 8D, 72D, 8D, 3);
        zpSDK.zp_draw_line(0.0D, 12D, 72D, 12D, 3);
        zpSDK.zp_draw_line(0.0D, 16D, 72D, 16D, 3);
        zpSDK.zp_draw_line(0.0D, 20D, 72D, 20D, 3);
        zpSDK.zp_draw_line(0.0D, 24D, 72D, 24D, 3);
        zpSDK.zp_draw_line(0.0D, 28D, 72D, 28D, 3);
        zpSDK.zp_draw_line(0.0D, 32D, 72D, 32D, 3);
        zpSDK.zp_draw_line(0.0D, 36D, 72D, 36D, 3);
        zpSDK.zp_draw_line(0.0D, 40D, 72D, 40D, 3);
        zpSDK.zp_draw_line(0.0D, 45D, 72D, 45D, 3);
        //zpSDK.zp_draw_line(0.0D, 48D, 72D, 48D, 3);
        zpSDK.zp_draw_line(12D, 0D, 12D, 8D, 3);

        /*
         * zpSDK.zp_draw_text_ex(1.0D, 7D, "聚信代收款  招行来代管", "宋体",
		 * 6.7999999999999998D, 0, true, true, false);
		 */
        zpSDK.zp_draw_text_ex(1.0D, 3D, info.consignedTm.substring(5, 10), "宋体", 3D, 0, true, true, false);
        zpSDK.zp_draw_text_ex(1.0D, 7D, info.consignedTm.substring(11, 16), "宋体", 3D, 0, true, true, false);

        if (info.productTypeCode.equals("B1")) {
            zpSDK.zp_draw_text_ex(13D, 3D, (new StringBuilder("快递  运单号")).append(info.waybillNo).append(" ").append
                    (info.sourceZoneCode).append(" ").append(info.consignorContName).toString(), "宋体", 3D, 0, true,
                    false, false);

        } else if (info.productTypeCode.equals("B2")) {
            zpSDK.zp_draw_text_ex(13D, 3D, (new StringBuilder("普货  运单号")).append(info.waybillNo).append(" ").append
                    (info.sourceZoneCode).append(" ").append(info.consignorContName).toString(), "宋体", 3D, 0, true,
                    false, false);
        } else if (info.productTypeCode.equals("B3")) {
            zpSDK.zp_draw_text_ex(13D, 3D, (new StringBuilder("普快  运单号")).append(info.waybillNo).append(" ").append
                    (info.sourceZoneCode).append(" ").append(info.consignorContName).toString(), "宋体", 3D, 0, true,
                    false, false);
        } else {
            zpSDK.zp_draw_text_ex(13D, 3D, (new StringBuilder("普货  运单号")).append(info.waybillNo).append(" ").append
                    (info.sourceZoneCode).append(" ").append(info.consignorContName).toString(), "宋体", 3D, 0, true,
                    false, false);
        }

        if (!MyUtils.isEmpty(info.addresseeMobile)) {
            if (!MyUtils.isEmpty(info.addresseePhone)) {
                zpSDK.zp_draw_text_ex(13D, 7D, (new StringBuilder(String.valueOf(info.addresseeDistName))).append(" "
                        + "").append(info.addresseeContName).append(" ").append(info.addresseeMobile).append(" 取件费  "
                        + "").append(fuelServiceFee).toString(), "宋体", 3D, 0, true, false, false);
            } else {
                zpSDK.zp_draw_text_ex(13D, 7D, (new StringBuilder(String.valueOf(info.addresseeDistName))).append(" "
                        + "").append(info.addresseeContName).append(" ").append(info.addresseeMobile).append(" 取件费  "
                        + "").append(fuelServiceFee).toString(), "宋体", 3D, 0, true, false, false);
            }
        } else {
            zpSDK.zp_draw_text_ex(13D, 7D, (new StringBuilder(String.valueOf(info.addresseeDistName))).append(" ")
                    .append(info.addresseeContName).append(" ").append(info.addresseePhone).append(" 取件费  ").append
                            (fuelServiceFee).toString(), "宋体", 3D, 0, true, false, false);
        }

        zpSDK.zp_draw_text_ex(1.0D, 11D, (new StringBuilder(String.valueOf(info.consName))).append(" ").append(info
                .quantity).append("件 重量").append(info.meterageWeightQty).append("kg " + "运费").append(info.waybillFee)
                .append("  垫付").append(info.deboursFee)/*
                        .append(" 派送费")
						.append(inputdatainfo.deliverFee)*/.toString(), "宋体", 2.7999999999999998D, 0, true, false,
                false);
        zpSDK.zp_draw_text_ex(1.0D, 15D, (new StringBuilder("声明价值")).append(info.insuranceAmount).append(" " + "保价费")
                .append(info.insuranceFee).append("元  回单费").append(info.signBackFee).append(" " + "").append(info
                        .signBackNo).toString(), "宋体", 3D, 0, true, false, false);

	/*	if (MyUtils.isHenan(inputdatainfo.provinceCode)) {
            zpSDK.zp_draw_text_ex(1.0D, 22.5D, "货款买方直接转给卖方", "宋体", 6D, 0, true, false, false);
		} else {
			zpSDK.zp_draw_text_ex(1.0D, 22.5D, "一  票   送  货  到   店  铺", "宋体", 6D, 0, true, false, false);
		}
*/
        String bankT = "";
        if (!"0".equals(info.bankType)) {
            if ("1".equals(info.bankType)) {
                bankT = "浦发银行";
            }
            if (!"5".equals(info.bankType)) {
                if (!"-1".equals(info.bankType) && !info.bankNo.isEmpty()) {
                } else {
                    bankT = "无卡号";
                }
            } else {
                bankT = "招商银行";
            }
        } else {
            bankT = "工商银行";
        }

        String s = "";
        if (MyUtils.isHenan(info.provinceCode)) {
            s = "货款";
        } else {
            s = "代收款";
        }
        zpSDK.zp_draw_text_ex(1.0D, 19D, (new StringBuilder(s)).append(info.goodsChargeFee).append(" 服务费").append
                (info.chargeAgentFee).append(" ").append(bankT).append(" ").append(" 等通知费").append(info
                .waitNotifyFee).toString(), "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(1.0D, 23D, (new StringBuilder("银行账号 ")).append(info.bankNo)
                /*.append(inputdatainfo.transferDays).append("天转")*/.toString(), "宋体", 3D, 0, true, false, false);

        zpSDK.zp_draw_text_box(1.0D, 27.5D, 72D, 3D, "客户须知 1：货物应当申明价值， " +
                "每件货物遗失或损毁，承运人按总保价的平均价值赔偿。2：每票总价值如果超过5万元，不予承运。3：出现异常，90天内联系处理，超期不予处理", "宋体", 2.7D, 0, true, false,
                false);

        if (!info.paymentTypeCode.equals("1")) {
            if (!info.paymentTypeCode.equals("2")) {

            } else {
                zpSDK.zp_draw_text_ex(1.0D, 39.5D, (new StringBuilder("到付  ")).append(double1).append("元").toString()
                        , "宋体", 3D, 0, true, false, false);
            }
        } else {
            zpSDK.zp_draw_text_ex(1.0D, 39.5D, (new StringBuilder("寄付  ")).append(d).append("元").toString(), "宋体",
                    3D, 0, true, false, false);
        }
        zpSDK.zp_draw_text_ex(1.0D, 44D, (new StringBuilder("备注:")).append(info.waybillRemk).toString(), "宋体", 3D, 0,
                true, false, false);
        zpSDK.zp_draw_text_ex(39.5D, 39.5D, "客服热线:", "宋体", 3D, 0, true, false, false);

        zpSDK.zp_draw_text_ex(45D, 44D, "签字", "宋体", 3.8D, 0, true, false, false);

        zpSDK.zp_draw_text_ex(52.5D, 40D, "4008-111115", "宋体", 3.2000000000000002D, 0, true, false, false);

        zpSDK.zp_draw_text_ex(1.0D, 51D, (new StringBuilder(String.valueOf("派送费"))).append(" ").append(info
                .deliverFee).toString(), "宋体", 5, 0, true, false, false);
        zpSDK.zp_page_print(false);
        zpSDK.zp_page_clear();

        zpSDK.zp_goto_mark_label(120);
        zpSDK.zp_page_free();
        return true;
    }

    /**
     * 打印标签
     *
     * @param info
     * @param childList 子列表
     * @param isLook    接受快件页面的 是否验视
     * @return true：继续执行 false:关闭显示Dialog
     */
    public static boolean xiaopiao(InputDataInfo info, List<String> childList, boolean isLook) {
        if (MyUtils.isEmpty(info.teamCode)) {
            info.teamCode = "";
        }

        boolean normal = xiaopiao(info, info.waybillNo, isLook);
        if (!normal) return false;

        if (info.quantity < 2) {
            return true;
        }

        for (String childWaybillNo : childList) {
            boolean normal1 = xiaopiao(info, childWaybillNo, isLook);
            if (!normal1) return false;
        }
        return true;
    }

    @SuppressWarnings("unused")
    public static boolean xiaopiao(InputDataInfo info, String waybillNo, boolean isLook) {
        if (!zpSDK.zp_page_create(80D, 61D)) {
            return false;
        }

        zpSDK.TextPosWinStyle = false;
        zpSDK.zp_draw_rect(0.0D, 0.0D, 72D, 53D, 3);
        zpSDK.zp_draw_line(0.0D, 14D, 72D, 14D, 3);
        zpSDK.zp_draw_line(0.0D, 34D, 72D, 34D, 3);
        zpSDK.zp_draw_line(6D, 14D, 6D, 34D, 3);
        zpSDK.zp_draw_line(6D, 19D, 72D, 19D, 3);
        zpSDK.zp_draw_line(6D, 24D, 72D, 24D, 3);
        zpSDK.zp_draw_line(6D, 29D, 72D, 29D, 3);
        zpSDK.zp_draw_line(10D, 0.0D, 10D, 14D, 3);
        zpSDK.zp_draw_line(50D, 48D, 72D, 48D, 3);
        zpSDK.zp_draw_line(50D, 34D, 50D, 53D, 3);
        zpSDK.zp_draw_text_ex(1.0D, 5.0D, "聚信", "宋体", 4D, 0, true, true, false);
        zpSDK.zp_draw_text_box(1.0D, 9D, 9D, 3D, info.consignedTm.substring(5, 10), "宋体", 3D, 0, true, true, false);
        zpSDK.zp_draw_text_box(1.0D, 13D, 9D, 3D, info.consignedTm.substring(11, 16), "宋体", 3D, 0, true, true, false);
        if (MyUtils.isEmpty(info.partitionName)) {
            // 目的地城市 + 目的地网点
            zpSDK.zp_draw_text_ex(10D, 12D, info.addresseeDistName, "宋体", 12D, 0, true, false, false);
            zpSDK.zp_draw_text_ex(33D, 12.1D, info.outsiteName, "宋体", 12D, 0, true, false, false);
            zpSDK.zp_draw_text_ex(58D, 12.1D, info.teamCode, "宋体", 14D, 0, false, false, false);
        } else {
            zpSDK.zp_draw_text_ex(10D, 12.1D, info.partitionName, "宋体", 14D, 0, true, false, false);
            zpSDK.zp_draw_text_ex(28D, 12.1D, info.addresseeDistName, "宋体", 13D, 0, true, false, false);
            zpSDK.zp_draw_text_ex(57D, 12.1D, info.teamCode, "宋体", 14D, 0, false, false, false);
        }

        if (isLook) {
            zpSDK.zp_draw_text_box(67.5D, 19D, 5D, 4D, "已验视", "宋体", 4D, 0, false, false, false);
        }

        zpSDK.zp_draw_text_box(0.5D, 19D, 5D, 5D, info.addresseeContName, "宋体", 5D, 0, false, false, false);
        zpSDK.zp_draw_text_ex(7D, 18D, (new StringBuilder(String.valueOf(info.waybillNo))).append("  ").append(info
                .sourceZoneCode).append("  ").append(info.consignorContName).toString(), "宋体", 4D, 0, true, false,
                false);

        if (!MyUtils.isEmpty(info.addresseeMobile)) {
            if (!MyUtils.isEmpty(info.addresseePhone)) {
                zpSDK.zp_draw_text_ex(7D, 23D, (new StringBuilder(String.valueOf(info.addresseeDistName))).append
                        (info.addresseePhone).append(" / ").append(info.addresseeMobile).toString(), "宋体", 4D, 0,
                        true, false, false);
            } else {
                zpSDK.zp_draw_text_ex(7D, 23D, (new StringBuilder(String.valueOf(info.addresseeDistName))).append
                        (info.addresseeMobile).toString(), "宋体", 4D, 0, true, false, false);

            }
        } else {
            zpSDK.zp_draw_text_ex(7D, 23D, (new StringBuilder(String.valueOf(info.addresseeDistName))).append(info
                    .addresseePhone).toString(), "宋体", 4D, 0, true, false, false);

        }

        zpSDK.zp_draw_text_ex(7D, 28D, info.addresseeAddr, "宋体", 3.3999999999999999D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(7D, 33D, (new StringBuilder(String.valueOf(info.consName))).append("1/").append(info
                .quantity).append("件 重量").append(info.meterageWeightQty).append("kg " + "收件员").append(info
                .consigneeEmpCode).toString(), "宋体", 3.6000000000000001D, 0, true, false, false);

        if (info.productTypeCode.equals("B1")) {
            zpSDK.zp_draw_text_ex(51D, 46.5D, "快递", "宋体", 10D, 0, true, false, false);
        } else if (info.productTypeCode.equals("B2")) {
            zpSDK.zp_draw_text_ex(51D, 46.5D, "普货", "宋体", 10D, 0, true, false, false);
        } else if (info.productTypeCode.equals("B3")) {
            zpSDK.zp_draw_text_ex(51D, 46.5D, "普快", "宋体", 10D, 0, true, false, false);
        }
        zpSDK.zp_draw_text_ex(52D, 52D, "4008-111115", "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_barcode(7D, 35D, waybillNo, zpSDK.BARCODE_TYPE.BARCODE_CODE128, 14D, 3, 0);
        zpSDK.zp_draw_text_ex(10D, 52D, waybillNo, "宋体", 3D, 0, false, false, false);
        zpSDK.zp_page_print(false);
        zpSDK.zp_page_clear();
        zpSDK.zp_goto_mark_label(120);
        zpSDK.zp_page_free();

        return true;
    }

    /**
     * 派件 多单打印
     *
     * @param itemDataList   订单列表
     * @param deliverEmpCode 收件人员
     * @param signBitmap     签名的图片
     * @param context
     * @return
     */
    public static boolean printWaybillList(ArrayList<InputDataInfo> itemDataList, String deliverEmpCode, Bitmap
            signBitmap, Context context) {
        for (int i = 0; i < itemDataList.size(); i++) {
            InputDataInfo itemData = itemDataList.get(i);

            DepartmentDBWrapper departmentDB = DepartmentDBWrapper.getInstance(context);
            Cursor cursor = departmentDB.rawQueryRank(itemData.sourceZoneCode);// 查询发货网点
            if (!cursor.moveToNext()) {
                cursor.close();
                return false;
            }

            itemData.sourcedistrictCode = cursor.getString(cursor.getColumnIndex("districtCode"));
            Cursor mCursor = DistrictDBWrapper.getInstance(context).rawQueryRank(itemData.sourcedistrictCode);
            if (!mCursor.moveToNext()) {
                mCursor.close();
                return false;
            }

            itemData.consignorDistName = mCursor.getString(mCursor.getColumnIndex("distName"));
            itemData.provinceCode = mCursor.getString(mCursor.getColumnIndex("provinceCode"));


            DecimalFormat decimalformat = new DecimalFormat("0.00");

            if (!zpSDK.zp_page_create(80D, 61D)) {
                return false;
            }

            zpSDK.TextPosWinStyle = false;
            zpSDK.zp_draw_rect(0.0D, 0.0D, 72D, 50D, 3);
            zpSDK.zp_draw_line(48D, 0.0D, 48D, 10D, 3);
            zpSDK.zp_draw_line(0.0D, 6D, 72D, 6D, 3);
            zpSDK.zp_draw_line(0.0D, 10D, 72D, 10D, 3);
            zpSDK.zp_draw_line(0.0D, 14D, 72D, 14D, 3);
            zpSDK.zp_draw_line(0.0D, 18D, 72D, 18D, 3);
            zpSDK.zp_draw_line(0.0D, 22D, 72D, 22D, 3);
            zpSDK.zp_draw_line(45D, 22D, 45D, 34D, 3);
            zpSDK.zp_draw_line(0.0D, 30D, 45D, 30D, 3);
            zpSDK.zp_draw_line(0.0D, 34D, 72D, 34D, 3);
            zpSDK.zp_draw_line(0.0D, 38D, 72D, 38D, 3);
            zpSDK.zp_draw_line(0.0D, 42D, 72D, 42D, 3);
            zpSDK.zp_draw_line(0.0D, 46D, 72D, 46D, 3);
            String date = new SimpleDateFormat("MM-dd").format(new Date());
            zpSDK.zp_draw_text_ex(1.0D, 5D, date, "宋体", 4D, 0, true, false, false);
            // zpSDK.zp_draw_bitmap(BitmapFactory.decodeResource(getResources(),
            // R.drawable.jxe_icon), 5D, 10D);
            zpSDK.zp_draw_text_ex(49D, 5D, "聚信客服电话", "宋体", 3.5D, 0, true, false, false);
            zpSDK.zp_draw_text_ex(49D, 9.5D, "4008111115", "宋体", 4D, 0, true, false, false);
            zpSDK.zp_draw_text_ex(15D, 5D, (new StringBuilder("运单号: ")).append(itemData.waybillNo).append(" ")
                    .toString(), "宋体", 3D, 0, true, false, false);
            if (!TextUtils.isEmpty(itemData.consignorPhone) && !itemData.consignorPhone.equals("null")) {
                zpSDK.zp_draw_text_ex(1.0D, 9.5D, (new StringBuilder(String.valueOf(itemData.consignorDistName)))
                        .append(" ").append(itemData.sourceZoneCode).append(" ").append(itemData.consignorContName)
                        .append(" ").append(itemData.consignorPhone).toString(), "宋体", 3D, 0, true, false, false);
            } else {
                zpSDK.zp_draw_text_ex(1.0D, 9.5D, (new StringBuilder(String.valueOf(itemData.consignorDistName)))
                        .append(" ").append(itemData.sourceZoneCode).append(" ").append(itemData.consignorContName)
                        .append(" ").append(itemData.consignorMobile).toString(), "宋体", 3D, 0, true, false, false);
            }
            String addresseeAddrP = "";
            if (!TextUtils.isEmpty(itemData.addresseeAddr) && !itemData.addresseeAddr.equals("null")) {
                if (itemData.addresseeAddr.length() > 14) {
                    addresseeAddrP = itemData.addresseeAddr.substring(itemData.addresseeAddr.length() - 14, itemData
                            .addresseeAddr.length());
                } else {
                    addresseeAddrP = itemData.addresseeAddr;
                }
            }
            if (!TextUtils.isEmpty(itemData.addresseeMobile) && !itemData.addresseeMobile.equals("null")) {
                zpSDK.zp_draw_text_ex(1.0D, 13.5D, (new StringBuilder(String.valueOf(itemData.addresseeContName)))
                        .append(" ").append(itemData.addresseeMobile).append(" ").append(itemData.addresseeAddr)
                        .toString(), "宋体", 2.8999999999999999D, 0, true, false, false);
            } else {
                zpSDK.zp_draw_text_ex(1.0D, 13.5D, (new StringBuilder(String.valueOf(itemData.addresseeContName)))
                        .append(" ").append(itemData.addresseePhone).append(" ").append(addresseeAddrP).toString(),
                        "宋体", 3D, 0, true, false, false);
            }
            /*
             * if(itemData.deliverCommission!=null){ itemData.waybillFee =
			 * String.valueOf(decimalformat.format(Double.valueOf(itemData.
			 * waybillFee) -Double.valueOf(itemData.deliverCommission))); }
			 */

            zpSDK.zp_draw_text_ex(1.0D, 17.5D, (new StringBuilder(String.valueOf(itemData.consName))).append(" ")
                    .append(itemData.quantity).append("件 重量 ").append(itemData.meterageWeightQty).append("kg ")
                    .append("元 派件员 ").append(deliverEmpCode).toString(), "宋体", 3D, 0, true, false, false);
            zpSDK.zp_draw_text_ex(1.0D, 21.5D, (new StringBuilder("运费 ")).append(itemData.waybillFee).append("元 保价金额 " +
                    "" + "" + "" + "" + "" + "" + "" + "" + "" + "").append(itemData.insuranceAmount).append("元 保价费 " +
                    "").append(itemData.insuranceFee).append("元  ").toString(), "宋体", 3D, 0, true, false, false);

            if (MyUtils.isEmpty(itemData.goodsChargeFee)) {
                itemData.goodsChargeFee = "0.0";
            }
            if (MyUtils.isEmpty(itemData.chargeAgentFee)) {
                itemData.chargeAgentFee = "0.0";
            }
            if (MyUtils.isEmpty(itemData.signBackFee)) {
                itemData.signBackFee = "0.0";
            }
            if (MyUtils.isEmpty(itemData.deliverFee)) {
                itemData.deliverFee = "0.0";
            }

            if (MyUtils.isHenan(itemData.provinceCode)) {

                String bankT = "";
                if ("0".equals(itemData.bankType)) {
                    bankT = "工商银行";
                } else if ("1".equals(itemData.bankType)) {
                    bankT = "浦发银行";
                } else if ("3".equals(itemData.bankType)) {
                    bankT = "建设银行";
                } else if ("5".equals(itemData.bankType)) {
                    bankT = "招商银行";
                } else {
                    bankT = "无卡号";
                }

                if (MyUtils.isEmpty(itemData.bankNo)) {
                    itemData.bankNo = "";
                }

                zpSDK.zp_draw_text_ex(1.0D, 25D, (new StringBuilder("卡号类型： ")).append(bankT).toString(), "宋体", 3D, 0,
                        true, false, false);
                zpSDK.zp_draw_text_ex(1.0D, 29D, (new StringBuilder("银行账号 ")).append(itemData.bankNo).toString(),
                        "宋体", 3D, 0, true, false, false);

                double d = Double.parseDouble(itemData.goodsChargeFee) - Double.parseDouble(itemData.chargeAgentFee);
                zpSDK.zp_draw_text_ex(1.0D, 33.5D, (new StringBuilder("应转代收 ")).append(d)
                        /*.append("元 代收服务费 ").append(itemData.chargeAgentFee).append("元  ")*/.append("  派送费 ").append
                                (itemData.deliverFee == null ? "0.0" : itemData.deliverFee).append("元  ").toString(),
                        "宋体", 3D, 0, true, false, false);
            } else {
                zpSDK.zp_draw_text_ex(1.0D, 28.5D, "一票送货到店 铺", "宋体", 6D, 0, true, false, false);

                zpSDK.zp_draw_text_ex(1.0D, 33.5D, (new StringBuilder("代收金额 ")).append(itemData.goodsChargeFee)
								/*
								 * .append("元 代收服务费 ").append(itemData.
								 * chargeAgentFee).append("元  ")
								 */.append("  派送费 ").append(itemData.deliverFee == null ? "0.0" : itemData.deliverFee)
                        .append("元  ").toString(), "宋体", 3D, 0, true, false, false);
            }
            if (signBitmap != null) {
                signBitmap = scaleSignBitmap(signBitmap);
                zpSDK.zp_draw_bitmap(signBitmap, 370D, 177D);
            }


			/*
			 * if(itemData.deliverFee!=null &&
			 * itemData.deliverCommission!=null){ itemData.deliverFee =
			 * String.valueOf(decimalformat.format(Double.valueOf(itemData.
			 * deliverFee) + Double.valueOf(itemData.deliverCommission))); }
			 * if(itemData.deliverFee==null &&itemData.deliverCommission!=null){
			 * itemData.deliverFee = itemData.deliverCommission; }
			 */

            if (MyUtils.isEmpty(itemData.signBackFee)) {
                itemData.signBackFee = "0.0";
            }
            if (itemData.fuelServiceFee == null) {
                itemData.fuelServiceFee = 0.0;
            }
            zpSDK.zp_draw_text_ex(1.0D, 37.5D, (new StringBuilder("签回单费 ")).append(itemData.signBackFee).append("元   " +
                    "   取件费 ").append(itemData.fuelServiceFee + "元").append("  原返费" + " ").append(itemData
                    .backCargoFee == null ? "0.0" : itemData.backCargoFee + "元").toString(), "宋体", 3D, 0, true,
                    false, false);
            if (MyUtils.isEmpty(itemData.signBackNo)) {
                itemData.signBackNo = "";
            }
            zpSDK.zp_draw_text_ex(1.0D, 41.5D, (new StringBuilder("回单号 ")).append(itemData.signBackNo).append("  收条  " +
                    "" + "" + "" + "" + "" + "" + "" + "" + "" + " 身份证   盖章").toString(), "宋体", 3D, 0, true, false,
                    false);
            if (MyUtils.isEmpty(itemData.deboursFee)) {
                itemData.deboursFee = "0.0";
            }
            if (MyUtils.isEmpty(itemData.waitNotifyFee)) {
                itemData.waitNotifyFee = "0.0";
            }
            if (MyUtils.isEmpty(itemData.warehouseFee)) {
                itemData.warehouseFee = "0.0";
            }
            if (MyUtils.isEmpty(itemData.transferFee)) {
                itemData.transferFee = "0.0";
            }
            zpSDK.zp_draw_text_ex(1.0D, 45.5D, (new StringBuilder("垫付 ")).append(itemData.deboursFee).append("元  " +
                    "等通知费" + " ").append(itemData.waitNotifyFee).append("元 仓管费 ").append(itemData.warehouseFee)
                    .append(" 元").toString(), "宋体", 3D, 0, true, false, false);

            if (itemData.transferFee == null || "null".equals(itemData.transferFee)) {
                itemData.transferFee = "0.0";
            }
            zpSDK.zp_draw_text_ex(1.0D, 49.5D, (new StringBuilder("下送费 ")).append(itemData.transferFee).append("元")
                    .toString(), "宋体", 2.8999999999999999D, 0, true, false, false);

            if (itemData.FeeCount == null || "null".equals(itemData.FeeCount)) {
                itemData.FeeCount = 0.0;
            }
            if (!itemData.paymentTypeCode.equals("1")) {
                if (!itemData.paymentTypeCode.equals("2")) {

                } else {
                    zpSDK.zp_draw_text_ex(18.0D, 49.5D, (new StringBuilder("到付 ")).append(itemData.FeeCount).append
                            ("元").toString(), "宋体", 2.8999999999999999D, 0, true, false, false);
                }
            } else {
                zpSDK.zp_draw_text_ex(18.0D, 49.5D, (new StringBuilder("寄付 ")).append(itemData.FeeCount).append("元")
                        .toString(), "宋体", 2.8999999999999999D, 0, true, false, false);
            }

            zpSDK.zp_draw_text_ex(40D, 49.5D, (new StringBuilder("寄件时间: ")).append(itemData.consignedTm.substring(5,
                    16)).toString(), "宋体", 3D, 0, true, false, false);

            zpSDK.zp_page_print(false);
            zpSDK.zp_page_clear();
            zpSDK.zp_goto_mark_label(120);
            zpSDK.zp_page_free();
        }

        return true;
    }

    /**
     * 派件 单张打印
     *
     * @param itemData       数据
     * @param deliverEmpCode 收件人员
     * @return
     */
    public static boolean printWaybill(InputDataInfo itemData, String deliverEmpCode) {
        DecimalFormat decimalformat = new DecimalFormat("0.00");

        if (!zpSDK.zp_page_create(80D, 61D)) {
            return false;
        }

        zpSDK.TextPosWinStyle = false;
        zpSDK.zp_draw_rect(0.0D, 0.0D, 72D, 50D, 3);
        zpSDK.zp_draw_line(48D, 0.0D, 48D, 10D, 3);
        zpSDK.zp_draw_line(0.0D, 6D, 72D, 6D, 3);
        zpSDK.zp_draw_line(0.0D, 10D, 72D, 10D, 3);
        zpSDK.zp_draw_line(0.0D, 14D, 72D, 14D, 3);
        zpSDK.zp_draw_line(0.0D, 18D, 72D, 18D, 3);
        zpSDK.zp_draw_line(0.0D, 22D, 72D, 22D, 3);
        zpSDK.zp_draw_line(45.0D, 22D, 45D, 34D, 3);
        zpSDK.zp_draw_line(45.0D, 26D, 45D, 30D, 3);
        zpSDK.zp_draw_line(0.0D, 30D, 45D, 30D, 3);
        zpSDK.zp_draw_line(0.0D, 34D, 72D, 34D, 3);
        zpSDK.zp_draw_line(0.0D, 38D, 72D, 38D, 3);
        zpSDK.zp_draw_line(0.0D, 42D, 72D, 42D, 3);
        //zpSDK.zp_draw_line(0.0D, 46D, 72D, 46D, 3);
        String date = new SimpleDateFormat("MM-dd").format(new Date());
        zpSDK.zp_draw_text_ex(1.0D, 5D, date, "宋体", 4D, 0, true, false, false);
        //zpSDK.zp_draw_bitmap(BitmapFactory.decodeResource(getResources(), R.drawable.jxe_icon), 5D, 10D);
        zpSDK.zp_draw_text_ex(49D, 5D, "聚信客服电话", "宋体", 3.5D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(49D, 9.5D, "4008111115", "宋体", 4D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(15D, 5D, (new StringBuilder("运单号: ")).append(itemData.waybillNo).append(" ").toString()
                , "宋体", 3D, 0, true, false, false);
        if (MyUtils.isEmpty(itemData.consignorDistName)) {
            itemData.consignorDistName = "";
        }
        if (!TextUtils.isEmpty(itemData.consignorPhone) && !itemData.consignorPhone.equals("null")) {
            zpSDK.zp_draw_text_ex(1.0D, 9.5D, (new StringBuilder(String.valueOf(itemData.consignorDistName))).append
                    (" ").append(itemData.sourceZoneCode).append(" ").append(itemData.consignorContName).append(" ")
                    .append(itemData.consignorPhone).toString(), "宋体", 3D, 0, true, false, false);
        } else {
            zpSDK.zp_draw_text_ex(1.0D, 9.5D, (new StringBuilder(String.valueOf(itemData.consignorDistName))).append
                    (" ").append(itemData.sourceZoneCode).append(" ").append(itemData.consignorContName).append(" ")
                    .append(itemData.consignorMobile).toString(), "宋体", 3D, 0, true, false, false);
        }
        String addresseeAddrP = "";
        if (!TextUtils.isEmpty(itemData.addresseeAddr) && !itemData.addresseeAddr.equals("null")) {
            if (itemData.addresseeAddr.length() > 14) {
                addresseeAddrP = itemData.addresseeAddr.substring(itemData.addresseeAddr.length() - 14, itemData
                        .addresseeAddr.length());
            } else {
                addresseeAddrP = itemData.addresseeAddr;
            }
        }
        if (!TextUtils.isEmpty(itemData.addresseeMobile) && !itemData.addresseeMobile.equals("null")) {
            zpSDK.zp_draw_text_ex(1.0D, 13.5D, (new StringBuilder(String.valueOf(itemData.addresseeContName))).append
                    (" ").append(itemData.addresseeMobile).append(" ").append(itemData.addresseeAddr).toString(),
                    "宋体", 2.8999999999999999D, 0, true, false, false);
        } else {
            zpSDK.zp_draw_text_ex(1.0D, 13.5D, (new StringBuilder(String.valueOf(itemData.addresseeContName))).append
                    (" ").append(itemData.addresseePhone).append(" ").append(addresseeAddrP).toString(), "宋体", 3D, 0,
                    true, false, false);
        }
		/*if(itemData.deliverCommission!=null){
			itemData.waybillFee = String.valueOf(decimalformat.format(Double.valueOf(itemData.waybillFee) -Double
			.valueOf(itemData.deliverCommission)));
		}*/

        zpSDK.zp_draw_text_ex(1.0D, 17.5D, (new StringBuilder(String.valueOf(itemData.consName))).append(" ").append
                (itemData.quantity).append("件 重量 ").append(itemData.meterageWeightQty).append("kg ").append("元 派件员 ")
                .append(deliverEmpCode).toString(), "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(1.0D, 21.5D, (new StringBuilder("运费 ")).append(itemData.waybillFee).append("元 保价金额 ")
                .append(itemData.insuranceAmount).append("元 保价费 ").append(itemData.insuranceFee).append("元  ")
                .toString(), "宋体", 3D, 0, true, false, false);

        if (MyUtils.isEmpty(itemData.goodsChargeFee)) {
            itemData.goodsChargeFee = "0.0";
        }
        if (MyUtils.isEmpty(itemData.chargeAgentFee)) {
            itemData.chargeAgentFee = "0.0";
        }
        if (MyUtils.isEmpty(itemData.signBackFee)) {
            itemData.signBackFee = "0.0";
        }
        if (MyUtils.isEmpty(itemData.deliverFee)) {
            itemData.deliverFee = "0.0";
        }
		/*if(itemData.deliverFee!=null && itemData.deliverCommission!=null){
			itemData.deliverFee = String.valueOf(decimalformat.format(Double.valueOf(itemData.deliverFee) + Double
			.valueOf(itemData.deliverCommission)));
		}
		if(itemData.deliverFee==null &&itemData.deliverCommission!=null){
			itemData.deliverFee = itemData.deliverCommission;
		}*/

	/*	if (isHenan(itemData.provinceCode)) {

			String bankT = "";
			if("0".equals(itemData.bankType)){
				bankT ="工商银行";
			}else if ("1".equals(itemData.bankType)) {
				bankT = "浦发银行";
			}else if("3".equals(itemData.bankType)){
				bankT = "建设银行";
			}else if("5".equals(itemData.bankType)){
				bankT = "招商银行";
			}else{
				bankT = "无卡号";
			}
			if (!"0".equals(itemData.bankType)) {
				if ("1".equals(itemData.bankType)) {
					bankT = "浦发银行";
				}
				if("3".equals(itemData.bankType)){
					bankT = "建设银行";
				}
				if (!"5".equals(itemData.bankType)) {
					if (!"-1".equals(itemData.bankType) && itemData.bankNo!=null&&itemData.bankNo.length()>0) {
					} else {
						bankT = "无卡号";
					}
				} else {
					bankT = "招商银行";
				}
			} else {
				bankT = "工商银行";
			}

			if (MyUtils.isEmpty(itemData.bankNo)) {
				itemData.bankNo = "";
			}

			zpSDK.zp_draw_text_ex(1.0D, 25D, (new StringBuilder("卡号类型： ")).append(bankT).toString(), "宋体", 3D, 0,
			true, false, false);
			zpSDK.zp_draw_text_ex(1.0D, 29D, (new StringBuilder("银行账号 ")).append(itemData.bankNo).toString(), "宋体",
			3D, 0, true, false, false);

			double d = Double.parseDouble(itemData.goodsChargeFee) - Double.parseDouble(itemData.chargeAgentFee);
			zpSDK.zp_draw_text_ex(1.0D, 33.5D, (new StringBuilder("应转代收 ")).append(d)
					.append("元 代收服务费 ").append(itemData.chargeAgentFee).append("元  ").append("  派送费 ").append(itemData
					.deliverFee==null?"0.0":itemData.deliverFee).append("元  ").toString(), "宋体", 3D, 0, true, false,
					false);
		}else {*/
        //zpSDK.zp_draw_text_ex(1.0D, 28.5D, "一票送货到店 铺", "宋体", 6D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(1.0D, 25.5D, (new StringBuilder("代收金额  ")).append(itemData.goodsChargeFee).toString(),
                "宋体", 3D, 0, true, false, false);
        //	}

	/*		if (itemData.FeeCount == null || "null".equals(itemData.FeeCount)) {
				itemData.FeeCount = 0.0;
			}
			if (!itemData.paymentTypeCode.equals("1")) {
				if (!itemData.paymentTypeCode.equals("2")) {

				} else {
					zpSDK.zp_draw_text_ex(25.0D, 25.5D, (new StringBuilder("到付 ")).append(itemData.FeeCount).append
					("元")
							.toString(), "宋体", 3.2D, 0, true, false, false);
				}
			} else {
				zpSDK.zp_draw_text_ex(25.0D, 25.5D, (new StringBuilder("寄付 ")).append(itemData.FeeCount).append("元")
						.toString(), "宋体", 3.2D, 0, true, false, false);
			}*/


        if (MyUtils.isEmpty(itemData.signBackFee)) {
            itemData.signBackFee = "0.0";
        }
        if (itemData.fuelServiceFee == null) {
            itemData.fuelServiceFee = 0.0;
        }
        zpSDK.zp_draw_text_ex(1.0D, 29.5D, (new StringBuilder("签回单费 ")).append(itemData.signBackFee).append("  原返费 ")
                .append(itemData.backCargoFee == null ? "0.0" : itemData.backCargoFee + "元").toString(), "宋体", 3D, 0,
                true, false, false);
        if (MyUtils.isEmpty(itemData.signBackNo)) {
            itemData.signBackNo = "";
        }
        zpSDK.zp_draw_text_ex(1.0D, 33.5D, (new StringBuilder("回单号 ")).append(itemData.signBackNo).append("  收条   " +
                "身份证" + "   盖章").toString(), "宋体", 3D, 0, true, false, false);
        if (MyUtils.isEmpty(itemData.deboursFee)) {
            itemData.deboursFee = "0.0";
        }
        if (MyUtils.isEmpty(itemData.waitNotifyFee)) {
            itemData.waitNotifyFee = "0.0";
        }
        if (MyUtils.isEmpty(itemData.warehouseFee)) {
            itemData.warehouseFee = "0.0";
        }
        if (MyUtils.isEmpty(itemData.transferFee)) {
            itemData.transferFee = "0.0";
        }
        zpSDK.zp_draw_text_ex(1.0D, 37.5D, (new StringBuilder("垫付 ")).append(itemData.deboursFee).append("元  等通知费 ")
                .append(itemData.waitNotifyFee).append("元 仓管费 ").append(itemData.warehouseFee).append(" 元").toString
                        (), "宋体", 3D, 0, true, false, false);

        if (itemData.transferFee == null || "null".equals(itemData.transferFee)) {
            itemData.transferFee = "0.0";
        }
        zpSDK.zp_draw_text_ex(1.0D, 41.5D, (new StringBuilder("下送费 ")).append(itemData.transferFee).append("元")
                .toString(), "宋体", 2.8999999999999999D, 0, true, false, false);


        zpSDK.zp_draw_text_ex(18.0D, 41.5D, (new StringBuilder("取件费   ")).append(itemData.fuelServiceFee + "元")
                .toString(), "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(40D, 41.5D, (new StringBuilder("寄件时间: ")).append(itemData.consignedTm.substring(5, 16))
                .toString(), "宋体", 3D, 0, true, false, false);

        if (itemData.FeeCount == null || "null".equals(itemData.FeeCount)) {
            itemData.FeeCount = 0.0;
        }
        if (!itemData.paymentTypeCode.equals("1")) {
            if (!itemData.paymentTypeCode.equals("2")) {

            } else {
                zpSDK.zp_draw_text_ex(1.0D, 47.5D, (new StringBuilder("到付: ")).append(itemData.FeeCount).append("元")
                        .toString(), "宋体", 5D, 0, true, false, false);
            }
        } else {
            zpSDK.zp_draw_text_ex(1.0D, 47.5D, (new StringBuilder("寄付:")).append(itemData.FeeCount).append("元")
                    .toString(), "宋体", 5D, 0, true, false, false);
        }
        zpSDK.zp_draw_text_ex(35.0D, 47.5D, (new StringBuilder("派件费:")).append(itemData.downDeliverFee).toString(),
                "宋体", 5D, 0, true, false, false);

        if (itemData.downDeliverFee == null || "".equals(itemData.downDeliverFee)) {
            itemData.downDeliverFee = "0.0";
        }
        //	zpSDK.zp_draw_text_ex(30.0D, 47.5D, (new StringBuilder("合计:")).append(Double.valueOf(itemData
        // .downDeliverFee)+itemData.FeeCount).toString(), "宋体", 5D, 0, true, false, false);

        zpSDK.zp_page_print(false);
        zpSDK.zp_page_clear();
        zpSDK.zp_goto_mark_label(120);
        zpSDK.zp_page_free();

        return true;
    }

    /**
     * @param pdaGoodsChargePaymentList 数据
     * @param registerName              登记人
     * @param registerPhone             登记电话
     * @param receiveAccount            银行卡号
     * @return
     */
    public static boolean printGoodsChargeFeeEnrol(PdaGoodsChargePaymentList pdaGoodsChargePaymentList, String
            registerName, String registerPhone, String receiveAccount) {
        if (!zpSDK.zp_page_create(80D, 61D)) {
            return false;
        }
        zpSDK.TextPosWinStyle = false;
        zpSDK.zp_draw_rect(0.0D, 0.0D, 72D, 53D, 3);
        zpSDK.zp_draw_line(0.0D, 8D, 72D, 8D, 3);
        zpSDK.zp_draw_line(0.0D, 12D, 72D, 12D, 3);
        zpSDK.zp_draw_line(0.0D, 16D, 72D, 16D, 3);
        zpSDK.zp_draw_line(0.0D, 20D, 72D, 20D, 3);
        zpSDK.zp_draw_line(0.0D, 24D, 72D, 24D, 3);
        zpSDK.zp_draw_line(0.0D, 28D, 72D, 28D, 3);
        zpSDK.zp_draw_line(0.0D, 32D, 72D, 32D, 3);
        zpSDK.zp_draw_line(0.0D, 36D, 72D, 36D, 3);
        zpSDK.zp_draw_line(0.0D, 40D, 72D, 40D, 3);
        zpSDK.zp_draw_line(0.0D, 44D, 72D, 44D, 3);
        zpSDK.zp_draw_line(0.0D, 48D, 72D, 48D, 3);
        zpSDK.zp_draw_line(18D, 8D, 18D, 12D, 3);
        zpSDK.zp_draw_line(15D, 12D, 15D, 20D, 3);
        zpSDK.zp_draw_line(18D, 20D, 18D, 32D, 3);
        zpSDK.zp_draw_line(33D, 8D, 33D, 16D, 3);
        zpSDK.zp_draw_line(29.5D, 16D, 29.5D, 28D, 3);
        // zpSDK.zp_draw_line(33D, 20D, 33D, 28D, 3);

        zpSDK.zp_draw_line(46D, 8D, 46D, 16D, 3);
        zpSDK.zp_draw_line(36D, 16D, 36D, 20D, 3);
        zpSDK.zp_draw_line(40D, 20D, 40D, 28D, 3);
        zpSDK.zp_draw_line(53D, 20D, 53D, 28D, 3);

        zpSDK.zp_draw_text_ex(2.0D, 7D, "聚信代收款转账登记清单", "宋体", 6.7999999999999998D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(2.0D, 11D, "收款人姓名", "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(2.0D, 15D, "固定电话", "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(2.0D, 19D, "卡号类型", "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(2.0D, 23D, "运单号", "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(1.0D, 27D, pdaGoodsChargePaymentList.waybillNo == null ? "" : pdaGoodsChargePaymentList
                .waybillNo, "宋体", 2.5D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(2.0D, 31D, "转款周期", "宋体", 3D, 0, true, false, false);

        String bankT = "";
        if (null != pdaGoodsChargePaymentList.bankAccountType) {
            if ("0".equals(pdaGoodsChargePaymentList.bankAccountType)) {
                bankT = "工商银行";
            }
            if ("1".equals(pdaGoodsChargePaymentList.bankAccountType)) {
                bankT = "浦发银行";
            }
            if ("3".equals(pdaGoodsChargePaymentList.bankAccountType)) {
                bankT = "建设银行";
            }
            if ("5".equals(pdaGoodsChargePaymentList.bankAccountType)) {
                bankT = "招商银行";
            }
            if ("-1".equals(pdaGoodsChargePaymentList.bankAccountType)) {
                bankT = "无卡号";
            }
        } else {
            bankT = "";
        }
        String date = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(new Date());

        zpSDK.zp_draw_text_ex(19.0D, 11D, registerName == null ? "" : registerName, "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(15.5D, 15D, pdaGoodsChargePaymentList.consignorPhone == null ? "" :
                pdaGoodsChargePaymentList.consignorPhone, "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(15.5D, 19D, bankT, "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(19.0D, 23D, "代收款", "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(19.0D, 27D, pdaGoodsChargePaymentList.feeAmount == null ? "0.0" :
                pdaGoodsChargePaymentList.feeAmount, "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(24.0D, 32D, pdaGoodsChargePaymentList.paymentPeriod == null ? "15" :
                pdaGoodsChargePaymentList.paymentPeriod + "天", "宋体", 3D, 0, true, false, false);

        zpSDK.zp_draw_text_ex(33.2D, 11D, "登记日期", "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(33.2D, 15D, "手机", "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(30D, 19D, "卡号", "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(30.0D, 23D, "服务费", "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(30.0D, 26.5D, pdaGoodsChargePaymentList.serviceFee == null ? "0.0" :
                pdaGoodsChargePaymentList.serviceFee, "宋体", 3D, 0, true, false, false);

        zpSDK.zp_draw_text_ex(46.0D, 11D, date, "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(46.0D, 15D, registerPhone == null ? "" : registerPhone, "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(36.5D, 19D, receiveAccount == null ? "" : receiveAccount, "宋体", 3D, 0, true, false,
                false);
        String count = "0.0";
        double d1 = 0.0;
        double d2 = 0.0;
        if (pdaGoodsChargePaymentList.feeAmount != null && pdaGoodsChargePaymentList.serviceFee != null) {
            d1 = Double.valueOf(pdaGoodsChargePaymentList.feeAmount);
            d2 = Double.valueOf(pdaGoodsChargePaymentList.serviceFee);
            count = String.valueOf(d1 - d2);
        } else {
            d1 = Double.valueOf(pdaGoodsChargePaymentList.feeAmount);
            count = String.valueOf(d1);
        }
        zpSDK.zp_draw_text_ex(40.5D, 23D, "实转金额", "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(40.5D, 27D, count, "宋体", 3D, 0, true, false, false);

        zpSDK.zp_draw_text_ex(53.5D, 23D, "寄件日期", "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(53.5D, 27D, pdaGoodsChargePaymentList.consignedTm == null ? "" :
                pdaGoodsChargePaymentList.consignedTm.substring(0, 10), "宋体", 3D, 0, true, false, false);

        zpSDK.zp_draw_text_box(1.0D, 35.5D, 72D, 3D, "温馨提示 1:为了您的资金安全，登记转款;请出示持卡人身份证原件.2:为了您方便," +
                "寄件时请您务必选择自动转款.3:自登记审核之日起72小时内转款，请查收.", "宋体", 2.7D, 0, true, false, false);

        zpSDK.zp_draw_text_ex(36.5D, 48D, "客服电话:", "宋体", 3.2000000000000002D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(52.5D, 48D, "4008-111115", "宋体", 3.2000000000000002D, 0, true, false, false);

        zpSDK.zp_page_print(false);
        zpSDK.zp_page_clear();

        zpSDK.zp_goto_mark_label(120);
        zpSDK.zp_page_free();
        return true;
    }

    private static Bitmap scaleSignBitmap(Bitmap src) {
        int width = src.getWidth();// 原来尺寸大小
        int height = src.getHeight();
        final float widthSize = 200;// 缩放到这个大小,你想放大多少就多少
        final float destSize = 100;// 缩放到这个大小,你想放大多少就多少

        // 图片缩放比例，新尺寸/原来的尺寸
        float scaleWidth = ((float) widthSize) / width;
        float scaleHeight = ((float) destSize) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // 返回缩放后的图片
        return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
    }
}
