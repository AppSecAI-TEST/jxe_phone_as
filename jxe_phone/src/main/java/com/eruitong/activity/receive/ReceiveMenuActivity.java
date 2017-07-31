package com.eruitong.activity.receive;

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

public class ReceiveMenuActivity extends BaseActivity implements OnClickListener, Hearer {

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

    @ViewInject(R.id.menu_upload_number)
    TextView mUnloadNumber;
    @ViewInject(R.id.ib_menu_inputorder)
    ImageView mInputorder;
    @ViewInject(R.id.ib_menu_delivery)
    ImageView mDelivery;
    @ViewInject(R.id.ib_menu_update)
    ImageView mUpdate;
    @ViewInject(R.id.ib_menu_upload)
    ImageView mUpload;
    @ViewInject(R.id.ib_menu_softwareupdate)
    ImageView mSoftwareUpdate;
    @ViewInject(R.id.ib_menu_goodsChargeFeeEnrol)
    ImageView mgoodsChargeFeeEnrol;

    @ViewInject(R.id.ib_menu_queryConsignorPiecesNum)
    ImageView mConsignorPiecesNum;
    @ViewInject(R.id.ib_menu_queryConsignorCommission)
    ImageView mConsignorCommission;


    @ViewInject(R.id.ib_menu_auditbill)
    ImageView mAuditbill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_main);
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
        mTitleBase.setText("收件操作");
        mInputorder.setOnClickListener(this);
        mDelivery.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        mUpload.setOnClickListener(this);
        mSoftwareUpdate.setOnClickListener(this);
        mgoodsChargeFeeEnrol.setOnClickListener(this);
        mAuditbill.setOnClickListener(this);
        mConsignorPiecesNum.setOnClickListener(this);
        mConsignorCommission.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ib_menu_inputorder:// 上门收件
                if (MyApp.mPermission.contains("02")) {
                    intent = new Intent(ReceiveMenuActivity.this, ReceiveActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ib_menu_auditbill:// 运单编辑
            /*if (MyApp.mPermission.contains("917")) {
                intent = new Intent(ReceiveMenuActivity.this,
						AuditbillActivity.class);
				startActivity(intent);
			}*/
                break;
            case R.id.ib_menu_delivery:// 本地查询
                intent = new Intent(ReceiveMenuActivity.this, QueryLocalActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_menu_update:// 运单查询
                intent = new Intent(ReceiveMenuActivity.this, QueryWaybillActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_menu_upload:// 查询已上传
                intent = new Intent(ReceiveMenuActivity.this, QueryUploadedActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_menu_softwareupdate:// 查询票数
                intent = new Intent(ReceiveMenuActivity.this, QueryWaybillNumActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_menu_goodsChargeFeeEnrol:// 代收款登记
                if (MyApp.mPermission.contains("916")) {
                    intent = new Intent(ReceiveMenuActivity.this, GoodsChargeFeeEnrolActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ib_menu_queryConsignorPiecesNum:// 收货量查询
                intent = new Intent(ReceiveMenuActivity.this, QueryConsignorPiecesNumActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_menu_queryConsignorCommission:// 收件提成查询
                intent = new Intent(ReceiveMenuActivity.this, QueryConsignorCommissionActivity.class);
                startActivity(intent);
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
