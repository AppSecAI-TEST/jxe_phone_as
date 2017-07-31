package com.eruitong.activity.receive;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.eruitong.activity.BaseActivity;
import com.eruitong.bluetooth.StatusBox;
import com.eruitong.config.Conf;
import com.eruitong.db.BluetoothDBWrapper;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.model.PdaGoodsChargePaymentList;
import com.eruitong.print.bluetooth.BluetoothPrintTool;
import com.eruitong.print.bluetooth.BluetoothUtil;
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
import com.zxing.activity.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zpSDK.zpSDK.zpSDK;

/**
 * 运单查询
 *
 * @author Administrator
 */
public class GoodsChargeFeeEnrolActivity extends BaseActivity implements Hearer {

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

    @ViewInject(R.id.item_enrol_waybillNo)
    EditText mKeyword;

    /**
     * 寄方
     **/
    @ViewInject(R.id.item_enrol_consignorContName)
    EditText mConsignorContName;
    /**
     * 寄电
     **/
    @ViewInject(R.id.item_enrol_consignorPhone)
    EditText mConsignorPhone;
    /**
     * 代收
     **/
    @ViewInject(R.id.item_enrol_goodsChargeFee)
    EditText mGoodsChargeFee;
    /**
     * 登记人
     **/
    @ViewInject(R.id.item_enrol_registerName)
    EditText mRegisterName;
    /**
     * 登记电话
     **/
    @ViewInject(R.id.item_enrol_registerPhone)
    EditText mRegisterPhone;
    /**
     * 银行卡号
     **/
    @ViewInject(R.id.item_enrol_bankAccount)
    EditText mBankAccount;

    /**
     * 银行卡类型
     **/
    @ViewInject(R.id.spn_accept_inputorder_bankType)
    public Spinner mSpnBankType;

    @ViewInject(R.id.query_btn)
    Button mQueryGoodsPaymentWaybill;

    /**
     * 保存
     **/
    @ViewInject(R.id.updata_btn_ok)
    Button mClient;

    private StatusBox statusBoxEnrol;

    private String enrolAddress;

    private PdaGoodsChargePaymentList pdaGoodsChargePaymentList;

    public String receiveAccount;
    // registerName
    public String registerName;
    // registerPhone
    public String registerPhone;
    /**
     * 输入的运单号
     **/
    private String charsequence;

    private String bankAccountType;

    public String paymentPeriod;

    private Context mcContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodschargeeee_enrol);
        ViewUtils.inject(this);
        pdaGoodsChargePaymentList = new PdaGoodsChargePaymentList();
        mcContext = GoodsChargeFeeEnrolActivity.this;

        initView();
    }

    private void initView() {
        mTitleBase.setText("代收款登记");
        mUserName.setText((new StringBuilder(String.valueOf(MyApp.mEmpName))).append(MyApp.mDeptName).toString());

        mKeyword.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent openCameraIntent = new Intent(GoodsChargeFeeEnrolActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                return false;
            }
        });
        ArrayList arraylist = new ArrayList();
        arraylist.add("");

        @SuppressWarnings({"unchecked", "rawtypes"}) ArrayAdapter arrayAdapter = new ArrayAdapter(mcContext, android
                .R.layout.simple_spinner_item, (String[]) arraylist.toArray(new String[arraylist.size()]));
        arrayAdapter = ArrayAdapter.createFromResource(mcContext, R.array.bank_type, android.R.layout
                .simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnBankType.setAdapter(arrayAdapter);

        // 银行卡类型
        mSpnBankType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String adapterview = mSpnBankType.getSelectedItem().toString();
                if (adapterview.equals("无卡号") && i == 0) {
                    bankAccountType = "-1";
                } else {
                    if (adapterview.equals("工商银行") && i == 1) {
                        bankAccountType = "0";
                    } else if (adapterview.equals("浦发银行") && i == 2) {
                        bankAccountType = "1";
                    } else if (adapterview.equals("招商银行") && i == 3) {
                        bankAccountType = "5";
                    } else if (adapterview.equals("建设银行") && i == 4) {
                        bankAccountType = "3";
                    }
                    pdaGoodsChargePaymentList.bankAccountType = bankAccountType;
                }
            }

            public void onNothingSelected(AdapterView adapterview) {
            }
        });
        // 查询
        mQueryGoodsPaymentWaybill.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                queryGoodsPaymentWaybill();
            }

        });

        // 登记
        mClient.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * if (!isCheck()) return;
				 */

                registerName = mRegisterName.getText().toString();
                registerPhone = mRegisterPhone.getText().toString();
                receiveAccount = mBankAccount.getText().toString();
                if (TextUtils.isEmpty(registerName)) {
                    showT("登记人不能为空");
                    return;
                }
                if (TextUtils.isEmpty(registerPhone)) {
                    showT("登记电话不能为空");
                    return;
                }
                if (TextUtils.isEmpty(receiveAccount)) {
                    showT("银行卡号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(pdaGoodsChargePaymentList.waybillNo)) {
                    showT("运单号不能为空");
                    return;
                }
                if (pdaGoodsChargePaymentList.waybillNo.trim().length() != 12) {
                    showT("运单号不合法");
                    return;
                }
                if (registerPhone.trim().length() != 11) {
                    if (registerPhone.trim().length() != 8) {
                        showT("登记电话不合法");
                        return;
                    }
                }

                statusBoxEnrol = new StatusBox(GoodsChargeFeeEnrolActivity.this, mClient);
                statusBoxEnrol.Show("正在打印 ...");
                if (!BluetoothUtil.OpenPrinter(GoodsChargeFeeEnrolActivity.this, enrolAddress)) {
                    statusBoxEnrol.Close();
                    return;
                }
                BluetoothPrintTool.printGoodsChargeFeeEnrol(pdaGoodsChargePaymentList, registerName, registerPhone,
                        receiveAccount);
                zpSDK.zp_close();
                statusBoxEnrol.Close();
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                enrol();
            }
        });
    }

    /**
     * 查询单号
     **/
    private void queryGoodsPaymentWaybill() {
        charsequence = mKeyword.getText().toString();
        if (charsequence.length() == 12) {
            clearTxt();
            if (MyUtils.isNetworkConnect(GoodsChargeFeeEnrolActivity.this)) {
                HttpUtils httpUtils = new HttpUtils();

                StringBuilder builder = new StringBuilder();
                builder.append("?").append("waybillNo=").append(charsequence);
                String url = MyApp.mPathServerURL + Conf.queryGoodsPaymentWaybillAction + builder.toString();

                httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        showT("网络异常，请稍候再试");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        String result = arg0.result;
                        Gson gson = new Gson();
                        I_Send i_Send = gson.fromJson(result, I_Send.class);

                        if (i_Send == null || i_Send.getPdaGoodsChargePaymentList() == null || i_Send
                                .getPdaGoodsChargePaymentList().size() <= 0) {
                            showT("数据为空");
                            return;
                        }

                        if (i_Send.success) {
                            pdaGoodsChargePaymentList = i_Send.getPdaGoodsChargePaymentList().get(0);
                            setOrderView(pdaGoodsChargePaymentList);

                        } else {
                            showT(i_Send.error);
                        }
                    }
                });
            } else {
                showT("无网络连接，请稍后再试");
            }
        }
    }

    /**
     * 是否验收
     */
    /*
	 * private boolean isCheck() { if (pdaGoodsChargePaymentList==null) {
	 * showT("无订单"); return
	 * false; }
	 * 
	 * if ("0".equals(pdaGoodsChargePaymentList.paymentState)) { return true; }
	 * else { showT("货款已转,不能再次登记",
	 * 0).show(); return false; } }
	 */

    /**
     * 登记代收款
     **/
    private void enrol() {
        JSONArray jsonarray = new JSONArray();
        JSONObject jsonobject = new JSONObject();
        try {
            jsonobject.put("waybillNo", pdaGoodsChargePaymentList.waybillNo);
            jsonobject.put("registerName", registerName);
            jsonobject.put("registerPhone", registerPhone);
            jsonobject.put("receiveAccount", receiveAccount);
            jsonobject.put("bankAccountType", bankAccountType);
            jsonobject.put("registerDeptCode", MyApp.mDeptCode);
            jsonobject.put("registerOperCode", MyApp.mEmpCode);
            jsonarray.put(jsonobject);

            HttpUtils httpUtils = new HttpUtils();
            RequestParams params = new RequestParams();
            params.addBodyParameter("empCode", MyApp.mEmpCode);
            params.addBodyParameter("jsonData", jsonarray.toString());

            httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.enrolGoodsChargeFeeAction, params, new
                    RequestCallBack<String>() {

                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    showT("网络连接失败,请稍后再试");
                }

                @Override
                public void onSuccess(ResponseInfo<String> arg0) {
                    showT("登记成功");
                    finish();
                    startActivity(new Intent(GoodsChargeFeeEnrolActivity.this, GoodsChargeFeeEnrolActivity.class));
                    return;
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void clearTxt() {
        mConsignorContName.setText("");
        mConsignorPhone.setText("");
        mGoodsChargeFee.setText("");
        mRegisterName.setText("");
        mRegisterPhone.setText("");
        mBankAccount.setText("");
        mBankAccount.setText("");
        mSpnBankType.setSelection(0, true);
    }

    @SuppressWarnings("unchecked")
    private void setOrderView(PdaGoodsChargePaymentList data) {
        mConsignorContName.setText(data.consignorContName);

        if (MyUtils.isEmpty(data.consignorMobile)) {// 发货人电话
            mConsignorPhone.setText(data.consignorPhone);
        } else {
            mConsignorPhone.setText(data.consignorMobile);
        }

        mRegisterName.setText(data.registerName);

        if (MyUtils.isEmpty(data.registerMobile)) {// 发货人电话
            mRegisterPhone.setText(data.registerPhone);
        } else {
            mRegisterPhone.setText(data.registerMobile);
        }
        mBankAccount.setText(data.receiveAccount);
        mGoodsChargeFee.setText((data.feeAmount == null ? "0.0" : data.feeAmount));// 代收

        if (!"0".equals(data.bankAccountType)) {
            if ("1".equals(data.bankAccountType)) {
                mSpnBankType.setSelection(2, true);
            } else if ("5".equals(data.bankAccountType)) {
                mSpnBankType.setSelection(3, true);
            } else if ("3".equals(data.bankAccountType)) {
                mSpnBankType.setSelection(4, true);
            } else if ("-1".equals(data.bankAccountType)) {
                mSpnBankType.setSelection(0, true);
            }
        } else {
            mSpnBankType.setSelection(1, true);
        }
    }

    protected void onStart() {
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
            enrolAddress = cursor.getString(cursor.getColumnIndex("address"));
        } while (true);
    }

    protected void onStop() {
        super.onStop();
        BatteryReceiver.getInstant(this).delHeraer(this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            mKeyword.setText(scanResult);
        }
    }

    class I_Send {
        public String error;
        public List<PdaGoodsChargePaymentList> pdaGoodsChargePaymentList;
        public boolean success;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public List<PdaGoodsChargePaymentList> getPdaGoodsChargePaymentList() {
            return pdaGoodsChargePaymentList;
        }

        public void setPdaGoodsChargePaymentList(List<PdaGoodsChargePaymentList> pdaGoodsChargePaymentList) {
            this.pdaGoodsChargePaymentList = pdaGoodsChargePaymentList;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
