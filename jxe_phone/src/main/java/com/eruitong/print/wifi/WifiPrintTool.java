package com.eruitong.print.wifi;

import com.eruitong.model.InputDataInfo;
import com.eruitong.utils.MyUtils;
import com.postek.cdf.CDFPTKAndroid;

/**
 * Created by Administrator on 2017/8/10.
 */

public class WifiPrintTool {

    private static CDFPTKAndroid cdf = null;

    static {
        cdf = WifiPrintUtil.cdf;
    }

    public static boolean kehuLian(InputDataInfo info) {
        if (!WifiPrintUtil.isConnect()) {
            return false;
        }

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


        // 初始化打印设置
        WifiPrintUtil.initPrint();

        WifiPrintUtil.rect(0, 0, 72, 53, 3);
        WifiPrintUtil.line(0, 4, 72, 4, 3);
        WifiPrintUtil.line(0, 8, 72, 8, 3);
        WifiPrintUtil.line(0, 12, 72, 12, 3);
        WifiPrintUtil.line(0, 16, 72, 16, 3);
        WifiPrintUtil.line(0, 20, 72, 20, 3);
        WifiPrintUtil.line(0, 24, 72, 24, 3);
        WifiPrintUtil.line(0, 28, 72, 28, 3);
        WifiPrintUtil.line(0, 32, 72, 32, 3);
        WifiPrintUtil.line(0, 36, 72, 36, 3);
        WifiPrintUtil.line(0, 40, 72, 40, 3);
        WifiPrintUtil.line(0, 45, 72, 45, 3);
        WifiPrintUtil.line(12, 0, 12, 8, 3);

        WifiPrintUtil.text(1, 3, info.consignedTm.substring(5, 10));
        WifiPrintUtil.text(1, 7, info.consignedTm.substring(11, 16));

        String type;
        if (info.productTypeCode.equals("B1")) {
            type = "快递";
        } else if (info.productTypeCode.equals("B2")) {
            type = "普货";
        } else if (info.productTypeCode.equals("B3")) {
            type = "普快";
        } else {
            type = "普货";
        }
        WifiPrintUtil.text(13, 3, joinStr(type, "  运单号", info.waybillNo, " ", info.sourceZoneCode, " ", info
                .consignorContName));


        String phone;
        if (!MyUtils.isEmpty(info.addresseeMobile)) {
            if (!MyUtils.isEmpty(info.addresseePhone)) {
                phone = info.addresseeMobile;
            } else {
                phone = info.addresseeMobile;
            }
        } else {
            phone = info.addresseePhone;
        }
        WifiPrintUtil.text(13, 7, joinStr(info.addresseeDistName, " ", info.addresseeContName, " ", phone, " 取件费 ",
                String.valueOf(fuelServiceFee)));

        WifiPrintUtil.text(1, 11, joinStr(info.consName, " ", String.valueOf(info.quantity), "件 重量", info
                        .meterageWeightQty, "kg 运费", info.waybillFee, "  垫付", info.deboursFee)
                //," 派送费",String.valueOf(info.deliverFee)
        );

        WifiPrintUtil.text(1, 15, joinStr("声明价值", info.insuranceAmount, " 保价费", info.insuranceFee, "元  回单费", info
                .signBackFee, " ", info.signBackNo));


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

        WifiPrintUtil.text(1, 19, joinStr(s, info.goodsChargeFee, " 服务费", info.chargeAgentFee, " ", bankT, " 等通知费",
                info.waitNotifyFee));
        WifiPrintUtil.text(1, 23, joinStr("银行账号 ", info.bankNo));
        WifiPrintUtil.text(1, 27, joinStr("客户须知 " +
                "1：货物应当申明价值，每件货物遗失或损毁，承运人按总保价的平均价值赔偿。2：每票总价值如果超过5万元，不予承运。3：出现异常，90天内联系处理，超期不予处理"));

        if (!info.paymentTypeCode.equals("1")) {
            if (info.paymentTypeCode.equals("2")) {
                WifiPrintUtil.text(1, 39, joinStr("到付  ", String.valueOf(double1), "元"));
            }
        } else {
            WifiPrintUtil.text(1, 40, joinStr("寄付  ", String.valueOf(d), "元"));
        }
        WifiPrintUtil.text(1, 44, joinStr("备注:", info.waybillRemk));
        WifiPrintUtil.text(45, 44, joinStr("签字"));
        WifiPrintUtil.text(40, 40, joinStr("客服热线:"));
        WifiPrintUtil.text(53, 40, joinStr("4008-111115"));

        WifiPrintUtil.text(1, 51, 4, joinStr("派送费 ", String.valueOf(info.deliverFee)));

        // 打印数量，复制份数
        cdf.PTK_PrintLabel(1, 1);

        return true;
    }


    public static String joinStr(String... args) {
        StringBuilder builder = new StringBuilder();
        for (String s : args) {
            builder.append(s);
        }
        return builder.toString();
    }
}
