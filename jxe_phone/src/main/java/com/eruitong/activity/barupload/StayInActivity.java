package com.eruitong.activity.barupload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.eruitong.activity.BaseActivity;
import com.eruitong.config.Conf;
import com.eruitong.db.FailCauseDBWrapper;
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
 * 滞留入仓
 *
 * @author yl.li
 */
public class StayInActivity extends BaseActivity implements Hearer {

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
     * 运单号
     **/
    @ViewInject(R.id.edt_stayIn_waybillNo)
    TextView mWaybillNo;
    /**
     * 滞留原因
     **/
    @ViewInject(R.id.edt_stayIn_stayWhyCode)
    AutoCompleteTextView mStayWhyCode;
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
    private String mOperTypeCode = "17";
    private String stayWhyCode;
    private ArrayList<itemData> itemDataList = new ArrayList<itemData>();

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_stayin);
        ViewUtils.inject(this);
        mContext = StayInActivity.this;
        initView();
        initAdapter();

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
        mTitleBase.setText("滞留入仓");

        // Header 随着 Data 一起滑动
        mDataHorizontal.setScrollView(mHeaderHorizontal);
        mHeaderHorizontal.setScrollView(mDataHorizontal);

        mDataAdapter = new DataAdapter();
        mDataList.setAdapter(mDataAdapter);

        // 滞留原因
        mStayWhyCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String stayWhyCodeString = mStayWhyCode.getEditableText().toString();
                if (!stayWhyCodeString.isEmpty()) {
                    stayWhyCode = (new StringBuilder(String.valueOf(stayWhyCodeString.replaceAll("[^0-9]", "")))).toString();
                }

            }
        });


        mWaybillNo.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    String waybillNo = mWaybillNo.getText().toString();
                    String stayWhyCodeData = mStayWhyCode.getText().toString();

                    if (MyUtils.isEmpty(stayWhyCode)) {
                        MyUtils.showText(mContext, "滞留原因不能为空");
                        return false;
                    }
                    if (!"".equals(mWaybillNo.getText().toString()) && mWaybillNo.getText().toString().length() == 12) {

                        setItemDataList(waybillNo, stayWhyCodeData);
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
                        jsonobject.put("stayWayCode", itemData.stayWhyCodeData);
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

                    httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.addBarRecode, params, new
							RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            showT("网络连接失败,请稍后再试");
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            String result = arg0.result;
                            I_PdaUploadData uploadData = new Gson().fromJson(result, I_PdaUploadData.class);

                            if (uploadData.isSuccess()) {
                                showT("上传成功");
                                clearData();
                            } else {
                                showT(uploadData.getError());
                            }

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void setItemDataList(String waybillNo, String stayWhyCodeData) {
        itemData itemData = new itemData();
        itemData.operTypeCode = "滞留";
        itemData.stayWhyCode = stayWhyCodeData;
        itemData.stayWhyCodeData = stayWhyCode;
        itemData.waybillNo = waybillNo;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        //获取当前时间
        String str = formatter.format(curDate);
        itemData.operTm = str;
        itemData.operTypeCodeData = "17";
        itemDataList.add(itemData);
        mDataAdapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        Cursor stayWhyCodeRank = FailCauseDBWrapper.getInstance(getApplication()).rawQueryRank();
        ArrayList arraylist = new ArrayList();
        String[] str = null;
        do {
            String s1;
            String s2;
            if (!stayWhyCodeRank.moveToNext()) {
                stayWhyCodeRank.close();

                if (arraylist.size() != 0) {
                    str = new String[arraylist.size()];
                    for (int i = 0; i < arraylist.size(); i++) {
                        str[i] = (String) arraylist.get(i);
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter<String>(StayInActivity.this, android.R.layout
							.simple_expandable_list_item_1, str);
                    mStayWhyCode.setAdapter(arrayAdapter);
                    mStayWhyCode.setThreshold(1);
                }
                break;
            }
            s1 = stayWhyCodeRank.getString(stayWhyCodeRank.getColumnIndex("causecode"));
            s2 = stayWhyCodeRank.getString(stayWhyCodeRank.getColumnIndex("causecont"));
            arraylist.add((new StringBuilder("[")).append(s1).append("]").append(s2).toString());
        } while (true);

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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bar_stayin, null);
                holder.waybillNo = (TextView) convertView.findViewById(R.id.item_stayIn_waybillNo);
                holder.operTypeCode = (TextView) convertView.findViewById(R.id.item_stayIn_opertypecode);
                holder.stayWhyCode = (TextView) convertView.findViewById(R.id.item_stayIn_stayWhyCode);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            final itemData data = itemDataList.get(position);

            holder.waybillNo.setText(data.waybillNo);
            holder.operTypeCode.setText(data.operTypeCode);
            holder.stayWhyCode.setText(data.stayWhyCode);

            return convertView;
        }

        class ViewHolder {
            TextView waybillNo;
            TextView operTypeCode;
            TextView stayWhyCode;
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
        String stayWhyCode;
        String operTm;
        String operTypeCodeData;
        String stayWhyCodeData;


    }

    private void clearData() {
        finish();
        startActivity(new Intent(mContext, StayInActivity.class));
    }
}
