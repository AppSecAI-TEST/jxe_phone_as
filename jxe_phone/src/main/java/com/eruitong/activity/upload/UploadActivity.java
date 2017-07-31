package com.eruitong.activity.upload;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eruitong.activity.BaseActivity;
import com.eruitong.config.Conf;
import com.eruitong.db.AcceptInputDBWrapper;
import com.eruitong.db.DeliveryDBWrapper;
import com.eruitong.db.childNoListDBWrapper;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.model.net.I_SaveWaybill;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UploadActivity extends BaseActivity implements Hearer {

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

    @ViewInject(R.id.lst_upload)
    ListView mListView;
    @ViewInject(R.id.btn_upload_cancel)
    Button mCencal;
    @ViewInject(R.id.btn_upload_ok)
    Button mOK;

    private LstSyncDataAdapter mAdapter;

    /**
     * 所选择的储存列表
     **/
    private List list;
    /**
     * 所显示的文字的列表
     **/
    private String arraylist[];
    /**
     * 显示内容的列表
     **/
    private List mList;

    private List childList;

    private Cursor mCursor;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_upload);
        ViewUtils.inject(this);

        list = new ArrayList();
        addlist();
        initView();
    }

    private void initView() {
        mTitleBase.setText("上传数据");

        mAdapter = new LstSyncDataAdapter();
        mListView.setAdapter(mAdapter);

        // 列表的选择
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> print, View view, int i, long id) {
                ViewHolder adapterview = (ViewHolder) view.getTag();
                adapterview.checkBox.toggle();
                switch (i) {
                    default:
                        return;

                    case 0: // 上门收件
                        if (adapterview.checkBox.isChecked()) {
                            list.add("0");
                            ((ItemData) mList.get(i)).checked = adapterview.checkBox.isChecked();
                            return;
                        } else {
                            list.remove("0");
                            ((ItemData) mList.get(i)).checked = adapterview.checkBox.isClickable();
                            return;
                        }

                    case 1: // 派件签收
                        break;
                }
                if (adapterview.checkBox.isChecked()) {
                    list.add("1");
                    ((ItemData) mList.get(i)).checked = adapterview.checkBox.isChecked();
                    return;
                } else {
                    list.remove("1");
                    ((ItemData) mList.get(i)).checked = adapterview.checkBox.isClickable();
                    return;
                }
            }
        });

        // 返回
        mCencal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog = createLoginDialog();
        // 确定
        mOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() <= 0) {
                    MyUtils.showText(UploadActivity.this, "请选择要上传的数据");
                    return;
                }
                if (!MyUtils.isNetworkConnect(UploadActivity.this)) {
                    MyUtils.showText(UploadActivity.this, "网络未连接，请稍后再试");
                    return;
                }

                dialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        upLoad();
                    }
                }).start();

            }
        });

    }

    private void addlist() {
        int i;
        arraylist = getResources().getStringArray(R.array.upload);
        mList = new ArrayList();
        i = 0;
        ItemData itemdata;
        do {
            if (i >= arraylist.length) {
                return;
            }
            itemdata = new ItemData();
            itemdata.itemName = arraylist[i];
            int j;
            switch (i++) {
                case 0: {
                    j = 0;
                    mCursor = AcceptInputDBWrapper.getInstance(getApplication()).rawQueryRank(0);
                    do {
                        if (mCursor.moveToNext()) {
                            j++;
                            continue;
                        } else {
                            if (j != 0) {
                                itemdata.checked = true;
                                list.add("0");
                            } else {
                                list.remove("0");
                                itemdata.checked = false;
                            }
                            itemdata.itemNumber = j;
                            mList.add(itemdata);
                            break;
                        }
                    } while (true);
                    break;
                }
                case 1: {
                    j = 0;
                    mCursor = DeliveryDBWrapper.getInstance(getApplication()).rawQueryRank(0, 13);
                    do {

                        if (mCursor.moveToNext()) {
                            j++;
                            continue;
                        } else {
                            if (j != 0) {
                                itemdata.checked = true;
                                list.add("1");
                            } else {
                                list.remove("1");
                                itemdata.checked = false;
                            }
                            itemdata.itemNumber = j;
                            mList.add(itemdata);
                            break;
                        }
                    } while (true);
                    break;
                }
            }
        } while (true);
    }

    private void upLoad() {
        int i = 0;
        do {
            if (i >= list.size()) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.cancel();
                }
                return;
            }
            int index = Integer.parseInt((String) list.get(i));
            switch (i++) {
                case 0: {
                    JSONArray jsonarray = new JSONArray();
                    childNoListDBWrapper childnolistdbwrapper;
                    AcceptInputDBWrapper acceptinputdbwrapper = AcceptInputDBWrapper.getInstance(getApplication());
                    childnolistdbwrapper = childNoListDBWrapper.getInstance(getApplication());
                    mCursor = acceptinputdbwrapper.rawQueryRank(0);
                    do {

                        if (!mCursor.moveToNext()) {
                            mCursor.close();
                            break;
                        } else {
                            double d;
                            double d1;
                            double d2;
                            double d3;
                            double d4;
                            double d5;
                            double d6;
                            double d7;
                            double d8;
                            String s3;
                            String s5;
                            String s7;
                            String s8;
                            String s9;
                            String s10;
                            String s11;
                            String s12;
                            String s13;
                            String s14;
                            String s15;
                            String s16;
                            String s17;
                            String s18;
                            String s19;
                            String s20;
                            String s21;
                            String s22;
                            String s23;
                            String s24;
                            String s25;
                            String s26;
                            String s27;
                            String s28;
                            String s29;
                            String s30;
                            String s31;
                            String s32;
                            String s33;
                            String s34;
                            String s35;
                            String s36;
                            String s37;
                            String s38;
                            String s39;
                            String s40;
                            String s41;
                            String s42;
                            String s43;
                            String s44;
                            Cursor cursor;
                            int j;
                            int l;
                            j = mCursor.getColumnIndex("waybillNo");
                            s3 = mCursor.getString(j);
                            j = mCursor.getColumnIndex("sourceZoneCode");
                            s5 = mCursor.getString(j);
                            j = mCursor.getColumnIndex("destZoneCode");
                            s7 = mCursor.getString(j);
                            j = mCursor.getColumnIndex("custCode");
                            s8 = mCursor.getString(j);
                            j = mCursor.getColumnIndex("consignorCompName");
                            s9 = mCursor.getString(j);
                            j = mCursor.getColumnIndex("consignorAddr");
                            s10 = mCursor.getString(j);
                            j = mCursor.getColumnIndex("consignorPhone");
                            s11 = mCursor.getString(j);
                            j = mCursor.getColumnIndex("consignorContName");
                            s12 = mCursor.getString(j);
                            j = mCursor.getColumnIndex("consignorMobile");
                            s13 = mCursor.getString(j);
                            j = mCursor.getColumnIndex("addresseeCompName");
                            s14 = mCursor.getString(j);
                            j = mCursor.getColumnIndex("addresseeAddr");
                            s15 = mCursor.getString(j);
                            j = mCursor.getColumnIndex("addresseePhone");
                            s16 = mCursor.getString(j);
                            j = mCursor.getColumnIndex("addresseeContName");
                            s17 = mCursor.getString(j);
                            j = mCursor.getColumnIndex("addresseeMobile");
                            s18 = mCursor.getString(j);
                            j = mCursor.getColumnIndex("realWeightQty");
                            d = mCursor.getDouble(j);
                            j = mCursor.getColumnIndex("meterageWeightQty");
                            d1 = mCursor.getDouble(j);
                            j = mCursor.getColumnIndex("consName");
                            s19 = mCursor.getString(j);
                            j = mCursor.getColumnIndex("quantity");
                            j = mCursor.getInt(j);
                            l = mCursor.getColumnIndex("volume");
                            d2 = mCursor.getDouble(l);
                            l = mCursor.getColumnIndex("consigneeEmpCode");
                            s20 = mCursor.getString(l);
                            l = mCursor.getColumnIndex("consignedTm");
                            s21 = mCursor.getString(l);
                            l = mCursor.getColumnIndex("waybillRemk");
                            s22 = mCursor.getString(l);
                            l = mCursor.getColumnIndex("inputType");
                            s23 = mCursor.getString(l);
                            l = mCursor.getColumnIndex("paymentTypeCode");
                            s24 = mCursor.getString(l);
                            l = mCursor.getColumnIndex("settlementTypeCode");
                            s25 = mCursor.getString(l);
                            l = mCursor.getColumnIndex("serviceTypeCode");
                            s26 = mCursor.getString(l);
                            l = mCursor.getColumnIndex("waybillFee");
                            s27 = mCursor.getString(l);
                            l = mCursor.getColumnIndex("goodsChargeFee");
                            s28 = mCursor.getString(l);
                            l = mCursor.getColumnIndex("chargeAgentFee");
                            s29 = mCursor.getString(l);
                            l = mCursor.getColumnIndex("bankNo");
                            s30 = mCursor.getString(l);
                            l = mCursor.getColumnIndex("bankType");
                            s31 = mCursor.getString(l);
                            l = mCursor.getColumnIndex("transferDays");
                            l = mCursor.getInt(l);
                            int j1 = mCursor.getColumnIndex("insuranceAmount");
                            s32 = mCursor.getString(j1);
                            j1 = mCursor.getColumnIndex("insuranceFee");
                            d3 = mCursor.getDouble(j1);
                            j1 = mCursor.getColumnIndex("signBackNo");
                            s33 = mCursor.getString(j1);
                            j1 = mCursor.getColumnIndex("signBackCount");
                            s34 = mCursor.getString(j1);
                            j1 = mCursor.getColumnIndex("signBackSize");
                            s35 = mCursor.getString(j1);
                            j1 = mCursor.getColumnIndex("signBackReceipt");
                            s36 = mCursor.getString(j1);
                            j1 = mCursor.getColumnIndex("signBackSeal");
                            s37 = mCursor.getString(j1);
                            j1 = mCursor.getColumnIndex("signBackIdentity");
                            s38 = mCursor.getString(j1);
                            j1 = mCursor.getColumnIndex("waitNotifyFee");
                            d4 = mCursor.getDouble(j1);
                            j1 = mCursor.getColumnIndex("signBackFee");
                            d5 = mCursor.getDouble(j1);
                            j1 = mCursor.getColumnIndex("deboursFee");
                            d6 = mCursor.getDouble(j1);
                            j1 = mCursor.getColumnIndex("deboursFlag");
                            s39 = mCursor.getString(j1);
                            j1 = mCursor.getColumnIndex("deliverFee");
                            d7 = mCursor.getDouble(j1);
                            j1 = mCursor.getColumnIndex("productTypeCode");
                            s40 = mCursor.getString(j1);
                            j1 = mCursor.getColumnIndex("orderNo");
                            s41 = mCursor.getString(j1);
                            j1 = mCursor.getColumnIndex("teamCode");
                            s42 = mCursor.getString(j1);
                            j1 = mCursor.getColumnIndex("discountExpressFee");
                            d8 = mCursor.getDouble(j1);
                            j1 = mCursor.getColumnIndex("inputerEmpCode");
                            s43 = mCursor.getString(j1);
                            j1 = mCursor.getColumnIndex("distanceTypeCode");
                            s44 = mCursor.getString(j1);
                            cursor = childnolistdbwrapper.rawQueryRank(s3, 0);
                            childList = new ArrayList();
                            String obj = "";

                            do {
                                if (cursor.moveToNext()) {
                                    String s45 = cursor.getString(cursor.getColumnIndex("childwaybillNo"));
                                    String obj1 = obj;
                                    if (obj == "") {
                                        obj = (new StringBuilder(String.valueOf(obj1))).append(s45).toString();
                                        Log.e("childList", ((String) (obj)));
                                        break;
                                    } else {
                                        obj1 = (new StringBuilder(String.valueOf(obj))).append(",").toString();
                                    }
                                } else {
                                    cursor.close();
                                    break;
                                }
                            } while (true);
                            try {
                                JSONObject jsonobject = new JSONObject();
                                jsonobject.put("waybillNo", s3);
                                jsonobject.put("sourceZoneCode", s5);
                                jsonobject.put("destZoneCode", s7);
                                jsonobject.put("custCode", s8);
                                jsonobject.put("consignorCompName", s9);
                                jsonobject.put("consignorAddr", s10);
                                jsonobject.put("consignorPhone", s11);
                                jsonobject.put("consignorContName", s12);
                                jsonobject.put("consignorMobile", s13);
                                jsonobject.put("addresseeCompName", s14);
                                jsonobject.put("addresseeAddr", s15);
                                jsonobject.put("addresseePhone", s16);
                                jsonobject.put("addresseeContName", s17);
                                jsonobject.put("addresseeMobile", s18);
                                jsonobject.put("realWeightQty", Double.valueOf(d));
                                jsonobject.put("meterageWeightQty", Double.valueOf(d1));
                                jsonobject.put("quantity", j);
                                jsonobject.put("volume", Double.valueOf(d2));
                                jsonobject.put("consigneeEmpCode", s20);
                                jsonobject.put("consignedTm", s21);
                                jsonobject.put("waybillRemk", s22);
                                jsonobject.put("consName", s19);
                                jsonobject.put("inputType", s23);
                                jsonobject.put("paymentTypeCode", s24);
                                jsonobject.put("settlementTypeCode", s25);
                                jsonobject.put("serviceTypeCode", s26);
                                jsonobject.put("waybillFee", s27);
                                jsonobject.put("goodsChargeFee", s28);
                                jsonobject.put("chargeAgentFee", s29);
                                jsonobject.put("bankNo", s30);
                                jsonobject.put("bankType", s31);
                                jsonobject.put("transferDays", l);
                                jsonobject.put("insuranceAmount", s32);
                                jsonobject.put("insuranceFee", Double.valueOf(d3));
                                jsonobject.put("signBackNo", s33);
                                jsonobject.put("signBackCount", s34);
                                jsonobject.put("signBackSize", s35);
                                jsonobject.put("signBackReceipt", s36);
                                jsonobject.put("signBackSeal", s37);
                                jsonobject.put("signBackIdentity", s38);
                                jsonobject.put("waitNotifyFee", Double.valueOf(d4));
                                jsonobject.put("signBackFee", Double.valueOf(d5));
                                jsonobject.put("deboursFee", Double.valueOf(d6));
                                jsonobject.put("deboursFlag", s39);
                                jsonobject.put("deliverFee", Double.valueOf(d7));
                                jsonobject.put("productTypeCode", s40);
                                jsonobject.put("orderNo", s41);
                                jsonobject.put("childNoList", obj);
                                jsonobject.put("teamCode", s42);
                                jsonobject.put("discountExpressFee", Double.valueOf(d8));
                                jsonobject.put("inputerEmpCode", s43);
                                jsonobject.put("distanceTypeCode", s44);
                                jsonarray.put(jsonobject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } while (true);
                    if (!jsonarray.toString().equals("[]")) {
                        HttpUtils httpUtils = new HttpUtils();
                        RequestParams params = new RequestParams();
                        params.addBodyParameter("jsonData", jsonarray.toString());
                        httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.saveWaybill, params, new
                                RequestCallBack<String>() {

                            @Override
                            public void onFailure(HttpException arg0, String arg1) {
                                MyUtils.showText(UploadActivity.this, "网络异常");
                                dialog.cancel();
                            }

                            @Override
                            public void onSuccess(ResponseInfo<String> arg0) {
                                String result = arg0.result;

                                I_SaveWaybill saveWaybill = new Gson().fromJson(result, I_SaveWaybill.class);
                                if (saveWaybill.isSuccess()) {

                                    AcceptInputDBWrapper inputDBWrapper = AcceptInputDBWrapper.getInstance
                                            (getApplication());
                                    childNoListDBWrapper childNoListDBWrapper2 = childNoListDBWrapper.getInstance
                                            (getApplication());
                                    // 查询到未上传的
                                    mCursor = inputDBWrapper.rawQueryRank(0);
                                    do {
                                        if (mCursor.moveToNext()) {
                                            int i = mCursor.getColumnIndex("waybillNo");
                                            String waybillNo = mCursor.getString(i);
                                            // 改成已上传
                                            inputDBWrapper.updateRank(waybillNo, 1);

                                            // 根据单号查询是否有子单号
                                            Cursor cursor = childNoListDBWrapper2.rawQueryRank(waybillNo, 0);
                                            do {
                                                if (cursor.moveToNext()) {
                                                    childNoListDBWrapper2.rawUpdateRank(waybillNo, 1);
                                                } else {
                                                    cursor.close();
                                                    break;
                                                }
                                            } while (true);
                                        } else {
                                            mCursor.close();
                                            addlist();
                                            mAdapter.notifyDataSetChanged();
                                            showT("上传成功");
                                            return;
                                        }
                                    } while (true);
                                } else {
                                    showT(saveWaybill.getError());
                                }

                                dialog.cancel();
                            }
                        });

                    }

                    break;
                }
                case 1: {

                    JSONArray jsonarray = new JSONArray();
                    mCursor = DeliveryDBWrapper.getInstance(getApplication()).rawQueryRank(0, 13);
                    _L16:
                    if (mCursor.moveToNext()) {
                        int k = mCursor.getColumnIndex("opCode");
                        k = mCursor.getInt(k);
                        int i1 = mCursor.getColumnIndex("waybillNo");
                        String s = mCursor.getString(i1);
                        i1 = mCursor.getColumnIndex("empCode");
                        String s1 = mCursor.getString(i1);
                        i1 = mCursor.getColumnIndex("recipients");
                        String s2 = mCursor.getString(i1);
                        i1 = mCursor.getColumnIndex("operTm");
                        String s4 = mCursor.getString(i1);
                        i1 = mCursor.getColumnIndex("inputType");
                        String s6 = mCursor.getString(i1);
                        JSONObject jsonobject1 = new JSONObject();
                        try {
                            jsonobject1.put("operTypeCode", (new StringBuilder(String.valueOf(k))).toString());
                            jsonobject1.put("deliverEmpCode", s1);
                            jsonobject1.put("waybillNo", s);
                            jsonobject1.put("strOperTm", s4);
                            jsonobject1.put("inputType", s6);
                            jsonobject1.put("carCode", "");
                            jsonobject1.put("carNo", "");
                            jsonobject1.put("lineCode", "");
                            jsonobject1.put("stayWayCode", "");
                            jsonobject1.put("lockedCarCode", "");
                            jsonobject1.put("srcCarNo", "");
                            jsonobject1.put("bagCageNo", s2);
                            jsonobject1.put("deptCode", MyApp.mDeptCode);
                            jsonobject1.put("operEmpCode", MyApp.mEmpCode);
                            jsonobject1.put("macNo", MyApp.mSession);
                            jsonobject1.put("upLoadEmpCode", MyApp.mEmpCode);
                            jsonobject1.put("acquisitionInfo", "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonarray.put(jsonobject1);
                        break;
                    } else {
                        mCursor.close();
                    }

                    if (!jsonarray.toString().equals("[]")) {
                        HttpUtils httpUtils = new HttpUtils();
                        RequestParams params = new RequestParams();
                        params.addBodyParameter("deptCode", MyApp.mDeptCode);
                        params.addBodyParameter("empCode", MyApp.mEmpCode);
                        params.addBodyParameter("devId", MyApp.mSession);
                        params.addBodyParameter("jsonData", jsonarray.toString());
                        httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.deliverSigninAction, params, new RequestCallBack<String>() {

                            @Override
                            public void onFailure(HttpException arg0, String arg1) {
                                MyUtils.showText(UploadActivity.this, "网络异常");
                                dialog.cancel();
                            }

                            @Override
                            public void onSuccess(ResponseInfo<String> arg0) {
                                String result = arg0.result;

                                dialog.cancel();

                            }
                        });
                    }
                    break;
                    // if (obj4 != null) goto _L4; else goto _L3
                    // _L3:
                    // obj3 = new JSONObject(((StringBuilder) (obj3)).toString());
                    // if (!((JSONObject) (obj3)).optBoolean("success")) goto _L6;
                    // else
                    // goto _L5
                    // _L5:
                    // obj3 = DeliveryDBWrapper.getInstance(getApplication());
                    // mCursor = ((DeliveryDBWrapper) (obj3)).rawQueryRank(0, 13);
                    // _L10:
                    // if (mCursor.moveToNext()) goto _L8; else goto _L7
                    // _L7:
                    // mCursor.close();
                    // addlist();
                    // mAdapter.notifyDataSetChanged();
                    // Toast.makeText(UploadActivity.this,
                    // "\u4E0A\u4F20\u6210\u529F",
                    // 0).show();
                    // _L12:
                    // ((BufferedReader) (obj2)).close();
                    // file.close();
                    // _L2:
                    // return;
                    // _L4:
                    // ((StringBuilder) (obj3)).append(((String) (obj4)));
                    // goto _L9
                    // exception1;
                    // obj4 = obj2;
                    // _L14:
                    // obj2 = obj4;
                    // obj3 = file;
                    // exception1.printStackTrace();
                    // try
                    // {
                    // ((BufferedReader) (obj4)).close();
                    // file.close();
                    // return;
                    // }
                    // // Misplaced declaration of an exception variable
                    // catch (File file)
                    // {
                    // file.printStackTrace();
                    // }
                    // return;
                    // _L8:
                    // ((DeliveryDBWrapper) (obj3)).rawUpdateRank(13, 1);
                    // goto _L10
                    // Exception exception;
                    // exception;
                    // obj3 = file;
                    // file = exception;
                    // _L13:
                    // try
                    // {
                    // ((BufferedReader) (obj2)).close();
                    // ((FileReader) (obj3)).close();
                    // }
                    // // Misplaced declaration of an exception variable
                    // catch (Object obj2)
                    // {
                    // ((Exception) (obj2)).printStackTrace();
                    // }
                    // throw file;
                    // _L6:
                    // if (((JSONObject) (obj3)).optBoolean("success")) goto _L12;
                    // else
                    // goto _L11
                    // _L11:
                    // Toast.makeText(UploadActivity.this, ((JSONObject)
                    // (obj3)).optString("error"), 0).show();
                    // goto _L12

                }
            }
        } while (true);
    }

    protected void onStart() {
        mUserName.setText((new StringBuilder(String.valueOf(MyApp.mEmpName))).append(MyApp.mDeptName).toString());
        super.onStart();
        BatteryReceiver.getInstant(this).addHeraer(this);
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

    private Dialog createLoginDialog() {
        ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setProgressStyle(0);
        progressdialog.setTitle("数据上传");
        progressdialog.setMessage("正在上传，请稍等...");
        progressdialog.setCancelable(false);
        return progressdialog;
    }

    class LstSyncDataAdapter extends BaseAdapter {

        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        public Object getItem(int i) {
            return mList.get(i);
        }

        public long getItemId(int i) {
            return (long) ((ItemData) mList.get(i)).hashCode();
        }

        public View getView(int i, View view, ViewGroup v) {
            ViewHolder viewgroup;
            if (view == null) {
                viewgroup = new ViewHolder();
                view = getLayoutInflater().inflate(R.layout.item_upload, null);
                viewgroup.checkBox = (CheckBox) view.findViewById(R.id.chb_upload);
                viewgroup.itemName = (TextView) view.findViewById(R.id.txt_upload_name);
                viewgroup.itemNumber = (TextView) view.findViewById(R.id.txt_upload_number);
                view.setTag(viewgroup);
            } else {
                viewgroup = (ViewHolder) view.getTag();
            }
            ((ViewHolder) (viewgroup)).checkBox.setChecked(((ItemData) mList.get(i)).checked);
            ((ViewHolder) (viewgroup)).itemName.setText(((ItemData) mList.get(i)).itemName);
            ((ViewHolder) (viewgroup)).itemNumber.setText((new StringBuilder("未上传：[")).append(((ItemData) mList.get(i)).itemNumber).append("]").toString());
            return view;
        }

    }

    class ViewHolder {
        CheckBox checkBox;
        TextView itemName;
        TextView itemNumber;
    }

    class ItemData {
        boolean checked;
        String itemName;
        int itemNumber;
    }
}
