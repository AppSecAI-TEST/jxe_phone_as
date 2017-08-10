package com.eruitong.activity.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eruitong.activity.BaseActivity;
import com.eruitong.eruitong.R;
import com.eruitong.print.wifi.WifiPrintUtil;
import com.postek.cdf.CDFPTKAndroid;
import com.postek.cdf.CDFPTKAndroidImpl;

/**
 * Created by lyl on 2017/8/7.
 */

public class SetWifiActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 10000;
    private static final int RESULT_CODE = 11111;
    public static CDFPTKAndroid cdf = null;
    private long mExitTime;

    private Button btnConnectAndClose = null;

    private EditText et_ip1 = null;
    private EditText et_ip2 = null;
    private EditText et_ip3 = null;
    private EditText et_ip4 = null;
    private EditText et_port = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setwifi);

        initView();

        WifiPrintUtil.cdf = new CDFPTKAndroidImpl(this, handler);
    }

    private void initView() {
        btnConnectAndClose = (Button) findViewById(R.id.btn_wifi_connectandclose);
        Button btn_U = (Button) findViewById(R.id.btn_wifi_U);
        Button btn_FM = (Button) findViewById(R.id.btn_wifi_FM);
        Button btn_MD = (Button) findViewById(R.id.btn_wifi_MD);

        et_ip1 = (EditText) findViewById(R.id.et_wifi_ip1);
        et_ip2 = (EditText) findViewById(R.id.et_wifi_ip2);
        et_ip3 = (EditText) findViewById(R.id.et_wifi_ip3);
        et_ip4 = (EditText) findViewById(R.id.et_wifi_ip4);
        et_port = (EditText) findViewById(R.id.et_wifi_port);

        btnConnectAndClose.setOnClickListener(this);
        btn_U.setOnClickListener(this);
        btn_FM.setOnClickListener(this);
        btn_MD.setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == CDFPTKAndroid.PTK_MSG_WHAT_WIFICONNECT) {
                if (msg.arg1 == 0) {
                    WifiPrintUtil.ConnectOrClose = 1;
                    btnConnectAndClose.setText(R.string.disconnect);
                    Toast.makeText(mContext, R.string.connect_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, R.string.disconnect_wifi, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_wifi_connectandclose: // 连接 按钮
                // 已经连接，就断掉。否则，连接
                if (WifiPrintUtil.isConnect()) {
                    WifiPrintUtil.closeConnectWiFi();
                    btnConnectAndClose.setText(R.string.connect);
                    Toast.makeText(mContext, R.string.unConnect, Toast.LENGTH_SHORT).show();
                } else {
                    String ip = et_ip1.getText().toString() + "." + et_ip2.getText().toString() + "." +
                            et_ip3.getText().toString() + "." + et_ip4.getText().toString();
                    int port = Integer.parseInt(et_port.getText().toString());
                    WifiPrintUtil.connectWiFi(ip, port);
                }
                break;
            case R.id.btn_wifi_U:// 打印自检页
                WifiPrintUtil.printConfiguration(mContext);
                break;
            case R.id.btn_wifi_FM:// 打印机走纸
                WifiPrintUtil.feedMedia(mContext);
                break;
            case R.id.btn_wifi_MD:// 校准纸张探测器
                WifiPrintUtil.mediaDetect(mContext);
                break;
            case R.id.btn_rfid_calibration:// RFID校准
                WifiPrintUtil.RFIDCalibration(mContext);
                break;
            default:
                break;
        }
    }
}
