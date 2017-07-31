package com.eruitong.activity.barupload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
 * 解封车操作
 *
 * @author yl.li
 */
public class LockedCarRecordActivity extends BaseActivity implements Hearer {

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
     * 封车
     **/
    @ViewInject(R.id.rb_typecode_fc)
    RadioButton mRbFc;
    /**
     * 解封车
     **/
    @ViewInject(R.id.rb_typecode_jfc)
    RadioButton mRbJfc;
    /**
     * 发车
     **/
    @ViewInject(R.id.rb_typecode_fcs)
    RadioButton mRbFcs;
    /**
     * 到车
     **/
    @ViewInject(R.id.rb_typecode_dc)
    RadioButton mRbDc;
    /**
     * 封车牌
     **/
    @ViewInject(R.id.edt_lockedCar_fcCode)
    TextView mFcCode;
    /**
     * 车牌号
     **/
    @ViewInject(R.id.edt_lockedCar_carNo)
    TextView mCarNo;


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
        setContentView(R.layout.activity_bar_lockedcarrecord);
        ViewUtils.inject(this);
        mContext = LockedCarRecordActivity.this;
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
        mTitleBase.setText("解封车操作");

        // Header 随着 Data 一起滑动
        mDataHorizontal.setScrollView(mHeaderHorizontal);
        mHeaderHorizontal.setScrollView(mDataHorizontal);

        mDataAdapter = new DataAdapter();
        mDataList.setAdapter(mDataAdapter);

        // 选择要查询的类型
        mRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mRbFc.getId()) {
                    mOperTypeCode = "33";
                    opertTypeCode = "封车";
                } else if (checkedId == mRbJfc.getId()) {
                    mOperTypeCode = "34";
                    opertTypeCode = "解封车";
                }
            }
        });
        mRbFc.setChecked(true);

        // 封车码失去焦点
        mFcCode.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean flag) {
                if (flag) {// 当焦点失去的时候
                    return;
                }
                String lockedCarCode = mFcCode.getText().toString();
                String carNo = mCarNo.getText().toString();
                if (MyUtils.isEmpty(lockedCarCode)) {
                    MyUtils.showText(mContext, "封车码不能为空");
                    mFcCode.setText(null);
                    return;
                }

                if (MyUtils.isEmpty(carNo)) {
                    MyUtils.showText(mContext, "车牌号不能为空");
                    mFcCode.setText(null);
                    return;
                }

                if (!(lockedCarCode.length() == 12 && lockedCarCode.startsWith("121"))) {
                    MyUtils.showText(mContext, "封车码不合法");
                    mFcCode.setText(null);
                    return;

                }
                setItemDataList(opertTypeCode, lockedCarCode, carNo, null, null);

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
                        jsonobject.put("operTypeCode", itemData.operTypeCodeData);
                        jsonobject.put("carNo", itemData.carNo);
                        jsonobject.put("lockedCarCode", itemData.lockedCarCode);
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

    private void setItemDataList(String operTypeCode, String lockedCarCode, String carNo, String lineCode, String
			carCode) {
        itemData itemData = new itemData();
        itemData.operTypeCode = opertTypeCode;
        itemData.lockedCarCode = lockedCarCode;
        itemData.carNo = carNo;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        //获取当前时间
        String str = formatter.format(curDate);
        itemData.operTm = str;
        itemData.operEmpCode = MyApp.mEmpCode;
        itemData.deptCode = MyApp.mDeptCode;
        itemData.macNo = MyApp.mSession;
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bar_lockedcarrecord, null);
                holder.operTypeCode = (TextView) convertView.findViewById(R.id.item_bar_opertypecode);
                holder.carNo = (TextView) convertView.findViewById(R.id.item_locked_carno);
                holder.lockedCarCode = (TextView) convertView.findViewById(R.id.item_locked_lockedcarcode);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            final itemData data = itemDataList.get(position);

            holder.operTypeCode.setText(data.operTypeCode);
            holder.carNo.setText(data.carNo);
            holder.lockedCarCode.setText(data.lockedCarCode);

            return convertView;
        }

        class ViewHolder {
            TextView operTypeCode;
            TextView carNo;
            TextView lockedCarCode;
        }
    }

    class I_Send {
        public String error;
        public List<itemData> pdaWaybillList;
        public boolean success;
    }

    class itemData {

        String operTypeCode;
        String operTypeCodeData;
        String carNo;
        String lockedCarCode;
        String operTm;
        String operEmpCode;
        String deptCode;
        String macNo;

    }

    private void clearData() {
        finish();
        startActivity(new Intent(mContext, LockedCarRecordActivity.class));
    }
}
