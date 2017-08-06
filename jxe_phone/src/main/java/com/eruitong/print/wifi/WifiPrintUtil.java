package com.eruitong.print.wifi;

import android.content.Context;
import android.widget.Toast;

import com.eruitong.eruitong.R;
import com.postek.cdf.CDFPTKAndroid;

/**
 * Created by lyl on 2017/7/28.
 */

public class WifiPrintUtil {

    public static CDFPTKAndroid cdf = null;
    public static int ConnectOrClose = 0;

    /**
     * 链接WIFI
     */
    public static void connectWiFi(String ip, int port) {
        cdf.PTK_ConnectWiFi(ip, port);
    }

    public static void closeConnectWiFi() {
        cdf.PTK_CloseConnectWiFi();
    }

    /**
     * 打印自检页
     */
    public static void printConfiguration(Context context) {
        if (ConnectOrClose == 0) {
            Toast.makeText(context, "请先连接WiFi！", Toast.LENGTH_SHORT).show();
        } else {
            cdf.PTK_PrintConfiguration();
        }
    }

    /**
     * 打印机走纸
     */
    public static void feedMedia(Context context) {
        if (ConnectOrClose == 0) {
            Toast.makeText(context, R.string.connect_wifi, Toast.LENGTH_SHORT).show();
        } else {
            cdf.PTK_FeedMedia();
        }
    }

    /**
     * 校准纸张探测器
     */
    public static void mediaDetect(Context context) {
        if (ConnectOrClose == 0) {
            Toast.makeText(context, R.string.connect_wifi, Toast.LENGTH_SHORT).show();
        } else {
            cdf.PTK_MediaDetect();
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

}
