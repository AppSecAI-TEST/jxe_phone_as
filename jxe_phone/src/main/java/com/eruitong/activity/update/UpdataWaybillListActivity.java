package com.eruitong.activity.update;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eruitong.activity.BaseActivity;
import com.eruitong.config.Conf;
import com.eruitong.db.DepartmentDBWrapper;
import com.eruitong.db.WaybillNoDBWrapper;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.model.net.I_GetOrder;
import com.eruitong.model.net.O_GetOrder;
import com.eruitong.model.OrderList;
import com.eruitong.receiver.BaseReceiver;
import com.eruitong.receiver.BatteryReceiver;
import com.eruitong.receiver.Hearer;
import com.eruitong.utils.MyUtils;
import com.eruitong.utils.WiFiUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpdataWaybillListActivity extends BaseActivity implements Hearer {

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

    @ViewInject(R.id.updata_list)
    ListView mListView;
    @ViewInject(R.id.updata_btn_ok)
    Button mOK;
    @ViewInject(R.id.updata_btn_cancel)
    Button mCancel;

    private String[] arraylist;
    private MyUpdataAdapter myAdapter;
    /**
     * 列表所选择的记录
     **/
    private List<String> seletlist;
    // 运单号
    private WaybillNoDBWrapper WaybillNoDB;
    private String mDistrictCode;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata_list);
        ViewUtils.inject(this);

        initData();
        initView();
    }

    private void initView() {
        mTitleBase.setText("运单号更新");
        myAdapter = new MyUpdataAdapter();
        mListView.setAdapter(myAdapter);
        // 返回
        mCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyUtils.isNetworkConnect(UpdataWaybillListActivity.this)) {
                    showT("网络未连接，请检查网络");
                    return;
                }

                waybill();

            }

        });

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder tagView = (ViewHolder) view.getTag();
                tagView.checkBox.toggle();
                if (tagView.checkBox.isChecked()) {
                    seletlist.add("0");
                    return;
                } else {
                    seletlist.remove("0");
                    return;
                }
            }
        });

    }

    /**
     * 运单号-点击确定
     **/
    public void waybill() {
        if (TextUtils.isEmpty(mDistrictCode)) {
            showT("请先更新基础数据");
            return;
        }

        dialog.show();

        ArrayList<O_GetOrder> orders = new ArrayList<O_GetOrder>();
        O_GetOrder getOrder;
        // 子单号
        getOrder = new O_GetOrder();
        getOrder.setAreaCode("001");
        getOrder.setApplyCount(1000);
        getOrder.setEmpCode(MyApp.mEmpCode);
        getOrder.setDevId(MyApp.mSession);
        orders.add(getOrder);
        // 签回单单号
        getOrder = new O_GetOrder();
        getOrder.setAreaCode("123");
        getOrder.setApplyCount(1000);
        getOrder.setEmpCode(MyApp.mEmpCode);
        getOrder.setDevId(MyApp.mSession);
        orders.add(getOrder);
        // 主单号
        getOrder = new O_GetOrder();
        getOrder.setAreaCode(mDistrictCode);
        getOrder.setApplyCount(1000);
        getOrder.setEmpCode(MyApp.mEmpCode);
        getOrder.setDevId(MyApp.mSession);
        orders.add(getOrder);

        String jsonString = new Gson().toJson(orders);

        HttpUtils httpUtils = new HttpUtils();

        RequestParams params = new RequestParams();
        params.addBodyParameter("jsonData", jsonString);
        String url = MyApp.mPathServerURL + Conf.waybillUpdataUrl;

        httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                dialog.cancel();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String result = arg0.result;

                I_GetOrder baseData = new Gson().fromJson(result, I_GetOrder.class);

                if (baseData.isSuccess()) {
                    WaybillNoDB.deleteRank();// 删除原有数据
                    List<OrderList> dataList = baseData.getDataList();
                    if (dataList != null && dataList.size() >= 0) {
                        // 插入新的数据
                        WaybillNoDB.insertRank(dataList);
                        showT("运单号更新成功");
                    } else {
                        showT("运单号列表为空");
                    }
                } else {
                    showT(baseData.getError() + "");
                }
                dialog.cancel();
            }
        });
    }

    private void initData() {
        WaybillNoDB = WaybillNoDBWrapper.getInstance(getApplication());
        arraylist = getResources().getStringArray(R.array.updata_waybill);
        seletlist = new ArrayList<String>();
        dialog = createUpdataDialog();

        Cursor cursor = DepartmentDBWrapper.getInstance(getApplication()).rawQueryRank(MyApp.mDeptCode);
        do {
            if (!cursor.moveToNext()) {
                cursor.close();
                return;
            }
            mDistrictCode = cursor.getString(cursor.getColumnIndex("districtCode"));
        } while (true);
    }

    private Dialog createUpdataDialog() {
        ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setProgressStyle(0);
        progressdialog.setTitle("数据更新");
        progressdialog.setMessage("正在更新，请稍等...");
        progressdialog.setCancelable(false);
        return progressdialog;
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
        Cursor mCursor = DepartmentDBWrapper.getInstance(getApplication()).rawQueryRank(MyApp.mDeptCode);
        int i = mCursor.getColumnIndex("deptName");
        String mDeptName = "";
        if (mCursor.moveToFirst()) {
            mDeptName = (new StringBuilder("-")).append(mCursor.getString(i)).toString();
        }
        mCursor.close();
        mUserName.setText((new StringBuilder(String.valueOf(MyApp.mEmpName))).append(mDeptName).toString());
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

    class MyUpdataAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arraylist.length;
        }

        @Override
        public Object getItem(int position) {
            return arraylist[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            ViewHolder viewgroup;
            if (view == null) {
                viewgroup = new ViewHolder();
                view = getLayoutInflater().inflate(R.layout.syncdata_item, null);
                viewgroup.itemName = (TextView) view.findViewById(R.id.txt_syncdata);
                viewgroup.checkBox = (CheckBox) view.findViewById(R.id.chb_syncdata);
                view.setTag(viewgroup);
            } else {
                viewgroup = (ViewHolder) view.getTag();
            }
            viewgroup.itemName.setText(arraylist[i]);
            return view;
        }

    }

    class ViewHolder {
        CheckBox checkBox;
        TextView itemName;
    }
}
