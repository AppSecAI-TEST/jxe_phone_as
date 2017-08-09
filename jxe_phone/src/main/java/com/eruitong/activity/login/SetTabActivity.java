package com.eruitong.activity.login;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.eruitong.eruitong.R;

@SuppressWarnings("deprecation")
public class SetTabActivity extends TabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settab);

        Resources res = getResources(); // Resource object to get Drawables
        final TabHost tabHost = getTabHost(); // The activity TabHost

        Intent intent; // Reusable Intent for each tab

        // 蓝牙设置
        View viewLeft = LayoutInflater.from(this).inflate(R.layout.tab_spec, null);
        TextView txtLeft = (TextView) viewLeft.findViewById(R.id.txt_spec_label);
        txtLeft.setText("蓝牙设置");
        intent = new Intent().setClass(this, SetTabOperateActivity.class);
        TabSpec spec1 = tabHost.newTabSpec("operate").setIndicator(viewLeft).setContent(intent);
        tabHost.addTab(spec1);

        // 蓝牙打印
        View viewRight = LayoutInflater.from(this).inflate(R.layout.tab_spec, null);
        TextView txtRight = (TextView) viewRight.findViewById(R.id.txt_spec_label);
        txtRight.setText("蓝牙打印");
        intent = new Intent().setClass(this, SetTabPrintActivity.class);
        TabSpec spec2 = tabHost.newTabSpec("print").setIndicator(viewRight).setContent(intent);
        tabHost.addTab(spec2);

        // WIFI 设置 和 打印
        View viewWifi = LayoutInflater.from(this).inflate(R.layout.tab_spec, null);
        TextView txtWifiTitle = (TextView) viewWifi.findViewById(R.id.txt_spec_label);
        txtWifiTitle.setText("Wifi设置");
        intent = new Intent().setClass(this, SetWifiActivity.class);
        TabSpec spec3 = tabHost.newTabSpec("wifi").setIndicator(viewWifi).setContent(intent);
        tabHost.addTab(spec3);

        // 设置默认显示的页面
        tabHost.setCurrentTab(0);

        tabHost.getTabWidget().getChildTabViewAt(0).setBackgroundResource(R.drawable.tab_bg_press);
        tabHost.getTabWidget().getChildTabViewAt(1).setBackgroundResource(R.drawable.tab_bg);
        tabHost.getTabWidget().getChildTabViewAt(2).setBackgroundResource(R.drawable.tab_bg);
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                TabWidget tabWidget = tabHost.getTabWidget();
                View jianceMenu = tabWidget.getChildTabViewAt(0);
                View weihuMenu = tabWidget.getChildTabViewAt(1);
                View wifiMenu = tabWidget.getChildTabViewAt(2);

                if ("operate".equals(tabId)) {
                    jianceMenu.setBackgroundResource(R.drawable.tab_bg_press);
                    weihuMenu.setBackgroundResource(R.drawable.tab_bg);
                    wifiMenu.setBackgroundResource(R.drawable.tab_bg);
                } else if ("print".endsWith(tabId)) {
                    jianceMenu.setBackgroundResource(R.drawable.tab_bg);
                    weihuMenu.setBackgroundResource(R.drawable.tab_bg_press);
                    wifiMenu.setBackgroundResource(R.drawable.tab_bg);
                } else if ("wifi".endsWith(tabId)) {
                    jianceMenu.setBackgroundResource(R.drawable.tab_bg);
                    weihuMenu.setBackgroundResource(R.drawable.tab_bg);
                    wifiMenu.setBackgroundResource(R.drawable.tab_bg_press);
                }else {
                    jianceMenu.setBackgroundResource(R.drawable.tab_bg_press);
                    weihuMenu.setBackgroundResource(R.drawable.tab_bg);
                    wifiMenu.setBackgroundResource(R.drawable.tab_bg);
                }
            }
        });
    }

    public void return1(View view) {
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

}
