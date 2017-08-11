package com.eruitong.print.wifi;

import android.content.Context;
import android.widget.Toast;

import com.eruitong.eruitong.R;
import com.eruitong.print.XY;
import com.postek.cdf.CDFPTKAndroid;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by lyl on 2017/7/28.
 */

public class WifiPrintUtil {

    public static CDFPTKAndroid cdf = null;
    public static int ConnectOrClose = 0;

    /**
     * 连接WIFI
     */
    public static void connectWiFi(String ip, int port) {
        cdf.PTK_ConnectWiFi(ip, port);
    }

    /**
     * 关闭Wifi
     */
    public static void closeConnectWiFi() {
        cdf.PTK_CloseConnectWiFi();
        WifiPrintUtil.ConnectOrClose = 0;
    }

    /**
     * Wifi 是否连接
     */
    public static boolean isConnect() {
        return ConnectOrClose == 1;
    }

    /**
     * 打印自检页
     */
    public static void printConfiguration(Context context) {
        if (ConnectOrClose == 0) {
            Toast.makeText(context.getApplicationContext(), R.string.connect_wifi, Toast.LENGTH_SHORT).show();
        } else {
            cdf.PTK_PrintConfiguration();
        }
    }

    /**
     * 打印机走纸
     */
    public static void feedMedia(Context context) {
        if (ConnectOrClose == 0) {
            Toast.makeText(context.getApplicationContext(), R.string.connect_wifi, Toast.LENGTH_SHORT).show();
        } else {
            cdf.PTK_FeedMedia();
        }
    }

    /**
     * 校准纸张探测器
     */
    public static void mediaDetect(Context context) {
        if (ConnectOrClose == 0) {
            Toast.makeText(context.getApplicationContext(), R.string.connect_wifi, Toast.LENGTH_SHORT).show();
        } else {
            cdf.PTK_MediaDetect();
        }
    }

    /**
     * RFID校准
     */
    public static void RFIDCalibration(Context context) {
        if (ConnectOrClose == 0) {
            Toast.makeText(context.getApplicationContext(), R.string.connect_wifi, Toast.LENGTH_SHORT).show();
        } else {
            int nReturn = cdf.PTK_RFIDCalibration();
            if (nReturn == 0) {
                Toast.makeText(context.getApplicationContext(), R.string.calibration_success, Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(context.getApplicationContext(), R.string.calibration_fail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 初始化打印设置
     */
    public static void initPrint() {
        cdf.PTK_ClearBuffer();// 清除打印机缓冲内存的内容
        cdf.PTK_SetPrintSpeed(4);// 设置打印速度
        cdf.PTK_SetDarkness(10);// 设置打印温度
        cdf.PTK_SetDirection("T");// 设置打印方向
        cdf.PTK_SetLabelWidth(600);// 设置标签的宽度
        cdf.PTK_SetLabelHight(400, 2, 0, false);// 设置标签的高度，和定位间隙\黑线\穿孔的高度。
    }

    /**
     * 打印 直线
     *
     * @param fromX x轴起点
     * @param fromY y轴起点
     * @param toX   x轴终点
     * @param toY   y轴终点
     * @param width 线条粗细
     */
    public static void line(int fromX, int fromY, int toX, int toY, int width) {
        cdf.PTK_DrawDiagonal(fromX, fromY, 3, toX, toY);
    }

    /**
     * 画矩形
     *
     * @param fromX x轴起点
     * @param fromY y轴起点
     * @param toX   x轴终点
     * @param toY   y轴终点
     * @param width 线条粗细
     */
    public static void rect(int fromX, int fromY, int toX, int toY, int width) {
        cdf.PTK_DrawRectangle(fromX, fromY, 3, toX, toY);
    }


    /**
     * 打印文字
     *
     * @param x   X 坐标
     * @param y   Y 坐标
     * @param str 一个长度为 1-100 的字符串
     */
    public static void text(int x, int y, String str) {
        text(x, y, 0, '6', 2, 2, 'N', str);
    }

    /**
     * 打印文字
     *
     * @param x    X 坐标
     * @param y    Y 坐标
     * @param zoom 放大系数，系数范围:1～24。
     * @param str  一个长度为 1-100 的字符串
     */
    public static void text(int x, int y, int zoom, String str) {
        text(x, y, 0, '6', zoom, zoom, 'N', str);
    }

    /**
     * 打印文字
     *
     * @param x      X 坐标
     * @param y      Y 坐标
     * @param zoom   放大系数，系数范围:1～24。
     * @param rotate 0 - 不旋转;1 - 旋转 90°;2 - 旋转 180°;3 - 旋转 270°。
     * @param str    一个长度为 1-100 的字符串
     */
    public static void text(int x, int y, int zoom, int rotate, String str) {
        text(x, y, rotate, '6', zoom, zoom, 'N', str);
    }

    /**
     * 打印文字
     *
     * @param x      X 坐标
     * @param y      Y 坐标
     * @param rotate 0 - 不旋转;1 - 旋转 90°;2 - 旋转 180°;3 - 旋转 270°。
     * @param font   1～5: 打印机内置 5 种西文字体；6：打印机内置 1 种中文字体；‘A’～‘Z’: 为下载的软字体
     * @param zoomH  当 pFont 设置为内置字体时（1～6），设置点阵的水平放大的系数， 系数范围:1～24。
     * @param zoomV  当 pFont 设置为内置字体时（1～6），设置点阵的垂直放大的系数， 系数范围:1～24。
     * @param ptext  ‘N’打印正常文本(如黑字白底文本)；‘R’打印文本反色文本(如白字黑底文本)。
     * @param str    一个长度为 1-100 的字符串
     */
    public static void text(int x, int y, int rotate, char font, int zoomH, int zoomV, char ptext, String str) {
        int nReturn = cdf.PTK_DrawText(XY.W(x), XY.W(y), rotate, font, zoomH, zoomV, ptext, str);
        if (nReturn != 0) {
            Exception exception = new Exception("WIFI打印出错。错误的内容为：" + str);
            CrashReport.postCatchedException(exception);
        }
    }


    /**
     * 打印一个条码
     *
     * @param x      X 坐标
     * @param y      Y 坐标
     * @param height 设置条码高度,
     * @param str    一个长度为 1-100 的字符串
     */
    public static void barcode(int x, int y, int height, String str) {
        barcode(x, y, 0, "1", 2, 2, height, 'N', str);
    }

    /**
     * 打印一个条码
     *
     * @param x      X 坐标
     * @param y      Y 坐标
     * @param rotate 0～不旋转;1～旋转 90°; 2～旋转 180°; 3～旋转 270°
     * @param pCode  1  Code 128 AUTO
     * @param sWidth 设置条码中窄单元的宽度
     * @param lWidth 设置条码中宽单元的宽度
     * @param height 设置条码高度,
     * @param ptext  ‘N’不显示条码下方的可读文字；‘B’显示条码下方的可读文字
     * @param str    一个长度为 1-100 的字符串
     */
    public static void barcode(int x, int y, int rotate, String pCode, int sWidth, int lWidth, int height, char
            ptext, String str) {
        int nReturn = cdf.PTK_DrawBarcode(XY.W(x), XY.W(y), rotate, pCode, sWidth, lWidth, height, ptext, str);
        if (nReturn != 0) {
            Exception exception = new Exception("WIFI在打印条码时出错。错误的内容为：" + str);
            CrashReport.postCatchedException(exception);
        }
    }
}
