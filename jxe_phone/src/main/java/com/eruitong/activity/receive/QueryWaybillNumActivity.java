package com.eruitong.activity.receive;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eruitong.activity.BaseActivity;
import com.eruitong.activity.receive.QueryWaybillNumActivity.LstOrderAdapter.I_QueryNumber;
import com.eruitong.config.Conf;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.receiver.BaseReceiver;
import com.eruitong.receiver.BatteryReceiver;
import com.eruitong.receiver.Hearer;
import com.eruitong.utils.DialogUtils;
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

/**
 * 查询票数
 *
 * @author Administrator
 */
public class QueryWaybillNumActivity extends BaseActivity implements Hearer {

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

    @ViewInject(R.id.query_number)
    ListView mListQuery;

    private LstOrderAdapter orderAdapter;
    private List listOrder;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_number);
        ViewUtils.inject(this);

        initView();

    }

    private void initView() {
        mTitleBase.setText("运单查询");

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
            showT("网点代码不能为空");
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
            showT("无网络连接，请稍后再试");
            return;
        }
        dialog = DialogUtils.createLoginDialog(mContext);
        dialog.show();

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("deptCode", MyApp.mDeptCode);

        httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.queryWaybillNumber, params, new
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
                view = getLayoutInflater().inflate(R.layout.item_qurie_waybill_number, null);

                holder.empcode = (TextView) view.findViewById(R.id.item_txt_qurie_waybillnum_code);
                holder.pollCount = (TextView) view.findViewById(R.id.item_txt_qurie_waybillnum_piaonum);
                holder.jiannum = (TextView) view.findViewById(R.id.item_txt_qurie_waybillnum_jiannum);
                holder.weightQtynum = (TextView) view.findViewById(R.id.item_txt_qurie_waybillnum_weightQtynum);
                holder.commission = (TextView) view.findViewById(R.id.item_txt_qurie_waybillnum_commission);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.empcode.setText(((ItemData) listOrder.get(i)).inputerEmpCode);
            holder.pollCount.setText((new StringBuilder(String.valueOf(((ItemData) listOrder.get(i)).pollCount)))
                    .append("票").toString());
            holder.jiannum.setText((new StringBuilder(String.valueOf(((ItemData) listOrder.get(i)).quantity))).append
                    ("件").toString());
            holder.weightQtynum.setText((new StringBuilder(String.valueOf(((ItemData) listOrder.get(i))
                    .meterageWeightQty))).append("kg").toString());
            holder.commission.setText((new StringBuilder(String.valueOf(((ItemData) listOrder.get(i))
                    .inputCommission))).append("元").toString());

            return view;
        }

        class ViewHolder {
            TextView empcode;
            TextView pollCount;
            TextView jiannum;
            TextView weightQtynum;
            TextView commission;

        }

        class ItemData {
            String inputerEmpCode;
            String pollCount;
            String quantity;
            String meterageWeightQty;
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
