package com.eruitong.activity.receive;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.eruitong.activity.BaseActivity;
import com.eruitong.bluetooth.StatusBox;
import com.eruitong.config.Conf;
import com.eruitong.db.AcceptInputDBWrapper;
import com.eruitong.db.BluetoothDBWrapper;
import com.eruitong.db.DepartmentDBWrapper;
import com.eruitong.db.DistanceTypeSpecDBWrapper;
import com.eruitong.db.DistrictDBWrapper;
import com.eruitong.db.DropinFeeDBWrapper;
import com.eruitong.db.ProdpriceDBWrapper;
import com.eruitong.db.ServiceProdPriceDBWrapper;
import com.eruitong.db.WaybillNoDBWrapper;
import com.eruitong.db.childNoListDBWrapper;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.model.InputDataInfo;
import com.eruitong.model.PriceInfo;
import com.eruitong.utils.AppManager;
import com.eruitong.print.bluetooth.BluetoothUtil;
import com.eruitong.utils.MyUtils;
import com.eruitong.print.bluetooth.BluetoothPrintTool;
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import zpSDK.zpSDK.zpSDK;

public class AuditbillActivity extends BaseActivity implements OnCheckedChangeListener, OnClickListener {

    /**
     * 标题
     **/
    @ViewInject(R.id.edt_accept_inputorder_include)
    TextView mTxtInclude;
    //查询
    @ViewInject(R.id.query_btn)
    Button mQueryWaybill;
    /**
     * 保存
     **/
    @ViewInject(R.id.btn_accept_inputorder_next)
    public Button mBtnNext;
    /**
     * 保存并打印
     **/
    @ViewInject(R.id.btn_accept_inputorder_savePrint)
    public Button mBtnSaveprint;
    /**
     * 代收
     **/
    @ViewInject(R.id.chb_accept_inputorder_chargeAgent)
    public CheckBox mChkChargeAgent;
    /**
     * 垫付
     **/
    @ViewInject(R.id.chb_accept_inputorder_debours)
    public CheckBox mChkDebours;
    /**
     * 垫付标签
     **/
    @ViewInject(R.id.chb_accept_inputorder_deboursFlag)
    public CheckBox mChkDeboursFlag;
    /**
     * 保价
     **/
    @ViewInject(R.id.chb_accept_inputorder_insurance)
    public CheckBox mChkInsurance;
    /**
     * 派送
     **/
    @ViewInject(R.id.chb_deliverFee)
    public CheckBox mCkbDeliver;
    /**
     * 签回单
     **/
    @ViewInject(R.id.chb_signBack)
    public CheckBox mCkbSignBack;
    /**
     * 身份证
     **/
    @ViewInject(R.id.chb_signBack_identity)
    public CheckBox mCkbSignBackIdentity;
    /**
     * 签单条
     **/
    @ViewInject(R.id.chb_signBack_receipt)
    public CheckBox mCkbSignBackReceipt;
    /**
     * 盖章
     **/
    @ViewInject(R.id.chb_signBack_seal)
    public CheckBox mCkbSignBackSeal;
    /**
     * 等通知
     **/
    @ViewInject(R.id.chb_waitNotifyFee)
    public CheckBox mCkbWaitNotifyFee;
    /**
     * 运单
     **/
    @ViewInject(R.id.edt_accept_inputorder_waybillNo)
    public EditText mEdtWaybillNo;
    /**
     * 运单-后
     **/
    @ViewInject(R.id.edt_accept_inputorder_consigneeEmpCode)
    public EditText mEdtConsigneeEmpCode;
    /**
     * 寄电
     **/
    @ViewInject(R.id.edt_accept_inputorder_consignorPhone)
    public EditText mEdtConsignorPhone;
    /**
     * 寄电后
     **/
    @ViewInject(R.id.edt_accept_inputorder_consignorContName)
    public EditText mEdtConsignorContName;
    /**
     * 寄址
     **/
    @ViewInject(R.id.edt_accept_inputorder_consignorAddr)
    public EditText mEdtConsignorAddr;
    /**
     * 收电
     **/
    @ViewInject(R.id.edt_accept_inputorder_addresseePhone)
    public EditText mEdtAddresseePhone;
    /**
     * 收电-后
     **/
    @ViewInject(R.id.edt_accept_inputorder_addresseeContName)
    public EditText mEdtAddresseeContName;
    /**
     * 收址
     **/
    @ViewInject(R.id.edt_accept_inputorder_addresseeAddr)
    public EditText mEdtAddresseeAddr;

    /**
     * 银行卡号
     **/
    @ViewInject(R.id.edt_accept_inputorder_bankNo)
    public EditText mEdtBankNo;
    /**
     * 代收费用
     **/
    @ViewInject(R.id.edt_accept_inputorder_chargeAgentFee)
    public EditText mEdtChargeAgentFee;
    /**
     * 垫付费用
     **/
    @ViewInject(R.id.edt_accept_inputorder_deboursFee)
    public EditText mEdtDeboursFee;
    /**
     * 派送费
     **/
    @ViewInject(R.id.edt_accept_inputorder_deliverFee)
    public EditText mEdtDeliverFee;
    /**
     * 代收
     **/
    @ViewInject(R.id.edt_accept_inputorder_goodsChargeFee)
    public EditText mEdtGoodsChargeFee;
    /**
     * 保价
     **/
    @ViewInject(R.id.edt_accept_inputorder_insuranceAmount)
    public EditText mEdtInsuranceAmount;
    /**
     * 保价费用
     **/
    @ViewInject(R.id.edt_accept_inputorder_insuranceFee)
    public EditText mEdtInsuranceFee;
    /**
     * 件数
     **/
    @ViewInject(R.id.edt_accept_inputorder_quantity)
    public EditText mEdtQuantity;
    /**
     * 重量
     **/
    @ViewInject(R.id.edt_accept_inputorder_realWeightQty)
    public EditText mEdtRealWeightQty;
    /**
     * 备注
     **/
    @ViewInject(R.id.edt_accept_inputorder_remark)
    public EditText mEdtRemark;
    /**
     * 返单份
     **/
    @ViewInject(R.id.edt_accept_inputorder_signBackCount)
    public EditText mEdtSignBackCount;
    /**
     * 回单号
     **/
    @ViewInject(R.id.edt_accept_inputorder_signBackNo)
    public EditText mEdtSignBackNo;
    /**
     * 返单张
     **/
    @ViewInject(R.id.edt_accept_inputorder_signBackSize)
    public EditText mEdtSignBackSize;
    /**
     * 签回单费
     **/
    @ViewInject(R.id.edt_accept_inputorder_signBackFee)
    public EditText mEdtSignBackfee;
    /**
     * 体积
     **/
    @ViewInject(R.id.edt_accept_inputorder_volume)
    public EditText mEdtVolume;
    /**
     * 体积-
     **/
    public EditText mEdtVolumeHeight;
    public EditText mEdtVolumeLong;
    public EditText mEdtVolumeWight;
    /**
     * 等通知 费
     **/
    @ViewInject(R.id.edt_accept_inputorder_waitNotifyFee)
    public EditText mEdtWaitNotifyFee;
    /**
     * 运费
     **/
    @ViewInject(R.id.edt_accept_inputorder_waybillFee)
    public EditText mEdtWaybillFee;
    /**
     * 上门费
     **/
    @ViewInject(R.id.edt_accept_inputorder_shangmenFee)
    public EditText mEdtFuelService;

    /**
     * 银行卡类型
     **/
    @ViewInject(R.id.spn_accept_inputorder_bankType)
    public Spinner mSpnBankType;
    /**
     * 货名
     **/
    @ViewInject(R.id.edt_accept_inputorder_consName)
    public EditText mEdtConsName;
    /**
     * 目的代码
     **/
    @ViewInject(R.id.edt_accept_inputorder_destZoneCode_code)
    public EditText mEdtDestZoneCode;
    /**
     * 目的名称
     **/
    @ViewInject(R.id.edt_accept_inputorder_destZoneCode_name)
    public EditText mEdtDestZoneName;
    /**
     * 付款 方式
     **/
    @ViewInject(R.id.spn_accept_inputorder_payMethod)
    public Spinner mSpnPayMethod;
    /**
     * 时期
     **/
    @ViewInject(R.id.spn_accept_inputorder_period)
    public Spinner mSpnPeriod;
    /**
     * 结算
     **/
    @ViewInject(R.id.spn_accept_inputorder_settlemethed)
    public Spinner mSpnSetterMethod;

    @ViewInject(R.id.item_edit_waybillNo)
    public EditText mWaybillNo;

    private AcceptInputDBWrapper acceptInputDB;
    private WaybillNoDBWrapper waybillNoDB;
    private childNoListDBWrapper childNoListDB;

    /**
     * 付款方式
     **/
    private String paymentTypeCode;
    private String productTypeCode;
    //是否审核
    private String auditedFlg;

    //之前货物名称
    private String beforeConsName = "";
    //之前发货人
    private String beforeAddresseeContName = "";
    //之前代收款
    private String beforeGoodsChargeFee = "";

    private String beforeRealWeightQty = "";

    private String beforewaybillFee = "";

    private String beforeInsuranceFee = "";

    private String beforePaymentTypeCode = "";


    private String versionNo;

    /**
     * 结算方式
     **/
    private String settlementTypeCode;

    public String biaoqianAddress;

    /**
     * 输入的运单号
     **/
    private String charsequence;

    //代收
    private String goodsChargeFeeChecked;
    // 声明价值;
    private String insuranceFeeChecked;
    // 签回单费
    private String signBackFeeChecked;
    // 等通知费
    private String waitNotifyFeeChecked;
    // 垫付费
    private String deboursFeeChecked;
    // 送货费
    private String deliverFeeChecked;

    private ItemData itemData;

    private InputDataInfo inputDataInfo;

    private Cursor cursor;


    //private Double commission = 0.0;

    //public Double deliverFeeCommissio;

    int TYPE_METHOD;
    int ADDRESSEE_TYPE;
    int CONSIGNOR_TYPE;

    private Context mcContext;
    public String[] PROVICE_CODES = new String[]{"029", "371"};
    private ServiceProdPriceDBWrapper serviceProdPriceDB;

    String addresseeAddr;
    String addresseeCompName;
    String addresseeContName;
    String addresseeDistName;
    String addresseeMobile;
    String addresseePhone;
    String addresseedeptCode;
    String bankNo;
    String bankType;
    String consName;
    String consignedTm;
    String consigneeEmpCode;
    String consignorAddr;
    String consignorCompName;
    String consignorContName;
    String consignorDistName;
    String provinceCode;
    String consignorMobile;
    String consignorPhone;
    String custCode;
    String deboursFee;
    String deboursFlag;
    String deliverFee;
    String destZoneCode;
    String destZoneName;
    String destdistrictCode;
    String discountExpressFee;
    String distanceTypeCode;
    String goodsChargeFee;
    String inFeeRate;
    String inputType;
    String inputerEmpCode;
    String insuranceAmount;
    String insuranceFee;
    String limitTypeCode;
    String lockFee;
    String meterageWeightQty;
    String orderNo;
    String outFeeRate;
    String outsiteName;
    String partitionName;
    PopupWindow popupWindow;
    ArrayList mCustomerSeleList;
    String realWeightQty;
    String seleDestCode;
    String signBackCount;
    String signBackFee;
    String signBackIdentity;
    String signBackNo;
    String signBackReceipt;
    String signBackSeal;
    String signBackSize;
    String sourceZoneCode;
    String sourcedistrictCode;
    StatusBox statusBox;
    String teamCode;
    Timer timer;
    String transferDays;
    View viewType;
    String waitNotifyFee;
    String chargeAgentFee;
    String waybillFee;
    ArrayList waybillList;
    String waybillNo;
    String Quantity;
    String Volume;
    String mDeptAddress;
    String mDestZoneAddress;
    String waybillRemk;
    String serviceTypeCode;
    String mSestdeptCode;
    String custIdentityCard;
    Double fuelServiceFee;

    /**
     * 总的子单号
     **/
    List childList;
    /**
     * 使用的子单号
     **/
    List childNoList;
    /**
     * 千回单号列表
     **/
    List signBackNoList;

    List PriceInfoList;

    private Dialog dialog;

    private int mPeriodSize;

    private long oldTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_auditbillorder);
        ViewUtils.inject(this);

        AppManager.getAppManager().addActivity(this);

        mcContext = AuditbillActivity.this;

        waybillList = new ArrayList();
        childList = new ArrayList();

        initData();

        initView();

        initAdapter();
    }

    private void initAdapter() {
        Cursor queryRank = DistrictDBWrapper.getInstance(getApplication()).rawQueryRank();

        ArrayList arraylist = new ArrayList();
        arraylist.add("");
        do {
            String s;
            String s1;
            String s2;
            do {
                if (!queryRank.moveToNext()) {
                    queryRank.close();
                    @SuppressWarnings({"unchecked", "rawtypes"}) ArrayAdapter arrayAdapter = new ArrayAdapter
                            (mcContext, android.R.layout.simple_spinner_item, (String[]) arraylist.toArray(new
                                    String[arraylist.size()]));
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    arrayAdapter = ArrayAdapter.createFromResource(mcContext, R.array.pay_method, android.R.layout
                            .simple_spinner_item);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpnPayMethod.setAdapter(arrayAdapter);
                    arrayAdapter = ArrayAdapter.createFromResource(mcContext, R.array.setter_method, android.R.layout
                            .simple_spinner_item);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpnSetterMethod.setAdapter(arrayAdapter);
                    arrayAdapter = ArrayAdapter.createFromResource(mcContext, R.array.cons_name, android.R.layout
                            .simple_spinner_item);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    arrayAdapter = ArrayAdapter.createFromResource(mcContext, R.array.bank_type, android.R.layout
                            .simple_spinner_item);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpnBankType.setAdapter(arrayAdapter);
                    arrayAdapter = ArrayAdapter.createFromResource(mcContext, R.array.period, android.R.layout
                            .simple_spinner_item);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpnPeriod.setSelection(mPeriodSize - 1);
                    mSpnPeriod.setAdapter(arrayAdapter);
                    return;
                }
                s = queryRank.getString(queryRank.getColumnIndex("typeCode"));
                s1 = queryRank.getString(queryRank.getColumnIndex("distCode"));
                s2 = queryRank.getString(queryRank.getColumnIndex("distName"));
            } while (!s.equals("3"));
            arraylist.add((new StringBuilder("[")).append(s1).append("]").append(s2).toString());
        } while (true);
    }

    private void initData() {
        acceptInputDB = AcceptInputDBWrapper.getInstance(getApplication());
        waybillNoDB = WaybillNoDBWrapper.getInstance(getApplication());
        childNoListDB = childNoListDBWrapper.getInstance(getApplication());
        SharedPreferences preferences = getSharedPreferences("cache_data", 0);
        consigneeEmpCode = preferences.getString("mStaffIdTxt", "");
        mPeriodSize = getResources().getStringArray(R.array.period).length;

        CONSIGNOR_TYPE = 1;
        ADDRESSEE_TYPE = 2;
        mDeptAddress = "";
        waybillNo = "";
        orderNo = "";
        sourceZoneCode = "";
        sourcedistrictCode = "";
        destZoneCode = "";
        destdistrictCode = "";
        custCode = "";
        consignorCompName = "";
        consignorAddr = "";
        consignorPhone = "";
        consignorContName = "";
        consignorMobile = "";
        addresseeCompName = "";
        addresseeAddr = "";
        addresseePhone = "";
        addresseeContName = "";
        addresseedeptCode = "";
        addresseeMobile = "";
        realWeightQty = "";
        meterageWeightQty = "";
        Quantity = "";
        Volume = "0";
        fuelServiceFee = 0.0;
        consignedTm = "";
        waybillRemk = "";
        consName = "";
        inputType = "";
        paymentTypeCode = "";
        settlementTypeCode = "";
        serviceTypeCode = "";
        waybillFee = "0.0";
        goodsChargeFee = "0";
        chargeAgentFee = "0";
        bankNo = " ";
        bankType = "";
        transferDays = "";
        insuranceAmount = "0";
        insuranceFee = "0";
        signBackNo = "";
        signBackCount = "0";
        signBackSize = "0";
        signBackReceipt = "0";
        signBackSeal = "0";
        signBackIdentity = "0";
        waitNotifyFee = "0";
        signBackFee = "0";
        deboursFee = "0";
        deboursFlag = "0";
        deliverFee = "0";
        productTypeCode = "";
        limitTypeCode = "";
        teamCode = "";
        discountExpressFee = "0";
        inputerEmpCode = "";
        distanceTypeCode = "";
        mDestZoneAddress = "";
        destZoneName = "";
        lockFee = "";
        outFeeRate = "";
        inFeeRate = "";
        seleDestCode = "";
        consignorDistName = "";
        addresseeDistName = "";
        mSestdeptCode = "";
        outsiteName = "";
        mCustomerSeleList = new ArrayList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isConnect();
        Cursor departmentCursor = DepartmentDBWrapper.getInstance(getApplication()).rawQueryRank(MyApp.mDeptCode);
        if (departmentCursor.moveToNext()) {
            mSestdeptCode = departmentCursor.getString(departmentCursor.getColumnIndex("districtCode"));
            mDeptAddress = departmentCursor.getString(departmentCursor.getColumnIndex("deptAddress"));
            sourcedistrictCode = departmentCursor.getString(departmentCursor.getColumnIndex("districtCode"));
            lockFee = departmentCursor.getString(departmentCursor.getColumnIndex("lockFee"));
            outFeeRate = departmentCursor.getString(departmentCursor.getColumnIndex("outFeeRate"));
            String s = departmentCursor.getString(departmentCursor.getColumnIndex("typeCode"));
            Cursor districtCursor = DistrictDBWrapper.getInstance(getApplication()).rawQueryRank(sourcedistrictCode);
            if (districtCursor.moveToNext()) {
                consignorDistName = districtCursor.getString(districtCursor.getColumnIndex("distName"));
                provinceCode = districtCursor.getString(districtCursor.getColumnIndex("provinceCode"));
            } else {
                districtCursor.close();
            }
        } else {
            departmentCursor.close();
        }

        Cursor districtCursor = DistrictDBWrapper.getInstance(getApplication()).rawQueryRank(sourcedistrictCode);
        if (districtCursor.moveToNext()) {
            consignorDistName = districtCursor.getString(districtCursor.getColumnIndex("distName"));
            provinceCode = districtCursor.getString(districtCursor.getColumnIndex("provinceCode"));
        } else {
            districtCursor.close();
        }

        serviceProdPriceDB = ServiceProdPriceDBWrapper.getInstance(getApplication());
        sourceZoneCode = MyApp.mDeptCode;

        int i = 0;
        // 获取回执单号
        Cursor signbackCursor = waybillNoDB.rawQueryRank("123");
        signBackNoList = new ArrayList();
        if (signbackCursor.moveToNext()) {
            String s3 = signbackCursor.getString(signbackCursor.getColumnIndex("waybillNos"));
            signBackNoList.add(s3);
        } else {
            signbackCursor.close();
        }
        i = 0;
        do {

            String s;
            if (i >= signBackNoList.size()) {
                isConnect();
                return;
            }
            signBackNo = (String) signBackNoList.get(0);
            i++;
        } while (true);

    }

    private void isConnect() {
        Cursor cursor = BluetoothDBWrapper.getInstance(getApplication()).rawQueryRank("No1");
        do {
            if (!cursor.moveToNext()) {
                cursor.close();
                return;
            }
            biaoqianAddress = cursor.getString(cursor.getColumnIndex("address"));
        } while (true);
    }

    private void initView() {
        mSpnPayMethod.setFocusable(false);
        mEdtChargeAgentFee.setFocusable(false);
        mEdtGoodsChargeFee.setFocusable(false);
        mSpnBankType.setFocusable(false);
        mSpnBankType.setClickable(false);
        mEdtBankNo.setFocusable(false);
        mSpnPeriod.setFocusable(false);
        mSpnPeriod.setClickable(false);
        mEdtDeboursFee.setFocusable(false);
        mChkDeboursFlag.setFocusable(false);
        mChkDeboursFlag.setClickable(false);
        mEdtInsuranceAmount.setFocusable(false);
        mEdtInsuranceFee.setFocusable(false);
        mEdtWaitNotifyFee.setFocusable(false);
        mEdtSignBackfee.setFocusable(false);
        mEdtSignBackNo.setFocusable(false);
        mEdtSignBackCount.setFocusable(false);
        mEdtSignBackSize.setFocusable(false);
        mEdtDeliverFee.setFocusable(false);
        mCkbSignBackReceipt.setFocusable(false);
        mCkbSignBackReceipt.setClickable(false);
        mCkbSignBackSeal.setFocusable(false);
        mCkbSignBackSeal.setClickable(false);
        mCkbSignBackIdentity.setFocusable(false);
        mCkbSignBackIdentity.setClickable(false);
        //mEdtDestZoneCode.setFocusable(false);
        //mEdtDestZoneName.setFocusable(false);
        // 查询
        mQueryWaybill.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                queryWaybill();
            }

        });

        mTxtInclude.setText(MyApp.mEmpName + MyApp.mDeptName);

        mEdtDestZoneCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Cursor departmentCursor = DepartmentDBWrapper.getInstance(getApplication()).rawQueryRank(String
                        .valueOf(mEdtDestZoneCode.getText()));
                if (departmentCursor.moveToNext()) {
                    destZoneName = departmentCursor.getString(departmentCursor.getColumnIndex("deptName"));
                    mEdtDestZoneName.setText(destZoneName);
                    destdistrictCode = departmentCursor.getString(departmentCursor.getColumnIndex("districtCode"));
                    inFeeRate = departmentCursor.getString(departmentCursor.getColumnIndex("inFeeRate"));
                    Double double1;

                    realWeightQty = mEdtRealWeightQty.getText().toString().trim();
                    String Volume = mEdtVolume.getText().toString().trim();
                    if ("".equals(Volume)) {
                        Volume = "0.0";
                    }
                    double1 = Double.valueOf(0.0D);

                    if (MyUtils.isEmpty(realWeightQty)) {
                        return;
                    }
                    // 没有重量，没有体积，运费是0
                    if (MyUtils.isEmpty(realWeightQty) && TextUtils.isEmpty(Volume)) {
                        mEdtWaybillFee.setText("0");
                        return;
                    }
                    // 没有重量，有体积
                    if ("0.0".equals(realWeightQty) && !TextUtils.isEmpty(Volume) && !"0.0".equals(Volume)) {
                        waybillFeeCount(Double.valueOf((Double.parseDouble(Volume) * 1000000D) / 4500D));
                        return;
                    }

                    double weight = Double.parseDouble(realWeightQty);
                    // 体积为空，有重量
                    if ("0.0".equals(Volume) && !"0.0".equals(realWeightQty)) {
                        waybillFeeCount(Double.valueOf(weight));
                        return;
                    }

                    Double VolumeFee;
                    if (!distCodeType(mSestdeptCode, destdistrictCode).equals("D4")) {
                        VolumeFee = Double.valueOf((Double.parseDouble(Volume) * 1000000D) / 4500D);
                    } else {
                        VolumeFee = Double.valueOf((Double.parseDouble(Volume) * 1000000D) / 6000D);
                    }

                    if (double1.doubleValue() <= VolumeFee.doubleValue()) {
                        waybillFeeCount(VolumeFee);
                        return;
                    }

                    if (double1.doubleValue() > VolumeFee.doubleValue()) {
                        waybillFeeCount(double1);
                        return;
                    }
                }
            }
        });

	/*	// 付款方式
        mSpnPayMethod.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView adapterview, View view, int i, long l) {
				if (i == 0) {
					paymentTypeCode = "2";
					if (productTypeCode.equals("B2") || productTypeCode.equals("B3")) {
						mSpnSetterMethod.setFocusable(false);
						mSpnSetterMethod.setClickable(false);
						mSpnSetterMethod.setSelection(0);
						mEdtRealWeightQty.setEnabled(true);
					}
				} else if (i == 1) {
					paymentTypeCode = "1";
					if (productTypeCode.equals("B1")) {
						mSpnSetterMethod.setFocusable(true);
						mSpnSetterMethod.setClickable(true);
						return;
					}
				}
				//priceFee();
			}

			public void onNothingSelected(AdapterView adapterview) {
			}
		});*/

        // 结算方式
        mSpnSetterMethod.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapterview, View view, int i, long l) {
                if (i == 0) {
                    settlementTypeCode = "1";
                } else if (i == 1) {
                    settlementTypeCode = "2";
                    return;
                }
            }

            public void onNothingSelected(AdapterView adapterview) {
            }
        });

	/*	mEdtConsignorPhone.setFocusableInTouchMode(true);
        mEdtConsignorPhone.requestFocus();
		// 寄方电话
		mEdtConsignorPhone.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

			public void onFocusChange(View view, boolean flag) {
				if (flag) {// 当焦点失去的时候
					return;
				}

				if (!MyUtils.isNetworkConnect(AuditbillActivity.this)) {
					Toast.makeText(AuditbillActivity.this, "网络未连接 ", 0).show();
					return;
				}

				String conPhone = mEdtConsignorPhone.getText().toString().trim();
				String phoneNo1 = "";
				if (conPhone.length() == 0) {
					return;
				} else {
					phoneNo1 = conPhone.substring(0, 1);

					if ("1".equals(phoneNo1) && conPhone.length() != 11) {
						Toast.makeText(AuditbillActivity.this, "请输入正确的手机号", 0).show();
						return;
					}
					if (conPhone.length() == 11) {
						consignorMobile = conPhone;
					} else {
						consignorPhone = conPhone;
					}
					HttpUtils httpUtils = new HttpUtils();
					String url = MyApp.mPathServerURL + Conf.queryCustomer;
					StringBuilder builder = new StringBuilder(url);
					builder.append("?").append("phoneNo=").append(conPhone);
					builder.append("&").append("deptCode=").append(MyApp.mDeptCode);

					httpUtils.send(HttpMethod.GET, builder.toString(), new RequestCallBack<String>() {

						@Override
						public void onStart() {
							super.onStart();
							dialog = DialogUtils.createUpdataDialog();
							dialog.show();
						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							Toast.makeText(mcContext, "网络未连接，请稍后再试", 0).show();
							if (dialog != null && dialog.isShowing()) {
								dialog.cancel();
							}
						}

						@SuppressWarnings("unchecked")
						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							String restult = arg0.result;
							I_QueryCustomer queryCustomer = new Gson().fromJson(restult, I_QueryCustomer.class);

							if (dialog != null && dialog.isShowing()) {
								dialog.cancel();
							}

							if (queryCustomer.isSuccess()) {

								//mChkChargeAgent.setChecked(false);
								List<CustList> custList = queryCustomer.getCustList();
								if (custList == null || custList.size() <= 0) {
									Toast.makeText(AuditbillActivity.this, "客户信息为空", 0).show();
									mEdtConsignorContName.setFocusableInTouchMode(true);
									mEdtConsignorContName.requestFocus();
									custCode = "";
									bankType = "-1";
									bankNo = "";
									return;
								}

								CustList cust = custList.getTypeCode(0);
								consignorContName = cust.getCustName();
								mEdtConsignorContName.setText(consignorContName);// 寄电姓名

								custCode = cust.getCustCode();
								if (custCode == null || custCode.equals("null") || custCode.isEmpty()) {
									custCode = "";
								}
								//mEdMonthlyNo.setText(custCode);// 订单-后

								bankType = cust.getBankType(); // 银行卡类型
								if (bankType == null || bankType.equals("null") || bankType.isEmpty()) {
									bankType = "-1";
								}

								bankNo = cust.getBankAccount();// 银行卡号
								if (bankNo == null || bankNo.equals("null") || bankNo.isEmpty()) {
									bankNo = "";
								}

								consignorCompName = cust.getCustCompany();

								List<AddressList> addressList = cust.getAddressList();
								if (addressList == null || addressList.size() <= 0) {
									mEdtAddresseePhone.requestFocus();
									return;
								}

								if (addressList.size() == 1) {
									AddressList address = addressList.getTypeCode(0);
									mEdtConsignorContName.setText(consignorContName);// 寄电姓名
									mEdtConsignorAddr.setText(address.getAddress() == null ? "" : address.getAddress
									());

									// 收电，获取焦点
									getPhoneRequestFocus();
								} else {
									mCustomerSeleList.clear();
									Map<String, String> map;

									AddressList addressList2;
									int max = 0;
									int size = addressList.size();
									if (size >= 6) {
										max = 6;
									} else {
										max = size;
									}

									for (int i = 0; i < max; i++) {
										map = new HashMap<String, String>();
										map.put("name", consignorContName);
										map.put("tel", cust.getCustMobile());
										String adr = addressList.getTypeCode(i).getAddress();
										map.put("address", adr);
										mCustomerSeleList.add(map);
									}
									initControls(1);
								}

							} else {
								Toast.makeText(mcContext, queryCustomer.getError(), 0).show();
							}
						}
					});

				}

			}
		});

		// 收电
		mEdtAddresseePhone.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {// 当焦点失去的时候
					return;
				}
				if (!MyUtils.isNetworkConnect(AuditbillActivity.this)) {
					Toast.makeText(AuditbillActivity.this, "网络未连接 ", 0).show();
					return;
				}

				String conPhone = mEdtAddresseePhone.getText().toString().trim();
				String phoneNo1 = "";
				if (conPhone.length() == 0) {
					return;
				} else {
					phoneNo1 = conPhone.substring(0, 1);

					if ("1".equals(phoneNo1) && conPhone.length() != 11) {
						Toast.makeText(AuditbillActivity.this, "请输入正确的手机号", 0).show();
						return;
					}

					if (conPhone.length() == 11) {
						addresseeMobile = conPhone;
					} else {
						addresseePhone = conPhone;
					}
					HttpUtils httpUtils = new HttpUtils();
					String url = MyApp.mPathServerURL + Conf.queryCustomer;
					StringBuilder builder = new StringBuilder(url);
					builder.append("?").append("phoneNo=").append(conPhone);
					// builder.append("&").append("deptCode=").append(MyApp.mDeptCode);

					httpUtils.send(HttpMethod.GET, builder.toString(), new RequestCallBack<String>() {

						@Override
						public void onStart() {
							super.onStart();
							dialog = DialogUtils.createUpdataDialog();
							dialog.show();
						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							Toast.makeText(mcContext, "网络未连接，请稍后再试", 0).show();
							if (dialog != null && dialog.isShowing()) {
								dialog.cancel();
							}
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							String restult = arg0.result;
							I_QueryCustomer queryCustomer = new Gson().fromJson(restult, I_QueryCustomer.class);
							if (dialog != null && dialog.isShowing()) {
								dialog.cancel();
							}
							if (queryCustomer.isSuccess()) {

								List<CustList> custList = queryCustomer.getCustList();
								if (custList == null || custList.size() <= 0) {
									Toast.makeText(AuditbillActivity.this, "客户信息为空", 0).show();
									mEdtAddresseeContName.requestFocus();
									return;
								}

								CustList cust = custList.getTypeCode(0);
								addresseeContName = cust.getCustName();
								mEdtAddresseeContName.setText(addresseeContName);// 收电姓名

								if (!MyUtils.isEmpty(cust.getCustPhone())) {
									addresseePhone = cust.getCustPhone();// 收电
								}

								if (!MyUtils.isEmpty(cust.getCustMobile())) {
									addresseeMobile = cust.getCustMobile();
								}

								addresseeCompName = cust.getCustCompany();// 收件人公司名称

								List<AddressList> addressList = cust.getAddressList();
								if (addressList == null || addressList.size() <= 0) {
									mEdtQuantity.setFocusableInTouchMode(true);
									mEdtQuantity.requestFocus();
									return;
								}

								if (addressList.size() == 1) {
									AddressList address = addressList.getTypeCode(0);
									mEdtAddresseeContName.setText(cust.getCustName());// 收电姓名
									addresseeAddr = address.getAddress();
									mEdtAddresseeAddr.setText(addresseeAddr == null ? "" : addresseeAddr);

									addresseedeptCode = address.getDeptCode();
									teamCode = address.getTeamCode();
									mEdtQuantity.setFocusableInTouchMode(true);
									mEdtQuantity.requestFocus();
								} else {
									Map<String, String> map;
									mCustomerSeleList.clear();

									AddressList addressList2;
									int max = 0;
									int size = addressList.size();
									if (size >= 6) {
										max = 6;
									} else {
										max = size;
									}

									for (int i = 0; i < max; i++) {
										addressList2 = addressList.getTypeCode(i);
										map = new HashMap<String, String>();
										map.put("name", cust.getCustName());
										map.put("tel", cust.getCustMobile());
										map.put("address", addressList2.getAddress());
										map.put("teamCode", addressList2.getTeamCode());
										map.put("deptCode", addressList2.getDeptCode());
										mCustomerSeleList.add(map);
										addressList2 = null;
									}

									initControls(2);
								}

							} else {
								Toast.makeText(mcContext, queryCustomer.getError(), 0).show();
							}
						}
					});

				}

			}
		});*/

		/*
		// 件数
		mEdtQuantity.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String quantity = mEdtQuantity.getText().toString().trim();
				childNoList = new ArrayList();
				int i;
				if (!quantity.isEmpty()) {
					i = Integer.parseInt(quantity);
				} else {
					i = 0;
				}

				// 如果小于一件，就使用主单号。否则继续
				if (i < 2) {
					return;  Loop/switch isn't completed 
				}

				int j = 0;
				do {
					// 如果当前的大于数据库所有的子单号
					if ( j >= childList.size()) {
						return;
					}
					System.out.println(j);
					// 取得的，等于件数的时候就返回
					if (i - 1 <= j) {
						return;
					}

					childNoList.add(childList.getTypeCode(j));

					j++;
				} while (true);

			}
		});
		*/

        // 重量
        mEdtRealWeightQty.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Double double1;

                realWeightQty = mEdtRealWeightQty.getText().toString().trim();
                String Volume = mEdtVolume.getText().toString().trim();
                double1 = Double.valueOf(0.0D);

                if (MyUtils.isEmpty(realWeightQty)) {
                    return;
                }
                // 没有重量，没有体积，运费是0
                if (MyUtils.isEmpty(realWeightQty) && TextUtils.isEmpty(Volume)) {
                    mEdtWaybillFee.setText("0");
                    return;
                }
                // 没有重量，有体积
                if (MyUtils.isEmpty(realWeightQty) && !TextUtils.isEmpty(Volume)) {
                    waybillFeeCount(Double.valueOf((Double.parseDouble(Volume) * 1000000D) / 4500D));
                    return;
                }

                double weight = Double.parseDouble(realWeightQty);
                // 体积为空，有重量
                if (MyUtils.isEmpty(Volume) || "0.0".equals(Volume)) {
                    waybillFeeCount(Double.valueOf(weight));
                    return;
                }

                Double VolumeFee;
                if (!distCodeType(mSestdeptCode, seleDestCode).equals("D4")) {
                    VolumeFee = Double.valueOf((Double.parseDouble(Volume) * 1000000D) / 4500D);
                } else {
                    VolumeFee = Double.valueOf((Double.parseDouble(Volume) * 1000000D) / 6000D);
                }

                if (double1.doubleValue() <= VolumeFee.doubleValue()) {
                    waybillFeeCount(VolumeFee);
                    return;
                }

                if (double1.doubleValue() > VolumeFee.doubleValue()) {
                    waybillFeeCount(double1);
                    return;
                }
            }
        });

        // 体积
        mEdtVolume.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Volume = mEdtVolume.getText().toString().trim();
                realWeightQty = mEdtRealWeightQty.getText().toString().trim();

                if (MyUtils.isEmpty(Volume)) {
                    return;
                }

                if (MyUtils.isEmpty(Volume) && realWeightQty.isEmpty()) {
                    mEdtWaybillFee.setText("0");
                    return;
                }

                // 没有体积，有重量
                if (MyUtils.isEmpty(Volume) && !realWeightQty.isEmpty()) {
                    double d = Double.parseDouble(realWeightQty);
                    waybillFeeCount(Double.valueOf(d));
                    return;
                }

                if (Volume.startsWith(".")) {
                    Volume = "0" + Volume;
                }

                Double volume;
                if (!distCodeType(mSestdeptCode, seleDestCode).equals("D4")) {
                    volume = Double.valueOf((Double.parseDouble(Volume) * 1000000D) / 4500D);
                } else {
                    volume = Double.valueOf((Double.parseDouble(Volume) * 1000000D) / 6000D);
                }

                if (MyUtils.isEmpty(realWeightQty)) {
                    waybillFeeCount(volume);
                    return;
                }

                Double double1;
                double1 = Double.valueOf(Double.parseDouble(realWeightQty));
                if (volume.doubleValue() >= double1.doubleValue()) {
                    waybillFeeCount(volume);
                } else {
                    waybillFeeCount(double1);
                }

            }
        });

        // 下部的开关
        mChkChargeAgent.setOnCheckedChangeListener(this);// 代收开关
        mChkDebours.setOnCheckedChangeListener(this);// 垫付
        mChkDeboursFlag.setOnCheckedChangeListener(this);// 是否垫结
        mChkInsurance.setOnCheckedChangeListener(this);// 保价
        mCkbDeliver.setOnCheckedChangeListener(this);// 派送
        mCkbWaitNotifyFee.setOnCheckedChangeListener(this);// 等通知
        mCkbSignBack.setOnCheckedChangeListener(this);// 签回单
        mCkbSignBackReceipt.setOnCheckedChangeListener(this);// 签单条
        mCkbSignBackSeal.setOnCheckedChangeListener(this);// 盖章
        mCkbSignBackIdentity.setOnCheckedChangeListener(this);// 身份证

        // 代收 -输入
        mEdtGoodsChargeFee.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                DaishouPrice();
            }

        });

        // 银行卡类型
        mSpnBankType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String adapterview = mSpnBankType.getSelectedItem().toString();
                if (adapterview.equals("无卡号") && i == 0) {
                    bankType = "-1";
                } else {
                    if (adapterview.equals("工商银行") && i == 1) {
                        bankType = "0";
                        return;
                    }
                    if (adapterview.equals("浦发银行") && i == 2) {
                        bankType = "1";
                        return;
                    }
                    if (adapterview.equals("招商银行") && i == 3) {
                        bankType = "5";
                        return;
                    }
                    if (adapterview.equals("建设银行") && i == 4) {
                        bankType = "3";
                        return;
                    }
                }
            }

            public void onNothingSelected(AdapterView adapterview) {
            }
        });

        // 时期
        mSpnPeriod.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapterview, View view, int i, long l) {
                DaishouPrice();// 计算代收价格
            }

            public void onNothingSelected(AdapterView adapterview) {
            }

        });

        // 保价
        mEdtInsuranceAmount.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable editable) {
                String s = mEdtInsuranceAmount.getText().toString();
                String charsequence;

                if (MyUtils.isEmpty(s)) {
                    charsequence = "0";
                } else {
                    charsequence = s;

                }

                if (productTypeCode.equals("B1")) {
                    double d = Double.parseDouble(charsequence);
                    double fee = Double.valueOf(d * 0.001D).doubleValue();
                    mEdtInsuranceFee.setText((MyUtils.ceil(fee)));
                    return;
                }

                if (productTypeCode.equals("B2") || productTypeCode.equals("B3")) {
                    double d1 = Double.parseDouble(charsequence);
                    double fee = Double.valueOf(d1 * 0.001D);
                    mEdtInsuranceFee.setText(MyUtils.ceil(fee));
                    return;
                }

            }

            public void beforeTextChanged(CharSequence charsequence, int i, int j, int k) {
            }

            public void onTextChanged(CharSequence charsequence, int i, int j, int k) {

            }
        });
        // 派送费
        mEdtDeliverFee.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charsequence, int i, int j, int k) {
            }

            public void onTextChanged(CharSequence charsequence, int i, int j, int k) {
                String deliverFee = mEdtDeliverFee.getText().toString();
                if (MyUtils.isEmpty(deliverFee)) {
                    return;
                }

                if (Double.valueOf(Double.parseDouble(deliverFee)).doubleValue() >= 300D) {
                    MyUtils.showText(mcContext, "请输入 小于等于300");
                    return;
                }
            }
        });

        // 四个按钮
        //mBtnClear.setOnClickListener(this);
        //mBtnRemark1.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
        mBtnSaveprint.setOnClickListener(this);

    }

	/*private void setExpressText(String destDeptCode){
			if( !Arrays.asList(PROVICE_CODES).contains(sourcedistrictCode)
				&& !Arrays.asList(PROVICE_CODES).contains(destDeptCode)){
				mEdtWaybillFee.setFocusable(true);
				mEdtWaybillFee.setFocusableInTouchMode(true);
			}else{
				mEdtWaybillFee.setFocusable(false);
			}
	}
*/
    /**
     * 计算运费
     *//*
	private void priceFee() {
		volume = mEdtVolume.getText().toString().trim();
		realWeightQty = mEdtRealWeightQty.getText().toString();

		if (MyUtils.isEmpty(volume)) {
			// 体积是空的
			if (MyUtils.isEmpty(realWeightQty)) {
				// 重量是空
				mEdtWaybillFee.setText("0");
			} else {
				// 重量不是空
				double d = Double.parseDouble(realWeightQty);
				waybillFeeCount(Double.valueOf(d));
			}
		} else {
			// 体积不是空
			Double volume;
			if (!distCodeType(mSestdeptCode, seleDestCode).equals("D4")) {
				volume = Double.valueOf((Double.parseDouble(volume) * 1000000D) / 4500D);
			} else {
				volume = Double.valueOf((Double.parseDouble(volume) * 1000000D) / 6000D);
			}
			if (MyUtils.isEmpty(realWeightQty)) {
				// 重量是空
				waybillFeeCount(volume);
			} else {
				// 重量不是空
				Double double1;
				double1 = Double.valueOf(Double.parseDouble(realWeightQty));
				if (volume.doubleValue() >= double1.doubleValue()) {
					waybillFeeCount(volume);
				} else {
					waybillFeeCount(double1);
				}
			}

		}

	}
*/

    /**
     * 代收价格 的计算
     **/
    private void DaishouPrice() {
        DecimalFormat decimalformat = new DecimalFormat("0.00");

        Double chargeFee = 0.0D;
        String goodsChargeFee = mEdtGoodsChargeFee.getText().toString();
        String charsequence = goodsChargeFee;
        if (MyUtils.isEmpty(goodsChargeFee)) {
            charsequence = "0";
        }
        String period = mSpnPeriod.getSelectedItem().toString().replaceAll("[^0-9]", "");

	/*	boolean isHeNan = true;
		if (!"Henansheng".equals(provinceCode) || !"1".equals(period)) {
			if ("Henansheng".equals(provinceCode)) {
				chargeFee = Double.valueOf(Double.parseDouble(charsequence) / 1000);
				isHeNan = true;
			} else {
				isHeNan = false;
			}
		} else {
			double d = (Double.parseDouble(charsequence) * 2) / 1000;
			chargeFee = Double.valueOf(d);
			isHeNan = true;
		}*/

        //	if (!isHeNan) {
        int i = 0;
        Cursor priceCursor = serviceProdPriceDB.rawQueryRank("SF04", period);

        do {
            if (priceCursor.moveToNext()) {
                i = priceCursor.getInt(priceCursor.getColumnIndex("attrId"));
            } else {
                priceCursor.close();
                break;
            }
        } while (true);

        String propValue = "0";
        Cursor serviceCursor = serviceProdPriceDB.rawQueryRank("SF04", i + 100);

        do {
            if (!serviceCursor.moveToNext()) {
                serviceCursor.close();
                chargeFee = Double.valueOf(Double.parseDouble(charsequence) * Double.parseDouble(propValue));
                break;
            } else {
                propValue = serviceCursor.getString(serviceCursor.getColumnIndex("propValue"));
            }
        } while (true);
        //	}

        if (chargeFee.doubleValue() > 100D) {
            mEdtChargeAgentFee.setText("100");
            return;
        } else {
            mEdtChargeAgentFee.setText(Math.ceil(chargeFee) + "");
            return;
        }
    }

    private void waybillFeeCount(Double double1) {
        DecimalFormat decimalformat = new DecimalFormat("0.00");
        if (MyUtils.isEmpty(outFeeRate)) {
            outFeeRate = "0";
        }
        if (MyUtils.isEmpty(inFeeRate)) {
            inFeeRate = "0";
        }
        meterageWeightQty = (new StringBuilder(String.valueOf(decimalformat.format(double1)))).toString();

        // 根据重量计算上门费
        if (productTypeCode.equals("B3")) {
            fuelServiceFee = getShangmenFee(meterageWeightQty);
            mEdtFuelService.setText(String.valueOf(fuelServiceFee));
        } else {
            fuelServiceFee = 0.0;
            mEdtFuelService.setText(null);
        }

        double1 = Double.valueOf(Double.parseDouble(meterageWeightQty));
        PriceInfoList = new ArrayList();
        String sestdeptCode = mSestdeptCode;
        String seleDest = seleDestCode;
        distCodeType(sestdeptCode, seleDest);
        String productCode = "";
        if ("B2".equals(productTypeCode) || "B3".equals(productTypeCode)) {
            productCode = "B2";
            limitTypeCode = "T5";
        } else {
            limitTypeCode = "T4";
        }
        Cursor prodpriceCursor = ProdpriceDBWrapper.getInstance(getApplication()).rawQueryRank(sestdeptCode,
                destdistrictCode, serviceTypeCode, "C3", limitTypeCode, "B2");
        int i;
        i = 0;
        do {
            if (prodpriceCursor.moveToNext()) {
                PriceInfo itemdata = new PriceInfo();
                itemdata.startWeightQty = prodpriceCursor.getString(prodpriceCursor.getColumnIndex("startWeightQty"));
                itemdata.maxWeightQty = prodpriceCursor.getString(prodpriceCursor.getColumnIndex("maxWeightQty"));
                itemdata.basePrice = prodpriceCursor.getString(prodpriceCursor.getColumnIndex("basePrice"));
                itemdata.baseWeightQty = prodpriceCursor.getString(prodpriceCursor.getColumnIndex("baseWeightQty"));
                itemdata.weightPriceQty = Double.valueOf(prodpriceCursor.getDouble(prodpriceCursor.getColumnIndex
                        ("weightPriceQty")));
                PriceInfoList.add(itemdata);
            } else {

                if (!prodpriceCursor.isClosed()) {
                    prodpriceCursor.close();
                }

                if (i >= PriceInfoList.size()) {
                    return;
                }

                PriceInfo item = (PriceInfo) PriceInfoList.get(i);

                if (double1.doubleValue() > Double.parseDouble(item.startWeightQty) && double1.doubleValue() < Double
                        .parseDouble(item.maxWeightQty)) {
                    double d = Double.parseDouble(((PriceInfo) PriceInfoList.get(i)).basePrice);
                    double d2 = double1.doubleValue();
                    double d4 = Double.parseDouble(((PriceInfo) PriceInfoList.get(i)).startWeightQty);
                    double d5 = ((PriceInfo) PriceInfoList.get(i)).weightPriceQty.doubleValue();
                    double d6 = Math.min(Double.parseDouble(outFeeRate), Double.parseDouble(inFeeRate));

                    mEdtWaybillFee.setText((new BigDecimal(decimalformat.format(Double.valueOf((d + (d2 - d4) * d5) *
                            d6)))).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
                    return;
                }

                if (Double.parseDouble(item.startWeightQty) >= double1.doubleValue() && double1.doubleValue() > 0.0D) {
                    double d1 = Double.parseDouble(((PriceInfo) PriceInfoList.get(i)).basePrice);
                    double d3 = Math.min(Double.parseDouble(outFeeRate), Double.parseDouble(inFeeRate));

                    mEdtWaybillFee.setText((new BigDecimal(decimalformat.format(Double.valueOf(d1 * d3)))).setScale
                            (0, BigDecimal.ROUND_HALF_UP).toString());
                    return;
                }
                i++;
            }
        } while (true);

    }

    private String distCodeType(String s, String s1) {
        Cursor queryRank = DistanceTypeSpecDBWrapper.getInstance(getApplication()).rawQueryRank(s, s1);
        do {
            if (!queryRank.moveToNext()) {
                queryRank.close();
                return distanceTypeCode;
            }
            distanceTypeCode = queryRank.getString(queryRank.getColumnIndex("distancetypecode"));
        } while (true);
    }

	/*private void initControls(final int phoneType) {
		try {
			View view = getLayoutInflater().inflate(R.layout.popupwindow_list, null);
			@SuppressWarnings("unchecked")
			SimpleAdapter simpleadapter = new SimpleAdapter(this, mCustomerSeleList, R.layout.popupwindow_item,
					new String[] { "name", "tel", "address" }, new int[] { R.id.item_pop_name, R.id.item_pop_phone,
							R.id.item_pop_adress });
			ListView listview = (ListView) view.findViewById(R.id.listview);
			listview.setAdapter(simpleadapter);
			listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

				public void onItemClick(AdapterView adapterview, View view1, int i, long l) {

					Map map = (Map) mCustomerSeleList.getTypeCode(i);
					switch (phoneType) {
					case 1: {// 寄电
						consignorAddr = (String) map.getTypeCode("address");
						mEdtConsignorAddr.setText(consignorAddr == null ? "" : consignorAddr);

						consignorContName = (String) map.getTypeCode("name");
						mEdtConsignorContName.setText(consignorContName);// 寄电姓名

						// 收电，获取焦点
						getPhoneRequestFocus();
						break;
					}
					case 2: {// 收电
						addresseeAddr = (String) map.getTypeCode("address");
						mEdtAddresseeAddr.setText(addresseeAddr);

						addresseeContName = (String) map.getTypeCode("name");
						mEdtAddresseeContName.setText(addresseeContName);

						String deptCode = (String) map.getTypeCode("deptCode");
						teamCode = (String) map.getTypeCode("teamCode");
						if (teamCode == null) {
							teamCode = "";
						}

						mEdtQuantity.setFocusableInTouchMode(true);
						mEdtQuantity.requestFocus();

						break;
					}
					}
					if (popupWindow != null && popupWindow.isShowing()) {
						popupWindow.dismiss();
						return;
					}
				}

			});
			popupWindow = new PopupWindow(view, -2, -2);
			popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));
			popupWindow.setOutsideTouchable(true);
			popupWindow.update();
			popupWindow.setTouchable(true);
			popupWindow.setFocusable(true);
			popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		} catch (Exception e) {
			CrashReport.postCatchedException(e);
		}
	}

	*//**
     * 收电，获取焦点
     *//*
	private void getPhoneRequestFocus() {
		mEdtAddresseePhone.setFocusableInTouchMode(true);
		mEdtAddresseePhone.requestFocus();
	}

	*/

    /**
     * 计算上门费
     */
    private Double getShangmenFee(String w) {
        double shangmenfei = 0;
        Double weight = Double.parseDouble(w);
        DropinFeeDBWrapper dbWrapper = DropinFeeDBWrapper.getInstance(getApplication());
        Cursor queryRank = dbWrapper.rawQueryRank(sourcedistrictCode);

        String dropinFeeId = "";
        Double baseWeightQty = 0.0;
        Double baseDropInFee = 0.0;
        Double weightDropInFeeQty = 0.0;
        String priceRoundTypeCode = "";
        String weightRoundTypeCode = "";

        if (queryRank.moveToNext()) {
            dropinFeeId = queryRank.getString(queryRank.getColumnIndex("dropinFeeId"));
            baseWeightQty = queryRank.getDouble(queryRank.getColumnIndex("baseWeightQty"));
            baseDropInFee = queryRank.getDouble(queryRank.getColumnIndex("baseDropInFee"));
            weightDropInFeeQty = queryRank.getDouble(queryRank.getColumnIndex("weightDropInFeeQty"));
            priceRoundTypeCode = queryRank.getString(queryRank.getColumnIndex("priceRoundTypeCode"));
            weightRoundTypeCode = queryRank.getString(queryRank.getColumnIndex("weightRoundTypeCode"));
        }
        queryRank.close();

        if (baseWeightQty == 0.0) {
            return 0.0;
        }

        if (weight <= baseWeightQty) {
            shangmenfei = baseDropInFee;
        } else {
            shangmenfei = baseDropInFee + (weight - baseWeightQty) * weightDropInFeeQty;
        }

        if ("2".equals(priceRoundTypeCode)) {// 四舍五入
            shangmenfei = Math.round(shangmenfei);
        } else if ("1".endsWith(priceRoundTypeCode)) {// 进位取整
            shangmenfei = Math.ceil(shangmenfei);
        } else if ("3".equals(priceRoundTypeCode)) {// 舍掉小数
            shangmenfei = Math.floor(shangmenfei);
        }

        return shangmenfei;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int i = buttonView.getId();
        switch (i) {
            case R.id.chb_accept_inputorder_chargeAgent: {// 代收
                if (isChecked) {
                    mEdtChargeAgentFee.setFocusable(true);
                    mEdtGoodsChargeFee.setFocusable(true);
                    mEdtGoodsChargeFee.setFocusableInTouchMode(true);
                    mEdtGoodsChargeFee.requestFocus();
                    mSpnBankType.setFocusable(false);
                    mSpnBankType.setClickable(false);
                    mEdtBankNo.setFocusable(true);
                    mEdtBankNo.setFocusableInTouchMode(true);
                    mSpnPeriod.setFocusable(false);
                    mSpnPeriod.setClickable(false);
                    mSpnPeriod.setSelection(mPeriodSize - 1, true);
                    goodsChargeFeeChecked = "1";
                    if (!"0".equals(bankType)) {
                        if ("1".equals(bankType)) {
                            mSpnBankType.setSelection(2, true);
                        } else if ("5".equals(bankType)) {
                            mSpnBankType.setSelection(3, true);
                        } else if ("3".equals(bankType)) {
                            mSpnBankType.setSelection(4, true);
                        } else if ("-1".equals(bankType)) {
                            mSpnBankType.setSelection(0, true);
                        }
                    } else {
                        mSpnBankType.setSelection(1, true);
                    }
                    mEdtBankNo.setText(bankNo);
                    return;
                } /*else {
				if(mEdtGoodsChargeFee.getText()!=null&&!"0".equals(mEdtGoodsChargeFee.getText())){
					Toast.makeText(AuditbillActivity.this, "代收不能取消，请修改代收金额为0", 0).show();
				}*/ else {
                    mSpnBankType.setSelection(0, true);
                    mEdtChargeAgentFee.setFocusable(false);
                    mEdtGoodsChargeFee.setFocusable(false);
                    mSpnBankType.setFocusable(false);
                    mSpnBankType.setClickable(false);
                    mEdtBankNo.setFocusable(false);
                    mSpnPeriod.setFocusable(false);
                    mSpnPeriod.setClickable(false);
                    mEdtChargeAgentFee.setText("");
                    mEdtGoodsChargeFee.setText("");
                    mEdtBankNo.getEditableText().clear();
                    mEdtRemark.requestFocus();
                    goodsChargeFeeChecked = "0";
                }
                return;
            }
            //}
            case R.id.chb_accept_inputorder_debours: {// 垫付
                if (isChecked) {
                    mChkDebours.setFocusable(true);
                    mEdtDeboursFee.setFocusable(true);
                    mEdtDeboursFee.setFocusableInTouchMode(true);
                    mEdtDeboursFee.requestFocus();
                    mChkDeboursFlag.setFocusable(true);
                    mChkDeboursFlag.setClickable(true);
                    deboursFeeChecked = "1";
                    return;
                } else {
                    mEdtDeboursFee.setFocusable(false);
                    mChkDeboursFlag.setFocusable(false);
                    mChkDeboursFlag.setClickable(false);
                    mChkDeboursFlag.setChecked(false);
                    mEdtDeboursFee.getEditableText().clear();
                    deboursFlag = "0";
                    mEdtRemark.requestFocus();
                    deboursFeeChecked = "0";
                    return;
                }
            }
            case R.id.chb_accept_inputorder_deboursFlag: {// 是否垫结
                if (isChecked) {
                    deboursFlag = "1";
                    return;
                } else {
                    deboursFlag = "0";
                    return;
                }
            }
            case R.id.chb_accept_inputorder_insurance: {// 保价
                if (isChecked) {
                    mEdtInsuranceAmount.setFocusable(true);
                    mEdtInsuranceAmount.setFocusableInTouchMode(true);
                    mEdtInsuranceAmount.requestFocus();
                    mEdtInsuranceFee.setFocusable(true);
                    insuranceFeeChecked = "1";
                    if (null != mEdtInsuranceAmount.getText() && !"".equals(mEdtInsuranceAmount.getText())) {
                        return;
                    }
                    if (mChkChargeAgent.isChecked()) {
                        String s = mEdtGoodsChargeFee.getText().toString();
                        String compoundbutton = s;
                        if (s.isEmpty()) {
                            compoundbutton = "0";
                        }
                        if (Integer.parseInt(compoundbutton) < 1000) {
                            mEdtInsuranceAmount.setText("1000");
                            return;
                        } else {
                            mEdtInsuranceAmount.setText(compoundbutton);
                            return;
                        }
                    } else {
                        mEdtInsuranceAmount.setText("");
                        return;
                    }
                } else {
                    mEdtInsuranceAmount.setFocusable(false);
                    mEdtInsuranceFee.setFocusable(false);
                    mEdtInsuranceAmount.getEditableText().clear();
                    mEdtInsuranceFee.getEditableText().clear();
                    mEdtRemark.requestFocus();
                    insuranceFeeChecked = "0";
                }
                break;
            }
            case R.id.chb_deliverFee: {// 派送
                if (isChecked) {
                    mEdtDeliverFee.setFocusable(true);
                    mEdtDeliverFee.setFocusableInTouchMode(true);
                    mEdtDeliverFee.requestFocus();
                    deliverFeeChecked = "1";
                } else {
                    mEdtDeliverFee.setFocusable(false);
                    mEdtDeliverFee.getEditableText().clear();
                    mEdtRemark.requestFocus();
                    deliverFeeChecked = "0";
                    return;
                }
                break;
            }
            case R.id.chb_waitNotifyFee: {// 等通知
                if (isChecked) {
                    mEdtWaitNotifyFee.setText("10");
                    waitNotifyFeeChecked = "1";
                    return;
                } else {
                    mEdtWaitNotifyFee.getEditableText().clear();
                    mEdtRemark.requestFocus();
                    waitNotifyFeeChecked = "0";
                    return;
                }
            }
            case R.id.chb_signBack: {// 签回单
                if (isChecked) {
                    mEdtSignBackNo.setFocusable(true);
                    mEdtSignBackCount.setFocusable(true);
                    mEdtSignBackCount.setFocusableInTouchMode(true);
                    mEdtSignBackCount.requestFocus();
                    mEdtSignBackSize.setFocusable(true);
                    mEdtSignBackSize.setFocusableInTouchMode(true);
                    mCkbSignBackReceipt.setFocusable(true);
                    mCkbSignBackReceipt.setClickable(true);
                    mCkbSignBackSeal.setFocusable(true);
                    mCkbSignBackSeal.setClickable(true);
                    mCkbSignBackIdentity.setFocusable(true);
                    mCkbSignBackIdentity.setClickable(true);
                    mEdtSignBackfee.setText("5");
                    mEdtSignBackNo.setText(signBackNo);
                    waybillNoDB.deleteRank(signBackNo);
                    signBackFeeChecked = "1";
                    return;
                } else {
                    mEdtSignBackNo.setFocusable(false);
                    mEdtSignBackCount.setFocusable(false);
                    mEdtSignBackSize.setFocusable(false);
                    mEdtSignBackCount.getEditableText().clear();
                    mEdtSignBackSize.getEditableText().clear();

                    mCkbSignBackReceipt.setFocusable(false);
                    mCkbSignBackReceipt.setClickable(false);
                    mCkbSignBackReceipt.setChecked(false);
                    signBackReceipt = "0";
                    mCkbSignBackSeal.setFocusable(false);
                    mCkbSignBackSeal.setClickable(false);
                    mCkbSignBackSeal.setChecked(false);
                    signBackSeal = "0";
                    mCkbSignBackIdentity.setFocusable(false);
                    mCkbSignBackIdentity.setClickable(false);
                    mCkbSignBackIdentity.setChecked(false);
                    signBackIdentity = "0";
                    mEdtSignBackNo.getEditableText().clear();
                    mEdtSignBackfee.getEditableText().clear();
                    mEdtRemark.requestFocus();
                    signBackFeeChecked = "0";
                    return;
                }
            }
            case R.id.chb_signBack_receipt: {// 签单条
                if (isChecked) {
                    signBackReceipt = "1";
                    return;
                } else {
                    signBackReceipt = "0";
                    return;
                }
            }
            case R.id.chb_signBack_seal: {// 盖章
                if (isChecked) {
                    signBackSeal = "1";
                    return;
                } else {
                    signBackSeal = "0";
                    return;
                }
            }
            case R.id.chb_signBack_identity: {// 身份证
                if (isChecked) {
                    signBackIdentity = "1";
                    return;
                } else {
                    signBackIdentity = "0";
                    return;

                }
            }

            default:
                break;
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_accept_inputorder_next: {// 保存

                if (System.currentTimeMillis() - oldTime < 3000) {
                    return;
                }
                oldTime = System.currentTimeMillis();

                if (updateWayBillNo()) {


                    JSONArray jsonarray = new JSONArray();
                    JSONObject jsonobject = new JSONObject();
                    try {
                        jsonobject.put("waybillNo", waybillNo);
                        jsonobject.put("sourceZoneCode", sourceZoneCode);
                        jsonobject.put("destZoneCode", destZoneCode);
                        jsonobject.put("custCode", custCode);
                        jsonobject.put("consignorCompName", consignorCompName);
                        jsonobject.put("consignorAddr", consignorAddr);
                        jsonobject.put("consignorPhone", consignorPhone);
                        jsonobject.put("consignorContName", consignorContName);
                        jsonobject.put("consignorMobile", consignorMobile);
                        jsonobject.put("addresseeCompName", addresseeCompName);
                        jsonobject.put("addresseeAddr", addresseeAddr);
                        jsonobject.put("addresseePhone", addresseePhone);
                        jsonobject.put("addresseeContName", addresseeContName);
                        jsonobject.put("addresseeMobile", addresseeMobile);
                        jsonobject.put("realWeightQty", realWeightQty);
                        jsonobject.put("meterageWeightQty", meterageWeightQty);
                        jsonobject.put("quantity", Quantity);
                        jsonobject.put("volume", Volume);
                        jsonobject.put("consigneeEmpCode", consigneeEmpCode);
                        jsonobject.put("consignedTm", consignedTm);
                        jsonobject.put("waybillRemk", waybillRemk);
                        jsonobject.put("consName", consName);
                        jsonobject.put("inputType", inputType);
                        jsonobject.put("paymentTypeCode", paymentTypeCode);
                        jsonobject.put("settlementTypeCode", settlementTypeCode);
                        jsonobject.put("serviceTypeCode", serviceTypeCode);
                        jsonobject.put("waybillFee", waybillFee);
                        jsonobject.put("goodsChargeFee", goodsChargeFee);
                        jsonobject.put("chargeAgentFee", chargeAgentFee);
                        jsonobject.put("bankNo", bankNo);
                        jsonobject.put("bankType", bankType);
                        jsonobject.put("transferDays", transferDays);
                        jsonobject.put("insuranceAmount", insuranceAmount);
                        jsonobject.put("insuranceFee", insuranceFee);
                        jsonobject.put("signBackNo", signBackNo);
                        jsonobject.put("signBackCount", signBackCount);
                        jsonobject.put("signBackSize", signBackSize);
                        jsonobject.put("signBackReceipt", signBackReceipt);
                        jsonobject.put("signBackSeal", signBackSeal);
                        jsonobject.put("signBackIdentity", signBackIdentity);
                        jsonobject.put("waitNotifyFee", waitNotifyFee);
                        jsonobject.put("signBackFee", signBackFee);
                        jsonobject.put("deboursFee", deboursFee);
                        jsonobject.put("deboursFlag", deboursFlag);
                        jsonobject.put("deliverFee", deliverFee);
                        jsonobject.put("productTypeCode", productTypeCode);
                        jsonobject.put("orderNo", orderNo);
                        jsonobject.put("childNoList", childNoList);
                        jsonobject.put("teamCode", teamCode);
                        jsonobject.put("discountExpressFee", discountExpressFee);
                        jsonobject.put("inputerEmpCode", inputerEmpCode);
                        jsonobject.put("distanceTypeCode", distanceTypeCode);
                        jsonobject.put("fuelServiceFee", fuelServiceFee);
                        jsonobject.put("versionNo", versionNo);
                        jsonobject.put("custIdentityCard", custIdentityCard);
                        jsonobject.put("goodsChargeFeeChecked", goodsChargeFeeChecked);
                        jsonobject.put("deboursFeeChecked", deboursFeeChecked);
                        jsonobject.put("insuranceFeeChecked", insuranceFeeChecked);
                        jsonobject.put("deliverFeeChecked", deliverFeeChecked);
                        jsonobject.put("waitNotifyFeeChecked", waitNotifyFeeChecked);
                        jsonobject.put("signBackFeeChecked", signBackFeeChecked);
                        jsonarray.put(jsonobject);

                        HttpUtils httpUtils = new HttpUtils();
                        RequestParams params = new RequestParams();
                        params.addBodyParameter("empCode", MyApp.mEmpCode);
                        params.addBodyParameter("jsonData", jsonarray.toString());

                        httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.updateWaybill, params, new
                                RequestCallBack<String>() {

                            @Override
                            public void onFailure(HttpException arg0, String arg1) {
                                showT("网络连接失败,请稍后再试");
                            }

                            @Override
                            public void onSuccess(ResponseInfo<String> arg0) {
                                showT("修改成功");
                                clearTxt();
                                startActivity(new Intent(AuditbillActivity.this, AuditbillActivity.class));
                                finish();
                                return;
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            case R.id.btn_accept_inputorder_savePrint: {// 保存并打印

                if (System.currentTimeMillis() - oldTime < 3000) {
                    return;
                }
                oldTime = System.currentTimeMillis();

                statusBox = new StatusBox(this, mBtnSaveprint);
                if (!updateWayBillNo()) {
                    showT("修改运单失败");
                    return;
                }

                if (updateWayBillNo()) {


                    JSONArray jsonarray = new JSONArray();
                    JSONObject jsonobject = new JSONObject();
                    try {
                        jsonobject.put("waybillNo", waybillNo);
                        jsonobject.put("sourceZoneCode", sourceZoneCode);
                        jsonobject.put("destZoneCode", destZoneCode);
                        jsonobject.put("custCode", custCode);
                        jsonobject.put("consignorCompName", consignorCompName);
                        jsonobject.put("consignorAddr", consignorAddr);
                        jsonobject.put("consignorPhone", consignorPhone);
                        jsonobject.put("consignorContName", consignorContName);
                        jsonobject.put("consignorMobile", consignorMobile);
                        jsonobject.put("addresseeCompName", addresseeCompName);
                        jsonobject.put("addresseeAddr", addresseeAddr);
                        jsonobject.put("addresseePhone", addresseePhone);
                        jsonobject.put("addresseeContName", addresseeContName);
                        jsonobject.put("addresseeMobile", addresseeMobile);
                        jsonobject.put("realWeightQty", realWeightQty);
                        jsonobject.put("meterageWeightQty", meterageWeightQty);
                        jsonobject.put("quantity", Quantity);
                        jsonobject.put("volume", Volume);
                        jsonobject.put("consigneeEmpCode", consigneeEmpCode);
                        jsonobject.put("consignedTm", consignedTm);
                        jsonobject.put("waybillRemk", waybillRemk);
                        jsonobject.put("consName", consName);
                        jsonobject.put("inputType", inputType);
                        jsonobject.put("paymentTypeCode", paymentTypeCode);
                        jsonobject.put("settlementTypeCode", settlementTypeCode);
                        jsonobject.put("serviceTypeCode", serviceTypeCode);
                        jsonobject.put("waybillFee", waybillFee);
                        jsonobject.put("goodsChargeFee", goodsChargeFee);
                        jsonobject.put("chargeAgentFee", chargeAgentFee);
                        jsonobject.put("bankNo", bankNo);
                        jsonobject.put("bankType", bankType);
                        jsonobject.put("transferDays", transferDays);
                        jsonobject.put("insuranceAmount", insuranceAmount);
                        jsonobject.put("insuranceFee", insuranceFee);
                        jsonobject.put("signBackNo", signBackNo);
                        jsonobject.put("signBackCount", signBackCount);
                        jsonobject.put("signBackSize", signBackSize);
                        jsonobject.put("signBackReceipt", signBackReceipt);
                        jsonobject.put("signBackSeal", signBackSeal);
                        jsonobject.put("signBackIdentity", signBackIdentity);
                        jsonobject.put("waitNotifyFee", waitNotifyFee);
                        jsonobject.put("signBackFee", signBackFee);
                        jsonobject.put("deboursFee", deboursFee);
                        jsonobject.put("deboursFlag", deboursFlag);
                        jsonobject.put("deliverFee", deliverFee);
                        jsonobject.put("productTypeCode", productTypeCode);
                        jsonobject.put("orderNo", orderNo);
                        jsonobject.put("childNoList", childNoList);
                        jsonobject.put("teamCode", teamCode);
                        jsonobject.put("discountExpressFee", discountExpressFee);
                        jsonobject.put("inputerEmpCode", inputerEmpCode);
                        jsonobject.put("distanceTypeCode", distanceTypeCode);
                        jsonobject.put("fuelServiceFee", fuelServiceFee);
                        jsonobject.put("versionNo", versionNo);
                        jsonobject.put("custIdentityCard", custIdentityCard);
                        jsonobject.put("goodsChargeFeeChecked", goodsChargeFeeChecked);
                        jsonobject.put("deboursFeeChecked", deboursFeeChecked);
                        jsonobject.put("insuranceFeeChecked", insuranceFeeChecked);
                        jsonobject.put("deliverFeeChecked", deliverFeeChecked);
                        jsonobject.put("waitNotifyFeeChecked", waitNotifyFeeChecked);
                        jsonobject.put("signBackFeeChecked", signBackFeeChecked);
                        jsonarray.put(jsonobject);

                        HttpUtils httpUtils = new HttpUtils();
                        RequestParams params = new RequestParams();
                        params.addBodyParameter("empCode", MyApp.mEmpCode);
                        params.addBodyParameter("jsonData", jsonarray.toString());

                        httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.updateWaybill, params, new
                                RequestCallBack<String>() {

                            @Override
                            public void onFailure(HttpException arg0, String arg1) {
                                showT("网络连接失败,请稍后再试");
                            }

                            @Override
                            public void onSuccess(ResponseInfo<String> arg0) {
                                String result = arg0.result;
                                Gson gson = new Gson();
                                I_SendInputInfo i_Send = gson.fromJson(result, I_SendInputInfo.class);

                                if (i_Send == null || i_Send.getPdaWaybillInputDataInfoList() == null || i_Send
                                        .getPdaWaybillInputDataInfoList().size() <= 0) {
                                    showT("数据异常，请重新打印");
                                    return;
                                }

                                if (i_Send.success) {
                                    inputDataInfo = i_Send.getPdaWaybillInputDataInfoList().get(0);
                                    if (TextUtils.isEmpty(biaoqianAddress)) {
                                        toastTxt("打印机未连接");
                                    } else {
                                        if (!beforeConsName.equals(consName) || !beforeAddresseeContName.equals
                                                (addresseeContName) || !beforeGoodsChargeFee.equals(mEdtGoodsChargeFee
                                                .getText().toString()) || !beforeRealWeightQty.equals(mEdtRealWeightQty
                                                .getText().toString()) || !beforewaybillFee.equals(mEdtWaybillFee.getText()
                                                .toString()) || !beforeInsuranceFee.equals(mEdtInsuranceAmount.getText()
                                                .toString()) || !beforePaymentTypeCode.equals(paymentTypeCode)) {
                                            isPrint();
                                        }
                                    }
                                    finish();
                                    startActivity(new Intent(AuditbillActivity.this, AuditbillActivity.class));
                                    return;
                                } else {
                                    showT(i_Send.error);
                                }
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            default:
                break;
        }
    }

    private void appendData() {

        DepartmentDBWrapper departmentDB = DepartmentDBWrapper.getInstance(getApplication());
        cursor = departmentDB.rawQueryRank(inputDataInfo.sourceZoneCode);// 查询发货网点
        String districtCode;
        if (!cursor.moveToNext()) {
            cursor.close();
            return;
        }

        inputDataInfo.sourceZoneAddress = cursor.getString(cursor.getColumnIndex("deptAddress"));// 发货地址
        String sourceDeptName = cursor.getString(cursor.getColumnIndex("deptName"));// 发货网点

        //mTxtSourceZone.setText(sourceDeptName);

        cursor = departmentDB.rawQueryRank(inputDataInfo.destZoneCode);// 查询收货网点
        if (!cursor.moveToNext()) {
            cursor.close();
            return;
        }

        inputDataInfo.destZoneAddress = cursor.getString(cursor.getColumnIndex("deptAddress"));// 收货地址
        String destDeptName = cursor.getString(cursor.getColumnIndex("deptName"));// 收货网店
        districtCode = cursor.getString(cursor.getColumnIndex("districtCode"));
        inputDataInfo.outsiteName = destDeptName;
        //mTxtDestZone.setText(destDeptName);

        String outsiteName = cursor.getString(cursor.getColumnIndex("outsiteName"));

        if (TextUtils.isEmpty(outsiteName) || outsiteName.length() < 3) {
            inputDataInfo.outsiteName = outsiteName;
        } else {
            inputDataInfo.outsiteName = outsiteName.substring(0, 3);
        }

        cursor.close();

        Cursor mCursor = DistrictDBWrapper.getInstance(getApplication()).rawQueryRank(districtCode);
        if (!mCursor.moveToNext()) {
            mCursor.close();
            return;
        }

        inputDataInfo.addresseeDistName = mCursor.getString(mCursor.getColumnIndex("distName"));
        inputDataInfo.consignorDistName = mCursor.getString(mCursor.getColumnIndex("distName"));
        inputDataInfo.provinceCode = mCursor.getString(mCursor.getColumnIndex("provinceCode"));
    }

    private void kehulianData() {
        //1.运费
        if (inputDataInfo.waybillFee == null) {
            inputDataInfo.waybillFee = "0.0";
        }/*else{
			inputDataInfo.deliverWaybillFee = String.valueOf(Double.parseDouble(inputDataInfo.waybillFee) -
			commission);
		}*/
        //签回单
        if (inputDataInfo.signBackFee == null) {
            inputDataInfo.signBackFee = "0.0";
        }
        //等通知
        if (inputDataInfo.waitNotifyFee == null) {
            inputDataInfo.waitNotifyFee = "0.0";
        }
        //垫付
        if (inputDataInfo.deboursFee == null) {
            inputDataInfo.deboursFee = "0.0";
        }
        //保件
        if (inputDataInfo.insuranceFee == null) {
            inputDataInfo.insuranceFee = "0.0";
        }
        //派送
        if (MyUtils.isEmpty(inputDataInfo.deliverFee)) {
            inputDataInfo.deliverFee = "0";
            //inputDataInfo.deliverFeeCommissio  = String.valueOf(Double.parseDouble(inputDataInfo.deliverFee) +
            // commission);
        } else {
            //	inputDataInfo.deliverFeeCommissio =String.valueOf(Double.parseDouble(inputDataInfo.deliverFee) +
            // commission);
        }
        //代收
        if (inputDataInfo.goodsChargeFee == null) {
            inputDataInfo.goodsChargeFee = "0.0";
        }
        if (inputDataInfo.bankType == null) {
            inputDataInfo.bankType = "";
        }
        if (inputDataInfo.bankNo == null) {
            inputDataInfo.bankNo = "";
        }
        if (inputDataInfo.signBackNo == null) {
            inputDataInfo.signBackNo = "";
        }
    }

    private void isPrint() {
        appendData();
        kehulianData();
        try {
            statusBox.Show("正在打印...");
            if (!BluetoothUtil.OpenPrinter(this, biaoqianAddress)) {
                statusBox.Close();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        //  BluetoothPrintTool.xiaopiao(inputDataInfo, inputDataInfo.getChildNoLists());
        BluetoothPrintTool.kehuLian(inputDataInfo);
        zpSDK.zp_close();
        if (statusBox != null && statusBox.isShow()) {
            statusBox.Close();
        }
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void Print0() {
        if (MyUtils.isEmpty(addresseePhone)) {
            addresseePhone = "";
        }
        if (MyUtils.isEmpty(goodsChargeFee)) {
            goodsChargeFee = "0";
        }
        if (MyUtils.isEmpty(waitNotifyFee)) {
            waitNotifyFee = "0";
        }
        if (MyUtils.isEmpty(deboursFee)) {
            deboursFee = "0";
        }
        if (MyUtils.isEmpty(deliverFee)) {
            deliverFee = "0";
        }
        Double double1;
        double1 = Double.valueOf(0.0D);
        Object obj1 = Double.valueOf(Double.parseDouble(waybillFee) + Double.parseDouble(signBackFee) + Double
                .parseDouble(waitNotifyFee) + Double.parseDouble(deboursFee) + Double.parseDouble(insuranceFee) + Double
                .parseDouble(deliverFee) + fuelServiceFee);
        double d = ((Double) (obj1)).doubleValue();
        double d1 = Double.parseDouble(goodsChargeFee);
        //double d2 = Double.parseDouble(deboursFee)+fuelServiceFee;
        double1 = Double.valueOf(d + d1);

        // if ("02911".equals(sourcedistrictCode)) {
        // DecimalFormat decimalformat = new DecimalFormat("0.0");
        // if (Double.parseDouble(meterageWeightQty) <= 5D) {
        // shangmenFee = "4";
        // waybillFee = (new
        // StringBuilder(String.valueOf(decimalformat.format(Double.parseDouble(waybillFee)
        // - Double.parseDouble(shangmenFee))))).toString();
        // } else {
        // Double shang = 4D + (Double.parseDouble(meterageWeightQty) - 5D) *
        // 0.10000000000000001D;
        // shangmenFee = new StringBuilder(MyUtils.round(shang)).toString();
        // waybillFee = (new
        // StringBuilder(String.valueOf(decimalformat.format(Double.parseDouble(waybillFee)
        // - Double.parseDouble(shangmenFee))))).toString();
        // }
        // }

        if (!zpSDK.zp_page_create(80D, 61D)) {
            statusBox.Close();
            return;
        }

        // ================================ 客户联 ================================

        zpSDK.TextPosWinStyle = false;
        zpSDK.zp_draw_rect(0.0D, 0D, 72D, 53D, 3);
        zpSDK.zp_draw_line(0.0D, 4D, 72D, 4D, 3);
        zpSDK.zp_draw_line(0.0D, 8D, 72D, 8D, 3);
        zpSDK.zp_draw_line(0.0D, 12D, 72D, 12D, 3);
        zpSDK.zp_draw_line(0.0D, 16D, 72D, 16D, 3);
        //zpSDK.zp_draw_line(0.0D, 20D, 72D, 20D, 3);
        zpSDK.zp_draw_line(0.0D, 24D, 72D, 24D, 3);
        zpSDK.zp_draw_line(0.0D, 28D, 72D, 28D, 3);
        zpSDK.zp_draw_line(0.0D, 32D, 72D, 32D, 3);
        zpSDK.zp_draw_line(0.0D, 36D, 72D, 36D, 3);
        zpSDK.zp_draw_line(0.0D, 40D, 72D, 40D, 3);
        zpSDK.zp_draw_line(0.0D, 44D, 72D, 44D, 3);
        zpSDK.zp_draw_line(0.0D, 48D, 72D, 48D, 3);
        zpSDK.zp_draw_line(12D, 0D, 12D, 8D, 3);

        zpSDK.zp_draw_text_ex(0.9D, 3D, "聚", "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(0.9D, 7D, "信", "宋体", 3D, 0, true, false, false);
		
		/*zpSDK.zp_draw_text_ex(1.0D, 7D, "聚信代收款  招行来代管", "宋体", 6.7999999999999998D, 0, true, true, false);*/
		/*zpSDK.zp_draw_text_ex(1.0D, 11D, consignedTm.substring(5, 10), "宋体", 3D, 0, true, true, false);
		zpSDK.zp_draw_text_ex(1.0D, 15D, consignedTm.substring(11, 16), "宋体", 3D, 0, true, true, false);
		
*/
        zpSDK.zp_draw_text_ex(4.0D, 3.5D, consignedTm.substring(5, 10), "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(4.0D, 7.5D, consignedTm.substring(11, 16), "宋体", 3D, 0, true, false, false);
        //	if (!"02911".equals(sourcedistrictCode)) {

        if (productTypeCode.equals("B1")) {
            zpSDK.zp_draw_text_ex(13D, 11D, (new StringBuilder("快递  运单号")).append(waybillNo).append(" ").append
                    (sourceZoneCode).append(" ").append(consignorContName).toString(), "宋体", 3D, 0, true, false, false);
        } else if (productTypeCode.equals("B2")) {
            zpSDK.zp_draw_text_ex(13D, 11D, (new StringBuilder("普货  运单号")).append(waybillNo).append(" ").append
                    (sourceZoneCode).append(" ").append(consignorContName).toString(), "宋体", 3D, 0, true, false, false);
        } else if (productTypeCode.equals("B3")) {
            zpSDK.zp_draw_text_ex(13D, 11D, (new StringBuilder("普快  运单号")).append(waybillNo).append(" ").append
                    (sourceZoneCode).append(" ").append(consignorContName).toString(), "宋体", 3D, 0, true, false, false);
        }

		/*} else {
			zpSDK.zp_draw_text_ex(13D, 11D,
					(new StringBuilder("普快  运单号")).append(waybillNo).append(" ").append(sourceZoneCode).append(" ")
							.append(consignorContName).toString(), "宋体", 3D, 0, true, false, false);

		}*/

        if (!MyUtils.isEmpty(addresseeMobile)) {
            if (!MyUtils.isEmpty(addresseePhone)) {
                zpSDK.zp_draw_text_ex(13D, 15D, (new StringBuilder(String.valueOf(addresseeDistName))).append(" ")
                        .append(addresseeContName).append(" ").append(addresseeMobile).append(" 取件费  ").append
                                (fuelServiceFee).toString(), "宋体", 3D, 0, true, false, false);
            } else {
                zpSDK.zp_draw_text_ex(13D, 15D, (new StringBuilder(String.valueOf(addresseeDistName))).append(" ")
                        .append(addresseeContName).append(" ").append(addresseeMobile).append(" 取件费  ").append
                                (fuelServiceFee).toString(), "宋体", 3D, 0, true, false, false);
            }
        } else {
            zpSDK.zp_draw_text_ex(13D, 15D, (new StringBuilder(String.valueOf(addresseeDistName))).append(" ").append
                            (addresseeContName).append(" ").append(addresseePhone).append(" 取件费  ").append(fuelServiceFee).toString()
                    , "宋体", 3D, 0, true, false, false);
        }

        zpSDK.zp_draw_text_ex(1.0D, 19D, (new StringBuilder(String.valueOf(consName))).append(" ").append(Quantity)
                        .append("件 重量").append(meterageWeightQty).append("kg 运费").append(waybillFee).append("  垫付").append
                                (deboursFee).append(" 等通知费").append(waitNotifyFee).toString(), "宋体", 2.7999999999999998D, 0, true, false,
                false);
        zpSDK.zp_draw_text_ex(1.0D, 23D, (new StringBuilder("声明价值")).append(insuranceAmount).append(" 保价费").append
                        (insuranceFee).append("元  回单费").append(signBackFee).append(" ").append(signBackNo).toString(), "宋体", 3D, 0,
                true, false, false);

        String bankT = "";
        if (!"0".equals(bankType)) {
            if ("1".equals(bankType)) {
                bankT = "浦发银行";
            }
            if ("3".equals(bankType)) {
                bankT = "建设银行";
            }

            if (!"5".equals(bankType)) {
                if (!"-1".equals(bankNo) && !bankNo.isEmpty()) {
                } else {
                    bankT = "无卡号";
                }
            } else {
                bankT = "招商银行";
            }
        } else {
            bankT = "工商银行";
        }

        zpSDK.zp_draw_text_ex(1.0D, 27D, (new StringBuilder("代收款")).append(goodsChargeFee).append(" 服务费").append
                        (chargeAgentFee).append(" ").append(bankT).append(" ").append(transferDays).append("天转").toString(), "宋体",
                3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(1.0D, 31D, (new StringBuilder("银行账号 ")).append(bankNo).toString(), "宋体", 3D, 0, true,
                false, false);

        zpSDK.zp_draw_text_box(1.0D, 35.5D, 72D, 3D, "客户须知 1：货物应当申明价值， 每件 " +
                "货物遗失或损毁，承运人按总保价的平均价值赔偿。2：每票总价值如果超过5万元，不予承运。3：出现异常，90天内联系处理，超期不予处理", "宋体", 2.7D, 0, true, false, false);

        if (!paymentTypeCode.equals("1")) {
            if (!paymentTypeCode.equals("2")) {

            } else {
                zpSDK.zp_draw_text_ex(1.0D, 47.5D, (new StringBuilder("到付")).append(double1).append("元").toString(),
                        "宋体", 3D, 0, true, false, false);
            }
        } else {
            zpSDK.zp_draw_text_ex(1.0D, 47.5D, (new StringBuilder("寄付")).append(obj1).append("元").toString(), "宋体",
                    3D, 0, true, false, false);
        }

        zpSDK.zp_draw_text_ex(39.5D, 47.5D, "客服热线:", "宋体", 3D, 0, true, false, false);

        zpSDK.zp_draw_text_ex(1.0D, 52D, (new StringBuilder("备注:")).append(waybillRemk).toString(), "宋体", 3D, 0,
                true, false, false);

        zpSDK.zp_draw_text_ex(45D, 52D, "签字", "宋体", 3.8D, 0, true, false, false);

        zpSDK.zp_draw_text_ex(52.5D, 48D, "4008-111115", "宋体", 3.2000000000000002D, 0, true, false, false);
        //zpSDK.zp_draw_text_ex(60.5D, 52D, "111115", "宋体", 3.2000000000000002D, 0, true, false, false);

        zpSDK.zp_page_print(false);
        zpSDK.zp_page_clear();

        zpSDK.zp_goto_mark_label(120);
        zpSDK.zp_page_free();

        if (!zpSDK.zp_page_create(80D, 61D)) {
            statusBox.Close();
            return;
        }
        // ================================ 标签 ================================

        zpSDK.TextPosWinStyle = false;
        zpSDK.zp_draw_rect(0.0D, 0.0D, 72D, 53D, 3);
        zpSDK.zp_draw_line(0.0D, 14D, 72D, 14D, 3);
        zpSDK.zp_draw_line(6D, 19D, 72D, 19D, 3);
        zpSDK.zp_draw_line(6D, 24D, 72D, 24D, 3);
        zpSDK.zp_draw_line(6D, 29D, 72D, 29D, 3);
        zpSDK.zp_draw_line(0.0D, 34D, 72D, 34D, 3);
        zpSDK.zp_draw_line(52D, 48D, 72D, 48D, 3);
        zpSDK.zp_draw_line(10D, 0.0D, 10D, 14D, 3);
        zpSDK.zp_draw_line(6D, 14D, 6D, 34D, 3);
        zpSDK.zp_draw_line(6D, 14D, 6D, 34D, 3);
        zpSDK.zp_draw_line(52D, 34D, 52D, 53D, 3);
        zpSDK.zp_draw_text_ex(1.0D, 5.0D, "聚信", "宋体", 4D, 0, true, true, false);
        zpSDK.zp_draw_text_box(1.0D, 9D, 9D, 3D, consignedTm.substring(5, 10), "宋体", 3D, 0, true, true, false);
        zpSDK.zp_draw_text_box(1.0D, 13D, 9D, 3D, consignedTm.substring(11, 16), "宋体", 3D, 0, true, true, false);
        if (MyUtils.isEmpty(partitionName)) {
            // 目的地城市 + 目的地网点
            zpSDK.zp_draw_text_ex(10D, 12D, addresseeDistName, "宋体", 12D, 0, true, false, false);
            zpSDK.zp_draw_text_ex(33D, 12.1D, outsiteName, "宋体", 12D, 0, true, false, false);
            zpSDK.zp_draw_text_ex(58D, 12.1D, teamCode, "宋体", 14D, 0, false, false, false);
        } else {
            zpSDK.zp_draw_text_ex(10D, 12.1D, partitionName, "宋体", 14D, 0, true, false, false);
            zpSDK.zp_draw_text_ex(28D, 12.1D, addresseeDistName, "宋体", 13D, 0, true, false, false);
            zpSDK.zp_draw_text_ex(57D, 12.1D, teamCode, "宋体", 14D, 0, false, false, false);
        }

		/*if (mChbIsLook.isChecked()) {
			zpSDK.zp_draw_text_box(67.5D, 19D, 5D, 4D, "已验视", "宋体", 4D, 0, false, false, false);
		}*/

        zpSDK.zp_draw_text_box(0.5D, 19D, 5D, 5D, addresseeContName, "宋体", 5D, 0, false, false, false);
        zpSDK.zp_draw_text_ex(7D, 18D, (new StringBuilder(String.valueOf(waybillNo))).append("  ").append
                (sourceZoneCode).append("  ").append(consignorContName).toString(), "宋体", 4D, 0, true, false, false);

        if (!MyUtils.isEmpty(addresseeMobile)) {
            if (!MyUtils.isEmpty(addresseePhone)) {
                zpSDK.zp_draw_text_ex(7D, 23D, (new StringBuilder(String.valueOf(addresseeDistName))).append
                        (addresseePhone).append(" / ").append(addresseeMobile).toString(), "宋体", 4D, 0, true, false, false);
            } else {
                zpSDK.zp_draw_text_ex(7D, 23D, (new StringBuilder(String.valueOf(addresseeDistName))).append
                        (addresseeMobile).toString(), "宋体", 4D, 0, true, false, false);

            }
        } else {
            zpSDK.zp_draw_text_ex(7D, 23D, (new StringBuilder(String.valueOf(addresseeDistName))).append
                    (addresseePhone).toString(), "宋体", 4D, 0, true, false, false);

        }

        zpSDK.zp_draw_text_ex(7D, 28D, addresseeAddr, "宋体", 3.3999999999999999D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(7D, 33D, (new StringBuilder(String.valueOf(consName))).append("1/").append(Quantity)
                        .append("件 重量").append(meterageWeightQty).append("kg 收件员").append(consigneeEmpCode).toString(), "宋体",
                3.6000000000000001D, 0, true, false, false);

        if (!"02911".equals(sourcedistrictCode)) {

            if (productTypeCode.equals("B1")) {
                zpSDK.zp_draw_text_ex(53D, 46.5D, "快", "宋体", 14D, 0, true, false, false);
            } else if (productTypeCode.equals("B2") || productTypeCode.equals("B3")) {
                zpSDK.zp_draw_text_ex(53D, 46.5D, "普", "宋体", 14D, 0, true, false, false);
            }
        } else {
            zpSDK.zp_draw_text_ex(53D, 46.5D, "普", "宋体", 14D, 0, true, false, false);
        }
        zpSDK.zp_draw_text_ex(52D, 52D, "4008-111115", "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_barcode(7D, 35D, waybillNo, zpSDK.BARCODE_TYPE.BARCODE_CODE128, 14D, 3, 0);
        zpSDK.zp_draw_text_ex(10D, 52D, waybillNo, "宋体", 3D, 0, false, false, false);
        zpSDK.zp_page_print(false);
        zpSDK.zp_page_clear();
        zpSDK.zp_goto_mark_label(120);
        zpSDK.zp_page_free();

        if (Integer.parseInt(Quantity) < 2) {
            return;
        }

        int i = 0;

        do {

            if (i >= childNoList.size()) {
                return;
            }

            if (!zpSDK.zp_page_create(80D, 61D)) {
                statusBox.Close();
                return;
            }

            zpSDK.TextPosWinStyle = false;
            zpSDK.zp_draw_rect(0.0D, 0.0D, 72D, 53D, 3);
            zpSDK.zp_draw_line(0.0D, 14D, 72D, 14D, 3);
            zpSDK.zp_draw_line(6D, 19D, 72D, 19D, 3);
            zpSDK.zp_draw_line(6D, 24D, 72D, 24D, 3);
            zpSDK.zp_draw_line(6D, 29D, 72D, 29D, 3);
            zpSDK.zp_draw_line(0.0D, 34D, 72D, 34D, 3);
            zpSDK.zp_draw_line(52D, 48D, 72D, 48D, 3);
            zpSDK.zp_draw_line(10D, 0.0D, 10D, 14D, 3);
            zpSDK.zp_draw_line(6D, 14D, 6D, 34D, 3);
            zpSDK.zp_draw_line(6D, 14D, 6D, 34D, 3);
            zpSDK.zp_draw_line(52D, 34D, 52D, 53D, 3);
            zpSDK.zp_draw_text_ex(1.0D, 5.0D, "聚信", "宋体", 4D, 0, true, true, false);
            zpSDK.zp_draw_text_box(1.0D, 9D, 9D, 3D, consignedTm.substring(5, 10), "宋体", 3D, 0, true, true, false);
            zpSDK.zp_draw_text_box(1.0D, 13D, 9D, 3D, consignedTm.substring(11, 16), "宋体", 3D, 0, true, true, false);
            if (MyUtils.isEmpty(partitionName)) {
                // 目的地城市 + 目的地网点
                zpSDK.zp_draw_text_ex(10D, 12D, addresseeDistName, "宋体", 12D, 0, true, false, false);
                zpSDK.zp_draw_text_ex(33D, 12.1D, outsiteName, "宋体", 12D, 0, true, false, false);
                zpSDK.zp_draw_text_ex(58D, 12.1D, teamCode, "宋体", 14D, 0, false, false, false);
            } else {
                zpSDK.zp_draw_text_ex(10D, 12.1D, partitionName, "宋体", 14D, 0, true, false, false);
                zpSDK.zp_draw_text_ex(28D, 12.1D, addresseeDistName, "宋体", 13D, 0, true, false, false);
                zpSDK.zp_draw_text_ex(57D, 12.1D, teamCode, "宋体", 14D, 0, false, false, false);
            }

		/*	if (mChbIsLook.isChecked()) {
				zpSDK.zp_draw_text_box(67.5D, 19D, 5D, 4D, "已验视", "宋体", 4D, 0, false, false, false);
			}*/

            zpSDK.zp_draw_text_box(0.5D, 19D, 5D, 5D, addresseeContName, "宋体", 5D, 0, false, false, false);
            zpSDK.zp_draw_text_ex(7D, 18D, (new StringBuilder(String.valueOf(waybillNo))).append("  ").append
                    (sourceZoneCode).append("  ").append(consignorContName).toString(), "宋体", 4D, 0, true, false, false);

            if (!MyUtils.isEmpty(addresseeMobile)) {
                if (!MyUtils.isEmpty(addresseePhone)) {
                    zpSDK.zp_draw_text_ex(7D, 23D, (new StringBuilder(String.valueOf(addresseeDistName))).append
                            (addresseePhone).append(" / ").append(addresseeMobile).toString(), "宋体", 4D, 0, true, false, false);
                } else {
                    zpSDK.zp_draw_text_ex(7D, 23D, (new StringBuilder(String.valueOf(addresseeDistName))).append
                            (addresseeMobile).toString(), "宋体", 4D, 0, true, false, false);

                }
            } else {
                zpSDK.zp_draw_text_ex(7D, 23D, (new StringBuilder(String.valueOf(addresseeDistName))).append
                        (addresseePhone).toString(), "宋体", 4D, 0, true, false, false);

            }

            zpSDK.zp_draw_text_ex(7D, 28D, addresseeAddr, "宋体", 3.3999999999999999D, 0, true, false, false);
            zpSDK.zp_draw_text_ex(7D, 33D, (new StringBuilder(String.valueOf(consName))).append(i + 2).append("/")
                    .append(Quantity).append("件 重量").append(meterageWeightQty).append("kg 收件员").append(consigneeEmpCode)
                    .toString(), "宋体", 3.6000000000000001D, 0, true, false, false);

            //	if (!"02911".equals(sourcedistrictCode)) {

            if (productTypeCode.equals("B1")) {
                zpSDK.zp_draw_text_ex(53D, 46.5D, "快", "宋体", 14D, 0, true, false, false);
            } else { //if (productTypeCode.equals("B2") || productTypeCode.equals("B3")) {
                zpSDK.zp_draw_text_ex(53D, 46.5D, "普", "宋体", 14D, 0, true, false, false);

            }
			/*} else {
				zpSDK.zp_draw_text_ex(53D, 46.5D, "普", "宋体", 14D, 0, true, false, false);
			}*/

            zpSDK.zp_draw_text_ex(52D, 52D, "4008-111115", "宋体", 3D, 0, true, false, false);
            zpSDK.zp_draw_barcode(7D, 35D, (String) childNoList.get(i), zpSDK.BARCODE_TYPE.BARCODE_CODE128, 14D, 3, 0);
            zpSDK.zp_draw_text_ex(10D, 52D, (String) childNoList.get(i), "宋体", 3D, 0, false, false, false);
            zpSDK.zp_page_print(false);
            zpSDK.zp_page_clear();
            zpSDK.zp_goto_mark_label(120);
            zpSDK.zp_page_free();
            i++;
        } while (true);

    }

    @SuppressLint("SimpleDateFormat")
    private boolean updateWayBillNo() {
        Double insurance;
        Double double1;
        waybillNo = mEdtWaybillNo.getText().toString().trim();
        consignedTm = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(new Date());
        //	orderNo = mEdtOrderNo.getText().toString().trim();
        consignorContName = mEdtConsignorContName.getText().toString().trim();
        consignorAddr = mEdtConsignorAddr.getText().toString().trim();
        addresseeContName = mEdtAddresseeContName.getText().toString().trim();
        addresseeAddr = mEdtAddresseeAddr.getText().toString().trim();
        Quantity = mEdtQuantity.getText().toString().trim();
        consName = mEdtConsName.getText().toString().trim();
        paymentTypeCode = mSpnPayMethod.getSelectedItem().toString();
        waybillRemk = mEdtRemark.getText().toString().trim();
        if ("寄付".equals(paymentTypeCode)) {
            paymentTypeCode = "1";
        } else {
            paymentTypeCode = "2";
        }

        //if(volume ==null){
        realWeightQty = mEdtRealWeightQty.getText().toString().trim();
        meterageWeightQty = mEdtRealWeightQty.getText().toString().trim();
        //}
        Volume = mEdtVolume.getText().toString().trim();
        waybillFee = mEdtWaybillFee.getText().toString().trim();
        destZoneCode = mEdtDestZoneCode.getText().toString().trim();
        if (fuelServiceFee != null && mEdtFuelService.getText() != null && !"".equals(mEdtFuelService.getText()
                .toString())) {
            fuelServiceFee = Double.valueOf(mEdtFuelService.getText().toString());
        }

        String conPhone = mEdtConsignorPhone.getText().toString().trim();
        if (conPhone.length() == 11) {
            consignorMobile = conPhone;
        } else {
            consignorPhone = conPhone;
        }
        String addrPhone = mEdtAddresseePhone.getText().toString().trim();

        if (addrPhone.length() == 11) {
            addresseeMobile = addrPhone;
        } else {
            addresseePhone = addrPhone;
        }
        //	custIdentityCard = mEdtCustIdentityCard.getText().toString().trim();

        if (!mChkChargeAgent.isChecked()) {
            bankType = "";
            bankNo = "";
            transferDays = "0";
        } else {
            chargeAgentFee = mEdtChargeAgentFee.getText().toString().trim();
            goodsChargeFee = mEdtGoodsChargeFee.getText().toString().trim();
            bankNo = mEdtBankNo.getText().toString().trim();
            transferDays = mSpnPeriod.getSelectedItem().toString().trim();

            if (MyUtils.isEmpty(goodsChargeFee)) {
                showT("请填写代收费用");
                return false;
            }
        }

        insurance = Double.valueOf(0.0D);
        if (mChkInsurance.isChecked()) {
            insuranceFee = mEdtInsuranceFee.getText().toString().trim();
            insuranceAmount = mEdtInsuranceAmount.getText().toString().trim();

            if (MyUtils.isEmpty(insuranceAmount)) {
                showT("请填写保价费用");
                return false;
            }
            if (MyUtils.isEmpty(insuranceFee) && "0".equals(insuranceFee)) {
                showT("请填写保价费用");
                return false;
            }
            insurance = Double.valueOf(Double.parseDouble(insuranceAmount));
        }
        if (mChkDebours.isChecked()) {
            deboursFee = mEdtDeboursFee.getText().toString().trim();
            if (MyUtils.isEmpty(deboursFee)) {
                showT("请填写垫付费用");
                return false;
            }
        } else {
            deboursFee = "0";
        }
        if (mCkbWaitNotifyFee.isChecked()) {
            waitNotifyFee = mEdtWaitNotifyFee.getText().toString().trim();
        }
        if (!mCkbSignBack.isChecked()) {
            signBackFee = "0.0";
            signBackNo = "";
            signBackCount = "";
            signBackSize = "";
        } else {
            signBackFee = mEdtSignBackfee.getText().toString().trim();
            signBackNo = mEdtSignBackNo.getText().toString().trim();
            signBackCount = mEdtSignBackCount.getText().toString().trim();
            signBackSize = mEdtSignBackSize.getText().toString().trim();
        }

        waybillRemk = mEdtRemark.getText().toString().trim();
        double1 = Double.valueOf(0.0D);
        if (mCkbDeliver.isChecked()) {
            deliverFee = mEdtDeliverFee.getText().toString().trim();
            if (MyUtils.isEmpty(deliverFee)) {
                showT("请输入派送费");
                return false;
            }
            double1 = Double.valueOf(Double.parseDouble(deliverFee));
        } else {
            deliverFee = "0";
        }
        if (TextUtils.isEmpty(waybillNo)) {
            MyUtils.showText(mcContext, "运单号不能为空");
            return false;
        }

        if (MyUtils.isEmpty(consigneeEmpCode)) {
            toastTxt("收件员工号不能为空");
            return false;
        }
        if (MyUtils.isEmpty(consignorPhone) && MyUtils.isEmpty(consignorMobile)) {
            toastTxt("寄件方电话不能为空");
            return false;
        }
        if (MyUtils.isEmpty(consignorContName)) {
            toastTxt("寄件人不能为空");
            return false;
        }
		/*if (MyUtils.isEmpty(consignorAddr) || consignorAddr.length() <= 2) {
			toastTxt("寄件地址不能为空或小于2个字");
			return false;
		}*/
        if ("0".equals(meterageWeightQty)) {
            toastTxt("重量不能为0");
            return false;
        }
        if (!MyUtils.isEmpty(destZoneCode) || !MyUtils.isEmpty(destZoneCode)) {
        } else {
            toastTxt("目的地代码不能为空");
            return false;
        }
        if (MyUtils.isEmpty(addresseePhone) && MyUtils.isEmpty(addresseeMobile)) {
            toastTxt("收件方电话不能为空");
            return false;
        }

        if (MyUtils.isEmpty(addresseeContName)) {
            toastTxt("收件人不能为空");
            return false;
        }

        if (MyUtils.isEmpty(addresseeAddr) || addresseeAddr.length() <= 2) {
            toastTxt("收件地址不能为空或小于2个字");
            return false;
        }

        if (mChkInsurance.isChecked()) {
        } else {
            toastTxt("保价费为必选项");
            return false;
        }
        if (MyUtils.isEmpty(insuranceAmount)) {
            toastTxt("保价费不能为空");
            return false;
        }
        if (!productTypeCode.equals("B1") || insurance.doubleValue() <= 20000D) {
        } else {
            toastTxt("保价费不能大于20000元");
            return false;
        }
        if ((!productTypeCode.equals("B2") && !productTypeCode.equals("B3")) || insurance.doubleValue() <= 50000D) {
        } else {
            toastTxt("保价费不能大于50000元");
            return false;
        }
        // if (insurance.doubleValue() >= 1000D) {
        // } else {
        // toastTxt("保价费不能小于1000元");
        // return false;
        // }
        if (!mChkInsurance.isChecked() || !mChkChargeAgent.isChecked() || insurance.doubleValue() >= Double
                .parseDouble(goodsChargeFee)) {
        } else {
            toastTxt("声明价值不能小于代收");
            return false;
        }
        if (!mChkDebours.isChecked() || Double.parseDouble(deboursFee) <= 50D) {
        } else {
            toastTxt("垫付不能大于50");
            return false;
        }
        if (!TextUtils.isEmpty(realWeightQty) || !TextUtils.isEmpty(Volume)) {
        } else {
            toastTxt("重量或体积至少一个不为空");
            return false;
        }
        if (TextUtils.isEmpty(Quantity)) {
            toastTxt("件数不能为空");
            return false;
        }
		/*if (Integer.parseInt(quantity) <= 19) {
		} else {
			toastTxt("件数不能大于19件");
			return false;
		}*/
        if (!MyUtils.isEmpty(waybillFee)) {
        } else {
            toastTxt("运费不能为空");
            return false;
        }

        if (!mCkbSignBack.isChecked() || !TextUtils.isEmpty(signBackFee)) {
        } else {
            toastTxt("请输入签回单费");
            return false;
        }
        if (!mCkbSignBack.isChecked() || !TextUtils.isEmpty(signBackNo)) {
        } else {
            toastTxt("签回单号不能为空");
            return false;
        }
        if (!mCkbSignBack.isChecked() || !TextUtils.isEmpty(signBackCount)) {
        } else {
            toastTxt("返单份数不能为空");
            return false;
        }
        if (!mCkbSignBack.isChecked() || !TextUtils.isEmpty(signBackSize)) {
        } else {
            toastTxt("返单张数不能为空");
            return false;
        }
        if (!mCkbDeliver.isChecked() || double1.doubleValue() <= 10000D) {
        } else {
            toastTxt("派送费不能大于10000");
            return false;
        }
        if (!mCkbDeliver.isChecked() || double1.doubleValue() != 0.0D) {
        } else {
            toastTxt("派送费不能为空");
            return false;
        }
        if (productTypeCode.equals("B3") && (fuelServiceFee.doubleValue() == 0 || fuelServiceFee == null)) {
            toastTxt("上门费不能为空");
            return false;
        }
        if (isCheck()) {
            toastTxt("审核过的数据代收不能取消，请红冲代收金额为0!");
            return false;
        }
/*		
		if (quantity.equals("1")) {
			acceptInputDB.updateRank(waybillNo, sourceZoneCode, destZoneCode, custCode, consignorCompName,
					consignorAddr, consignorPhone, consignorContName, consignorMobile, addresseeCompName,
					addresseeAddr, addresseePhone, addresseeContName, addresseeMobile, consName, quantity, volume,
					realWeightQty, meterageWeightQty, consigneeEmpCode, consignedTm, paymentTypeCode,
					settlementTypeCode, waybillFee, goodsChargeFee, chargeAgentFee, bankNo, bankType, transferDays,
					insuranceAmount, insuranceFee, deboursFee, deboursFlag, productTypeCode, orderNo, teamCode,
					discountExpressFee, MyApp.mEmpCode, distanceTypeCode, MyApp.mEmpName, "02", "4", 0, signBackFee,
					signBackNo, signBackCount, signBackSize, signBackReceipt, signBackSeal, signBackIdentity,
					waitNotifyFee, deliverFee, waybillRemk, serviceTypeCode, custIdentityCard, fuelServiceFee,
					versionNo);
			return true;
		}

		if (Integer.parseInt(quantity) <= 1) {
			toastTxt("数据添加失败");
			return false;
		} else {
			acceptInputDB.updateRank(waybillNo, sourceZoneCode, destZoneCode, custCode, consignorContName,
					consignorAddr, consignorPhone, consignorContName, consignorMobile, addresseeCompName,
					addresseeAddr, addresseePhone, addresseeContName, addresseeMobile, consName, quantity, volume,
					realWeightQty, meterageWeightQty, consigneeEmpCode, consignedTm, paymentTypeCode,
					settlementTypeCode, waybillFee, goodsChargeFee, chargeAgentFee, bankNo, bankType, transferDays,
					insuranceAmount, insuranceFee, deboursFee, deboursFlag, productTypeCode, orderNo, teamCode,
					discountExpressFee, MyApp.mEmpCode, distanceTypeCode, MyApp.mEmpName, "02", "4", 0, signBackFee,
					signBackNo, signBackCount, signBackSize, signBackReceipt, signBackSeal, signBackIdentity,
					waitNotifyFee, deliverFee, waybillRemk, serviceTypeCode, custIdentityCard, fuelServiceFee,
					versionNo);
		}*/
        return true;
        //int i = 0;

	/*	do {

			if (i >= childNoList.size()) {
				return true;
			}
			childNoListDB.insertRank(waybillNo, (String) childNoList.getTypeCode(i),
					(new StringBuilder(String.valueOf(i))).toString(), 0);
			waybillNoDB.deleteRank((String) childNoList.getTypeCode(i));
			i++;
		} while (true);*/

    }

    /**
     * 查询单号
     **/
    private void queryWaybill() {
        charsequence = mWaybillNo.getText().toString();
        if (charsequence.length() == 12) {
            clearTxt();
            if (MyUtils.isNetworkConnect(AuditbillActivity.this)) {
                HttpUtils httpUtils = new HttpUtils();

                StringBuilder builder = new StringBuilder();
                builder.append("?").append("waybillNo=").append(charsequence);
                String url = MyApp.mPathServerURL + Conf.queryWaybillAction + builder.toString();

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

                        if (i_Send == null || i_Send.getPdaWaybillList() == null || i_Send.getPdaWaybillList().size()
                                <= 0) {
                            initData();
                            initView();
                            showT("数据为空");
                            return;
                        }

                        if (i_Send.success) {
                            itemData = i_Send.getPdaWaybillList().get(0);
                            setOrderView(itemData);
                            //queryDeliverCommission(itemData);
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

    @SuppressWarnings("unchecked")
    private void setOrderView(ItemData data) {
        String sourceDeptcode = MyApp.mDeptCode;
        //if(!sourceDeptcode.equals(data.sourceZoneCode)&&!"001".equals(sourceDeptcode)){
        //	mEdtDestZoneCode.setEnabled(false);
        mEdtDestZoneName.setEnabled(false);
        mEdtWaybillNo.setEnabled(false);

        mEdtConsigneeEmpCode.setEnabled(false);
        mEdtConsignorPhone.setEnabled(false);
        mEdtConsignorAddr.setEnabled(false);
        mEdtAddresseePhone.setEnabled(false);
        mEdtAddresseeContName.setEnabled(false);
        mEdtAddresseeAddr.setEnabled(false);
        mEdtConsName.setEnabled(false);
        //mSpnPayMethod.setEnabled(false);
        mSpnSetterMethod.setEnabled(false);
        //mEdtRealWeightQty.setEnabled(false);
        mEdtVolume.setEnabled(false);
        mEdtQuantity.setEnabled(false);

        //mEdtRemark.setEnabled(false);
        mEdtFuelService.setEnabled(false);
        //代收
        mChkChargeAgent.setEnabled(false);
        //mEdtGoodsChargeFee.setEnabled(false);
        mEdtChargeAgentFee.setEnabled(false);
        mEdtBankNo.setEnabled(false);
        mSpnPeriod.setFocusable(false);
        mSpnPeriod.setClickable(false);
        //保价
        mChkInsurance.setEnabled(false);
        //  mEdtInsuranceFee.setEnabled(false);
        //mEdtInsuranceAmount.setEnabled(false);
        //垫付
        mChkDebours.setEnabled(false);
        mEdtDeboursFee.setEnabled(false);
        mChkDeboursFlag.setEnabled(false);
        //派送费
        mCkbDeliver.setEnabled(false);
        mEdtDeliverFee.setEnabled(false);
        //等通知
        mCkbWaitNotifyFee.setEnabled(false);
        mEdtWaitNotifyFee.setEnabled(false);
        //签回单
        mCkbSignBack.setEnabled(false);
        mEdtSignBackfee.setEnabled(false);
        mEdtSignBackNo.setEnabled(false);
        mEdtSignBackCount.setEnabled(false);
        mEdtSignBackSize.setEnabled(false);
        mCkbSignBackIdentity.setEnabled(false);
        mCkbSignBackReceipt.setEnabled(false);
        mCkbSignBackSeal.setEnabled(false);
			
			

		/*}else{
			
			mEdtDestZoneCode.setEnabled(true);
			mEdtDestZoneName.setEnabled(true);
			mEdtWaybillNo.setEnabled(true);
			//mSpnPayMethod.setEnabled(false);
			//mSpnSetterMethod.setEnabled(false);
			mEdtConsigneeEmpCode.setEnabled(true);
			mEdtConsignorPhone.setEnabled(true);
			mEdtConsignorAddr.setEnabled(true);
			mEdtAddresseePhone.setEnabled(true);
			mEdtAddresseeContName.setEnabled(true);
			mEdtAddresseeAddr.setEnabled(true);
			mEdtConsName.setEnabled(true);
			
			//mEdtRealWeightQty.setEnabled(true);
			//mEdtVolume.setEnabled(true);
			//mEdtWaybillFee.setEnabled(true);
			mEdtRemark.setEnabled(true);
			//mEdtFuelService.setEnabled(true);
			//代收
			mChkChargeAgent.setEnabled(true);
			//mChkChargeAgent.setEnabled(true);
			mEdtGoodsChargeFee.setEnabled(true);
			mEdtChargeAgentFee.setEnabled(true);
			mEdtBankNo.setEnabled(true);
			//保价
		    mChkInsurance.setEnabled(true);
		    mEdtInsuranceFee.setEnabled(true);
			mEdtInsuranceAmount.setEnabled(true);
			//垫付
			mChkDebours.setEnabled(true);
			mEdtDeboursFee.setEnabled(true);
			mChkDeboursFlag.setEnabled(true);
			//派送费
			mCkbDeliver.setEnabled(true);
			mEdtDeliverFee.setEnabled(true);
			//等通知
			mCkbWaitNotifyFee.setEnabled(true);
			mEdtWaitNotifyFee.setEnabled(true);
			//签回单
			mCkbSignBack.setEnabled(true);
			mEdtSignBackfee.setEnabled(true);
			mEdtSignBackNo.setEnabled(true);
			mEdtSignBackCount.setEnabled(true);
			mEdtSignBackSize.setEnabled(true);
			mCkbSignBackIdentity.setEnabled(true);
			mCkbSignBackReceipt.setEnabled(true);
			mCkbSignBackSeal.setEnabled(true);
			mEdtRealWeightQty.setEnabled(false);
			mEdtVolume.setEnabled(false);
		
			if(null!=data.paymentTypeCode){
				if("1".equals(data.paymentTypeCode)){
					mSpnPayMethod.setSelection(1);
					}else{
					mSpnPayMethod.setSelection(0);
					mEdtRealWeightQty.setEnabled(true);
					mEdtVolume.setEnabled(true);
				}
			}
		}*/
	/*	if(!"001".equals(sourceDeptcode)){			
			//mSpnPayMethod.setEnabled(true);
			//mSpnSetterMethod.setEnabled(true);
			mEdtConsignorContName.setEnabled(false);
			mEdtRealWeightQty.setEnabled(false);
			mEdtVolume.setEnabled(false);
			mEdtWaybillFee.setEnabled(false);
			mEdtQuantity.setEnabled(false);
		}*/

        mEdtDestZoneCode.setText(data.destZoneCode);
        Cursor departmentCursor = DepartmentDBWrapper.getInstance(getApplication()).rawQueryRank(data.destZoneCode);
        if (departmentCursor.moveToNext()) {
            destZoneName = departmentCursor.getString(departmentCursor.getColumnIndex("deptName"));
        }
        mEdtDestZoneName.setText(destZoneName);
        mEdtWaybillNo.setText(data.waybillNo);
        mEdtConsigneeEmpCode.setText(data.consigneeEmpCode);
        if (null != data.paymentTypeCode) {
            if ("1".equals(data.paymentTypeCode)) {
                mSpnPayMethod.setSelection(1);
            } else {
                mSpnPayMethod.setSelection(0);
            }
        }
        beforePaymentTypeCode = data.paymentTypeCode;
        auditedFlg = data.auditedFlg;
        productTypeCode = data.productTypeCode;
        mEdtConsignorPhone.setText(data.consignorPhone != null ? data.consignorPhone : data.consignorMobile);
        mEdtConsignorContName.setText(data.consignorContName);
        mEdtConsignorAddr.setText(data.consignorAddr);
        mEdtAddresseePhone.setText(data.addresseePhone != null ? data.addresseePhone : data.addresseeMobile);
        mEdtAddresseeContName.setText(data.addresseeContName);
        beforeAddresseeContName = data.addresseeContName;
        mEdtAddresseeAddr.setText(data.addresseeAddr);
        mEdtConsName.setText(data.consName);
        beforeConsName = data.consName;
        mEdtQuantity.setText(String.valueOf(data.quantity));
        mEdtRealWeightQty.setText(data.meterageWeightQty);
        beforeRealWeightQty = data.meterageWeightQty;
        beforewaybillFee = data.waybillFee;
        mEdtVolume.setText(data.volume);
        mEdtWaybillFee.setText(data.waybillFee);
        mEdtRemark.setText(data.waybillRemk);
        mEdtFuelService.setText(data.fuelServiceFee == null ? "0" : String.valueOf(data.fuelServiceFee));
        serviceTypeCode = data.serviceTypeCode;
        paymentTypeCode = data.paymentTypeCode;
        //增值服务费
        //1.代收
        if (null != data.goodsChargeFee && !"".equals(data.goodsChargeFee)) {
            mChkChargeAgent.setChecked(true);
            mEdtGoodsChargeFee.setFocusable(true);
            mEdtGoodsChargeFee.setText(data.goodsChargeFee);
            mEdtChargeAgentFee.setText(data.chargeAgentFee);
            mEdtBankNo.setText(data.bankNo);
            beforeGoodsChargeFee = data.goodsChargeFee;
            //银行类型
            if (null != data.bankType) {
                //工行
                if ("0".equals(data.bankType)) {
                    mSpnBankType.setSelection(1);
                }
                //浦发
                else if ("1".equals(data.bankType)) {
                    mSpnBankType.setSelection(2);
                }
                //招行
                else if ("5".equals(data.bankType)) {
                    mSpnBankType.setSelection(3);
                }
                //建行
                else if ("3".equals(data.bankType)) {
                    mSpnBankType.setSelection(4);
                }
                //无卡号
                else {
                    mSpnBankType.setSelection(0);
                }
            }
            //转款周期
            if (null != data.transferDays) {
                //1天O
                if ("1".equals(data.transferDays)) {
                    mSpnPeriod.setSelection(0);
                }
                //3天
                else if ("3".equals(data.transferDays)) {
                    mSpnPeriod.setSelection(1);
                }
                //7天
                else if ("7".equals(data.transferDays)) {
                    mSpnPeriod.setSelection(2);
                }
                //15天
                else if ("15".equals(data.transferDays)) {
                    mSpnPeriod.setSelection(3);
                }
            }
        }
        //	try {
        //2.保价
        if (null != data.insuranceAmount && !"".equals(insuranceAmount)) {
            mEdtInsuranceFee.setText(data.insuranceFee);
            mEdtInsuranceAmount.setText(data.insuranceAmount);
            mChkInsurance.setChecked(true);
            beforeInsuranceFee = insuranceAmount;
        }
		/*} catch (Exception e) {
			e.printStackTrace();
		}*/

        //3.垫付
        if (null != data.deboursFee && !"".equals(data.deboursFee)) {
            mChkDebours.setChecked(true);
            mEdtDeboursFee.setText(data.deboursFee);
            if ("1".equals(data.deboursFlag)) {
                mChkDeboursFlag.setChecked(true);
            }
        }
        //4.派送
        if (null != data.deliverFee && !"".equals(data.deliverFee)) {
            mCkbDeliver.setChecked(true);
            mEdtDeliverFee.setText(data.deliverFee);
        }
        //5.等通知
        if (null != data.waitNotifyFee && !"".equals(data.waitNotifyFee)) {
            mCkbWaitNotifyFee.setChecked(true);
            mEdtWaitNotifyFee.setText(data.waitNotifyFee);
        }
        //6.签回单
        if (null != data.signBackFee && !"".equals(data.signBackFee)) {
            mCkbSignBack.setChecked(true);
            mEdtSignBackfee.setText(data.signBackFee);
            mEdtSignBackNo.setText(data.signBackNo);
            mEdtSignBackCount.setText(data.signBackCount);
            mEdtSignBackSize.setText(data.signBackSize);
            if (null != data.signBackIdentity && "1".equals(data.signBackIdentity)) {
                mCkbSignBackIdentity.setChecked(true);
            }
            if (null != data.signBackReceipt && "1".equals(data.signBackReceipt)) {
                mCkbSignBackReceipt.setChecked(true);
            }
            if (null != data.signBackSeal && "1".equals(data.signBackSeal)) {
                mCkbSignBackSeal.setChecked(true);
            }
        }
        versionNo = data.versionNo;
    }

    private void clearTxt() {

        mEdtDestZoneCode.setText("");
        mEdtDestZoneName.setText("");
        mEdtWaybillNo.setText("");

        mEdtConsigneeEmpCode.setText("");
        mEdtConsignorPhone.setText("");
        mEdtConsignorAddr.setText("");
        mEdtAddresseePhone.setText("");
        mEdtAddresseeContName.setText("");
        mEdtAddresseeAddr.setText("");
        mEdtConsName.setText("");

        //mSpnPayMethod.setEnabled(false);
        //mSpnPayMethod.setSelection(0);
        mSpnSetterMethod.setEnabled(false);
        mEdtConsignorContName.setText("");
        mEdtRealWeightQty.setText("");
        mEdtVolume.setText("");
        mEdtWaybillFee.setText("");
        mEdtQuantity.setText("");

        mEdtRemark.setText("");
        mEdtFuelService.setText("");
        //代收
        mChkChargeAgent.setEnabled(true);
        mChkChargeAgent.setChecked(false);

        //保价
        mChkInsurance.setEnabled(true);
        mChkInsurance.setChecked(false);

        //垫付
        mChkDebours.setEnabled(true);
        mChkDebours.setChecked(false);

        //派送费
        mCkbDeliver.setEnabled(true);
        mCkbDeliver.setChecked(false);

        //等通知
        mCkbWaitNotifyFee.setEnabled(true);
        mCkbWaitNotifyFee.setChecked(false);

        //签回单
        mCkbSignBack.setEnabled(true);
        mCkbSignBack.setChecked(false);

    }

    /**
     * 是否验收
     */
    private boolean isCheck() {
        if (itemData == null) {
            showT("无数据");
            return false;
        }

        if ("1".equals(itemData.auditedFlg)) {
            if ("0".equals(goodsChargeFeeChecked)) {
                return true;
            }
        }
        return false;
    }

    private void toastTxt(String s) {
        showT(s);
    }
	
/*	public void queryDeliverCommission( itemData itemData) {
		if(itemData !=null){
			if (MyUtils.isNetworkConnect(AuditbillActivity.this)) {
				HttpUtils httpUtils = new HttpUtils();
				RequestParams params = new RequestParams();
				params.addBodyParameter("meterageWeightQty", inputDataInfo.meterageWeightQty);
				params.addBodyParameter("deptCode", inputDataInfo.destZoneCode);
				params.addBodyParameter("productTypeCode", inputDataInfo.productTypeCode);

				StringBuilder builder = new StringBuilder();
				builder.append("?").append("meterageWeightQty=").append(itemData.meterageWeightQty).append("&")
				.append("deptCode=").append(itemData.destZoneCode).append("&").append("productTypeCode=").append
				(itemData.productTypeCode);
				String url = MyApp.mPathServerURL
						+ Conf.queryDeliverCommission
						+builder.toString();
				// http://copserver.jx-express.cn/jxe-cop/pda/queryWaybillByList.action?waybillNo=029143685850
				// http://copserver.jx-express.cn/jxe-cop/pda/queryWaybillByList
				.action?waybillNo=029143685850&devId=865372022551524
				httpUtils.send(HttpMethod.GET, url,
						new RequestCallBack<String>() {


							@Override
							public void onFailure(HttpException arg0,String arg1) {
								Toast.makeText(AuditbillActivity.this, "网络异常，请稍候再试", 0).show();
								commission = 0.0;
							}

							@Override
							public void onSuccess(ResponseInfo<String> arg0) {
								String result = arg0.result;
								Gson gson = new Gson();
								I_Send i_Send = gson.fromJson(result,I_Send.class);

								if (i_Send == null|| i_Send.getCommission()==null) {
									commission = 0.0;
									return;
								}

								if (i_Send.success) {
									commission = i_Send.getCommission();

								} else {
									Toast.makeText(
											AuditbillActivity.this,
											i_Send.error, 0).show();
								}
							}

						});

			} else {
				Toast.makeText(AuditbillActivity.this,
						"无网络连接，请稍后再试", 0).show();
			}
		}
	}
*/

    class I_Send {
        public String error;
        public List<ItemData> pdaWaybillList;
        public boolean success;
        public Double commission;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public List<ItemData> getPdaWaybillList() {
            return pdaWaybillList;
        }

        public void setPdaWaybillList(List<ItemData> pdaWaybillList) {
            this.pdaWaybillList = pdaWaybillList;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public Double getCommission() {
            return commission;
        }

        public void setCommission(Double commission) {
            this.commission = commission;
        }

    }

    class ItemData {

        Double FeeCount;
        String WarehouseFee;
        String addresseeAddr;
        String addresseeCompName;
        String addresseeContName;
        String addresseeDistName;
        String addresseeMobile;
        String addresseePhone;
        String bankNo;
        String bankType;
        String chargeAgentFee;
        String childNoList;
        List childNoLists;
        String auditedFlg;
        String consName;
        String consignedTm;
        String consigneeEmpCode;
        String consignorAddr;
        String consignorCompName;
        String consignorContName;
        String consignorDistName;
        String consignorMobile;
        String consignorPhone;
        String custCode;
        String deboursFee;
        String deboursFlag;
        String deliverFee;
        String destZoneAddress;
        String destZoneCode;
        String destZoneName;
        String distanceTypeCode;
        String goodsChargeFee;
        String inputType;
        String inputerEmpCode;
        String insurance;
        public String insuranceAmount;
        String insuranceFee;
        String meterageWeightQty;
        String orderNo;
        public String outsiteName;
        String paymentTypeCode;
        String productTypeCode;
        String quantity;
        String setterMethod;
        String signBackCount;
        String signBackFee;
        String signBackIdentity;
        String signBackNo;
        String signBackReceipt;
        String signBackSeal;
        String signBackSize;
        String sourceZoneCode;
        String strConsignedTm;
        String teamCode;
        String transferDays;
        String volume;
        String waitNotifyFee;
        String waybillFee;
        String waybillNo;
        String waybillRemk;
        String sourceZoneAddress;
        String sourcedistrictCode;
        Double fuelServiceFee;
        String versionNo;
        String serviceTypeCode;

        ItemData() {
            FeeCount = Double.valueOf(0.0D);
            productTypeCode = "";
            consignedTm = "";
            inputType = "";
            waitNotifyFee = "";
            signBackFee = "0.0";
            waitNotifyFee = "0.0";
            insuranceAmount = "0.0";
            inputerEmpCode = "";
            auditedFlg = "";
            fuelServiceFee = 0.0;
            consigneeEmpCode = "";
            sourceZoneCode = "";
            destZoneCode = "";
            waybillNo = "";
            consignorPhone = "";
            consignorContName = "";
            consignorAddr = "";
            addresseePhone = "";
            addresseeContName = "";
            addresseeAddr = "";
            teamCode = "";
            deliverFee = "0.0";
            destZoneName = "";
            consName = "";
            volume = "0";
            waybillFee = "0";
            deboursFee = "0.0";
            goodsChargeFee = "";
            chargeAgentFee = "0.0";
            insuranceFee = "0.0";
            bankType = "";
            bankNo = "";
            transferDays = "0";
            insurance = "";
            deboursFlag = "";
            custCode = "";
            consignorMobile = "";
            consignorCompName = "";
            addresseeCompName = "";
            addresseeMobile = "";
            meterageWeightQty = "0";
            signBackNo = "";
            signBackCount = "0";
            signBackSize = "0";
            signBackReceipt = "0";
            signBackSeal = "0";
            signBackIdentity = "0";
            WarehouseFee = "0";
            strConsignedTm = "";
            versionNo = "";
            serviceTypeCode = "2";
        }

    }

    class I_SendInputInfo {
        public String error;
        public List<InputDataInfo> pdaWaybillInputDataInfoList;
        public boolean success;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }


        public List<InputDataInfo> getPdaWaybillInputDataInfoList() {
            return pdaWaybillInputDataInfoList;
        }

        public void setPdaWaybillInputDataInfoList(List<InputDataInfo> pdaWaybillInputDataInfoList) {
            this.pdaWaybillInputDataInfoList = pdaWaybillInputDataInfoList;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

    }
}

