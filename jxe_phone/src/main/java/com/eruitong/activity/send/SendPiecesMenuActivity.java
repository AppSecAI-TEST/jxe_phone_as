package com.eruitong.activity.send;

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

public class SendPiecesMenuActivity extends BaseActivity implements OnClickListener, Hearer {

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

    @ViewInject(R.id.ib_menu_sendPieces_sign)
    ImageView mSenPieces;

    @ViewInject(R.id.ib_menu_sendPieces_query)
    ImageView mQuerySenPieces;

    @ViewInject(R.id.ib_menu_sendPieces_signlist)
    ImageView mSenPiecesSign;

    @ViewInject(R.id.ib_menu_sendPieces_subscribe)
    ImageView mSenPiecesSubscribe;
    @ViewInject(R.id.ib_menu_query_goodscharge)
    ImageView mQueryGoodschargePayment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendpieces_main);
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
        mTitleBase.setText(" 派件操作");
        mSenPieces.setOnClickListener(this);
        mQuerySenPieces.setOnClickListener(this);
        mSenPiecesSign.setOnClickListener(this);
        mSenPiecesSubscribe.setOnClickListener(this);
        mQueryGoodschargePayment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ib_menu_sendPieces_sign:// 派件签收
                //if (MyApp.mPermission.contains("02")) {
                intent = new Intent(SendPiecesMenuActivity.this, SendPiecesActivity.class);
                //	}
                break;

            case R.id.ib_menu_sendPieces_query:// 派件签收
                //if (MyApp.mPermission.contains("02")) {
                intent = new Intent(SendPiecesMenuActivity.this, QuerySendPiecesNumActivity.class);
                //	}
                break;

            case R.id.ib_menu_sendPieces_signlist:// 多单派件签收
                intent = new Intent(SendPiecesMenuActivity.this, SendPiecesSignActivity.class);
                break;

            case R.id.ib_menu_sendPieces_subscribe:// 预约派件
                intent = new Intent(SendPiecesMenuActivity.this, SendPiecesSubscribeActivity.class);
                break;
            case R.id.ib_menu_query_goodscharge:// 代收款转款
                if (MyApp.mPermission.contains("9281")) {
                    intent = new Intent(SendPiecesMenuActivity.this, QueryGoodsChargePaymentActivity.class);
                }
                break;

            default:
                break;
        }

        if (null != intent) {
            startActivity(intent);
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
