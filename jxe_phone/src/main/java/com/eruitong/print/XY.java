package com.eruitong.print;

/**
 * Created by lyl on 2017/8/6.
 */
public class XY {

    /**
     * 将蓝牙所用的尺寸，转换成WIFI所用的尺寸
     *
     * @param b 蓝牙所用的尺寸
     * @return
     */
    public static int W(double b) {
        return (int) Math.round(b / 0.125 * 0.9);
    }
}
