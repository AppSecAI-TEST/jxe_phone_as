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
import com.eruitong.model.InputDataInfo;
import com.eruitong.model.PriceInfo;
import com.eruitong.model.net.I_QueryCustomer;
import com.eruitong.print.bluetooth.BluetoothPrintTool;
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

    public String biaoqianAddress;

    int TYPE_METHOD;
    int ADDRESSEE_TYPE;
    int CONSIGNOR_TYPE;

    public String[] PROVICE_CODES = new String[]{"029", "371"};
    private ServiceProdPriceDBWrapper serviceProdPriceDB;

    private InputDataInfo mInfo;

    PopupWindow popupWindow;
    ArrayList mCustomerSeleList;

    String addresseedeptCode;
    String destdistrictCode;
    String inFeeRate;
    String limitTypeCode;
    String lockFee;
    String outFeeRate;
    String seleDestCode;
    StatusBox statusBox;
    Timer timer;
    View viewType;
    ArrayList waybillList;
    String mDeptAddress;
    String mDestZoneAddress;
    String mSestdeptCode;
    String custIdentityCard;

    /**
     * 品类名称
     **/
    String consType;

    /**
     * 总的子单号
     **/
    List<String> childList;
    /**
     * 使用的子单号
     **/
    List<String> childNoList;
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
        mInfo = new InputDataInfo();
        acceptInputDB = AcceptInputDBWrapper.getInstance(getApplication());
        waybillNoDB = WaybillNoDBWrapper.getInstance(getApplication());
        childNoListDB = childNoListDBWrapper.getInstance(getApplication());
        SharedPreferences preferences = getSharedPreferences("cache_data", 0);
        mPeriodSize = getResources().getStringArray(R.array.period).length;
        mInfo.consigneeEmpCode = preferences.getString("mStaffIdTxt", "");

        CONSIGNOR_TYPE = 1;
        ADDRESSEE_TYPE = 2;
        mDeptAddress = "";
        destdistrictCode = "";
        addresseedeptCode = "";
        mInfo.serviceTypeCode = "3";
        limitTypeCode = "";
        mDestZoneAddress = "";
        lockFee = "";
        outFeeRate = "";
        inFeeRate = "";
        seleDestCode = "";
        mSestdeptCode = "";

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
            mInfo.sourcedistrictCode = departmentCursor.getString(departmentCursor.getColumnIndex("districtCode"));
            lockFee = departmentCursor.getString(departmentCursor.getColumnIndex("lockFee"));
            outFeeRate = departmentCursor.getString(departmentCursor.getColumnIndex("outFeeRate"));
            String s = departmentCursor.getString(departmentCursor.getColumnIndex("typeCode"));
            Cursor districtCursor = DistrictDBWrapper.getInstance(getApplication()).rawQueryRank(mInfo
                    .sourcedistrictCode);
            if (districtCursor.moveToNext()) {
                mInfo.consignorDistName = districtCursor.getString(districtCursor.getColumnIndex("distName"));
                mInfo.provinceCode = districtCursor.getString(districtCursor.getColumnIndex("provinceCode"));
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

        Cursor districtCursor = DistrictDBWrapper.getInstance(getApplication()).rawQueryRank(mInfo.sourcedistrictCode);
        if (districtCursor.moveToNext()) {
            mInfo.consignorDistName = districtCursor.getString(districtCursor.getColumnIndex("distName"));
            mInfo.provinceCode = districtCursor.getString(districtCursor.getColumnIndex("provinceCode"));
        } else {
            districtCursor.close();
        }

        serviceProdPriceDB = ServiceProdPriceDBWrapper.getInstance(getApplication());
        mInfo.sourceZoneCode = MyApp.mDeptCode;

        boolean flag = true;
        // 获取主单号
        Cursor waybillCursor = waybillNoDB.rawQueryRank(mInfo.sourcedistrictCode);

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
                mInfo.waybillNo = (String) waybillList.get(0);
                mEdtWaybillNo.setText(mInfo.waybillNo + "");
                mEdtConsigneeEmpCode.setText(MyApp.mEmpCode + "");
                Log.e("waybillNo", (new StringBuilder(String.valueOf(mInfo.waybillNo))).append("|").append(i)
                        .toString());
                waybillNoDB.deleteRank(mInfo.waybillNo);
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
            if (i >= signBackNoList.size()) {
                isConnect();
                return;
            }
            mInfo.signBackNo = (String) signBackNoList.get(0);
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
                    mInfo.productTypeCode = "B2";
                    limitTypeCode = "T5";
                    mEdtWaybillFee.setFocusable(false);
                    mRbPortToPort.setEnabled(true);
                    mRbPortToDoor.setEnabled(true);
                    mRbDoorToDoor.setEnabled(false);
                    if ("3".equals(mInfo.serviceTypeCode)) {
                        mRbPortToPort.setChecked(true);
                    } else {
                        mRbPortToDoor.setChecked(true);
                    }
                    initDepartAdapter(seleDestCode);
                } else if (checkedId == mRbGeneral2.getId()) {
                    mInfo.productTypeCode = "B3";
                    limitTypeCode = "T5";
                    mEdtWaybillFee.setFocusable(false);
                    mRbPortToPort.setEnabled(true);
                    mRbPortToDoor.setEnabled(true);
                    mRbDoorToDoor.setEnabled(false);
                    if ("3".equals(mInfo.serviceTypeCode)) {
                        mRbPortToPort.setChecked(true);
                    } else {
                        mRbPortToDoor.setChecked(true);
                    }
                    initDepartAdapter(seleDestCode);
                } else if (checkedId == mRbExpress.getId()) {
                    mInfo.productTypeCode = "B1";
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
                    mInfo.serviceTypeCode = "3";
                } else if (checkedId == mRbPortToDoor.getId()) {
                    mInfo.serviceTypeCode = "2";
                } else if (checkedId == mRbDoorToDoor.getId()) {
                    mInfo.serviceTypeCode = "1";
                }
                priceFee();
            }
        });

        // 付款方式
        mSpnPayMethod.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapterview, View view, int i, long l) {
                if (i == 0) {
                    mInfo.paymentTypeCode = "2";
                    if (mInfo.productTypeCode.equals("B2") || mInfo.productTypeCode.equals("B3")) {
                        mSpnSetterMethod.setFocusable(false);
                        mSpnSetterMethod.setClickable(false);
                    }
                } else if (i == 1) {
                    mInfo.paymentTypeCode = "1";
                    if (mInfo.productTypeCode.equals("B1")) {
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
                    mInfo.settlementTypeCode = "1";
                } else if (i == 1) {
                    mInfo.settlementTypeCode = "2";
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
                        mInfo.consignorMobile = conPhone;
                    } else {
                        mInfo.consignorPhone = conPhone;
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
                                    mInfo.custCode = "";
                                    mInfo.bankType = "-1";
                                    mInfo.bankNo = "";
                                    return;
                                }

                                CustList cust = custList.get(0);
                                mInfo.consignorContName = cust.getCustName();
                                mEdtConsignorContName.setText(mInfo.consignorContName);// 寄电姓名

                                mInfo.custCode = cust.getCustCode();
                                if (mInfo.custCode == null || mInfo.custCode.equals("null") || mInfo.custCode.isEmpty
                                        ()) {
                                    mInfo.custCode = "";
                                }
                                mEdMonthlyNo.setText(mInfo.custCode);// 订单-后

                                mInfo.bankType = cust.getBankType(); // 银行卡类型
                                if (mInfo.bankType == null || mInfo.bankType.equals("null") || mInfo.bankType.isEmpty
                                        ()) {
                                    mInfo.bankType = "-1";
                                }

                                mInfo.bankNo = cust.getBankAccount();// 银行卡号
                                if (mInfo.bankNo == null || mInfo.bankNo.equals("null") || mInfo.bankNo.isEmpty()) {
                                    mInfo.bankNo = "";
                                }

                                mInfo.consignorCompName = cust.getCustCompany();

                                List<AddressList> addressList = cust.getAddressList();
                                if (addressList == null || addressList.size() <= 0) {
                                    mEdtAddresseePhone.requestFocus();
                                    return;
                                }

                                if (addressList.size() == 1) {
                                    AddressList address = addressList.get(0);
                                    mEdtConsignorContName.setText(mInfo.consignorContName);// 寄电姓名
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
                                        map.put("name", mInfo.consignorContName);
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
                        mInfo.addresseeMobile = conPhone;
                    } else {
                        mInfo.addresseePhone = conPhone;
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
                                mInfo.addresseeContName = cust.getCustName();
                                mEdtAddresseeContName.setText(mInfo.addresseeContName);// 收电姓名

                                if (!MyUtils.isEmpty(cust.getCustPhone())) {
                                    mInfo.addresseePhone = cust.getCustPhone();// 收电
                                }

                                if (!MyUtils.isEmpty(cust.getCustMobile())) {
                                    mInfo.addresseeMobile = cust.getCustMobile();
                                }

                                mInfo.addresseeCompName = cust.getCustCompany();// 收件人公司名称

                                List<AddressList> addressList = cust.getAddressList();
                                if (addressList == null || addressList.size() <= 0) {
                                    mEdtQuantity.setFocusableInTouchMode(true);
                                    mEdtQuantity.requestFocus();
                                    return;
                                }

                                if (addressList.size() == 1) {
                                    AddressList address = addressList.get(0);
                                    mEdtAddresseeContName.setText(cust.getCustName());// 收电姓名
                                    mInfo.addresseeAddr = address.getAddress();
                                    mEdtAddresseeAddr.setText(mInfo.addresseeAddr == null ? "" : mInfo.addresseeAddr);

                                    addresseedeptCode = address.getDeptCode();
                                    mInfo.teamCode = address.getTeamCode();
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
                mInfo.destZoneCode = (new StringBuilder(String.valueOf(destName.replaceAll("[^0-9]", "")))).append
                        (destName.replaceAll("[^a-zA-Z]", "")).toString();
                Cursor queryRank = DepartmentDBWrapper.getInstance(getApplication()).rawQueryRank(mInfo.destZoneCode);
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

                mInfo.realWeightQty = mEdtRealWeightQty.getText().toString().trim();
                String Volume = mEdtVolume.getText().toString().trim();
                double1 = Double.valueOf(0.0D);

                if (MyUtils.isEmpty(mInfo.realWeightQty)) {
                    return;
                }
                // 没有重量，没有体积，运费是0
                if (MyUtils.isEmpty(mInfo.realWeightQty) && TextUtils.isEmpty(Volume)) {
                    mEdtWaybillFee.setText("0");
                    return;
                }
                // 没有重量，有体积
                if (MyUtils.isEmpty(mInfo.realWeightQty) && !TextUtils.isEmpty(Volume)) {
                    waybillFeeCount(Double.valueOf((Double.parseDouble(Volume) * 1000000D) / 4500D));
                    return;
                }

                double weight = Double.parseDouble(mInfo.realWeightQty);
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
                mInfo.volume = mEdtVolume.getText().toString().trim();
                mInfo.realWeightQty = mEdtRealWeightQty.getText().toString().trim();

                if (MyUtils.isEmpty(mInfo.volume)) {
                    return;
                }

                if (MyUtils.isEmpty(mInfo.volume) && mInfo.realWeightQty.isEmpty()) {
                    mEdtWaybillFee.setText("0");
                    return;
                }

                // 没有体积，有重量
                if (MyUtils.isEmpty(mInfo.volume) && !mInfo.realWeightQty.isEmpty()) {
                    double d = Double.parseDouble(mInfo.realWeightQty);
                    waybillFeeCount(Double.valueOf(d));
                    return;
                }

                if (mInfo.volume.startsWith(".")) {
                    mInfo.volume = "0" + mInfo.volume;
                }

                Double volume;
                if (!distCodeType(mSestdeptCode, seleDestCode).equals("D4")) {
                    volume = Double.valueOf((Double.parseDouble(mInfo.volume) * 1000000D) / 4500D);
                } else {
                    volume = Double.valueOf((Double.parseDouble(mInfo.volume) * 1000000D) / 6000D);
                }

                if (MyUtils.isEmpty(mInfo.realWeightQty)) {
                    waybillFeeCount(volume);
                    return;
                }

                Double double1;
                double1 = Double.valueOf(Double.parseDouble(mInfo.realWeightQty));
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
                    mInfo.bankType = "-1";
                } else {
                    if (adapterview.equals("工商银行") && i == 1) {
                        mInfo.bankType = "0";
                        return;
                    }
                    if (adapterview.equals("浦发银行") && i == 2) {
                        mInfo.bankType = "1";
                        return;
                    }
                    if (adapterview.equals("招商银行") && i == 3) {
                        mInfo.bankType = "5";
                        return;
                    }
                    if (adapterview.equals("建设银行") && i == 4) {
                        mInfo.bankType = "3";
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

                if (mInfo.productTypeCode.equals("B1")) {
                    double d = Double.parseDouble(charsequence);
                    double fee = Double.valueOf(d * 0.001D).doubleValue();
                    mEdtInsuranceFee.setText((MyUtils.ceil(fee)));
                    return;
                }

                if (mInfo.productTypeCode.equals("B2") || mInfo.productTypeCode.equals("B3")) {
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

    /**
     * 计算运费
     */
    private void priceFee() {
        mInfo.volume = mEdtVolume.getText().toString().trim();
        mInfo.realWeightQty = mEdtRealWeightQty.getText().toString();

        if (MyUtils.isEmpty(mInfo.volume)) {
            // 体积是空的
            if (MyUtils.isEmpty(mInfo.realWeightQty)) {
                // 重量是空
                mEdtWaybillFee.setText("0");
            } else {
                // 重量不是空
                double d = Double.parseDouble(mInfo.realWeightQty);
                waybillFeeCount(Double.valueOf(d));
            }
        } else {
            // 体积不是空
            Double volume;
            if (!distCodeType(mSestdeptCode, seleDestCode).equals("D4")) {
                volume = Double.valueOf((Double.parseDouble(mInfo.volume) * 1000000D) / 4500D);
            } else {
                volume = Double.valueOf((Double.parseDouble(mInfo.volume) * 1000000D) / 6000D);
            }
            if (MyUtils.isEmpty(mInfo.realWeightQty)) {
                // 重量是空
                waybillFeeCount(volume);
            } else {
                // 重量不是空
                Double double1;
                double1 = Double.valueOf(Double.parseDouble(mInfo.realWeightQty));
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

        if (MyUtils.isHenan(mInfo.provinceCode)) {
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
            return Double.valueOf((Double.parseDouble(mInfo.volume) * 1000000D) / 4500D);
        } else {
            return Double.valueOf((Double.parseDouble(mInfo.volume) * 1000000D) / 6000D);
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
        mInfo.meterageWeightQty = (new StringBuilder(String.valueOf(decimalformat.format(double1)))).toString();

        String quantityStr = mEdtQuantity.getText().toString().trim();

        // 根据重量计算上门费
        if (mInfo.productTypeCode.equals("B3")) {
            mInfo.fuelServiceFee = getShangmenFee(mInfo.meterageWeightQty);
            mEdtFuelService.setText(String.valueOf(mInfo.fuelServiceFee));
        } else {
            mInfo.fuelServiceFee = 0.0;
            mEdtFuelService.setText(null);
        }
        if (mInfo.meterageWeightQty != null && !"".equals(mInfo.meterageWeightQty)) {
            double1 = Double.valueOf(Double.parseDouble(mInfo.meterageWeightQty));
        }
        if (TextUtils.isEmpty(quantityStr)) {
            mInfo.quantity = Integer.parseInt(quantityStr);
        }

        PriceInfoList = new ArrayList();
        String sestdeptCode = mSestdeptCode;
        String seleDest = seleDestCode;
        distCodeType(sestdeptCode, seleDest);
        String productCode = "";
        if ("B2".equals(mInfo.productTypeCode) || "B3".equals(mInfo.productTypeCode)) {
            productCode = "B2";
        }

        if ("C1".equals(consType)) {
            Cursor categoryCursor = CategoryDBWrapper.getInstance(getApplication()).rawQueryRank(sestdeptCode,
                    seleDest, mInfo.serviceTypeCode, consType);
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

                    mEdtRealWeightQty.setText(String.valueOf(standardWeight * mInfo.quantity));


                    mEdtWaybillFee.setText((new BigDecimal(decimalformat.format(Double.valueOf(d * mInfo.quantity)))
                            .setScale(0, BigDecimal.ROUND_UP).toString()));
                    i++;
                }
            } while (true);
        } else {
            Cursor prodpriceCursor = ProdpriceDBWrapper.getInstance(getApplication()).rawQueryRank(sestdeptCode,
                    seleDest, mInfo.serviceTypeCode, "C3", limitTypeCode, "B2");
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
                return mInfo.distanceTypeCode;
            }
            mInfo.distanceTypeCode = queryRank.getString(queryRank.getColumnIndex("distancetypecode"));
        } while (true);
    }

    private void initControls(final int phoneType) {
        try {
            View view = getLayoutInflater().inflate(R.layout.popupwindow_list, null);
            @SuppressWarnings("unchecked") SimpleAdapter simpleadapter = new SimpleAdapter(this, mCustomerSeleList, R
                    .layout.popupwindow_item, new String[]{"name", "tel", "address"}, new int[]{R.id.item_pop_name, R
                    .id.item_pop_phone, R.id.item_pop_adress});
            ListView listview = (ListView) view.findViewById(R.id.listview);
            listview.setAdapter(simpleadapter);
            listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView adapterview, View view1, int i, long l) {

                    Map map = (Map) mCustomerSeleList.get(i);
                    switch (phoneType) {
                        case 1: {// 寄电
                            mInfo.consignorAddr = (String) map.get("address");
                            mEdtConsignorAddr.setText(mInfo.consignorAddr == null ? "" : mInfo.consignorAddr);

                            mInfo.consignorContName = (String) map.get("name");
                            mEdtConsignorContName.setText(mInfo.consignorContName);// 寄电姓名

                            // 收电，获取焦点
                            getPhoneRequestFocus();
                            break;
                        }
                        case 2: {// 收电
                            mInfo.addresseeAddr = (String) map.get("address");
                            mEdtAddresseeAddr.setText(mInfo.addresseeAddr);

                            mInfo.addresseeContName = (String) map.get("name");
                            mEdtAddresseeContName.setText(mInfo.addresseeContName);

                            String deptCode = (String) map.get("deptCode");
                            mInfo.teamCode = (String) map.get("teamCode");
                            if (mInfo.teamCode == null) {
                                mInfo.teamCode = "";
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
            if (s3.equals("4") && mInfo.productTypeCode.endsWith("B1")) {
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

            } else if (s3.equals("4") && (mInfo.productTypeCode.endsWith("B2") || mInfo.productTypeCode.equals("B3"))
                    && (s4.equals("DT07") || s4.equals("DT08"))) {
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
        Cursor queryRank = dbWrapper.rawQueryRank(mInfo.sourcedistrictCode);

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

                    if (!"0".equals(mInfo.bankType)) {
                        if ("1".equals(mInfo.bankType)) {
                            mSpnBankType.setSelection(2, true);
                        } else if ("5".equals(mInfo.bankType)) {
                            mSpnBankType.setSelection(3, true);
                        } else if ("3".equals(mInfo.bankType)) {
                            mSpnBankType.setSelection(4, true);
                        } else if ("-1".equals(mInfo.bankType)) {
                            mSpnBankType.setSelection(0, true);
                        }
                    } else {
                        mSpnBankType.setSelection(1, true);
                    }
                    mEdtBankNo.setText(mInfo.bankNo);
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
                    mInfo.deboursFlag = "0";
                    mEdtRemark.requestFocus();
                    return;
                }
            }
            case R.id.chb_accept_inputorder_deboursFlag: {// 是否垫结
                if (isChecked) {
                    mInfo.deboursFlag = "1";
                    return;
                } else {
                    mInfo.deboursFlag = "0";
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
                    mEdtSignBackNo.setText(mInfo.signBackNo);
                    waybillNoDB.deleteRank(mInfo.signBackNo);
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
                    mInfo.signBackReceipt = "0";
                    mCkbSignBackSeal.setFocusable(false);
                    mCkbSignBackSeal.setClickable(false);
                    mCkbSignBackSeal.setChecked(false);
                    mInfo.signBackSeal = "0";
                    mCkbSignBackIdentity.setFocusable(false);
                    mCkbSignBackIdentity.setClickable(false);
                    mCkbSignBackIdentity.setChecked(false);
                    mInfo.signBackIdentity = "0";
                    mEdtSignBackNo.getEditableText().clear();
                    mEdtSignBackfee.getEditableText().clear();
                    mEdtRemark.requestFocus();
                    return;
                }
            }
            case R.id.chb_signBack_receipt: {// 签单条
                if (isChecked) {
                    mInfo.signBackReceipt = "1";
                    return;
                } else {
                    mInfo.signBackReceipt = "0";
                    return;
                }
            }
            case R.id.chb_signBack_seal: {// 盖章
                if (isChecked) {
                    mInfo.signBackSeal = "1";
                    return;
                } else {
                    mInfo.signBackSeal = "0";
                    return;
                }
            }
            case R.id.chb_signBack_identity: {// 身份证
                if (isChecked) {
                    mInfo.signBackIdentity = "1";
                    return;
                } else {
                    mInfo.signBackIdentity = "0";
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
            mInfo.addresseeDistName = districtCursor.getString(districtCursor.getColumnIndex("distName"));
        } else {
            districtCursor.close();
        }

        Cursor departmentCursor = DepartmentDBWrapper.getInstance(getApplication()).rawQueryRank(mInfo.destZoneCode);

        if (departmentCursor.moveToNext()) {
            mDestZoneAddress = departmentCursor.getString(departmentCursor.getColumnIndex("deptAddress"));
            mInfo.partitionName = departmentCursor.getString(departmentCursor.getColumnIndex("partitionName"));
            mInfo.outsiteName = departmentCursor.getString(departmentCursor.getColumnIndex("outsiteName"));
            if (!TextUtils.isEmpty(mInfo.outsiteName) && mInfo.outsiteName.length() >= 2) {
                mInfo.outsiteName = mInfo.outsiteName.substring(0, 2);
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

        if (MyUtils.isEmpty(mInfo.addresseePhone)) {
            mInfo.addresseePhone = "";
        }
        if (MyUtils.isEmpty(mInfo.goodsChargeFee)) {
            mInfo.goodsChargeFee = "0";
        }
        if (MyUtils.isEmpty(mInfo.waitNotifyFee)) {
            mInfo.waitNotifyFee = "0";
        }
        if (MyUtils.isEmpty(mInfo.deboursFee)) {
            mInfo.deboursFee = "0";
        }
        if (MyUtils.isEmpty(mInfo.deliverFee) || "0".equals(mInfo.deliverFee)) {
            mInfo.deliverFee = "0";
        }

        BluetoothPrintTool.kehuLian(mInfo);
        BluetoothPrintTool.xiaopiao(mInfo, childNoList, mChbIsLook.isChecked());
    }


    @SuppressLint("SimpleDateFormat")
    private boolean addWayBillNo() {
        Double insurance;
        Double double1;

        mInfo.consignedTm = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(new Date());
        mInfo.orderNo = mEdtOrderNo.getText().toString().trim();
        mInfo.consignorContName = mEdtConsignorContName.getText().toString().trim();
        mInfo.consignorAddr = mEdtConsignorAddr.getText().toString().trim();
        mInfo.addresseeContName = mEdtAddresseeContName.getText().toString().trim();
        mInfo.addresseeAddr = mEdtAddresseeAddr.getText().toString().trim();
        mInfo.quantity = Integer.parseInt(mEdtQuantity.getText().toString().trim());
        mInfo.consName = mSpnConsName.getSelectedItem().toString().trim();
        mInfo.realWeightQty = mEdtRealWeightQty.getText().toString().trim();
        mInfo.volume = mEdtVolume.getText().toString().trim();
        mInfo.waybillFee = mEdtWaybillFee.getText().toString().trim();
        custIdentityCard = mEdtCustIdentityCard.getText().toString().trim();

        if ("C1".equals(consType)) {
            mInfo.meterageWeightQty = mInfo.realWeightQty;
        }

        if (!mChkChargeAgent.isChecked()) {
            mInfo.bankType = "";
            mInfo.bankNo = "";
            mInfo.transferDays = "0";
        } else {
            mInfo.chargeAgentFee = mEdtChargeAgentFee.getText().toString().trim();
            mInfo.goodsChargeFee = mEdtGoodsChargeFee.getText().toString().trim();
            mInfo.bankNo = mEdtBankNo.getText().toString().trim();
            //transferDays = mSpnPeriod.getSelectedItem().toString().trim();

            if (MyUtils.isEmpty(mInfo.goodsChargeFee)) {
                showT("请填写代收费用");
                return false;
            }
        }

        insurance = Double.valueOf(0.0D);
        if (mChkInsurance.isChecked()) {
            mInfo.insuranceFee = mEdtInsuranceFee.getText().toString().trim();
            mInfo.insuranceAmount = mEdtInsuranceAmount.getText().toString().trim();

            if (MyUtils.isEmpty(mInfo.insuranceAmount)) {
                showT("请填写保价费用");
                return false;
            }
            if (MyUtils.isEmpty(mInfo.insuranceFee) && "0".equals(mInfo.insuranceFee)) {
                showT("请填写保价费用");
                return false;
            }
            insurance = Double.valueOf(Double.parseDouble(mInfo.insuranceAmount));
        }
        if (mChkDebours.isChecked()) {
            mInfo.deboursFee = mEdtDeboursFee.getText().toString().trim();
            if (MyUtils.isEmpty(mInfo.deboursFee)) {
                showT("请填写垫付费用");
                return false;
            }
        } else {
            mInfo.deboursFee = "0";
        }
        if (mCkbWaitNotifyFee.isChecked()) {
            mInfo.waitNotifyFee = mEdtWaitNotifyFee.getText().toString().trim();
        }
        if (!mCkbSignBack.isChecked()) {
            mInfo.signBackFee = "0.0";
            mInfo.signBackNo = "";
            mInfo.signBackCount = "";
            mInfo.signBackSize = "";
        } else {
            mInfo.signBackFee = mEdtSignBackfee.getText().toString().trim();
            mInfo.signBackNo = mEdtSignBackNo.getText().toString().trim();
            mInfo.signBackCount = mEdtSignBackCount.getText().toString().trim();
            mInfo.signBackSize = mEdtSignBackSize.getText().toString().trim();
        }

        mInfo.waybillRemk = mEdtRemark.getText().toString().trim();
        double1 = Double.valueOf(0.0D);
        if (mCkbDeliver.isChecked()) {
            mInfo.deliverFee = mEdtDeliverFee.getText().toString().trim();
            if (MyUtils.isEmpty(mInfo.deliverFee)) {
                showT("请输入派送费");
                return false;
            }
            double1 = Double.valueOf(Double.parseDouble(mInfo.deliverFee));
        } else {
            mInfo.deliverFee = "0";
        }
        if (TextUtils.isEmpty(mInfo.waybillNo)) {
            MyUtils.showText(mContext, "运单号不能为空");
            return false;
        }

        if (MyUtils.isEmpty(mInfo.consigneeEmpCode)) {
            toastTxt("收件员工号不能为空");
            return false;
        }
        if (MyUtils.isEmpty(mInfo.consignorPhone) && MyUtils.isEmpty(mInfo.consignorMobile)) {
            toastTxt("寄件方电话不能为空");
            return false;
        }
        if (MyUtils.isEmpty(mInfo.consignorContName)) {
            toastTxt("寄件人不能为空");
            return false;
        }
        if (MyUtils.isEmpty(mInfo.consignorAddr) || mInfo.consignorAddr.length() <= 2) {
            toastTxt("寄件地址不能为空或小于2个字");
            return false;
        }
        if (!MyUtils.isEmpty(mInfo.destZoneCode) || !MyUtils.isEmpty(mInfo.destZoneCode)) {
        } else {
            toastTxt("目的地代码不能为空");
            return false;
        }
        if (MyUtils.isEmpty(mInfo.addresseePhone) && MyUtils.isEmpty(mInfo.addresseeMobile)) {
            toastTxt("收件方电话不能为空");
            return false;
        }

        if (MyUtils.isEmpty(mInfo.addresseeContName)) {
            toastTxt("收件人不能为空");
            return false;
        }

        if (MyUtils.isEmpty(mInfo.addresseeAddr) || mInfo.addresseeAddr.length() <= 2) {
            toastTxt("收件地址不能为空或小于2个字");
            return false;
        }

        if (mChkInsurance.isChecked()) {
        } else {
            toastTxt("保价费为必选项");
            return false;
        }
        if (MyUtils.isEmpty(mInfo.insuranceAmount)) {
            toastTxt("保价费不能为空");
            return false;
        }
        if (!mInfo.productTypeCode.equals("B1") || insurance.doubleValue() <= 50000D) {
        } else {
            toastTxt("保价费不能大于50000元");
            return false;
        }
        if ((!mInfo.productTypeCode.equals("B2") && !mInfo.productTypeCode.equals("B3")) || insurance.doubleValue()
                <= 50000D) {
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
                .parseDouble(mInfo.goodsChargeFee)) {
        } else {
            toastTxt("声明价值不能小于代收");
            return false;
        }
        if (!mChkDebours.isChecked() || Double.parseDouble(mInfo.deboursFee) <= 50D) {
        } else {
            toastTxt("垫付不能大于50");
            return false;
        }
        if (!TextUtils.isEmpty(mInfo.realWeightQty) || !TextUtils.isEmpty(mInfo.volume)) {
        } else {
            toastTxt("重量或体积至少一个不为空");
            return false;
        }
        if (mInfo.quantity <= 0) {
            toastTxt("件数不能为空");
            return false;
        }
	/*	if (Integer.parseInt(quantity) <= 19) {
		} else {
			toastTxt("件数不能大于19件");
			return false;
		}*/
        if (!MyUtils.isEmpty(mInfo.waybillFee) && !mInfo.waybillFee.equals("0")) {
        } else {
            toastTxt("运费不能为空");
            return false;
        }

        if (!mCkbSignBack.isChecked() || !TextUtils.isEmpty(mInfo.signBackFee)) {
        } else {
            toastTxt("请输入签回单费");
            return false;
        }
        if (!mCkbSignBack.isChecked() || !TextUtils.isEmpty(mInfo.signBackNo)) {
        } else {
            toastTxt("签回单号不能为空");
            return false;
        }
        if (!mCkbSignBack.isChecked() || !TextUtils.isEmpty(mInfo.signBackCount)) {
        } else {
            toastTxt("返单份数不能为空");
            return false;
        }
        if (!mCkbSignBack.isChecked() || !TextUtils.isEmpty(mInfo.signBackSize)) {
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
        if (mInfo.productTypeCode.equals("B3") && (mInfo.fuelServiceFee.doubleValue() == 0 || mInfo.fuelServiceFee ==
                null)) {
            toastTxt("上门费不能为空");
            return false;
        }
		/*if(destZoneCode!=null && productTypeCode!=null&&meterageWeightQty!=null){
			queryDeliverCommission();
		}*/
        if (mInfo.quantity == 1) {
            acceptInputDB.insertRank(mInfo, MyApp.mEmpCode, MyApp.mEmpName, "02", "4", 0, custIdentityCard, consType);
            return true;
        }

        if (mInfo.quantity <= 1) {
            toastTxt("数据添加失败");
            return false;
        } else {
            acceptInputDB.insertRank(mInfo, MyApp.mEmpCode, MyApp.mEmpName, "02", "4", 0, custIdentityCard, consType);
        }
        int i = 0;

        do {

            if (i >= childNoList.size()) {
                return true;
            }
            childNoListDB.insertRank(mInfo.waybillNo, (String) childNoList.get(i), (new StringBuilder(String.valueOf
                    (i))).toString(), 0);
            waybillNoDB.deleteRank((String) childNoList.get(i));
            i++;
        } while (true);

    }

    private void toastTxt(String s) {
        showT(s);
    }
}


