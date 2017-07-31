package com.eruitong.activity.addedvalue;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.eruitong.activity.BaseActivity;
import com.eruitong.config.Conf;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.utils.ProvisionTemplate;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class AddedValueActivity extends BaseActivity {

    /**
     * 标题
     **/
    @ViewInject(R.id.edt_added_title)
    TextView mTxtInclude;

    /**
     * 身份
     **/
    @ViewInject(R.id.radiogroup1)
    RadioGroup mRG;
    /**
     * 中巴长
     **/
    @ViewInject(R.id.rb_middle_driver)
    RadioButton mRbMiddleDriver;
    /**
     * 小巴长
     **/
    @ViewInject(R.id.rb_small_driver)
    RadioButton mRbSmallDriver;
    /**
     * 巴员
     **/
    @ViewInject(R.id.rb_passenger)
    RadioButton mRbPassenger;
    /**
     * 所属巴组
     **/
    @ViewInject(R.id.spn_added_genusGroup)
    Spinner mSpGenusGroup;
    /**
     * 巴组人数
     **/
    @ViewInject(R.id.spn_added_groupNo)
    Spinner mSpGroupNo;

    /**
     * 票数
     **/
    @ViewInject(R.id.edt_added_pollCount)
    EditText mEdtPollCount;
    /**
     * 票数计提
     **/
    @ViewInject(R.id.edt_added_pollCount_compute)
    EditText mEdtPollCountCompute;
    /**
     * 计费重量
     **/
    @ViewInject(R.id.edt_added_money_weight)
    EditText mEdtMoneyWeight;
    /**
     * 计提重量
     **/
    @ViewInject(R.id.edt_added_compute_weight)
    EditText mEdtMoneyWeightCompute;
    /**
     * 重量计提标准
     **/
    @ViewInject(R.id.edt_added_compute_weight_standard)
    EditText mEdtMoneyWeightComputeStandard;
    /**
     * 超区域派送费
     **/
    @ViewInject(R.id.edt_added_exceed_region_money)
    EditText mEdtExceedRegionMoney;
    /**
     * 代收款
     **/
    @ViewInject(R.id.edt_added_goodsChargeFee)
    EditText mEdtGoodsChargeFee;
    /**
     * 代收款
     **/
    @ViewInject(R.id.edt_added_goodsChargeFee_compute)
    EditText mEdtGoodsChargeFeeCompute;
    /**
     * 上门费
     **/
    @ViewInject(R.id.edt_added_fuelServiceFee)
    EditText mEdtFuelServiceFee;
    /**
     * 目标收入
     **/
    @ViewInject(R.id.edt_added_targetIncome)
    EditText mEdtTargetIncome;
    /**
     * 快递派送运费
     **/
    @ViewInject(R.id.edt_added_express_delivery_freight)
    EditText mEdtExpressDeliveryFreight;
    /**
     * 快递派送计提标准
     **/
    @ViewInject(R.id.edt_added_express_delivery_freight_standard)
    EditText mEdtExpressDeliveryFreightStandard;
    /**
     * 外阜-外阜运费
     **/
    @ViewInject(R.id.edt_added_outward)
    EditText mEdtOutward;
    /**
     * 外阜-外阜计提标准
     **/
    @ViewInject(R.id.edt_added_outward_standard)
    EditText mEdtOutwardStandard;
    /**
     * 外阜-西安运费
     **/
    @ViewInject(R.id.edt_added_xian)
    EditText mEdtXian;
    /**
     * 外阜-西安计提标准
     **/
    @ViewInject(R.id.edt_added_xian_standard)
    EditText mEdtXianStandard;
    /**
     * 固定费用
     **/
    @ViewInject(R.id.edt_added_fixed_money)
    EditText mEdtStaticFee;
    /**
     * 日常费用
     **/
    @ViewInject(R.id.edt_added_daily_money)
    EditText mEdtDailyFee;

    /**
     * 确定
     **/
    @ViewInject(R.id.btn_added_ok)
    Button mBtnOk;
    /**
     * 取消
     **/
    @ViewInject(R.id.btn_added_cancel)
    Button mBtnCancel;

    private Context mcContext;


    private AddedValue mAddedValue = new AddedValue();

    List<ProvisionTemplate> templateList = new ArrayList<ProvisionTemplate>();

    private ProvisionTemplate queryTemplate;

    private Double pollProfit;
    private Double meterageWeightProfit;
    private Double deliverFeeProfit;
    private Double goodsChargeFeeProfit;
    private Double deliverExpressFeeProfit;
    private Double insuranceFeeProfit;
    private Double fuelServiceFeeProfit;
    private Double outboundFeeProfit;
    private Double xianOutboundProfit;

/*	String groupNo;
    String genusGroup;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_value);
        ViewUtils.inject(this);
        mcContext = AddedValueActivity.this;

        initView();
        initAdapter();
    }

    private void initView() {

        mEdtPollCount.setEnabled(false);
        /** 票数计提 **/
        mEdtPollCountCompute.setEnabled(false);
        /** 计费重量 **/
        mEdtMoneyWeight.setEnabled(false);
        /** 计提重量 **/
        mEdtMoneyWeightCompute.setEnabled(false);
        /** 重量计提标准 **/
        mEdtMoneyWeightComputeStandard.setEnabled(false);
        /** 超区域派送费 **/
        mEdtExceedRegionMoney.setEnabled(false);
        /** 代收款 **/
        mEdtGoodsChargeFee.setEnabled(false);
        /** 代收款 **/
        mEdtGoodsChargeFeeCompute.setEnabled(false);
        /** 上门费 **/
        mEdtFuelServiceFee.setEnabled(false);
        /** 目标收入 **/
        mEdtTargetIncome.setEnabled(false);
        /** 快递派送运费 **/
        mEdtExpressDeliveryFreight.setEnabled(false);
        /** 快递派送计提标准 **/
        mEdtExpressDeliveryFreightStandard.setEnabled(false);
        /** 外阜-外阜运费 **/
        mEdtOutward.setEnabled(false);
        /** 外阜-外阜计提标准 **/
        mEdtOutwardStandard.setEnabled(false);
        /** 外阜-西安运费 **/
        mEdtXian.setEnabled(false);
        /** 外阜-西安计提标准 **/
        mEdtXianStandard.setEnabled(false);
        /** 固定费用 **/
        mEdtStaticFee.setEnabled(false);
        /** 日常费用 **/
        mEdtDailyFee.setEnabled(false);

        mRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mRbMiddleDriver.getId()) {// 中巴
                    mAddedValue.groupRole = "1";
                } else if (checkedId == mRbSmallDriver.getId()) {// 小巴
                    mAddedValue.groupRole = "2";
                } else if (checkedId == mRbPassenger.getId()) {// 巴员
                    mAddedValue.groupRole = "3";
                }
                findTemplateByGroupRole();
                setDisabled();
            }
        });

        // 巴组人数
        mSpGroupNo.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String adapterview = mSpGroupNo.getSelectedItem().toString();
                        /*if (adapterview.equals("1") && i == 0) {
							mAddedValue.groupNo = "1";
						}else*/
                if (adapterview.equals("2") && i == 0) {
                    mAddedValue.groupNo = "2";
                } else if (adapterview.equals("3") && i == 1) {
                    mAddedValue.groupNo = "3";
                }
                findTemplateByGroupRole();
                setDisabled();

            }

            public void onNothingSelected(AdapterView adapterview) {
            }
        });

        // 巴组人数
        mSpGenusGroup.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapter, View view, int i, long l) {
                String adapterview = mSpGenusGroup.getSelectedItem().toString();
                if (adapterview.equals("第一组") && i == 0) {
                    mAddedValue.genusGroup = "1";
                } else if (adapterview.equals("第二组") && i == 1) {
                    mAddedValue.genusGroup = "2";
                } else if (adapterview.equals("第三组") && i == 2) {
                    mAddedValue.genusGroup = "3";
                } else if (adapterview.equals("第四组") && i == 3) {
                    mAddedValue.genusGroup = "4";
                } else if (adapterview.equals("第五组") && i == 4) {
                    mAddedValue.genusGroup = "5";
                } else if (adapterview.equals("第六组") && i == 5) {
                    mAddedValue.genusGroup = "6";
                } else if (adapterview.equals("第七组") && i == 6) {
                    mAddedValue.genusGroup = "7";
                } else if (adapterview.equals("第八组") && i == 7) {
                    mAddedValue.genusGroup = "8";
                } else if (adapterview.equals("第九组") && i == 8) {
                    mAddedValue.genusGroup = "9";
                } else if (adapterview.equals("第十组") && i == 9) {
                    mAddedValue.genusGroup = "10";
                }
                findTemplateByGroupRole();
                setDisabled();
            }


            public void onNothingSelected(AdapterView adapterview) {
            }
        });

        queryProvisionTemplate();


        //票数
        mEdtPollCount.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("-------------------");
                String meterageWeightQty = mEdtMoneyWeight.getText().toString().trim();
                String pollCount = mEdtPollCount.getText().toString().trim();
                if (!"".equals(meterageWeightQty)) {
                    mAddedValue.meterageWeightQty = Double.valueOf(meterageWeightQty);
                }
                if (!"".equals(pollCount)) {
                    mAddedValue.pollCount = Long.valueOf(pollCount);
                }
                if (mAddedValue.pollCount != null) {
                    if (mAddedValue.meterageWeight != null) {
                        mEdtMoneyWeightCompute.setText(String.valueOf(mAddedValue.meterageWeightQty - (mAddedValue
                                .pollCount * 5)));

                    }
                    if (queryTemplate != null) {
                        pollProfit = mAddedValue.pollCount * queryTemplate.getPollProvision();
                    }
                }
            }
        });

        //重量
        mEdtMoneyWeight.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String meterageWeightQty = mEdtMoneyWeight.getText().toString().trim();
                String pollCount = mEdtPollCount.getText().toString().trim();
                if (!"".equals(meterageWeightQty)) {
                    mAddedValue.meterageWeightQty = Double.valueOf(meterageWeightQty);
                }
                if (!"".equals(pollCount)) {
                    mAddedValue.pollCount = Long.valueOf(pollCount);
                }

                if (mAddedValue.pollCount != null && mAddedValue.meterageWeightQty != null) {

                    mEdtMoneyWeightCompute.setText(String.valueOf(mAddedValue.meterageWeightQty - (mAddedValue
                            .pollCount * 5)));
                    if (queryTemplate != null) {
                        meterageWeightProfit = ((mAddedValue.meterageWeightQty - (mAddedValue.pollCount * 5)) *
                                queryTemplate.getWeightQtyProvision());
                    }
                }
            }
        });

        //
        mEdtExceedRegionMoney.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String deliverFee = mEdtExceedRegionMoney.getText().toString().trim();
                if (!"".equals(deliverFee)) {
                    mAddedValue.deliverFee = Double.valueOf(deliverFee);
                }
                if (mAddedValue.deliverFee != null) {
                    deliverFeeProfit = mAddedValue.deliverFee;

                }
            }
        });

        //
        mEdtTargetIncome.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String targetIncome = mEdtTargetIncome.getText().toString().trim();
                if (!"".equals(targetIncome)) {
                    mAddedValue.targetIncome = Double.valueOf(targetIncome);
                }
						/*if(mAddedValue.targetIncome!=null){
						mAddedValue.targetIncome = mAddedValue.targetIncome;
						*/
            }
        });

        //代收
        mEdtGoodsChargeFee.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String goodsChargeFee = mEdtGoodsChargeFee.getText().toString().trim();
                if (!"".equals(goodsChargeFee)) {
                    mAddedValue.goodsChargeFee = Double.valueOf(goodsChargeFee);
                }
                if (mAddedValue.goodsChargeFee != null && queryTemplate != null) {
                    goodsChargeFeeProfit = mAddedValue.goodsChargeFee * queryTemplate.getGoodsChageProvision();

                }
            }
        });

        //代收
        mEdtFuelServiceFee.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String fuelServiceFee = mEdtFuelServiceFee.getText().toString().trim();
                if (!"".equals(fuelServiceFee)) {
                    mAddedValue.fuelServiceFee = Double.valueOf(fuelServiceFee);
                }
                if (mAddedValue.fuelServiceFee != null) {
                    fuelServiceFeeProfit = mAddedValue.fuelServiceFee;

                }
            }
        });

        //代收
        mEdtExpressDeliveryFreight.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String deliverExpressFee = mEdtExpressDeliveryFreight.getText().toString().trim();
                if (!"".equals(deliverExpressFee)) {
                    mAddedValue.deliverExpressFee = Double.valueOf(deliverExpressFee);
                }
                if (mAddedValue.deliverExpressFee != null && queryTemplate != null) {
                    deliverExpressFeeProfit = mAddedValue.deliverExpressFee * queryTemplate.getExpressDeliverProsion();

                }
            }
        });

        //外埠--外埠
        mEdtOutward.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String outbound = mEdtOutward.getText().toString().trim();
                if (!"".equals(outbound)) {
                    mAddedValue.outbound = Double.valueOf(outbound);
                }
                if (mAddedValue.outbound != null && queryTemplate != null) {
                    outboundFeeProfit = mAddedValue.outbound * queryTemplate.getOutProvision();

                }
            }
        });

        //外埠--西安
        mEdtXian.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String xianOutbound = mEdtXian.getText().toString().trim();
                if (!"".equals(xianOutbound)) {
                    mAddedValue.xianOutbound = Double.valueOf(xianOutbound);
                }
                if (mAddedValue.xianOutbound != null && queryTemplate != null) {
                    outboundFeeProfit = mAddedValue.xianOutbound * queryTemplate.getOutXianProvision();

                }
            }
        });


        //费用
        mEdtStaticFee.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String staticFee = mEdtStaticFee.getText().toString().trim();
                if (!"".equals(staticFee)) {
                    mAddedValue.staticFee = Double.valueOf(staticFee);
                }
                if (mAddedValue.staticFee != null) {
                    outboundFeeProfit = mAddedValue.staticFee;

                }
            }
        });

        //日常费用
        mEdtDailyFee.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String dailyFee = mEdtDailyFee.getText().toString().trim();
                if (!"".equals(dailyFee)) {
                    mAddedValue.dailyFee = Double.valueOf(dailyFee);
                }
                if (mAddedValue.staticFee != null) {
                    outboundFeeProfit = mAddedValue.staticFee;

                }
            }
        });


        // 保存
        mBtnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                pushData();
            }

        });

    }


    /*@Override
    protected void onStart() {
        super.onStart();

                //票数
                mEdtPollCount.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                            int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mAddedValue.meterageWeightQty = Double.valueOf(mEdtMoneyWeight.getText().toString());
                        mAddedValue.pollCount = Long.valueOf(mEdtPollCount.getText().toString());
                        if(mAddedValue.pollCount!=null){
                            if(mAddedValue.meterageWeight!=null){
                                mEdtMoneyWeightCompute.setText(String.valueOf(mAddedValue.meterageWeightQty - 
                                (mAddedValue.pollCount*5)));

                            }
                            if(queryTemplate!=null){
                                pollProfit =mAddedValue.pollCount * queryTemplate.getPollProvision();
                            }
                        }
                    }
                });

                //重量
                mEdtMoneyWeight.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                            int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mAddedValue.meterageWeightQty = Double.valueOf(mEdtMoneyWeight.getText().toString());
                        mAddedValue.pollCount = Long.valueOf(mEdtPollCount.getText().toString());
                        if(mAddedValue.pollCount!=null&&mAddedValue.meterageWeight!=null){

                        mEdtMoneyWeightCompute.setText(String.valueOf(mAddedValue.meterageWeightQty - (mAddedValue
                        .pollCount*5)));
                        if(queryTemplate!=null){
                            meterageWeightProfit= (mAddedValue.meterageWeightQty-(mAddedValue.pollCount*5)
                            *queryTemplate.getWeightQtyProvision
                            ());
                        }
                        }
                    }
                });

                //
                mEdtDailyFee.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                            int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mAddedValue.deliverExpressFee = Double.valueOf(mEdtExpressDeliveryFreight.getText().toString());
                        if(mAddedValue.deliverExpressFee!=null&&queryTemplate!=null){
                        mAddedValue.deliverExpressFeeProvisio = (mAddedValue.meterageWeightQty-(mAddedValue
                        .pollCount*5)*queryTemplate
                        .getWeightQtyProvision());

                        }
                    }
                });

                //代收
                mEdtGoodsChargeFee.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                            int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mAddedValue.goodsChargeFee = Double.valueOf(mEdtGoodsChargeFee.getText().toString());
                        if(mAddedValue.goodsChargeFee!=null&&queryTemplate!=null){
                            goodsChargeFeeProfit= mAddedValue.goodsChargeFee*queryTemplate.getWeightQtyProvision();

                        }
                    }
                });

                //代收
                mEdtFuelServiceFee.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                            int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mAddedValue.fuelServiceFee = Double.valueOf(mEdtFuelServiceFee.getText().toString());
                        if(mAddedValue.fuelServiceFee!=null){
                            fuelServiceFeeProfit= mAddedValue.fuelServiceFee;

                        }
                    }
                });

                //代收
                mEdtExpressDeliveryFreight.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                            int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mAddedValue.deliverExpressFee = Double.valueOf(mEdtExpressDeliveryFreight.getText().toString());
                        if(mAddedValue.deliverExpressFee!=null&&queryTemplate!=null){
                            deliverExpressFeeProfit= mAddedValue.deliverExpressFee;

                        }
                    }
                });

                //外埠--外埠
                mEdtOutward.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                            int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mAddedValue.outbound = Double.valueOf(mEdtOutward.getText().toString());
                        if(mAddedValue.outbound!=null&&queryTemplate!=null){
                            outboundFeeProfit= mAddedValue.outbound*queryTemplate.getOutProvision();

                        }
                    }
                });

                //外埠--西安
                mEdtXian.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                            int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mAddedValue.xianOutbound = Double.valueOf(mEdtXian.getText().toString());
                        if(mAddedValue.xianOutbound!=null&&queryTemplate!=null){
                            outboundFeeProfit= mAddedValue.xianOutbound*queryTemplate.getOutXianProvision();

                        }
                    }
                });


                //费用
                mEdtStaticFee.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                            int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mAddedValue.staticFee = Double.valueOf(mEdtStaticFee.getText().toString());
                        if(mAddedValue.staticFee!=null){
                            outboundFeeProfit= mAddedValue.staticFee;

                        }
                    }
                });

                //日常费用
                mEdtStaticFee.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                            int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                            int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mAddedValue.staticFee = Double.valueOf(mEdtStaticFee.getText().toString());
                        if(mAddedValue.staticFee!=null){
                            outboundFeeProfit= mAddedValue.staticFee;

                        }
                    }
                });

    }
*/
    private void initAdapter() {
        //ArrayList arraylist = new ArrayList();
        //	@SuppressWarnings({ "unchecked", "rawtypes" })
		/*ArrayAdapter arrayAdapter = new ArrayAdapter(mcContext, android.R.layout.simple_spinner_item,
				(String[]) arraylist.toArray(new String[arraylist.size()]));*/
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(mcContext, R.array.genusGroup, android.R.layout
                .simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpGenusGroup.setAdapter(arrayAdapter);
        arrayAdapter = ArrayAdapter.createFromResource(mcContext, R.array.groupNo,

                android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpGroupNo.setAdapter(arrayAdapter);


    }


    private void pushData() {
        try {
            HttpUtils httpUtils = new HttpUtils();
            mAddedValue.dayIncomeCount = 0.0;
            if (pollProfit != null) {
                mAddedValue.dayIncomeCount += pollProfit;
            }
            if (meterageWeightProfit != null) {
                mAddedValue.dayIncomeCount += Double.valueOf(convert(meterageWeightProfit));
            }
            if (deliverFeeProfit != null) {
                mAddedValue.dayIncomeCount += Double.valueOf(convert(deliverFeeProfit));
            }
            if (goodsChargeFeeProfit != null) {
                mAddedValue.dayIncomeCount += Double.valueOf(convert(goodsChargeFeeProfit));
            }
            if (deliverExpressFeeProfit != null) {
                mAddedValue.dayIncomeCount += Double.valueOf(convert(deliverExpressFeeProfit));
            }
            if (fuelServiceFeeProfit != null) {
                mAddedValue.dayIncomeCount += Double.valueOf(convert(fuelServiceFeeProfit));
            }
            if (outboundFeeProfit != null) {
                mAddedValue.dayIncomeCount += Double.valueOf(convert(outboundFeeProfit));
            }
            if (xianOutboundProfit != null) {
                mAddedValue.dayIncomeCount += Double.valueOf(convert(xianOutboundProfit));
            }
            mAddedValue.hourIncomeCount = Double.valueOf(convert(mAddedValue.dayIncomeCount / 8));
            RequestParams params = new RequestParams();
            JSONArray jsonarray = new JSONArray();

            JSONObject jsonobject = new JSONObject();


            if (mAddedValue.pollCount == null) {
                mAddedValue.pollCount = 0L;
            }
            jsonobject.put("pollCount", mAddedValue.pollCount);
            jsonobject.put("groupRole", mAddedValue.groupRole);
            jsonobject.put("groupNo", mAddedValue.groupNo);
            jsonobject.put("genusGroup", mAddedValue.genusGroup);
            if (mAddedValue.meterageWeightQty == null) {
                mAddedValue.meterageWeightQty = 0.0;
            }
            jsonobject.put("meterageWeightQty", mAddedValue.meterageWeightQty);
            if (mAddedValue.goodsChargeFee == null) {
                mAddedValue.goodsChargeFee = 0.0;
            }
            jsonobject.put("goodsChargeFee", mAddedValue.goodsChargeFee);
            if (mAddedValue.deliverFee == null) {
                mAddedValue.deliverFee = 0.0;
            }
            jsonobject.put("deliverFee", mAddedValue.deliverFee);
            if (mAddedValue.insuranceFee == null) {
                mAddedValue.insuranceFee = 0.0;
            }
            jsonobject.put("insuranceFee", mAddedValue.insuranceFee);
            if (mAddedValue.deliverExpressFee == null) {
                mAddedValue.deliverExpressFee = 0.0;
            }
            jsonobject.put("deliverExpressFee", mAddedValue.deliverExpressFee);
            if (mAddedValue.fuelServiceFee == null) {
                mAddedValue.fuelServiceFee = 0.0;
            }
            jsonobject.put("fuelServiceFee", mAddedValue.fuelServiceFee);
            if (mAddedValue.outbound == null) {
                mAddedValue.outbound = 0.0;
            }
            jsonobject.put("outbound", mAddedValue.outbound);

            if (mAddedValue.staticFee == null) {
                mAddedValue.staticFee = 0.0;
            }
            jsonobject.put("staticFee", mAddedValue.staticFee);
            if (mAddedValue.dailyFee == null) {
                mAddedValue.dailyFee = 0.0;
            }
            jsonobject.put("dailyFee", mAddedValue.dailyFee);
            if (mAddedValue.dayIncomeCount == null) {
                mAddedValue.dayIncomeCount = 0.0;
            }
            jsonobject.put("dayIncomeCount", mAddedValue.dayIncomeCount);
            if (mAddedValue.hourIncomeCount == null) {
                mAddedValue.hourIncomeCount = 0.0;
            }
            jsonobject.put("hourIncomeCount", mAddedValue.hourIncomeCount);
            if (mAddedValue.targetIncome == null) {
                mAddedValue.targetIncome = 0.0;
            }
            jsonobject.put("targetIncome", mAddedValue.targetIncome);
            jsonobject.put("deptCode", MyApp.mDeptCode);

            jsonarray.put(jsonobject);
            params.addBodyParameter("empCode", MyApp.mEmpCode);
            params.addBodyParameter("jsonData", jsonarray.toString());
            httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.createAddedValueAction, params, new
                    RequestCallBack<String>() {

                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    showT("网络异常，请稍后再试");
                }

                @Override
                public void onSuccess(ResponseInfo<String> arg0) {
                    String result = arg0.result;

                    I_AddedValue baseData = new Gson().fromJson(result, I_AddedValue.class);

                    if (baseData.isSuccess()) {
                        showT("保存成功");
                        finish();
                    } else {
                        showT(baseData.getError() + "");
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static String convert(Double value) {
        long l1 = Math.round(value.doubleValue() * 100D);
        double ret = (double) l1 / 100D;
        return String.valueOf(ret);
    }


    private void queryProvisionTemplate() {

        HttpUtils httpUtils = new HttpUtils();

        RequestParams params = new RequestParams();
        //params.addBodyParameter("empCode", MyApp.mEmpCode);
        params.addBodyParameter("deptCode", MyApp.mDeptCode);
        //	params.addBodyParameter("jsonData", new Gson().toJson(mAddedValue));

        httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.queryProvisionTemplateAction, params, new
                RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                showT("网络异常，请稍后再试");
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String result = arg0.result;

                I_AddedValue baseData = new Gson().fromJson(result, I_AddedValue.class);

                if (baseData.isSuccess()) {
                    if (baseData.getTemplates() != null) {
                        templateList = baseData.getTemplates();
                    }
                    //showT("保存成功");
                    //finish();
                } else {
                    showT(baseData.getError() + "");
                }
            }
        });
    }


    private void findTemplateByGroupRole() {
        NumberFormat nFromat = NumberFormat.getPercentInstance();
        for (ProvisionTemplate template : templateList) {
            if (template.getGroupRole().equals(mAddedValue.groupRole)) {
                mEdtExpressDeliveryFreightStandard.setText(nFromat.format(template.getExpressDeliverProsion()));
                mEdtMoneyWeightComputeStandard.setText(String.valueOf(template.getWeightQtyProvision()));
                mEdtPollCountCompute.setText(String.valueOf(template.getPollProvision()));
                mEdtGoodsChargeFeeCompute.setText(String.valueOf(template.getGoodsChageProvision()));
                if ("1".equals(mAddedValue.groupRole)) {
                    if (null != template.getOutProvision()) {
                        mEdtOutwardStandard.setText(nFromat.format(template.getOutProvision()));
                    }
                    if (null != template.getOutXianProvision()) {
                        mEdtXianStandard.setText(nFromat.format(template.getOutXianProvision()));
                    }
                    queryTemplate = template;
                } else {
                    mEdtOutwardStandard.setText("");
                    mEdtXianStandard.setText("");
                }
                if (String.valueOf(template.getGroupNo()).equals(mAddedValue.groupNo)) {
                    queryTemplate = template;
                }
            }
        }
    }

    private void setDisabled() {
        if ("1".equals(mAddedValue.groupRole)) {
            mSpGenusGroup.setEnabled(false);
            mSpGroupNo.setEnabled(false);
            mEdtPollCount.setEnabled(true);
            /** 计费重量 **/
            mEdtMoneyWeight.setEnabled(true);
            /** 超区域派送费 **/
            mEdtExceedRegionMoney.setEnabled(true);
            /** 代收款 **/
            mEdtGoodsChargeFee.setEnabled(true);
            /** 上门费 **/
            mEdtFuelServiceFee.setEnabled(true);
            /** 目标收入 **/
            mEdtTargetIncome.setEnabled(true);
            /** 快递派送运费 **/
            mEdtExpressDeliveryFreight.setEnabled(true);
            /** 外阜-外阜运费 **/
            mEdtOutward.setEnabled(true);
            /** 外阜-西安运费 **/
            mEdtXian.setEnabled(true);
            /** 固定费用 **/
            mEdtStaticFee.setEnabled(true);
            /** 日常费用 **/
            mEdtDailyFee.setEnabled(true);
        } else {
            if (mAddedValue.groupNo != null && mAddedValue.genusGroup != null) {
                mSpGenusGroup.setEnabled(true);
                mSpGroupNo.setEnabled(true);
                mEdtPollCount.setEnabled(true);
                mEdtMoneyWeight.setEnabled(true);
                //meterageWeightQtyProvisionTextViewer.setEnabled(true);
                mEdtExpressDeliveryFreight.setEnabled(true);
                mEdtGoodsChargeFee.setEnabled(true);
                //insuranceFeeTextViewer.setEnabled(true);
                mEdtFuelServiceFee.setEnabled(true);
                mEdtExceedRegionMoney.setEnabled(true);
                mEdtOutward.setEnabled(false);
                mEdtXian.setEnabled(false);
                mEdtStaticFee.setEnabled(false);
                mEdtDailyFee.setEnabled(false);
                mEdtTargetIncome.setEnabled(true);
            } else {


                mSpGenusGroup.setEnabled(true);
                mSpGroupNo.setEnabled(true);
                mEdtPollCount.setEnabled(false);
                /** 计费重量 **/
                mEdtMoneyWeight.setEnabled(false);
                /** 超区域派送费 **/
                mEdtExceedRegionMoney.setEnabled(false);
                /** 代收款 **/
                mEdtGoodsChargeFee.setEnabled(false);
                /** 上门费 **/
                mEdtFuelServiceFee.setEnabled(false);
                /** 目标收入 **/
                mEdtTargetIncome.setEnabled(false);
                /** 快递派送运费 **/
                mEdtExpressDeliveryFreight.setEnabled(false);
                /** 外阜-外阜运费 **/
                mEdtOutward.setEnabled(false);
                /** 外阜-西安运费 **/
                mEdtXian.setEnabled(false);
                /** 固定费用 **/
                mEdtStaticFee.setEnabled(false);
                /** 日常费用 **/
                mEdtDailyFee.setEnabled(false);

            }

        }
    }

    class I_AddedValue {
        private boolean success;
        private String error;
        List<ProvisionTemplate> templates;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public List<ProvisionTemplate> getTemplates() {
            return templates;
        }

        public void setTemplates(List<ProvisionTemplate> templates) {
            this.templates = templates;
        }

    }


    class AddedValue {

        Long pollCount;
        String groupRole;
        String groupNo;
        String genusGroup;
        String deptCode;
        Double meterageWeightQty;
        //计提重量
        Double meterageWeight;
        Double goodsChargeFee;
        Double deliverFee;
        Double insuranceFee;
        Double deliverExpressFee;
        Double fuelServiceFee;
        Double outbound;
        Double xianOutbound;
        Double staticFee;
        Double dailyFee;
        Double dayIncomeCount;
        Double hourIncomeCount;
        Double targetIncome;

    }
}

