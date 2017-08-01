package com.eruitong.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.eruitong.eruitong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyl on 2017/8/1.
 */
public class WiFiUtil {
    private final static String TAG = "WifiUtil";
    public static final int IS_OPENING = 1, IS_CLOSING = 2, IS_OPENED = 3, IS_CLOSED = 4;

    private StringBuffer mStringBuffer = new StringBuffer();
    private List<ScanResult> scanResultList;
    private ScanResult mScanResult;
    // 定义WifiManager对象
    private WifiManager mWifiManager;
    // 定义WifiInfo对象
    private WifiInfo mWifiInfo;
    // 网络连接列表
    private List<WifiConfiguration> mWifiConfigList;
    // 定义一个WifiLock
    WifiManager.WifiLock mWifiLock;


    /**
     * 构造方法
     */
    public WiFiUtil(Context context) {
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
        mWifiConfigList = mWifiManager.getConfiguredNetworks();
    }

    /**
     * 打开Wifi网卡，能打开就返回true，无法打开返回false
     */
    public boolean openNetCard() {
        if (!mWifiManager.isWifiEnabled()) {
            return mWifiManager.setWifiEnabled(true);
        } else {
            return false;
        }
    }

    /**
     * 关闭Wifi网卡，能关闭返回true，不能关就返回false
     */
    public boolean closeNetCard() {
        if (mWifiManager.isWifiEnabled()) {
            return mWifiManager.setWifiEnabled(false);
        } else {
            return false;
        }
    }

    /**
     * 检查当前Wifi网卡状态，返回四种状态，如果出错返回-1
     */
    public int getWifitate() {
        int result = -1;
        switch (mWifiManager.getWifiState()) {
            case 0:
                Log.i(TAG, "网卡正在关闭");
                result = IS_CLOSING;
                break;
            case 1:
                Log.i(TAG, "网卡已经关闭");
                result = IS_CLOSED;
                break;
            case 2:
                Log.i(TAG, "网卡正在打开");
                result = IS_OPENING;
                break;
            case 3:
                Log.i(TAG, "网卡已经打开");
                result = IS_OPENED;
                break;
            default:
                Log.i(TAG, "---_---晕......没有获取到状态---_---");
                result = -1;
                break;
        }
        return result;
    }

    /**
     * 扫描周边网络，判断周边是否有wifi，有就返回true，没有就返回false
     */
    public boolean scan() {
        if (getWifitate() == IS_OPENED) {
            //开始扫描
            mWifiManager.startScan();
            //将扫描结果存入数据列中
            scanResultList = mWifiManager.getScanResults();
            if (scanResultList != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 得到附近wifi的扫描结果，是ScanResult对象
     * 得到的是附近网络的结果集，没有就返回null
     */
    public ArrayList<ScanResult> getScanResult() {
        // 每次点击扫描之前清空上一次的扫描结果
        if (mStringBuffer != null) {
            mStringBuffer = new StringBuffer();
        }

        scan();// 开始扫描网络
        ArrayList<ScanResult> scanResultsList = new ArrayList<ScanResult>();
        if (scanResultList != null) {
            for (int i = 0; i < scanResultList.size(); i++) {
                mScanResult = scanResultList.get(i);
                scanResultsList.add(mScanResult);

                /*mStringBuffer = mStringBuffer.append("NO.").append(i)
                        .append(" :")
                        .append(mScanResult.SSID).append("->")
                        .append(mScanResult.BSSID).append("->")
                        .append(mScanResult.capabilities).append("->")
                        .append(mScanResult.frequency).append("->")
                        .append(mScanResult.level).append("->")
                        .append(mScanResult.describeContents()).append("\n\n");*/
            }
            //Log.i(TAG, mStringBuffer.toString());
            return scanResultsList;
        } else {
            return null;
        }
    }

    /**
     * 判断指定的网络是否能被扫描到
     *
     * @param wifi_SSID
     * @return 如果能够在周边发现指定的网络就返回true，否则返回false
     */
    public boolean canScannable(String wifi_SSID) {
        boolean canScannable = false;
        scan();//开始扫描周边网络
        //得到扫描到的wifi列表
        if (scanResultList != null) {
            for (int i = 0; i < scanResultList.size(); i++) {
                System.out.println("scanResultList " + i + "----->" + scanResultList.get(i).SSID);
                if (scanResultList.get(i).SSID.equals(wifi_SSID)) {
                    canScannable = true;//如果想要链接的wifi能够扫描到，那么就说明能够链接
                    break;
                }
            }
        }
        return canScannable;
    }

    /**
     * 得到指定网络的index（从0开始计数），找不到就返回-1
     */
    public int getTagWifiId(String netName) {
        // 开始扫描网络
        scan();
        scanResultList = mWifiManager.getScanResults();
        if (scanResultList != null) {
            for (int i = 0; i < scanResultList.size(); i++) {
                mScanResult = scanResultList.get(i);
                if (mScanResult.SSID.equals(netName)) {
                    return i;
                }
                String show = "No = " + i + " SSID = " + mScanResult.SSID + " capabilities = " + mScanResult
                        .capabilities + " level = " + mScanResult.level;
                Log.i(TAG, show);
            }
        }
        return -1;
    }

    /**
     * 断开当前连接的网络
     */
    public void disconnectWifi() {
        int netId = getCurrentNetworkId();
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
        mWifiInfo = null;
    }

    /**
     * 检查当前网络状态
     * 如果有wifi链接，返回true，如果没有就返回false
     */
    public boolean getWifiConnectState() {
        return mWifiInfo != null ? true : false;
    }

    /**
     * @return 当前网络的名字，如果没有就返回null，否则返回string
     */
    public String getCurrentSSID() {
        return (mWifiInfo == null) ? null : mWifiInfo.getSSID();
    }

    /**
     * 得到连接的ID，如果没有就返回0，否则返回正确的id
     */
    public int getCurrentNetworkId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    /**
     * 得到IP地址，出错时返回0
     */
    public int getCurrentIPAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    /**
     * 得到MAC地址
     *
     * @return 出錯了返回null
     */
    public String getCurrentMacAddress() {
        return (mWifiInfo == null) ? null : mWifiInfo.getMacAddress();
    }

    /**
     * 得到接入点的BSSID
     *
     * @return 出錯返回null
     */
    public String getCurrentBSSID() {
        return (mWifiInfo == null) ? null : mWifiInfo.getBSSID();
    }

    /**
     * 得到WifiInfo的所有信息包
     *
     * @return 出错了返回null
     */
    public WifiInfo getCurrentWifiInfo() {
        return (mWifiInfo == null) ? null : mWifiInfo;
    }

    // 锁定WifiLock
    public void acquireWifiLock() {
        mWifiLock.acquire();
    }

    // 解锁WifiLock
    public void releaseWifiLock() {
        // 判断时候锁定
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }

    // 创建一个WifiLock
    public void creatWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("Test");
    }

    // 得到配置好的网络
    public List<WifiConfiguration> getConfiguration() {
        return mWifiConfigList;
    }

    /**
     *
     * @return 没有连接到返回false，正在连接则返回true
     */
    public boolean connectConfiguratedWifi(String wifi_SSID) {
        //如果当前网络不是想要链接的网络，要连接的网络是配置过的，并且要连接的网络能够被扫描到
        if (getCurrentSSID().indexOf(wifi_SSID) == -1) {
            if (getWifiConfigurated(wifi_SSID) != -1 && canScannable(wifi_SSID)) {
                mWifiManager.enableNetwork(getWifiConfigurated(wifi_SSID), true);
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断要连接的wifi名是否已经配置过了
     *
     * @return 返回要连接的wifi的ID，如果找不到就返回-1
     */
    public int getWifiConfigurated(String wifi_SSID) {
        int id = -1;
        if (mWifiConfigList != null) {
            for (int j = 0; j < mWifiConfigList.size(); j++) {
                if (mWifiConfigList.get(j).SSID.equals("\"" + wifi_SSID + "\"")) {
                    //如果要连接的wifi在已经配置好的列表中，那就设置允许链接，并且得到id
                    id = mWifiConfigList.get(j).networkId;
                    break;
                }
            }
        }
        return id;
    }

    public static int getWiFiLevel(Context context) {
        return Math.abs(((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getRssi());
    }

    public static Message wifiLevel(int i) {
        if (i <= 0 && i >= -50) {
            Message message = new Message();
            message.what = 1;
            return message;
        }
        if (i < -50 && i >= -70) {
            Message message1 = new Message();
            message1.what = 2;
            return message1;
        }
        if (i < -70 && i >= -80) {
            Message message2 = new Message();
            message2.what = 3;
            return message2;
        }
        if (i < -80 && i >= -100) {
            Message message3 = new Message();
            message3.what = 4;
            return message3;
        } else {
            Message message4 = new Message();
            message4.what = 5;
            return message4;
        }
    }

    public static ImageView wifiSignal(ImageView imageview, int i) {
        switch (i) {
            default:
                imageview.setImageResource(R.drawable.single0);
                return imageview;

            case 1: // '\001'
                imageview.setImageResource(R.drawable.single1);
                return imageview;

            case 2: // '\002'
                imageview.setImageResource(R.drawable.single2);
                return imageview;

            case 3: // '\003'
                imageview.setImageResource(R.drawable.single3);
                return imageview;

            case 4: // '\004'
                imageview.setImageResource(R.drawable.single3);
                return imageview;

            case 5: // '\005'
                imageview.setImageResource(R.drawable.single4);
                break;
        }
        return imageview;
    }
}
