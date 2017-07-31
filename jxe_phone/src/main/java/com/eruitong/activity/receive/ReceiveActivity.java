package com.eruitong.activity.receive;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.eruitong.activity.BaseActivity;
import com.eruitong.bluetooth.StatusBox;
import com.eruitong.config.Conf;
import com.eruitong.db.AcceptInputDBWrapper;
import com.eruitong.db.BluetoothDBWrapper;
import com.eruitong.db.CategoryDBWrapper;
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
import com.eruitong.model.AddressList;
import com.eruitong.model.CustList;
import com.eruitong.model.PriceInfo;
import com.eruitong.model.net.I_QueryCustomer;
import com.eruitong.print.bluetooth.BluetoothUtil;
import com.eruitong.utils.AppManager;
import com.eruitong.utils.DialogUtils;
import com.eruitong.utils.MyUtils;
import com.eruitong.utils.UploadWaybill;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tencent.bugly.crashreport.CrashReport;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import zpSDK.zpSDK.zpSDK;

public class ReceiveActivity extends BaseActivity implements OnCheckedChangeListener, OnClickListener {

    /**
     * 标题
     **/
    @ViewInject(R.id.edt_accept_inputorder_include)
    TextView mTxtInclude;

    /**
     * 清空
     **/
    @ViewInject(R.id.btn_accept_inputorder_clear)
    public Button mBtnClear;
    /**
     * 保存
     **/
    @ViewInject(R.id.btn_accept_inputorder_next)
    public Button mBtnNext;
    /**
     * 备注，变质
     **/
    @ViewInject(R.id.btn_accept_inputorder_remark1)
    public Button mBtnRemark1;
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
     * 订单
     **/
    @ViewInject(R.id.edt_accept_inputorder_orderNo)
    public EditText mEdtOrderNo;
    /**
     * 订单 - 后
     **/
    @ViewInject(R.id.edt_accept_inputorder_monthlyNo)
    public EditText mEdMonthlyNo;
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
     * 身份证
     **/
    @ViewInject(R.id.edt_accept_inputorder_custIdentityCard)
    public EditText mEdtCustIdentityCard;
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
    @ViewInject(R.id.spn_accept_inputorder_consName)
    public Spinner mSpnConsName;

    /**
     * 品类
     **/
    @ViewInject(R.id.spn_accept_inputorder_categoryCode)
    public Spinner mSpnCategoryCode;

    /**
     * 目的代码
     **/
    @ViewInject(R.id.spn_accept_inputorder_destZoneCode_code)
    public Spinner mSpnDestZoneCode;
    /**
     * 目的名称
     **/
    @ViewInject(R.id.spn_accept_inputorder_destZoneCode_name)
    public Spinner mSpnDestZoneName;
    /**
     * 付款 方式
     **/
    @ViewInject(R.id.spn_accept_inputorder_payMethod)
    public Spinner mSpnPayMethod;
    /** 时期 **//*
    @ViewInject(R.id.spn_accept_inputorder_period)
	public Spinner mSpnPeriod;*/
    /**
     * 结算
     **/
    @ViewInject(R.id.spn_accept_inputorder_settlemethed)
    public Spinner mSpnSetterMethod;

    /**
     * 类型
     **/
    @ViewInject(R.id.radiogroup1)
    public RadioGroup mRG;
    /**
     * 快递
     **/
    @ViewInject(R.id.rb_login_express)
    public RadioButton mRbExpress;
    /**
     * 普货
     **/
    @ViewInject(R.id.rb_login_general)
    public RadioButton mRbGeneral;
    /**
     * 普快
     **/
    @ViewInject(R.id.rb_login_general2)
    public RadioButton mRbGeneral2;
    /**
     * 检验
     **/
    @ViewInject(R.id.chb_is_look)
    public CheckBox mChbIsLook;

    /**
     * 服务类型
     **/
    @ViewInject(R.id.radiogroup2)
    public RadioGroup mRG2;
    /**
     * 港到港
     **/
    @ViewInject(R.id.rb_login_portToPort)
    public RadioButton mRbPortToPort;
    /**
     * 港到门
     **/
    @ViewInject(R.id.rb_login_portToDoor)
    public RadioButton mRbPortToDoor;
    /**
     * 门到门
     **/
    @ViewInject(R.id.rb_login_doorToDoor)
    public RadioButton mRbDoorToDoor;

    private AcceptInputDBWrapper acceptInputDB;
    private WaybillNoDBWrapper waybillNoDB;
    private childNoListDBWrapper childNoListDB;

    /**
     * 付款方式
     **/
    private String paymentTypeCode;
    private String productTypeCode;
    ;

    /**
     * 结算方式
     **/
    private String settlementTypeCode;

    public String biaoqianAddress;

    //public Double commission;

    //public Double deliverFeeCommissio;

    //private Double factWaybillFee;

    int TYPE_METHOD;
    int ADDRESSEE_TYPE;
    int CONSIGNOR_TYPE;

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
//	Double  commission = 0.0;

    /**
     * 品类名称
     **/
    String consType;

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
        setContentView(R.layout.activity_accept_inputorder);
        ViewUtils.inject(this);

        AppManager.getAppManager().addActivity(this);

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
                            (mContext, android.R.layout.simple_spinner_item, (String[]) arraylist.toArray(new
                                    String[arraylist.size()]));
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpnDestZoneCode.setAdapter(arrayAdapter);
                    arrayAdapter = ArrayAdapter.createFromResource(mContext, R.array.pay_method, android.R.layout
                            .simple_spinner_item);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpnPayMethod.setAdapter(arrayAdapter);
                    arrayAdapter = ArrayAdapter.createFromResource(mContext, R.array.setter_method, android.R.layout
                            .simple_spinner_item);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpnSetterMethod.setAdapter(arrayAdapter);
                    arrayAdapter = ArrayAdapter.createFromResource(mContext, R.array.cons_name, android.R.layout
                            .simple_spinner_item);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpnConsName.setAdapter(arrayAdapter);
                    arrayAdapter = ArrayAdapter.createFromResource(mContext, R.array.category_code, android.R.layout
                            .simple_spinner_item);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpnCategoryCode.setAdapter(arrayAdapter);

                    arrayAdapter = ArrayAdapter.createFromResource(mContext, R.array.bank_type, android.R.layout
                            .simple_spinner_item);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpnBankType.setAdapter(arrayAdapter);
                    arrayAdapter = ArrayAdapter.createFromResource(mContext, R.array.period, android.R.layout
                            .simple_spinner_item);
                    /*arrayAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					mSpnPeriod.setSelection(mPeriodSize - 1);
					mSpnPeriod.setAdapter(arrayAdapter);*/
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
        serviceTypeCode = "3";
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

			/*if ("Henansheng".equals(provinceCode)) {
				if (s.equals("DT07")) {
					mRbGeneral.setClickable(false);
					mRbGeneral2.setChecked(true);
					mRbExpress.setClickable(false);
				} else if (s.equals("DT08")) {
					mRbGeneral.setClickable(false);
					mRbGeneral2.setChecked(true);
					mRbExpress.setClickable(true);
				} else {
					mRbExpress.setChecked(true);
					mRbGeneral.setClickable(false);
					mRbGeneral2.setClickable(false);
				}
			} else {*/
            if (!s.equals("DT06")) {
                if (!s.equals("DT07")) {
                    if (!s.equals("DT08")) {
                        // 如果是总部，都能选
                        if (s.equals("ZB01")) {
                            mRbGeneral.setClickable(true);
                            mRbGeneral2.setClickable(true);
                            mRbExpress.setClickable(true);
                        }

                    } else {
                        // 综合网点
                        mRbGeneral.setChecked(true);
                        mRbGeneral2.setClickable(true);
                        mRbExpress.setClickable(true);
                    }
                } else {
						/*
						 * if ("Henansheng".equals(provinceCode)) {
						 * mRbGeneral.setChecked(false);
						 * mRbGeneral2.setClickable(false);
						 * mRbExpress.setClickable(true); }else{
						 */
                    // 普货
                    mRbGeneral.setChecked(true);
                    mRbGeneral2.setClickable(true);
                    mRbExpress.setClickable(false);
                    // }
						/*if ("Henansheng".equals(provinceCode)) {
							mRbGeneral.setChecked(false);
							mRbGeneral2.setClickable(true);
							mRbExpress.setClickable(false);
						}else{	*/
                    // 普货
							/*mRbGeneral.setChecked(true);
							mRbGeneral2.setClickable(true);
							mRbExpress.setClickable(false);*/
                    //}
                }
            } else {
                // 快递
                mRbExpress.setChecked(true);
                mRbGeneral.setClickable(false);
                mRbGeneral2.setClickable(false);

            }
            //}

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

        boolean flag = true;
        // 获取主单号
        Cursor waybillCursor = waybillNoDB.rawQueryRank(sourcedistrictCode);

        int i = 0;
        do {
            // 在运单号列表里面取第一个值，设置给Text之后，然后在数据库删掉这个
            if (waybillCursor.moveToNext()) {
                String s1 = waybillCursor.getString(waybillCursor.getColumnIndex("waybillNos"));
                waybillList.add(s1);
            } else {
                waybillCursor.close();
                if (waybillList.size() <= 0) {
                    MyUtils.showText(mContext, "请更新运单号");
                }
                return;
            }

            if (i < waybillList.size()) {
                if (i != 0) {
                    showT("本地主单号没有了");
                    flag = false;
                    break;
                }
                waybillNo = (String) waybillList.get(0);
                mEdtWaybillNo.setText(waybillNo + "");
                mEdtConsigneeEmpCode.setText(MyApp.mEmpCode + "");
                Log.e("waybillNo", (new StringBuilder(String.valueOf(waybillNo))).append("|").append(i).toString());
                waybillNoDB.deleteRank(waybillNo);
                waybillList.clear();

                flag = false;
            }

            i++;
        } while (flag);

        flag = true;

        // 获取子单号
        Cursor childCursor = waybillNoDB.rawQueryRank("001");

        do {
            if (childCursor.moveToNext()) {
                String s2 = childCursor.getString(childCursor.getColumnIndex("waybillNos"));
                childList.add(s2);
            } else {
                childCursor.close();
                flag = false;
            }
        } while (flag);

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
        Cursor departmentCursor = DepartmentDBWrapper.getInstance(getApplication()).rawQueryRank(MyApp.mDeptCode);
        String distCode = "";
        String provinceCodes = "";
        if (departmentCursor.moveToNext()) {
            distCode = departmentCursor.getString(departmentCursor.getColumnIndex("districtCode"));
            Cursor districtCursor = DistrictDBWrapper.getInstance(getApplication()).rawQueryRank(distCode);
            if (districtCursor.moveToNext()) {
                provinceCodes = districtCursor.getString(districtCursor.getColumnIndex("provinceCode"));
            } else {
                districtCursor.close();
            }
        } else {
            departmentCursor.close();
        }
        mEdtChargeAgentFee.setFocusable(false);
        mEdtGoodsChargeFee.setFocusable(false);
        mSpnBankType.setFocusable(false);
        mSpnBankType.setClickable(false);
        mEdtBankNo.setFocusable(false);
	/*	mSpnPeriod.setFocusable(false);
		mSpnPeriod.setClickable(false);*/
        mEdtDeboursFee.setFocusable(false);
        mChkDeboursFlag.setFocusable(false);
        mChkDeboursFlag.setClickable(false);
        if (!"029".equals(distCode) && !"Henansheng".equals(provinceCodes)) {
            mChkDebours.setEnabled(false);
        } else {
            mChkDebours.setEnabled(true);
        }
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
        mRbPortToPort.setChecked(true);
        mRbDoorToDoor.setEnabled(false);
        mTxtInclude.setText(MyApp.mEmpName + MyApp.mDeptName);
        // 快递类型
        mRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mRbGeneral.getId()) {
                    productTypeCode = "B2";
                    limitTypeCode = "T5";
                    mEdtWaybillFee.setFocusable(false);
                    mRbPortToPort.setEnabled(true);
                    mRbPortToDoor.setEnabled(true);
                    mRbDoorToDoor.setEnabled(false);
                    if ("3".equals(serviceTypeCode)) {
                        mRbPortToPort.setChecked(true);
                    } else {
                        mRbPortToDoor.setChecked(true);
                    }
                    initDepartAdapter(seleDestCode);
                } else if (checkedId == mRbGeneral2.getId()) {
                    productTypeCode = "B3";
                    limitTypeCode = "T5";
                    mEdtWaybillFee.setFocusable(false);
                    mRbPortToPort.setEnabled(true);
                    mRbPortToDoor.setEnabled(true);
                    mRbDoorToDoor.setEnabled(false);
                    if ("3".equals(serviceTypeCode)) {
                        mRbPortToPort.setChecked(true);
                    } else {
                        mRbPortToDoor.setChecked(true);
                    }
                    initDepartAdapter(seleDestCode);
                } else if (checkedId == mRbExpress.getId()) {
                    productTypeCode = "B1";
                    limitTypeCode = "T4";
                    mSpnSetterMethod.setFocusable(true);
                    mSpnSetterMethod.setClickable(true);
                    mEdtWaybillFee.setFocusable(true);
                    mEdtWaybillFee.setFocusableInTouchMode(true);
                    mRbPortToPort.setEnabled(false);
                    mRbPortToDoor.setEnabled(false);
                    mRbDoorToDoor.setEnabled(true);
                    mRbDoorToDoor.setChecked(true);
                    initDepartAdapter(seleDestCode);
                    return;
                }
                // 重新计算运费
                priceFee();
            }
        });

        // 服务类型
        mRG2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mRbPortToPort.getId()) {
                    serviceTypeCode = "3";
                } else if (checkedId == mRbPortToDoor.getId()) {
                    serviceTypeCode = "2";
                } else if (checkedId == mRbDoorToDoor.getId()) {
                    serviceTypeCode = "1";
                }
                priceFee();
            }
        });


        // 付款方式
        mSpnPayMethod.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapterview, View view, int i, long l) {
                if (i == 0) {
                    paymentTypeCode = "2";
                    if (productTypeCode.equals("B2") || productTypeCode.equals("B3")) {
                        mSpnSetterMethod.setFocusable(false);
                        mSpnSetterMethod.setClickable(false);
                    }
                } else if (i == 1) {
                    paymentTypeCode = "1";
                    if (productTypeCode.equals("B1")) {
                        mSpnSetterMethod.setFocusable(true);
                        mSpnSetterMethod.setClickable(true);
                        return;
                    }
                }
                priceFee();
            }

            public void onNothingSelected(AdapterView adapterview) {
            }
        });

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

        mEdtConsignorPhone.setFocusableInTouchMode(true);
        mEdtConsignorPhone.requestFocus();
        // 寄方电话
        mEdtConsignorPhone.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

            public void onFocusChange(View view, boolean flag) {
                if (flag) {// 当焦点失去的时候
                    return;
                }

                if (!MyUtils.isNetworkConnect(ReceiveActivity.this)) {
                    showT("网络未连接");
                    return;
                }

                String conPhone = mEdtConsignorPhone.getText().toString().trim();
                String phoneNo1 = "";
                if (conPhone.length() == 0) {
                    return;
                } else {
                    phoneNo1 = conPhone.substring(0, 1);

                    if ("1".equals(phoneNo1) && conPhone.length() != 11) {
                        showT("请输入正确的手机号");
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
                            dialog = DialogUtils.createUpdataDialog(mContext);
                            dialog.show();
                        }

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            showT("网络未连接，请稍后再试");
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

                                mChkChargeAgent.setChecked(false);
                                List<CustList> custList = queryCustomer.getCustList();
                                if (custList == null || custList.size() <= 0) {
                                    showT("客户信息为空");
                                    mEdtConsignorContName.setFocusableInTouchMode(true);
                                    mEdtConsignorContName.requestFocus();
                                    custCode = "";
                                    bankType = "-1";
                                    bankNo = "";
                                    return;
                                }

                                CustList cust = custList.get(0);
                                consignorContName = cust.getCustName();
                                mEdtConsignorContName.setText(consignorContName);// 寄电姓名

                                custCode = cust.getCustCode();
                                if (custCode == null || custCode.equals("null") || custCode.isEmpty()) {
                                    custCode = "";
                                }
                                mEdMonthlyNo.setText(custCode);// 订单-后

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
                                    AddressList address = addressList.get(0);
                                    mEdtConsignorContName.setText(consignorContName);// 寄电姓名
                                    mEdtConsignorAddr.setText(address.getAddress() == null ? "" : address.getAddress());

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
                                        String adr = addressList.get(i).getAddress();
                                        map.put("address", adr);
                                        mCustomerSeleList.add(map);
                                    }
                                    initControls(1);
                                }

                            } else {
                                showT(queryCustomer.getError());
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
                if (!MyUtils.isNetworkConnect(ReceiveActivity.this)) {
                    showT("网络未连接 ");
                    return;
                }

                String conPhone = mEdtAddresseePhone.getText().toString().trim();
                String phoneNo1 = "";
                if (conPhone.length() == 0) {
                    return;
                } else {
                    phoneNo1 = conPhone.substring(0, 1);

                    if ("1".equals(phoneNo1) && conPhone.length() != 11) {
                        showT("请输入正确的手机号");
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
                            dialog = DialogUtils.createUpdataDialog(mContext);
                            dialog.show();
                        }

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            showT("网络未连接，请稍后再试");
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
                                    showT("客户信息为空");
                                    mEdtAddresseeContName.requestFocus();
                                    return;
                                }

                                CustList cust = custList.get(0);
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
                                    AddressList address = addressList.get(0);
                                    mEdtAddresseeContName.setText(cust.getCustName());// 收电姓名
                                    addresseeAddr = address.getAddress();
                                    mEdtAddresseeAddr.setText(addresseeAddr == null ? "" : addresseeAddr);

                                    addresseedeptCode = address.getDeptCode();
                                    teamCode = address.getTeamCode();
                                    initDestZoneCode(addresseedeptCode);
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
                                        addressList2 = addressList.get(i);
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
                                showT(queryCustomer.getError());
                            }
                        }
                    });

                }

            }
        });

        // 目的代码
        mSpnDestZoneCode.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seleDestCode = mSpnDestZoneCode.getSelectedItem().toString();
                if (!seleDestCode.isEmpty()) {
                    seleDestCode = seleDestCode.replaceAll("[^0-9]", "");
                    // setExpressText(seleDestCode);
                }
                initDepartAdapter(seleDestCode);
                priceFee();
                return;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 目的地址
        mSpnDestZoneName.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String destName = mSpnDestZoneName.getSelectedItem().toString();
                destZoneCode = (new StringBuilder(String.valueOf(destName.replaceAll("[^0-9]", "")))).append(destName
                        .replaceAll("[^a-zA-Z]", "")).toString();
                Cursor queryRank = DepartmentDBWrapper.getInstance(getApplication()).rawQueryRank(destZoneCode);
                do {
                    if (!queryRank.moveToNext()) {
                        queryRank.close();
                        priceFee();
                        return;
                    }
                    inFeeRate = queryRank.getString(queryRank.getColumnIndex("inFeeRate"));
                } while (true);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpnCategoryCode.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpnCategoryCode.getSelectedItem().equals("标准件")) {
                    consType = "C0";
                    mEdtRealWeightQty.setEnabled(true);
                    mEdtVolume.setEnabled(true);
                } else if (mSpnCategoryCode.getSelectedItem().equals("标准鞋")) {
                    consType = "C1";
                    mEdtRealWeightQty.setEnabled(false);
                    mEdtVolume.setEnabled(false);
                }
                priceFee();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });

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
                if ("C1".equals(consType)) {
                    waybillFeeCount(0.0);
                }
                childNoList = new ArrayList();
                int i;
                if (!quantity.isEmpty()) {
                    i = Integer.parseInt(quantity);
                } else {
                    i = 0;
                }

                // 如果小于一件，就使用主单号。否则继续
                if (i < 2) {
                    return; /* Loop/switch isn't completed */
                }

                int j = 0;
                do {
                    // 如果当前的大于数据库所有的子单号
                    if (j >= childList.size()) {
                        return;
                    }
                    System.out.println(j);
                    // 取得的，等于件数的时候就返回
                    if (i - 1 <= j) {
                        return;
                    }

                    childNoList.add(childList.get(j));

                    j++;
                } while (true);

            }
        });

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

                if ("C1".equals(consType)) {
                    return;
                }

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
                if (MyUtils.isEmpty(Volume)) {
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

		/*// 时期
		mSpnPeriod
				.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView adapterview,
							View view, int i, long l) {
						DaishouPrice();// 计算代收价格
					}

					public void onNothingSelected(AdapterView adapterview) {
					}

				});*/

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
                    MyUtils.showText(mContext, "请输入 小于等于300");
                    return;
                }
            }
        });

        // 四个按钮
        mBtnClear.setOnClickListener(this);
        mBtnRemark1.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
        mBtnSaveprint.setOnClickListener(this);

    }

	/*
	 * private void setExpressText(String destDeptCode){ if(
	 * !Arrays.asList(PROVICE_CODES).contains(sourcedistrictCode) &&
	 * !Arrays.asList(PROVICE_CODES).contains(destDeptCode)){
	 * mEdtWaybillFee.setFocusable(true);
	 * mEdtWaybillFee.setFocusableInTouchMode(true); }else{
	 * mEdtWaybillFee.setFocusable(false); } }
	 */

    /**
     * 计算运费
     */
    private void priceFee() {
        Volume = mEdtVolume.getText().toString().trim();
        realWeightQty = mEdtRealWeightQty.getText().toString();

        if (MyUtils.isEmpty(Volume)) {
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
                volume = Double.valueOf((Double.parseDouble(Volume) * 1000000D) / 4500D);
            } else {
                volume = Double.valueOf((Double.parseDouble(Volume) * 1000000D) / 6000D);
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
		/*String period = mSpnPeriod.getSelectedItem().toString()
				.replaceAll("[^0-9]", "");*/


		/*  boolean isHeNan = true;
		  if (!"Henansheng".equals(provinceCode) ||!"1".equals(period)) {
			if ("Henansheng".equals(provinceCode)) {
				  chargeFee = Double.valueOf(Double.parseDouble(charsequence) / 1000);
				  isHeNan = true;
			} else {
				isHeNan = false;
				 }
			  } else {
				  double d =(Double.parseDouble(charsequence) * 2) / 1000;
				  chargeFee =Double.valueOf(d); isHeNan = true;
				  }*/

        if (MyUtils.isHenan(provinceCode)) {
            chargeFee = Double.valueOf((Double.parseDouble(charsequence) * 3) / 1000);
        } else {
            // if (!isHeNan) {
            int i = 0;
            Cursor priceCursor = serviceProdPriceDB.rawQueryRank("SF04", "7");

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

        }

        if (chargeFee.doubleValue() > 100D) {
            mEdtChargeAgentFee.setText("100");
            return;
        } else {
            mEdtChargeAgentFee.setText(Math.ceil(chargeFee) + "");
            return;
        }
    }

    /**
     * 体积转重量
     */
    private Double getVolumeWeight(Double volume) {
        if (!distCodeType(mSestdeptCode, seleDestCode).equals("D4")) {
            return Double.valueOf((Double.parseDouble(Volume) * 1000000D) / 4500D);
        } else {
            return Double.valueOf((Double.parseDouble(Volume) * 1000000D) / 6000D);
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

        Quantity = mEdtQuantity.getText().toString().trim();

        // 根据重量计算上门费
        if (productTypeCode.equals("B3")) {
            fuelServiceFee = getShangmenFee(meterageWeightQty);
            mEdtFuelService.setText(String.valueOf(fuelServiceFee));
        } else {
            fuelServiceFee = 0.0;
            mEdtFuelService.setText(null);
        }
        if (meterageWeightQty != null && !"".equals(meterageWeightQty)) {
            double1 = Double.valueOf(Double.parseDouble(meterageWeightQty));
        }
        Double quantity = 0.0;
        if (Quantity != null && !"".equals(Quantity)) {
            quantity = Double.valueOf(Double.parseDouble(Quantity));
        }

        PriceInfoList = new ArrayList();
        String sestdeptCode = mSestdeptCode;
        String seleDest = seleDestCode;
        distCodeType(sestdeptCode, seleDest);
        String productCode = "";
        if ("B2".equals(productTypeCode) || "B3".equals(productTypeCode)) {
            productCode = "B2";
        }

        if ("C1".equals(consType)) {
            Cursor categoryCursor = CategoryDBWrapper.getInstance(getApplication()).rawQueryRank(sestdeptCode,
                    seleDest, serviceTypeCode, consType);
            //System.out.println(categoryCursor.getCount());
            int i;
            i = 0;
            do {
                if (categoryCursor.moveToNext()) {
                    PriceInfo itemdata = new PriceInfo();
                    itemdata.price = categoryCursor.getString(categoryCursor.getColumnIndex("price"));
                    itemdata.standardWeight = categoryCursor.getString(categoryCursor.getColumnIndex("standardWeight"));
                    PriceInfoList.add(itemdata);
                } else {

                    if (!categoryCursor.isClosed()) {
                        categoryCursor.close();
                    }

                    if (i >= PriceInfoList.size()) {
                        return;
                    }

                    PriceInfo item = (PriceInfo) PriceInfoList.get(i);


                    double d = Double.parseDouble(((PriceInfo) PriceInfoList.get(i)).price);

                    double standardWeight = Double.parseDouble(((PriceInfo) PriceInfoList.get(i)).standardWeight);

                    mEdtRealWeightQty.setText(String.valueOf(standardWeight * quantity));


                    mEdtWaybillFee.setText((new BigDecimal(decimalformat.format(Double.valueOf(d * quantity)))
                            .setScale(0, BigDecimal.ROUND_UP).toString()));
                    i++;
                }
            } while (true);
        } else {
            Cursor prodpriceCursor = ProdpriceDBWrapper.getInstance(getApplication()).rawQueryRank(sestdeptCode,
                    seleDest, serviceTypeCode, "C3", limitTypeCode, "B2");
            int i;
            i = 0;
            do {
                if (prodpriceCursor.moveToNext()) {
                    PriceInfo itemdata = new PriceInfo();
                    itemdata.startWeightQty = prodpriceCursor.getString(prodpriceCursor.getColumnIndex
                            ("startWeightQty"));
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

                    if (double1.doubleValue() > Double.parseDouble(item.startWeightQty) && double1.doubleValue() <
                            Double.parseDouble(item.maxWeightQty)) {
                        double d = Double.parseDouble(((PriceInfo) PriceInfoList.get(i)).basePrice);
                        double d2 = double1.doubleValue();
                        double d4 = Double.parseDouble(((PriceInfo) PriceInfoList.get(i)).startWeightQty);
                        double d5 = ((PriceInfo) PriceInfoList.get(i)).weightPriceQty.doubleValue();
                        double d6 = Math.min(Double.parseDouble(outFeeRate), Double.parseDouble(inFeeRate));

                        mEdtWaybillFee.setText((new BigDecimal(decimalformat.format(Double.valueOf((d + (d2 - d4) *
                                d5) * d6)))).setScale(0, BigDecimal.ROUND_UP).toString());
                        return;
                    }

                    if (Double.parseDouble(item.startWeightQty) >= double1.doubleValue() && double1.doubleValue() >
                            0.0D) {
                        double d1 = Double.parseDouble(((PriceInfo) PriceInfoList.get(i)).basePrice);
                        double d3 = Math.min(Double.parseDouble(outFeeRate), Double.parseDouble(inFeeRate));

                        mEdtWaybillFee.setText((new BigDecimal(decimalformat.format(Double.valueOf(d1 * d3))))
                                .setScale(0, BigDecimal.ROUND_UP).toString());
                        return;
                    }
                    i++;
                }
            } while (true);
        }

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

    private void initControls(final int phoneType) {
        try {
            View view = getLayoutInflater().inflate(R.layout.popupwindow_list, null);
            @SuppressWarnings("unchecked") SimpleAdapter simpleadapter = new SimpleAdapter(this, mCustomerSeleList, R
                    .layout.popupwindow_item, new String[]{"name", "tel", "address"}, new int[]{R.id.item_pop_name, R.id
                    .item_pop_phone, R.id.item_pop_adress});
            ListView listview = (ListView) view.findViewById(R.id.listview);
            listview.setAdapter(simpleadapter);
            listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView adapterview, View view1, int i, long l) {

                    Map map = (Map) mCustomerSeleList.get(i);
                    switch (phoneType) {
                        case 1: {// 寄电
                            consignorAddr = (String) map.get("address");
                            mEdtConsignorAddr.setText(consignorAddr == null ? "" : consignorAddr);

                            consignorContName = (String) map.get("name");
                            mEdtConsignorContName.setText(consignorContName);// 寄电姓名

                            // 收电，获取焦点
                            getPhoneRequestFocus();
                            break;
                        }
                        case 2: {// 收电
                            addresseeAddr = (String) map.get("address");
                            mEdtAddresseeAddr.setText(addresseeAddr);

                            addresseeContName = (String) map.get("name");
                            mEdtAddresseeContName.setText(addresseeContName);

                            String deptCode = (String) map.get("deptCode");
                            teamCode = (String) map.get("teamCode");
                            if (teamCode == null) {
                                teamCode = "";
                            }
                            initDestZoneCode(deptCode);

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

    /**
     * 收电，获取焦点
     */
    private void getPhoneRequestFocus() {
        mEdtAddresseePhone.setFocusableInTouchMode(true);
        mEdtAddresseePhone.requestFocus();
    }

    private void initDestZoneCode(String s) {
        Cursor departmentCursor = DepartmentDBWrapper.getInstance(ReceiveActivity.this).rawQueryRank(s);

        if (departmentCursor.moveToNext()) {

            destdistrictCode = departmentCursor.getString(departmentCursor.getColumnIndex("districtCode"));
        }
        departmentCursor.close();

        ArrayList arraylist;
        int i;
        int k;
        Cursor queryRank = DistrictDBWrapper.getInstance(getApplication()).rawQueryRank();

        arraylist = new ArrayList();
        k = 0;
        i = 0;

        do {

            if (!queryRank.moveToNext()) {
                queryRank.close();
                mSpnDestZoneCode.setSelection(k, true);
                return;
            }

            String s1 = queryRank.getString(queryRank.getColumnIndex("typeCode"));
            String s2 = queryRank.getString(queryRank.getColumnIndex("distCode"));
            String s3 = queryRank.getString(queryRank.getColumnIndex("distName"));
            if (s1.equals("3")) {
                arraylist.add((new StringBuilder("[")).append(s2).append("]").append(s3).toString());
                int j = i + 1;
                i = j;
                if (destdistrictCode.equals(s2)) {
                    k = j;
                    i = j;
                }
            }
        } while (true);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void initDepartAdapter(String s) {
        Cursor districtCursor = DepartmentDBWrapper.getInstance(mContext).rawQueryRankdistrictCode(s);
        ArrayList arraylist = new ArrayList();
        int i = 0;
        int j = 0;
        String s1;
        String s2;
        String s3;
        String s4;
        int k;
        do {
            if (!districtCursor.moveToNext()) {
                districtCursor.close();

                ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, (String[])
                        arraylist.toArray(new String[arraylist.size()]));
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpnDestZoneName.setPrompt("网点");
                mSpnDestZoneName.setAdapter(arrayAdapter);

                if (!addresseedeptCode.isEmpty() && arraylist.size() > 0) {
                    mSpnDestZoneName.setSelection(j, true);
                }
                return;
            }
            s1 = districtCursor.getString(districtCursor.getColumnIndex("deptCode"));
            s2 = districtCursor.getString(districtCursor.getColumnIndex("deptName"));
            s3 = districtCursor.getString(districtCursor.getColumnIndex("typeLevel"));
            s4 = districtCursor.getString(districtCursor.getColumnIndex("typeCode"));
            if (s3.equals("4") && productTypeCode.endsWith("B1")) {
                if (s4.equals("DT06") || s4.equals("DT08")) {
                    arraylist.add((new StringBuilder("[")).append(s1).append("]").append(s2).toString());
                    k = j;
                    if (addresseedeptCode.equals(s1)) {
                        k = j;
                        if (!addresseedeptCode.isEmpty()) {
                            k = i;
                        }
                    }
                    i++;
                    j = k;
                }

            } else if (s3.equals("4") && (productTypeCode.endsWith("B2") || productTypeCode.equals("B3")) &&
                    (s4.equals("DT07") || s4.equals("DT08"))) {
                arraylist.add((new StringBuilder("[")).append(s1).append("]").append(s2).toString());
                k = j;
                if (addresseedeptCode.equals(s1)) {
                    k = j;
                    if (!addresseedeptCode.isEmpty()) {
                        k = i;
                    }
                }
                i++;
                j = k;
            }
        } while (true);
    }

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
                    mSpnBankType.setFocusable(true);
                    mSpnBankType.setClickable(true);
                    mEdtBankNo.setFocusable(true);
                    mEdtBankNo.setFocusableInTouchMode(true);
				/*if(!MyUtils.isHenan(provinceCode)){
					mSpnPeriod.setFocusable(true);
					mSpnPeriod.setClickable(true);
					mSpnPeriod.setSelection(mPeriodSize - 1, true);
				}else{
					mSpnPeriod.setFocusable(false);
					mSpnPeriod.setClickable(false);
					mSpnPeriod.setSelection(0, true);
				}*/

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
                } else {
                    mSpnBankType.setSelection(0, true);
                    mEdtChargeAgentFee.setFocusable(false);
                    mEdtGoodsChargeFee.setFocusable(false);
                    mSpnBankType.setFocusable(false);
                    mSpnBankType.setClickable(false);
                    mEdtBankNo.setFocusable(false);
			/*	mSpnPeriod.setFocusable(false);
				mSpnPeriod.setClickable(false);*/
                    mEdtChargeAgentFee.setText("0");
                    mEdtGoodsChargeFee.setText("0");
                    mEdtBankNo.getEditableText().clear();
                    mEdtRemark.requestFocus();
                    return;
                }
            }
            case R.id.chb_accept_inputorder_debours: {// 垫付
                if (isChecked) {
                    mChkDebours.setFocusable(true);
                    mEdtDeboursFee.setFocusable(true);
                    mEdtDeboursFee.setFocusableInTouchMode(true);
                    mEdtDeboursFee.requestFocus();
                    mChkDeboursFlag.setFocusable(true);
                    mChkDeboursFlag.setClickable(true);
                    return;
                } else {
                    mEdtDeboursFee.setFocusable(false);
                    mChkDeboursFlag.setFocusable(false);
                    mChkDeboursFlag.setClickable(false);
                    mChkDeboursFlag.setChecked(false);
                    mEdtDeboursFee.getEditableText().clear();
                    deboursFlag = "0";
                    mEdtRemark.requestFocus();
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
                }
                break;
            }
            case R.id.chb_deliverFee: {// 派送
                if (isChecked) {
                    mEdtDeliverFee.setFocusable(true);
                    mEdtDeliverFee.setFocusableInTouchMode(true);
                    mEdtDeliverFee.requestFocus();
                } else {
                    mEdtDeliverFee.setFocusable(false);
                    mEdtDeliverFee.getEditableText().clear();
                    mEdtRemark.requestFocus();
                    return;
                }
                break;
            }
            case R.id.chb_waitNotifyFee: {// 等通知
                if (isChecked) {
                    mEdtWaitNotifyFee.setText("10");
                    return;
                } else {
                    mEdtWaitNotifyFee.getEditableText().clear();
                    mEdtRemark.requestFocus();
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
            case R.id.btn_accept_inputorder_clear: {// 清空备注
                mEdtRemark.setText(null);
                break;
            }
            case R.id.btn_accept_inputorder_remark1: {// 备注-变质
                mEdtRemark.getEditableText().clear();
                mEdtRemark.setText("变质损坏不赔 ");
                break;
            }
            case R.id.btn_accept_inputorder_next: {// 保存

                if (System.currentTimeMillis() - oldTime < 3000) {
                    return;
                }
                oldTime = System.currentTimeMillis();

                if (addWayBillNo()) {
                    // 上传数据
                    UploadWaybill.getUploadActivity().upload(mContext, 0, new TextView(mContext));
                    finish();
                    startActivity(new Intent(ReceiveActivity.this, ReceiveActivity.class));
                    return;
                } else {
                    showT("录单失败，请重新录单");
                }
                break;
            }
            case R.id.btn_accept_inputorder_savePrint: {// 保存并打印

                if (System.currentTimeMillis() - oldTime < 3000) {
                    return;
                }
                oldTime = System.currentTimeMillis();

                statusBox = new StatusBox(this, mBtnSaveprint);
                //queryDeliverCommission();
                if (!addWayBillNo()) {
                    showT("录单失败，请重新录单");
                    return;
                }
                // 上传数据
                UploadWaybill.getUploadActivity().upload(mContext, 0, new TextView(mContext));
                if (TextUtils.isEmpty(biaoqianAddress)) {
                    toastTxt("打印机未连接");
                } else {
                    isPrint();
                }

                finish();
                startActivity(new Intent(ReceiveActivity.this, ReceiveActivity.class));
                return;

            }

            default:
                break;
        }
    }

    private void isPrint() {
        Cursor districtCursor = DistrictDBWrapper.getInstance(ReceiveActivity.this).rawQueryRank(seleDestCode);

        if (districtCursor.moveToNext()) {
            addresseeDistName = districtCursor.getString(districtCursor.getColumnIndex("distName"));
        } else {
            districtCursor.close();
        }

        Cursor departmentCursor = DepartmentDBWrapper.getInstance(getApplication()).rawQueryRank(destZoneCode);

        if (departmentCursor.moveToNext()) {
            mDestZoneAddress = departmentCursor.getString(departmentCursor.getColumnIndex("deptAddress"));
            partitionName = departmentCursor.getString(departmentCursor.getColumnIndex("partitionName"));
            outsiteName = departmentCursor.getString(departmentCursor.getColumnIndex("outsiteName"));
            if (!TextUtils.isEmpty(outsiteName) && outsiteName.length() >= 2) {
                outsiteName = outsiteName.substring(0, 2);
            }
        } else {
            departmentCursor.close();
        }
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
        Print0();

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
        DecimalFormat df = new DecimalFormat("#.00");

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
        if (MyUtils.isEmpty(deliverFee) || "0".equals(deliverFee)) {
            deliverFee = "0";
            //deliverFeeCommissio = commission;
        }/*else{
			deliverFeeCommissio =Double.parseDouble(deliverFee) + commission;
		}*/

		/*if(waybillFee!=null){
			factWaybillFee =  Double.valueOf(df.format(Double.parseDouble(waybillFee) - commission));
		}*/

        Double double1;
        double1 = Double.valueOf(0.0D);
        Object obj1 = Double.valueOf(Double.parseDouble(waybillFee) + Double.parseDouble(signBackFee) + Double
                .parseDouble(waitNotifyFee) + Double.parseDouble(deboursFee) + Double.parseDouble(insuranceFee) + Double
                .parseDouble(deliverFee) + fuelServiceFee);
        double d = ((Double) (obj1)).doubleValue();
        double d1 = Double.parseDouble(goodsChargeFee);
        // double d2 = Double.parseDouble(deboursFee)+fuelServiceFee;
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

        // ================================ 客户联 ================================

        if (!zpSDK.zp_page_create(80D, 61D)) {
            statusBox.Close();
            return;
        }

        zpSDK.TextPosWinStyle = false;
        zpSDK.zp_draw_rect(0.0D, 0.0D, 72D, 53D, 3);
        zpSDK.zp_draw_line(0.0D, 4D, 72D, 4D, 3);
        zpSDK.zp_draw_line(0.0D, 8D, 72D, 8D, 3);
        zpSDK.zp_draw_line(0.0D, 12D, 72D, 12D, 3);
        zpSDK.zp_draw_line(0.0D, 16D, 72D, 16D, 3);
        zpSDK.zp_draw_line(0.0D, 20D, 72D, 20D, 3);
        zpSDK.zp_draw_line(0.0D, 24D, 72D, 24D, 3);
        zpSDK.zp_draw_line(0.0D, 28D, 72D, 28D, 3);
        zpSDK.zp_draw_line(0.0D, 32D, 72D, 32D, 3);
        zpSDK.zp_draw_line(0.0D, 36D, 72D, 36D, 3);
        zpSDK.zp_draw_line(0.0D, 40D, 72D, 40D, 3);
        zpSDK.zp_draw_line(0.0D, 45D, 72D, 44D, 3);
        //zpSDK.zp_draw_line(0.0D, 48D, 72D, 48D, 3);
        zpSDK.zp_draw_line(12D, 0.0D, 12D, 8D, 3);

	/*	zpSDK.zp_draw_text_ex(0.9D, 3D, "聚", "宋体",
				3D, 0, true, false, false);
		zpSDK.zp_draw_text_ex(0.9D, 7D, "信", "宋体",
				3D, 0, true, false, false);*/

//		zpSDK.zp_draw_text_ex(1.0D, -2.0D, "JXE 聚信", "宋体",
//				6.7999999999999998D, 0, true, true, false);
		/*zpSDK.zp_draw_text_ex(4.0D, 3.5D, consignedTm.substring(5, 10), "宋体",
				3D, 0, true, false, false);
		zpSDK.zp_draw_text_ex(4.0D, 7.5D, consignedTm.substring(11, 16), "宋体",
				3D, 0, true, false, false);*/
        zpSDK.zp_draw_text_ex(1.0D, 3D, consignedTm.substring(5, 10), "宋体", 3D, 0, true, true, false);
        zpSDK.zp_draw_text_ex(1.0D, 7D, consignedTm.substring(11, 16), "宋体", 3D, 0, true, true, false);
        // if (!"02911".equals(sourcedistrictCode)) {

        if (productTypeCode.equals("B1")) {
            zpSDK.zp_draw_text_ex(13D, 3D, (new StringBuilder("快递  运单号")).append(waybillNo).append(" ").append
                    (sourceZoneCode).append(" ").append(consignorContName).toString(), "宋体", 3D, 0, true, false, false);
        } else if (productTypeCode.equals("B2")) {
            zpSDK.zp_draw_text_ex(13D, 3D, (new StringBuilder("普货  运单号")).append(waybillNo).append(" ").append
                    (sourceZoneCode).append(" ").append(consignorContName).toString(), "宋体", 3D, 0, true, false, false);
        } else if (productTypeCode.equals("B3")) {
            zpSDK.zp_draw_text_ex(13D, 3D, (new StringBuilder("普快  运单号")).append(waybillNo).append(" ").append
                    (sourceZoneCode).append(" ").append(consignorContName).toString(), "宋体", 3D, 0, true, false, false);
        }

		/*
		 * } else { zpSDK.zp_draw_text_ex(13D, 11D, (new
		 * StringBuilder("普快  运单号")
		 * ).append(waybillNo).append(" ").append(sourceZoneCOode).append(" ")
		 * .append(consignorContName).toString(), "宋体", 3D, 0, true, false,
		 * false);
		 *
		 * }
		 */

        if (!MyUtils.isEmpty(addresseeMobile)) {
            if (!MyUtils.isEmpty(addresseePhone)) {
                zpSDK.zp_draw_text_ex(13D, 7D, (new StringBuilder(String.valueOf(addresseeDistName))).append(" ")
                        .append(addresseeContName).append(" ").append(addresseeMobile).append(" 取件费  ").append
                                (fuelServiceFee).toString(), "宋体", 3D, 0, true, false, false);
            } else {
                zpSDK.zp_draw_text_ex(13D, 7D, (new StringBuilder(String.valueOf(addresseeDistName))).append(" ")
                        .append(addresseeContName).append(" ").append(addresseeMobile).append(" 取件费  ").append
                                (fuelServiceFee).toString(), "宋体", 3D, 0, true, false, false);
            }
        } else {
            zpSDK.zp_draw_text_ex(13D, 7D, (new StringBuilder(String.valueOf(addresseeDistName))).append(" ").append
                            (addresseeContName).append(" ").append(addresseePhone).append(" 取件费  ").append(fuelServiceFee).toString()
                    , "宋体", 3D, 0, true, false, false);
        }

        zpSDK.zp_draw_text_ex(1.0D, 11D, (new StringBuilder(String.valueOf(consName))).append(" ").append(Quantity)
                        .append("件 重量").append(meterageWeightQty).append("kg 运费").append(waybillFee).append("  垫付").append(deboursFee)
						/*.append(" 派送费").append(deliverFee)*/.toString(), "宋体", 2.7999999999999998D, 0, true, false,
                false);
        zpSDK.zp_draw_text_ex(1.0D, 15D, (new StringBuilder("声明价值")).append(insuranceAmount).append(" 保价费").append
                        (insuranceFee).append("元  回单费").append(signBackFee).append(" ").append(signBackNo).toString(), "宋体", 3D, 0,
                true, false, false);

		/*if (MyUtils.isHenan(provinceCode)) {
			zpSDK.zp_draw_text_ex(1.0D, 22.5D, "货款买方直接转给卖方", "宋体", 6D, 0, true, false, false);
		} else {
			zpSDK.zp_draw_text_ex(1.0D, 22.5D, "一  票   送  货  到   店  铺", "宋体", 6D, 0, true, false, false);
		}*/
        //zpSDK.zp_draw_text_ex(1.0D, 22.5D, "一  票   送  货   到   店  铺", "宋体", 6D, 0, true, false, false);


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

        String s = "";
        if (MyUtils.isHenan(provinceCode)) {
            s = "货款";
        } else {
            s = "代收款";
        }

        zpSDK.zp_draw_text_ex(1.0D, 19D, (new StringBuilder(s)).append(goodsChargeFee).append(" 服务费").append
                        (chargeAgentFee).append(" ").append(bankT).append(" ").append(" 等通知费").append(waitNotifyFee).toString(),
                "宋体", 3D, 0, true, false, false);
        zpSDK.zp_draw_text_ex(1.0D, 23D, (new StringBuilder("银行账号 ")).append(bankNo)/*.append(transferDays).append
        ("天转")*/.toString(), "宋体", 3D, 0, true, false, false);

        zpSDK.zp_draw_text_box(1.0D, 27.5D, 72D, 3D, "客户须知 1：货物应当申明价值， " +
                "每件货物遗失或损毁，承运人按总保价的平均价值赔偿。2：每票总价值如果超过5万元，不予承运。3：出现异常，90天内联系处理，超期不予处理", "宋体", 2.7D, 0, true, false, false);

        if (!paymentTypeCode.equals("1")) {
            if (!paymentTypeCode.equals("2")) {

            } else {
                zpSDK.zp_draw_text_ex(1.0D, 39.5D, (new StringBuilder("到付  ")).append(double1).append("元").toString()
                        , "宋体", 3D, 0, true, false, false);
            }
        } else {
            zpSDK.zp_draw_text_ex(1.0D, 39.5D, (new StringBuilder("寄付  ")).append(obj1).append("元").toString(), "宋体",
                    3D, 0, true, false, false);
        }

        zpSDK.zp_draw_text_ex(39.5D, 39.5D, "客服热线:", "宋体", 3D, 0, true, false, false);

        zpSDK.zp_draw_text_ex(1.0D, 44D, (new StringBuilder("备注:")).append(waybillRemk).toString(), "宋体", 3D, 0,
                true, false, false);

        zpSDK.zp_draw_text_ex(45D, 44D, "签字", "宋体", 3.8D, 0, true, false, false);

        zpSDK.zp_draw_text_ex(52.5D, 40D, "4008-111115", "宋体", 3.2000000000000002D, 0, true, false, false);
        // zpSDK.zp_draw_text_ex(60.5D, 52D, "111115", "宋体",
        // 3.2000000000000002D, 0, true, false, false);

        zpSDK.zp_draw_text_ex(1.0D, 51D, (new StringBuilder(String.valueOf("派送费"))).append(" ").append(deliverFee)
                .toString(), "宋体", 5, 0, true, false, false);

        zpSDK.zp_page_print(false);
        zpSDK.zp_page_clear();

        zpSDK.zp_goto_mark_label(120);
        zpSDK.zp_page_free();

        // ================================ 标签 ================================

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
        zpSDK.zp_draw_line(50D, 48D, 72D, 48D, 3);
        zpSDK.zp_draw_line(10D, 0.0D, 10D, 14D, 3);
        zpSDK.zp_draw_line(6D, 14D, 6D, 34D, 3);
        zpSDK.zp_draw_line(6D, 14D, 6D, 34D, 3);
        zpSDK.zp_draw_line(50D, 34D, 50D, 53D, 3);
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

        if (mChbIsLook.isChecked()) {
            zpSDK.zp_draw_text_box(67.5D, 19D, 5D, 4D, "已验视", "宋体", 4D, 0, false, false, false);
        }

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

	/*	if (!"02911".equals(sourcedistrictCode)) {

			if (productTypeCode.equals("B1")) {
				zpSDK.zp_draw_text_ex(53D, 46.5D, "快", "宋体", 14D, 0, true,
						false, false);
			} else if (productTypeCode.equals("B2")
					|| productTypeCode.equals("B3")) {
				zpSDK.zp_draw_text_ex(53D, 46.5D, "普", "宋体", 14D, 0, true,
						false, false);
			}
		} else {
			zpSDK.zp_draw_text_ex(53D, 46.5D, "普", "宋体", 14D, 0, true, false,
					false);
		}*/

        if (productTypeCode.equals("B1")) {
            zpSDK.zp_draw_text_ex(51D, 46.5D, "快递", "宋体", 10D, 0, true, false, false);
        } else if (productTypeCode.equals("B2")) {
            zpSDK.zp_draw_text_ex(51D, 46.5D, "普货", "宋体", 10D, 0, true, false, false);
        } else if (productTypeCode.equals("B3")) {
            zpSDK.zp_draw_text_ex(51D, 46.5D, "普快", "宋体", 10D, 0, true, false, false);
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
            zpSDK.zp_draw_line(50D, 48D, 72D, 48D, 3);
            zpSDK.zp_draw_line(10D, 0.0D, 10D, 14D, 3);
            zpSDK.zp_draw_line(6D, 14D, 6D, 34D, 3);
            zpSDK.zp_draw_line(6D, 14D, 6D, 34D, 3);
            zpSDK.zp_draw_line(50D, 34D, 50D, 53D, 3);
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

            if (mChbIsLook.isChecked()) {
                zpSDK.zp_draw_text_box(67.5D, 19D, 5D, 4D, "已验视", "宋体", 4D, 0, false, false, false);
            }

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

            // if (!"02911".equals(sourcedistrictCode)) {

            if (productTypeCode.equals("B1")) {
                zpSDK.zp_draw_text_ex(51D, 46.5D, "快递", "宋体", 10D, 0, true, false, false);
            } else if (productTypeCode.equals("B2")) {
                zpSDK.zp_draw_text_ex(51D, 46.5D, "普货", "宋体", 10D, 0, true, false, false);
            } else if (productTypeCode.equals("B3")) {
                zpSDK.zp_draw_text_ex(51D, 46.5D, "普快", "宋体", 10D, 0, true, false, false);
            }
			/*
			 * } else { zpSDK.zp_draw_text_ex(53D, 46.5D, "普", "宋体", 14D, 0,
			 * true, false, false); }
			 */

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
    private boolean addWayBillNo() {
        Double insurance;
        Double double1;

        consignedTm = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(new Date());
        orderNo = mEdtOrderNo.getText().toString().trim();
        consignorContName = mEdtConsignorContName.getText().toString().trim();
        consignorAddr = mEdtConsignorAddr.getText().toString().trim();
        addresseeContName = mEdtAddresseeContName.getText().toString().trim();
        addresseeAddr = mEdtAddresseeAddr.getText().toString().trim();
        Quantity = mEdtQuantity.getText().toString().trim();
        consName = mSpnConsName.getSelectedItem().toString().trim();
        realWeightQty = mEdtRealWeightQty.getText().toString().trim();
        Volume = mEdtVolume.getText().toString().trim();
        waybillFee = mEdtWaybillFee.getText().toString().trim();
        custIdentityCard = mEdtCustIdentityCard.getText().toString().trim();

        if ("C1".equals(consType)) {
            meterageWeightQty = realWeightQty;
        }

        if (!mChkChargeAgent.isChecked()) {
            bankType = "";
            bankNo = "";
            transferDays = "0";
        } else {
            chargeAgentFee = mEdtChargeAgentFee.getText().toString().trim();
            goodsChargeFee = mEdtGoodsChargeFee.getText().toString().trim();
            bankNo = mEdtBankNo.getText().toString().trim();
            //transferDays = mSpnPeriod.getSelectedItem().toString().trim();

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
            MyUtils.showText(mContext, "运单号不能为空");
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
        if (MyUtils.isEmpty(consignorAddr) || consignorAddr.length() <= 2) {
            toastTxt("寄件地址不能为空或小于2个字");
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
        if (!productTypeCode.equals("B1") || insurance.doubleValue() <= 50000D) {
        } else {
            toastTxt("保价费不能大于50000元");
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
	/*	if (Integer.parseInt(Quantity) <= 19) {
		} else {
			toastTxt("件数不能大于19件");
			return false;
		}*/
        if (!MyUtils.isEmpty(waybillFee) && !waybillFee.equals("0")) {
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
		/*if(destZoneCode!=null && productTypeCode!=null&&meterageWeightQty!=null){
			queryDeliverCommission();
		}*/
        if (Quantity.equals("1")) {
            acceptInputDB.insertRank(waybillNo, sourceZoneCode, destZoneCode, custCode, consignorCompName,
                    consignorAddr, consignorPhone, consignorContName, consignorMobile, addresseeCompName, addresseeAddr,
                    addresseePhone, addresseeContName, addresseeMobile, consName, Quantity, Volume, realWeightQty,
                    meterageWeightQty, consigneeEmpCode, consignedTm, paymentTypeCode, settlementTypeCode, waybillFee,
                    goodsChargeFee, chargeAgentFee, bankNo, bankType, transferDays, insuranceAmount, insuranceFee,
                    deboursFee, deboursFlag, productTypeCode, orderNo, teamCode, discountExpressFee, MyApp.mEmpCode,
                    distanceTypeCode, MyApp.mEmpName, "02", "4", 0, signBackFee, signBackNo, signBackCount, signBackSize,
                    signBackReceipt, signBackSeal, signBackIdentity, waitNotifyFee, deliverFee, waybillRemk, serviceTypeCode,
                    custIdentityCard, fuelServiceFee, consType);
            return true;
        }

        if (Integer.parseInt(Quantity) <= 1) {
            toastTxt("数据添加失败");
            return false;
        } else {
            acceptInputDB.insertRank(waybillNo, sourceZoneCode, destZoneCode, custCode, consignorContName,
                    consignorAddr, consignorPhone, consignorContName, consignorMobile, addresseeCompName, addresseeAddr,
                    addresseePhone, addresseeContName, addresseeMobile, consName, Quantity, Volume, realWeightQty,
                    meterageWeightQty, consigneeEmpCode, consignedTm, paymentTypeCode, settlementTypeCode, waybillFee,
                    goodsChargeFee, chargeAgentFee, bankNo, bankType, transferDays, insuranceAmount, insuranceFee,
                    deboursFee, deboursFlag, productTypeCode, orderNo, teamCode, discountExpressFee, MyApp.mEmpCode,
                    distanceTypeCode, MyApp.mEmpName, "02", "4", 0, signBackFee, signBackNo, signBackCount, signBackSize,
                    signBackReceipt, signBackSeal, signBackIdentity, waitNotifyFee, deliverFee, waybillRemk, serviceTypeCode,
                    custIdentityCard, fuelServiceFee, consType);
        }
        int i = 0;

        do {

            if (i >= childNoList.size()) {
                return true;
            }
            childNoListDB.insertRank(waybillNo, (String) childNoList.get(i), (new StringBuilder(String.valueOf(i)))
                    .toString(), 0);
            waybillNoDB.deleteRank((String) childNoList.get(i));
            i++;
        } while (true);

    }

    /**
     * 查询单号
     **/
	/*private void queryDeliverCommission() {

		if(meterageWeightQty==null||destZoneCode == null || productTypeCode ==null){
			return ;
		}

		if (MyUtils.isNetworkConnect(ReceiveActivity.this)) {
			HttpUtils httpUtils = new HttpUtils();
			RequestParams params = new RequestParams();
			params.addBodyParameter("meterageWeightQty", inputDataInfo.meterageWeightQty);
			params.addBodyParameter("deptCode", inputDataInfo.destZoneCode);
			params.addBodyParameter("productTypeCode", inputDataInfo.productTypeCode);

			StringBuilder builder = new StringBuilder();
			builder.append("?").append("meterageWeightQty=").append(meterageWeightQty).append("&")
			.append("deptCode=").append(destZoneCode).append("&").append("productTypeCode=").append(productTypeCode);
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
							showT("网络异常，请稍候再试");
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
										ReceiveActivity.this,
										i_Send.error);
								System.out.println("**************4");

							}
						}

					});

		} else {
			Toast.makeText(ReceiveActivity.this,
					"无网络连接，请稍后再试");
		}
	}
*/

    private void toastTxt(String s) {
        showT(s);
    }
}


