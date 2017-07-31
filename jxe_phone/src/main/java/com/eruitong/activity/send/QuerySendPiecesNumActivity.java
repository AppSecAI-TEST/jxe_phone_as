package com.eruitong.activity.send;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eruitong.activity.BaseActivity;
import com.eruitong.activity.send.QuerySendPiecesNumActivity.LstOrderAdapter.I_QueryGatherWaybill;
import com.eruitong.config.Conf;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.model.InputDataInfo;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 查询票数
 *
 * @author Administrator
 */
public class QuerySendPiecesNumActivity extends BaseActivity implements Hearer {

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

    @ViewInject(R.id.query_sendpieces_number)
    NoscrollListView mListQuery;

    private LstOrderAdapter orderAdapter;
    private List<InputDataInfo> listOrder;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendpieces_number2);
        ViewUtils.inject(this);

        initView();

    }

    private void initView() {
        mTitleBase.setText("已交款查询");
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
        if (!MyApp.mDeptCode.isEmpty()) {
            initDateNum();
            return;
        } else {
            Toast.makeText(this, "网点代码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    protected void onStop() {
        super.onStop();
        BatteryReceiver.getInstant(this).delHeraer(this);
    }

    private void initComps() {
        // Cursor mCursor =
        // DepartmentDBWrapper.getInstance(getApplication()).rawQueryRank(MyApp.mDeptCode);
        // int i = mCursor.getColumnIndex("deptName");
        // String mDeptName = "";
        // if (mCursor.moveToFirst()) {
        // mDeptName = (new
        // StringBuilder("-")).append(mCursor.getString(i)).toString();
        // }
        // mCursor.close();
        mUserName.setText((new StringBuilder(String.valueOf(MyApp.mEmpName))).append(MyApp.mDeptName).toString());
    }

    private void initDateNum() {
        if (!MyUtils.isNetworkConnect(this)) {
            Toast.makeText(this, "无网络连接，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog = createLoginDialog();
        dialog.show();

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("deptCode", MyApp.mDeptCode);

        httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.queryGatherWaybillNumber, params, new
                RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                showT("网络错误，请稍后再试");
                dialog.cancel();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String result = arg0.result;
                I_QueryGatherWaybill number = new Gson().fromJson(result, I_QueryGatherWaybill.class);

                if (number.success) {
                    listOrder = number.empGatherList;
                    orderAdapter.notifyDataSetChanged();
                    dialog.cancel();
                } else {
                    Toast.makeText(QuerySendPiecesNumActivity.this, number.error + "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Dialog createLoginDialog() {
        ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setProgressStyle(0);
        progressdialog.setTitle("\u6570\u636E\u67E5\u8BE2");
        progressdialog.setMessage("\u6B63\u5728\u67E5\u8BE2\uFF0C\u8BF7\u7A0D\u7B49...");
        progressdialog.setCancelable(false);
        return progressdialog;
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
                view = getLayoutInflater().inflate(R.layout.item_send_waybill_number2, null);

                holder.empcode = (TextView) view.findViewById(R.id.item_txt_send_waybillnum_code);
                holder.pollCount = (TextView) view.findViewById(R.id.item_txt_send_waybillnum_piaonum);
                holder.weightQtynum = (TextView) view.findViewById(R.id.item_txt_send_waybillnum_weightQtynum);
                holder.expressFee = (TextView) view.findViewById(R.id.item_txt_send_waybillnum_expressFee);
                holder.goodsChargeAgent = (TextView) view.findViewById(R.id.item_txt_qurie_waybillnum_goodsChargeAgent);
                holder.countFee = (TextView) view.findViewById(R.id.item_txt_qurie_waybillnum_count);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            InputDataInfo dataInfo = listOrder.get(i);

            holder.empcode.setText(dataInfo.deliverEmpCode);
            holder.pollCount.setText((new StringBuilder(String.valueOf(dataInfo.piaoCount))).append("票").toString());
            holder.weightQtynum.setText((new StringBuilder(String.valueOf(dataInfo.meterageWeightQty))).append("kg")
                    .toString());
            holder.expressFee.setText((new StringBuilder(String.valueOf(dataInfo.expressFee))).append("元").toString());
            holder.goodsChargeAgent.setText((new StringBuilder(String.valueOf(dataInfo.goodsChargeAgent))).append
                    ("元").toString());
            holder.countFee.setText((new StringBuilder(String.valueOf(dataInfo.countFee))).append("元").toString());

            return view;
        }

        class ViewHolder {
            TextView empcode;
            TextView pollCount;
            TextView weightQtynum;
            TextView expressFee;
            TextView goodsChargeAgent;
            TextView countFee;
        }


        class I_QueryGatherWaybill {
            String deptCode;
            List<InputDataInfo> empGatherList;
            String error;
            boolean success;
        }
    }
}
