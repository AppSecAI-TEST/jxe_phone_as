package com.eruitong.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.eruitong.activity.addedvalue.AddedValueActivity;
import com.eruitong.activity.barupload.BarUploadMenuActivity;
import com.eruitong.activity.login.LoginActivity;
import com.eruitong.activity.query.QueryCountMenuActivity;
import com.eruitong.activity.receive.ReceiveMenuActivity;
import com.eruitong.activity.send.SendPiecesMenuActivity;
import com.eruitong.activity.update.UpdataActivity;
import com.eruitong.activity.upload.UploadActivity;
import com.eruitong.db.AcceptInputDBWrapper;
import com.eruitong.db.DepartmentDBWrapper;
import com.eruitong.db.LoginDBWrapper;
import com.eruitong.db.childNoListDBWrapper;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.receiver.BaseReceiver;
import com.eruitong.receiver.BatteryReceiver;
import com.eruitong.receiver.Hearer;
import com.eruitong.updata.UpdateManager;
import com.eruitong.utils.DateUtils;
import com.eruitong.utils.MyUtils;
import com.eruitong.utils.UpdataWaybill;
import com.eruitong.utils.UploadWaybill;
import com.eruitong.utils.WiFiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tencent.bugly.crashreport.CrashReport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends BaseActivity implements OnClickListener, Hearer {

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
    @ViewInject(R.id.ib_menu_added)
    ImageView mAdded;
    @ViewInject(R.id.ib_menu_upload)
    ImageView mUpload;
    @ViewInject(R.id.ib_menu_queryCount)
    ImageView mQueryCount;
    @ViewInject(R.id.ib_menu_softwareupdate)
    ImageView mSoftwareUpdate;
    @ViewInject(R.id.ib_bar_upload)
    ImageView mBarUpload;

    private childNoListDBWrapper childNoListDB;
    private AcceptInputDBWrapper AcceptInputDB;

    private Cursor mCursor;
    /**
     * 线程池
     **/
    private ExecutorService cachedThreadPool;
    private Handler handler;
    /**
     * 存放上传的数据
     **/
    private Map mMap;
    private Long period;
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);

        CrashReport.setUserId(MyApp.mEmpCode);

        childNoListDB = childNoListDBWrapper.getInstance(getApplication());
        AcceptInputDB = AcceptInputDBWrapper.getInstance(getApplication());
        handler = new Handler();

        cachedThreadPool = Executors.newCachedThreadPool();

        new Thread(new Runnable() {

            @Override
            public void run() {
                // 检验版本更新
                new UpdateManager(handler, MainActivity.this).checkUpdateInfo();
            }
        }).start();

        // 不做提示，强行打开蓝牙
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }

        initData();
        initView();
        delSqlite();

        // 在指定时间更新单号
        handler.postDelayed(updataWayllThread, 0L);
        // 在指定时间更新数据
        handler.postDelayed(updataThread, 0L);

        // 查询权限代码
        Cursor loginCursor = LoginDBWrapper.getInstance(getApplication()).rawQueryRank(MyApp.mEmpCode);

        if (loginCursor.moveToNext()) {
            MyApp.mPermission = loginCursor.getString(loginCursor.getColumnIndex("permissionList"));
        }
        loginCursor.close();
    }

    private Runnable updataThread = new Runnable() {

        public void run() {
            if (!MyUtils.isNetworkConnect(MainActivity.this)) {
                return;
            }
            // 更新数据
            datasUpLoad(1);
            handler.postDelayed(updataThread, period.longValue());
            return;
        }

    };
    private Runnable updataWayllThread = new Runnable() {

        public void run() {
            if (!MyUtils.isNetworkConnect(MainActivity.this)) {
                return;
            }
            // 更新单号
            UpdataWaybill.waybill(MainActivity.this);
            handler.postDelayed(updataWayllThread, 33 * 60 * 1000);
            return;
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        try {
            initAmount();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        mCursor = DepartmentDBWrapper.getInstance(getApplication()).rawQueryRank(MyApp.mDeptCode);
        int i = mCursor.getColumnIndex("deptName");
        String mDeptName = "";
        if (mCursor.moveToFirst()) {
            mDeptName = (new StringBuilder("-")).append(mCursor.getString(i)).toString();
            MyApp.mDeptName = mDeptName;
        }
        mCursor.close();
        mUserName.setText((new StringBuilder(String.valueOf(MyApp.mEmpName))).append(MyApp.mDeptName).toString());

    }

    private void datasUpLoad(int type) {

        try {
            UploadWaybill.getUploadActivity().upload(MainActivity.this, type, mUnloadNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        SharedPreferences preferences = getSharedPreferences("cache_data", 0);
        String mAutoUploadTime = preferences.getString("mAutoUpload", "");
        if (MyUtils.isEmpty(mAutoUploadTime)) {
            mAutoUploadTime = "30";
        }
        period = Long.valueOf(Long.parseLong(mAutoUploadTime) * 60L * 1000L);

    }

    private void initView() {
        mTitleBase.setText("主菜单");

        mInputorder.setOnClickListener(this);
        mDelivery.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        mAdded.setOnClickListener(this);
        mUpload.setOnClickListener(this);
        mSoftwareUpdate.setOnClickListener(this);
        mQueryCount.setOnClickListener(this);
        mBarUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ib_menu_inputorder:// 收件操作
                if (MyApp.mPermission.contains("91")) {
                    intent = new Intent(MainActivity.this, ReceiveMenuActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ib_menu_delivery:// 派件操作
                if (MyApp.mPermission.contains("92")) {
                    intent = new Intent(MainActivity.this, SendPiecesMenuActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ib_menu_update:// 资料更新
                if (MyApp.mPermission.contains("99")) {
                    intent = new Intent(MainActivity.this, UpdataActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ib_menu_added:// 附加值录入
                intent = new Intent(MainActivity.this, AddedValueActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_menu_upload:// 上传
                if (MyApp.mPermission.contains("97")) {
                    intent = new Intent(MainActivity.this, UploadActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ib_menu_queryCount:// 查询统计
                //if (MyApp.mPermission.contains("97")) {
                intent = new Intent(MainActivity.this, QueryCountMenuActivity.class);
                startActivity(intent);
                //}
                break;

            case R.id.ib_menu_softwareupdate:// 软件更新
                if (MyApp.mPermission.contains("98")) {
                    new UpdateManager(handler, this).checkUpdateInfo();
                }
                break;
            case R.id.ib_bar_upload:// 巴枪操作
                //if (MyApp.mPermission.contains("98")) {
                intent = new Intent(MainActivity.this, BarUploadMenuActivity.class);
                startActivity(intent);
                //}
                break;

            default:
                break;
        }
    }

    /**
     * 删除指定时间之前的数据
     **/
    private void delSqlite() {
        new Thread(new Runnable() {
            private boolean isrun = true;

            @Override
            public void run() {
                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");

                SharedPreferences preferences = getSharedPreferences("cache_data", 0);
                String mDeleteDataTime = preferences.getString("mDeleteDataTimeTxt", "");
                String s = mDeleteDataTime;
                if (MyUtils.isEmpty(mDeleteDataTime)) {
                    s = "90";
                }
                s = simpledateformat.format(DateUtils.diffDate(new Date(), Integer.parseInt(s)));
                Cursor cursor = AcceptInputDB.rawQueryRank();
                do {
                    if (!cursor.moveToNext()) {
                        cursor.close();
                        isrun = false;
                        return;
                    }
                    String s1 = cursor.getString(cursor.getColumnIndex("consignedTm"));
                    String s2 = s1.substring(0, 10);
                    try {
                        if (simpledateformat.parse(s).after(simpledateformat.parse(s2))) {
                            AcceptInputDB.TMdeleteRank(s1);
                        }
                    } catch (ParseException parseexception) {
                        parseexception.printStackTrace();
                    }
                } while (isrun);
            }
        }).start();

    }

    /**
     * 查询有多少未上传订单
     **/
    private void initAmount() {
        int i = 0;
        Cursor mCursor = AcceptInputDB.rawQueryRank(0);
        // do {
        // if (mCursor.moveToFirst()) {
        // i = 0 + 1;
        // } else {
        // mCursor.close();
        // break;
        // }
        // } while (true);
        i = mCursor.getCount();
        mUnloadNumber.setText((new StringBuilder("未上传：[ ")).append(i).append(" ]").toString());
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("退出");
            dialog.setIcon(R.drawable.ic_launcher);

            dialog.setMessage("您确定要退出吗？");
            dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }

            });
            dialog.setNegativeButton(R.string.cancel, null);
            dialog.create().show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onDestroy() {
        super.onDestroy();
        // 退出时，上传没有上传的数据

        cachedThreadPool.execute(new Runnable() {
            public void run() {
                try {
                    if (MyUtils.isNetworkConnect(MainActivity.this)) {
                        Looper.prepare();
                        datasUpLoad(2);
                        Looper.loop();
                    }
                    return;
                } catch (Exception exception1) {
                    exception1.printStackTrace();
                }
            }
        });
        cachedThreadPool.shutdown();

        // 关闭自动上传的线程
        handler.removeCallbacks(updataThread);
        // 关闭更新单号的线程
        handler.removeCallbacks(updataWayllThread);

        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }
    }

}
