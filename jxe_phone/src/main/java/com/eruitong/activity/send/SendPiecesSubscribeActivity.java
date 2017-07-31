package com.eruitong.activity.send;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.eruitong.activity.BaseActivity;
import com.eruitong.config.Conf;
import com.eruitong.db.BluetoothDBWrapper;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.model.InputDataInfo;
import com.eruitong.model.net.I_QueryWaybill;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 预约派件
 *
 * @author yl.li
 */
public class SendPiecesSubscribeActivity extends BaseActivity implements Hearer {

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
     * 查询当天
     **/
    @ViewInject(R.id.rb_query_today)
    RadioButton mRbToday;
    /**
     * 查询昨天
     **/
    @ViewInject(R.id.rb_query_yesterday)
    RadioButton mRbYesterday;
    /**
     * 查询
     **/
    @ViewInject(R.id.btn_query_data)
    Button mBtnQuery;
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

    /**
     * 蓝牙地址
     **/
    private String xiqopiaoAddress;

    /**
     * 查询类型
     **/
    private String mQueryType;
    private ArrayList<InputDataInfo> itemDataList = new ArrayList<InputDataInfo>();
    private ArrayList<String> pushWaybillNo = new ArrayList<String>();

    /**
     * 签收人
     **/
    private String recipients;
    /**
     * 收件员
     **/
    private String deliverEmpCode;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendpieces_subscribe);
        ViewUtils.inject(this);
        mContext = SendPiecesSubscribeActivity.this;
        initView();
    }

    protected void onStart() {
        initComps();
        super.onStart();
        BatteryReceiver.getInstant(this).addHeraer(this);
        isConnect();
    }

    private void isConnect() {
        Cursor cursor = BluetoothDBWrapper.getInstance(getApplication()).rawQueryRank("No1");
        do {
            if (!cursor.moveToNext()) {
                cursor.close();
                return;
            }
            xiqopiaoAddress = cursor.getString(cursor.getColumnIndex("address"));
        } while (true);
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
        mTitleBase.setText("预约派件");

        // Header 随着 Data 一起滑动
        mDataHorizontal.setScrollView(mHeaderHorizontal);
        mHeaderHorizontal.setScrollView(mDataHorizontal);

        mDataAdapter = new DataAdapter();
        mDataList.setAdapter(mDataAdapter);

        // 选择要查询的类型
        mRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mRbToday.getId()) {
                    mQueryType = "1";
                } else if (checkedId == mRbYesterday.getId()) {
                    mQueryType = "2";
                }
            }
        });
        mRbToday.setChecked(true);

        mBtnQuery.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                queryWaybill();
            }
        });

        mBtnPush.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO
                String waybillNo = pushWaybillNo.toString();
                waybillNo = waybillNo.substring(1, waybillNo.length() - 2);

                NetworkInfo networkinfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
                if (networkinfo != null && networkinfo.isConnected()) {
                    HttpUtils httpUtils = new HttpUtils();
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("deliverEmpCode", MyApp.mEmpCode);
                    params.addBodyParameter("deptCode", MyApp.mDeptCode);
                    params.addBodyParameter("waybillNo", waybillNo);

                    httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.pushSubscribeWaybillNo, params, new
							RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            Toast.makeText(getApplicationContext(), "网络异常，请稍候再试", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            String result = arg0.result;
                            Gson gson = new Gson();
                            I_QueryWaybill i_Send = gson.fromJson(result, I_QueryWaybill.class);

                            if (i_Send.success) {
                                Toast.makeText(mContext, "提交成功", Toast.LENGTH_SHORT).show();
                            }

                        }

                    });
                } else {
                    Toast.makeText(mContext, R.string.net_error, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    class DataAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemDataList == null ? 0 : itemDataList.size();
        }

        @Override
        public InputDataInfo getItem(int position) {
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sendpieces_subscribe, null);
                holder.noticed = (CheckBox) convertView.findViewById(R.id.item_query_order_noticed);
                holder.empCode = (TextView) convertView.findViewById(R.id.item_query_order_empCode);
                holder.phone = (TextView) convertView.findViewById(R.id.item_query_order_phone);
                holder.name = (TextView) convertView.findViewById(R.id.item_query_order_name);
                holder.allmoney = (TextView) convertView.findViewById(R.id.item_query_order_allmoney);
                holder.quantity = (TextView) convertView.findViewById(R.id.item_query_order_quantity);
                holder.weight = (TextView) convertView.findViewById(R.id.item_query_order_weight);
                holder.sender = (TextView) convertView.findViewById(R.id.item_query_order_sender);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final InputDataInfo data = itemDataList.get(position);

            holder.noticed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        pushWaybillNo.add(data.waybillNo);
                    } else {
                        pushWaybillNo.remove(data.waybillNo);
                    }
                }
            });
            if (data.isCall) {
                holder.noticed.setChecked(true);
            }

            // 收件人
            holder.empCode.setText(data.addresseeContName + "");

            // 电话
            if ("0".equals(data.calledSign)) {
                holder.phone.setTextColor(Color.BLUE);
            } else if ("1".equals(data.calledSign)) {
                holder.phone.setTextColor(Color.BLACK);
            }
//			if (!TextUtils.isEmpty(data.addresseeMobile)) {
//				phone = String.valueOf(data.addresseeMobile);
//			} else {
//				holder.phone.setText(data.addresseePhone + "");
//			}
            holder.phone.setText(data.addresseeMobile + "");

            // 货物名
            holder.name.setText(data.consName + "");

            // 件数
            holder.quantity.setText(data.quantity + "");

            // 重量
            holder.weight.setText(data.meterageWeightQty);

            // 合计
            holder.allmoney.setText(getAllMoney(data));

            // 发件人
            holder.sender.setText(data.consignorContName + "");

            holder.phone.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + data.addresseeMobile));
                        startActivity(intent);

                        // TODO 打完电话之后，把电话号码一样的数据，全都选中
                        setCalled(data.addresseeMobile);
                        notifyDataSetChanged();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "未知异常，请重试", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return convertView;
        }

        class ViewHolder {
            CheckBox noticed;
            TextView empCode;
            TextView quantity;
            TextView allmoney;
            TextView name;
            TextView weight;
            TextView phone;
            TextView sender;
        }
    }

    private void setCalled(String phone) {
        for (int i = 0; i < itemDataList.size(); i++) {
            InputDataInfo data = itemDataList.get(i);
            if (phone.equals(data.addresseePhone) || phone.equals(data.addresseeMobile)) {
                itemDataList.get(i).isCall = true;
            }
        }
    }

    private String getAllMoney(InputDataInfo data) {
        double d = 0;
        double d1;
        double d2;
        double d3;
        double d4;
        double d5;
        double d6;
        double d7;
        double d8;
        double d9;
        double d10;
        double all;

        // 计算合计
        if ("1".equals(data.paymentTypeCode)) {// 寄付
            d = Double.parseDouble(data.goodsChargeFee == null ? "0.0" : data.goodsChargeFee);
            d8 = Double.parseDouble(data.warehouseFee == null ? "0.0" : data.warehouseFee);
            d10 = Double.parseDouble(data.transferFee == null ? "0.0" : data.transferFee);
            all = Double.valueOf(d + d8 + d10);
        } else {// 2-到付
            d = Double.parseDouble(data.waybillFee == null ? "0.0" : data.waybillFee);
            d1 = Double.parseDouble(data.insuranceFee == null ? "0.0" : data.insuranceFee);
            d2 = Double.parseDouble(data.signBackFee == null ? "0.0" : data.signBackFee);
            d3 = Double.parseDouble(data.deboursFee == null ? "0.0" : data.deboursFee);
            d4 = Double.parseDouble(data.waitNotifyFee == null ? "0.0" : data.waitNotifyFee);
            d5 = Double.parseDouble(data.deliverFee == null ? "0.0" : data.deliverFee);
            d6 = Double.parseDouble(data.goodsChargeFee == null ? "0.0" : data.goodsChargeFee);
            d7 = data.fuelServiceFee == null ? 0.0 : data.fuelServiceFee;
            d8 = Double.parseDouble(data.warehouseFee == null ? "0.0" : data.warehouseFee);
            d9 = Double.parseDouble(data.backCargoFee == null ? "0.0" : data.backCargoFee);
            d10 = Double.parseDouble(data.transferFee == null ? "0.0" : data.transferFee);
            all = Double.valueOf(d + d1 + d2 + d3 + d4 + d5 + d6 + d7 + d8 + d9 + d10);
        }
        String allMoney;
        if (all == 0) {
            allMoney = "0.0";
        } else {
            allMoney = String.valueOf(all);
        }
        return allMoney;
    }

    /*
     * 查询单号
     */
    private void queryWaybill() {
        NetworkInfo networkinfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
            HttpUtils httpUtils = new HttpUtils();
            RequestParams params = new RequestParams();
            params.addBodyParameter("deliverEmpCode", MyApp.mEmpCode);
            params.addBodyParameter("deptCode", MyApp.mDeptCode);
            params.addBodyParameter("timeSign", mQueryType);

            httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.querySubscribeWaybillNo, params, new
					RequestCallBack<String>() {

                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    Toast.makeText(getApplicationContext(), "网络异常，请稍候再试", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(ResponseInfo<String> arg0) {
                    String result = arg0.result;
                    Gson gson = new Gson();
                    I_QueryWaybill i_Send = gson.fromJson(result, I_QueryWaybill.class);

                    if (i_Send == null || i_Send.pdaWaybillList == null || i_Send.pdaWaybillList.size() <= 0) {
                        if ("waitNotifyFee is not null!".equals(i_Send.error)) {
                            Toast.makeText(getApplicationContext(), "此运单为等通知派送货物!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "数据为空", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    if (i_Send.success) {
                        List<InputDataInfo> items = i_Send.pdaWaybillList;
                        if (items.size() <= 0) {
                            Toast.makeText(getApplicationContext(), "数据为空", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        itemDataList.clear();
                        itemDataList.addAll(items);
                        mDataAdapter.notifyDataSetChanged();
                    }
                }
            });
        } else {
            Toast.makeText(mContext, R.string.net_error, Toast.LENGTH_SHORT).show();
        }
    }
}
