package com.eruitong.activity.barupload;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.eruitong.activity.BaseActivity;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.receiver.BaseReceiver;
import com.eruitong.receiver.BatteryReceiver;
import com.eruitong.receiver.Hearer;
import com.eruitong.utils.WiFiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BarUploadMenuActivity extends BaseActivity implements OnClickListener, Hearer {

    @ViewInject(R.id.img_basetitle_signal)
    ImageView mWifiSignal;
    @ViewInject(R.id.txt_basetitle_title)
    TextView mTitleBase;
    @ViewInject(R.id.img_basetitle_electric_quantity)
    ImageView mBatterySignal;
    @ViewInject(R.id.txt_basetitle_username)
    TextView mUserName;
    @ViewInject(R.id.txt_basetitle_date)
    TextView mDate;

    @ViewInject(R.id.ib_menu_carrecode)
    ImageView mCarRecode;
    @ViewInject(R.id.ib_menu_barload)
    ImageView mBarLoad;
    @ViewInject(R.id.ib_menu_starttocar)
    ImageView mStartTocar;
    @ViewInject(R.id.ib_menu_deliver)
    ImageView mDeliver;
    @ViewInject(R.id.ib_menu_stranded)
    ImageView mStranded;
    @ViewInject(R.id.ib_menu_package)
    ImageView mPackage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_barupload);
        ViewUtils.inject(this);

        initView();
    }

    protected void onStart() {
        initComps();
        super.onStart();
        BatteryReceiver.getInstant(this).addHeraer(this);
    }

    protected void onStop() {
        super.onStop();
        BatteryReceiver.getInstant(this).delHeraer(this);
    }

    private void initComps() {
        mUserName.setText((new StringBuilder(String.valueOf(MyApp.mEmpName))).append(MyApp.mDeptName).toString());
    }

    private void initView() {
        mCarRecode.setOnClickListener(this);
        mBarLoad.setOnClickListener(this);
        mStartTocar.setOnClickListener(this);
        mDeliver.setOnClickListener(this);
        mStranded.setOnClickListener(this);
        mPackage.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ib_menu_carrecode:// 解封车操作
                //if (MyApp.mPermission.contains("02")) {
                intent = new Intent(BarUploadMenuActivity.this, LockedCarRecordActivity.class);
                startActivity(intent);
                //}
                break;

            case R.id.ib_menu_barload:// 装卸车操作
                //if (MyApp.mPermission.contains("02")) {
                intent = new Intent(BarUploadMenuActivity.this, BarloadRecordActivity.class);
                startActivity(intent);
                //}
                break;
            case R.id.ib_menu_starttocar:// 到发车操作
                //if (MyApp.mPermission.contains("02")) {
                intent = new Intent(BarUploadMenuActivity.this, StartToCarActivity.class);
                startActivity(intent);
                //}
                break;
            case R.id.ib_menu_deliver:// 派件出仓
                //if (MyApp.mPermission.contains("02")) {
                intent = new Intent(BarUploadMenuActivity.this, SendAReturnActivity.class);
                startActivity(intent);
                //}
                break;
            case R.id.ib_menu_stranded:// 滞留入仓
                //if (MyApp.mPermission.contains("02")) {
                intent = new Intent(BarUploadMenuActivity.this, StayInActivity.class);
                startActivity(intent);
                //}
                break;
            case R.id.ib_menu_package:// 包操作
                //if (MyApp.mPermission.contains("02")) {
                intent = new Intent(BarUploadMenuActivity.this, PackageActivity.class);
                startActivity(intent);
                //}
                break;
            default:
                break;
        }
    }

    public void upDate(BaseReceiver basereceiver, Intent intent) {
        if (intent != null) {
            int i = intent.getIntExtra("level", 0);
            mBatterySignal.setImageLevel(i);
            String date = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(new Date());
            mDate.setText(date);
            mWifiSignal.setImageLevel(WiFiUtil.getWiFiLevel(this));
        }
    }
}
