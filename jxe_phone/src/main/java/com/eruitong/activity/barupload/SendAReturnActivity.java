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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.eruitong.activity.BaseActivity;
import com.eruitong.config.Conf;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.model.net.I_PdaUploadData;
import com.eruitong.receiver.BaseReceiver;
import com.eruitong.receiver.BatteryReceiver;
import com.eruitong.receiver.Hearer;
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
 * 派件出仓操作
 *
 * @author yl.li
 */
public class SendAReturnActivity extends BaseActivity implements Hearer {

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
    @ViewInject(R.id.edt_sendAreturn_waybillNo)
    TextView mWaybillNo;

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


    private DataAdapter mDataAdapter;

    private ArrayList<itemData> itemDataList = new ArrayList<itemData>();

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_sendareturn);
        ViewUtils.inject(this);
        mContext = SendAReturnActivity.this;
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
        mTitleBase.setText("派件出仓");

        // Header 随着 Data 一起滑动
        mDataHorizontal.setScrollView(mHeaderHorizontal);
        mHeaderHorizontal.setScrollView(mDataHorizontal);

        mDataAdapter = new DataAdapter();
        mDataList.setAdapter(mDataAdapter);


        mWaybillNo.setOnKeyListener(new OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    String waybillNo = mWaybillNo.getText().toString();


                    if (!"".equals(mWaybillNo.getText().toString()) && mWaybillNo.getText().toString().length() == 12) {

                        setItemDataList(waybillNo);
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
                        jsonobject.put("lineCode", itemData.lineCode);
                        jsonobject.put("carCode", itemData.carCode);
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


    private void setItemDataList(String waybillNo) {
        itemData itemData = new itemData();
        itemData.waybillNo = waybillNo;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        //获取当前时间
        String str = formatter.format(curDate);
        itemData.operTm = str;
        itemData.operTypeCode = "派件出仓";
        itemData.operTypeCodeData = "12";
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bar_sendareturn, null);
                holder.waybillNo = (TextView) convertView.findViewById(R.id.item_sendAreturn_waybillNo);
                holder.operTypeCode = (TextView) convertView.findViewById(R.id.item_sendAreturn_operTypeCode);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            final itemData data = itemDataList.get(position);

            holder.waybillNo.setText(data.waybillNo);
            holder.operTypeCode.setText(data.operTypeCode);
            return convertView;
        }

        class ViewHolder {
            TextView waybillNo;
            TextView operTypeCode;
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
        String lineCode;
        String carCode;
        String operTm;
        String operTypeCodeData;

    }

    private void clearData() {
        finish();
        startActivity(new Intent(mContext, SendAReturnActivity.class));
    }
}
