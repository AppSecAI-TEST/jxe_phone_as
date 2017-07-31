package com.eruitong.activity.barupload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.eruitong.activity.BaseActivity;
import com.eruitong.config.Conf;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.model.net.I_PdaUploadData;
import com.eruitong.receiver.BaseReceiver;
import com.eruitong.receiver.BatteryReceiver;
import com.eruitong.receiver.Hearer;
import com.eruitong.utils.MyUtils;
import com.eruitong.utils.WiFiUtil;
import com.eruitong.view.NoscrollListView;
import com.eruitong.view.SyncHorizontalScrollView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 装卸车操作
 *
 * @author yl.li
 */
public class PackageActivity extends BaseActivity implements Hearer {

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

    @ViewInject(R.id.radiogroup1)
    RadioGroup mRG;

    /**
     * 建包
     **/
    @ViewInject(R.id.rb_typecode_jb)
    RadioButton mRbJb;
    /**
     * 拆包
     **/
    @ViewInject(R.id.rb_typecode_cb)
    RadioButton mRbCb;
    /**
     * 运单号
     **/
    @ViewInject(R.id.edt_bag_waybillNo)
    TextView mWaybillNo;

    /**
     * 包号
     **/
    @ViewInject(R.id.edt_bag_cageCode)
    TextView mCageCode;

    /**
     * 提交
     **/
    @ViewInject(R.id.btn_push_data)
    Button mBtnPush;

    /**
     * 表格头部
     **/
    @ViewInject(R.id.header_horizontal)
    SyncHorizontalScrollView mHeaderHorizontal;
    /**
     * 表格头部
     **/
    @ViewInject(R.id.data_horizontal)
    SyncHorizontalScrollView mDataHorizontal;
    /**
     * 列表数据
     **/
    @ViewInject(R.id.list_data)
    NoscrollListView mDataList;

    private String opertTypeCode;

    private DataAdapter mDataAdapter;

    /**
     * 操作类型
     **/
    private String mOperTypeCode;
    private ArrayList<itemData> itemDataList = new ArrayList<itemData>();

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_package);
        ViewUtils.inject(this);
        mContext = PackageActivity.this;
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

    protected void onDestroy() {
        super.onDestroy();
        //BatteryReceiver.getInstant(this).delHeraer(this);
    }


    private void initComps() {
        mUserName.setText((new StringBuilder(String.valueOf(MyApp.mEmpName))).append(MyApp.mDeptName).toString());
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

    private void initView() {
        mTitleBase.setText("装卸车操作");

        // Header 随着 Data 一起滑动
        mDataHorizontal.setScrollView(mHeaderHorizontal);
        mHeaderHorizontal.setScrollView(mDataHorizontal);

        mDataAdapter = new DataAdapter();
        mDataList.setAdapter(mDataAdapter);

        // 选择要查询的类型
        mRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mRbJb.getId()) {
                    mOperTypeCode = "26";
                    opertTypeCode = "建包";
                } else if (checkedId == mRbCb.getId()) {
                    mOperTypeCode = "27";
                    opertTypeCode = "拆包";
                }
            }
        });
        mRbJb.setChecked(true);


        mWaybillNo.setOnKeyListener(new OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    String cageCode = mCageCode.getText().toString();
                    String waybillNo = mWaybillNo.getText().toString();

                    if (MyUtils.isEmpty(cageCode)) {
                        MyUtils.showText(mContext, "包号不能为空");
                        mWaybillNo.setText(null);
                        mCageCode.setText(null);
                        return false;
                    }

                    if (!"".equals(mWaybillNo.getText().toString()) && mWaybillNo.getText().toString().length() == 12) {

                        setItemDataList(mOperTypeCode, waybillNo, cageCode);
                    }
                }
                mWaybillNo.setText(null);
                return false;

            }

        });


        mBtnPush.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    JSONArray jsonarray = new JSONArray();

                    for (int i = 0; i < itemDataList.size(); i++) {
                        itemData itemData = itemDataList.get(i);

                        JSONObject jsonobject = new JSONObject();
                        jsonobject.put("waybillNo", itemData.waybillNo);
                        jsonobject.put("operTypeCode", itemData.operTypeCodeData);
                        jsonobject.put("bagCageCode", itemData.cageCode);
                        jsonobject.put("pdaOperTm", itemData.operTm);
                        jsonobject.put("deptCode", MyApp.mDeptCode);
                        jsonobject.put("operEmpCode", MyApp.mEmpCode);
                        jsonobject.put("macNo", MyApp.mSession);
                        jsonarray.put(jsonobject);
                    }

                    HttpUtils httpUtils = new HttpUtils();
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("deptCode", MyApp.mDeptCode);
                    params.addBodyParameter("empCode", MyApp.mEmpCode);
                    params.addBodyParameter("devId", MyApp.mSession);
                    params.addBodyParameter("jsonData", jsonarray.toString());

                    httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.addBarRecode, params, new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            Toast.makeText(getApplicationContext(), "网络连接失败,请稍后再试", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            String result = arg0.result;
                            I_PdaUploadData uploadData = new Gson().fromJson(result, I_PdaUploadData.class);

                            if (uploadData.isSuccess()) {
                                Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                                clearData();
                            } else {
                                Toast.makeText(getApplicationContext(), uploadData.getError(), Toast.LENGTH_SHORT)
										.show();
                            }

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void setItemDataList(String operTypeCode, String waybillNo, String cageCode) {
        itemData itemData = new itemData();
        itemData.operTypeCode = opertTypeCode;
        itemData.cageCode = cageCode;
        itemData.waybillNo = waybillNo;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        //获取当前时间
        String str = formatter.format(curDate);
        itemData.operTm = str;
        itemData.operTypeCodeData = mOperTypeCode;
        itemDataList.add(itemData);
        mDataAdapter.notifyDataSetChanged();
    }

    class DataAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemDataList == null ? 0 : itemDataList.size();
        }

        @Override
        public itemData getItem(int position) {
            return itemDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint({"ResourceAsColor", "InlinedApi"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bar_loadrecode, null);
                holder.waybillNo = (TextView) convertView.findViewById(R.id.item_bag_waybillNo);
                holder.operTypeCode = (TextView) convertView.findViewById(R.id.item_bag_opertypecode);
                holder.cageCode = (TextView) convertView.findViewById(R.id.item_bag_cagecode);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            final itemData data = itemDataList.get(position);

            holder.waybillNo.setText(data.waybillNo);
            holder.operTypeCode.setText(data.operTypeCode);
            holder.cageCode.setText(data.cageCode);

            return convertView;
        }

        class ViewHolder {
            TextView waybillNo;
            TextView operTypeCode;
            TextView cageCode;
        }
    }

    class I_Send {
        public String error;
        public List<itemData> pdaWaybillList;
        public boolean success;
    }

    class itemData {
        String waybillNo;
        String operTypeCode;
        String cageCode;
        String operTm;
        String operTypeCodeData;

    }

    private void clearData() {
        finish();
        startActivity(new Intent(mContext, BarloadRecordActivity.class));
    }
}
