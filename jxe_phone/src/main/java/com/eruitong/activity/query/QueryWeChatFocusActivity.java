package com.eruitong.activity.query;

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
import android.widget.ListView;
import android.widget.TextView;

import com.eruitong.activity.BaseActivity;
import com.eruitong.config.Conf;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.receiver.BaseReceiver;
import com.eruitong.receiver.BatteryReceiver;
import com.eruitong.receiver.Hearer;
import com.eruitong.utils.DialogUtils;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 查询微信关注人数
 *
 * @author Administrator
 */
public class QueryWeChatFocusActivity extends BaseActivity implements Hearer {

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

    @ViewInject(R.id.query_wechatFocus_startdate)
    EditText mstartDate;
    @ViewInject(R.id.query_wechatFocus_enddate)
    EditText mendDate;
    @ViewInject(R.id.query_number)
    ListView mListQuery;
    /*@ViewInject(R.id.query_uploaded_poll)
    TextView mQueryUploadedPoll;*/
    @ViewInject(R.id.query_wechatfocust_count)
    TextView mQueryWechatFocustCount;

    @ViewInject(R.id.query_btn)
    Button mQuery;

    private int wechatFocustCount = 0;
    private String waybill;

    private String mSelectStartDate;
    private String mSelectEndDate;

    private List listOrder;
    private LstOrderAdapter orderAdapter;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_wechatfocus);
        ViewUtils.inject(this);

        initView();
    }

    private void initView() {
        mTitleBase.setText("已关注微信查询");

        orderAdapter = new LstOrderAdapter();
        mListQuery.setAdapter(orderAdapter);
    }

    protected void onStart() {
        initComps();
        super.onStart();
        String s = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        listOrder = new ArrayList<ItemData>();

        BatteryReceiver.getInstant(this).addHeraer(this);
        mUserName.setText((new StringBuilder(String.valueOf(MyApp.mEmpName))).append(MyApp.mDeptName).toString());
    }

    protected void onStop() {
        super.onStop();
        BatteryReceiver.getInstant(this).delHeraer(this);
    }

    private void initComps() {
        mstartDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                @SuppressLint("WrongConstant") Dialog dialog = new DatePickerDialog(QueryWeChatFocusActivity.this,
                        new OnDateSetListener() {

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
                @SuppressLint("WrongConstant") Dialog dialog = new DatePickerDialog(QueryWeChatFocusActivity.this,
                        new OnDateSetListener() {

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
            return i;
        }

        public View getView(int i, View view, ViewGroup viewg) {
            ViewHolder viewholder;
            view = viewg;
            if (view != null) {
                viewholder = new ViewHolder();

                view = getLayoutInflater().inflate(R.layout.item_qurie_wechatfocusl_number, null);
                viewholder.deliverEmpCode = (TextView) view.findViewById(R.id.item_txt_qurie_wechat_deliverEmpCode);
                viewholder.deliverEmpName = (TextView) view.findViewById(R.id.item_txt_qurie_wechat_deliverEmpName);
                viewholder.count = (TextView) view.findViewById(R.id.item_txt_qurie_wechat_count);

                view.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) view.getTag();
            }

            viewholder.deliverEmpCode.setText((new StringBuilder()).append(((ItemData) listOrder.get(i))
                    .deliverEmpCode).toString());

            viewholder.deliverEmpName.setText((new StringBuilder()).append(((ItemData) listOrder.get(i))
                    .deliverEmpName).toString());

            viewholder.count.setText((new StringBuilder()).append(((ItemData) listOrder.get(i)).count).append("人")
                    .toString());

            wechatFocustCount = wechatFocustCount + Integer.valueOf(((ItemData) listOrder.get(i)).count);
            mQueryWechatFocustCount.setText(String.valueOf(wechatFocustCount) + "人");
            return view;
        }
    }

    private void query() {
        wechatFocustCount = 0;
        dialog = DialogUtils.createLoginDialog(this);
        dialog.show();


        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("deptCode", MyApp.mDeptCode);
        params.addBodyParameter("startDate", mSelectStartDate);
        params.addBodyParameter("endDate", mSelectEndDate);


        httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.findOpenMobileTotal, params, new
                RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                showT("网络连接失败,请稍后再试");
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String result = arg0.result;
                I_QueryCount openidMobileData = new Gson().fromJson(result, I_QueryCount.class);

                if (openidMobileData.success) {
                    listOrder = openidMobileData.totalList;
                    orderAdapter.notifyDataSetChanged();
                    dialog.cancel();
                } else {
                    showT(openidMobileData.error);
                }
            }

        });
    }


    class ViewHolder {
        TextView deliverEmpCode;
        TextView deliverEmpName;
        TextView count;
    }

    class ItemData {
        String deliverEmpCode;
        String deliverEmpName;
        String count;
        Double totalNum;
        String startDate;
        String endDate;
    }

    class I_QueryCount {
        String deptCode;
        List<ItemData> totalList;
        String error;
        boolean success;
    }
}
