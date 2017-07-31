package com.eruitong.activity.receive;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eruitong.activity.BaseActivity;
import com.eruitong.activity.receive.QueryConsignorCommissionActivity.LstOrderAdapter.I_QueryNumber;
import com.eruitong.config.Conf;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.receiver.BaseReceiver;
import com.eruitong.receiver.BatteryReceiver;
import com.eruitong.receiver.Hearer;
import com.eruitong.utils.DialogUtils;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 查询收件提成
 *
 * @author Administrator
 */
public class QueryConsignorCommissionActivity extends BaseActivity implements Hearer {

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

    @ViewInject(R.id.query_consinor_startdate)
    EditText mstartDate;
    @ViewInject(R.id.query_consinor_enddate)
    EditText mendDate;
    @ViewInject(R.id.query_btn)
    Button mQuery;

    @ViewInject(R.id.query_sendpieces_number)
    NoscrollListView mListQuery;

    private LstOrderAdapter orderAdapter;
    private List listOrder;

    private Dialog dialog;

    private String mSelectStartDate;
    private String mSelectEndDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignorcommission_number);
        ViewUtils.inject(this);

        initView();
    }

    private void initView() {
        mTitleBase.setText("收货量查询");
        // Header 随着 Data 一起滑动
        mDataHorizontal.setScrollView(mHeaderHorizontal);
        mHeaderHorizontal.setScrollView(mDataHorizontal);

        orderAdapter = new LstOrderAdapter();
        mListQuery.setAdapter(orderAdapter);
    }

    protected void onStart() {
        initComps();
        super.onStart();
        BatteryReceiver.getInstant(this).addHeraer(this);
        listOrder = new ArrayList();
        if (MyApp.mDeptCode.isEmpty()) {
            showT("网点代码不能为空");
            return;
        }
    }

    protected void onStop() {
        super.onStop();
        BatteryReceiver.getInstant(this).delHeraer(this);
    }

    private void initComps() {
        mUserName.setText((new StringBuilder(String.valueOf(MyApp.mEmpName))).append(MyApp.mDeptName).toString());

        mstartDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                @SuppressLint("WrongConstant") Dialog dialog = new DatePickerDialog(QueryConsignorCommissionActivity
                        .this, new OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        if (dayOfMonth >= 10 && monthOfYear >= 9) {
                            mSelectStartDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        } else if (dayOfMonth < 10 && monthOfYear >= 9) {
                            mSelectStartDate = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                        } else if (monthOfYear < 9 && dayOfMonth > 9) {
                            mSelectStartDate = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                        } else if (monthOfYear < 9 && dayOfMonth <= 9) {
                            mSelectStartDate = year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth;
                        }

                        mstartDate.setText(mSelectStartDate);
                        //addData(mSelectDate);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();

            }
        });

        mendDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                @SuppressLint("WrongConstant") Dialog dialog = new DatePickerDialog(QueryConsignorCommissionActivity
                        .this, new OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (dayOfMonth >= 10 && monthOfYear >= 9) {
                            mSelectEndDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        } else if (dayOfMonth < 10 && monthOfYear >= 9) {
                            mSelectEndDate = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                        } else if (monthOfYear < 9 && dayOfMonth > 9) {
                            mSelectEndDate = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                        } else if (monthOfYear < 9 && dayOfMonth <= 9) {
                            mSelectEndDate = year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth;
                        }

                        mendDate.setText(mSelectEndDate);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();

            }
        });

        // 查询
        mQuery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query();
            }

        });
    }

    private void query() {
        if (!MyUtils.isNetworkConnect(this)) {
            showT("无网络连接，请稍后再试");
            return;
        }
        if (mSelectStartDate == null || "".equals(mSelectStartDate)) {
            showT("请选择开始日期!");
            return;
        }

        if (mSelectEndDate == null || "".equals(mSelectEndDate)) {
            showT("请选择结束日期!");
            return;
        }

        dialog = DialogUtils.createLoginDialog(this);
        dialog.show();

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("empCode", MyApp.mEmpCode);
        params.addBodyParameter("startDate", mSelectStartDate);
        params.addBodyParameter("endDate", mSelectEndDate);
        params.addBodyParameter("deptCode", MyApp.mDeptCode);

        httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.queryConsignorCommission, params, new
                RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                showT("网络错误，请稍后再试");
                dialog.cancel();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String result = arg0.result;
                I_QueryNumber number = new Gson().fromJson(result, I_QueryNumber.class);

                if (number.success) {
                    listOrder = number.empList;
                    orderAdapter.notifyDataSetChanged();
                    dialog.cancel();

                } else {
                    showT(number.error);
                }
            }
        });
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

    class LstOrderAdapter extends BaseAdapter {

        public int getCount() {
            if (listOrder == null) {
                return 0;
            } else {
                return listOrder.size();
            }
        }

        public Object getItem(int i) {
            return listOrder.get(i);
        }

        public long getItemId(int i) {
            return (long) listOrder.get(i).hashCode();
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = getLayoutInflater().inflate(R.layout.item_consign_waybill_commission, null);

                holder.empcode = (TextView) view.findViewById(R.id.item_txt_consignor_commission_code);
                holder.pollCount = (TextView) view.findViewById(R.id.item_txt_consignor_commission_piaonum);
                holder.quantity = (TextView) view.findViewById(R.id.item_txt_consignor_commission_quantityynum);
                holder.expressFee = (TextView) view.findViewById(R.id.item_txt_consignor_commission_expressFee);
                holder.commission = (TextView) view.findViewById(R.id.item_txt_consignor_commission);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.empcode.setText(((ItemData) listOrder.get(i)).inputerEmpCode);
            holder.pollCount.setText((new StringBuilder(String.valueOf(((ItemData) listOrder.get(i)).pollCount)))
                    .append("票").toString());
            holder.quantity.setText((new StringBuilder(String.valueOf(((ItemData) listOrder.get(i)).quantity)))
                    .append("件").toString());
            holder.expressFee.setText((new StringBuilder(String.valueOf(((ItemData) listOrder.get(i)).expressFee)))
                    .append("元").toString());
            holder.commission.setText((new StringBuilder(String.valueOf(((ItemData) listOrder.get(i))
                    .inputCommission))).append("元").toString());

            return view;
        }

        class ViewHolder {
            TextView empcode;
            TextView pollCount;
            TextView quantity;
            TextView expressFee;
            TextView commission;
        }

        class ItemData {
            String inputerEmpCode;
            String pollCount;
            String quantity;
            String expressFee;
            String inputCommission;
        }

        class I_QueryNumber {
            String deptCode;
            List<ItemData> empList;
            String error;
            boolean success;
        }
    }
}
