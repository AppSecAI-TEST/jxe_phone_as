package com.eruitong.activity.receive;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eruitong.activity.BaseActivity;
import com.eruitong.bluetooth.StatusBox;
import com.eruitong.config.Conf;
import com.eruitong.db.BluetoothDBWrapper;
import com.eruitong.db.DepartmentDBWrapper;
import com.eruitong.db.DistrictDBWrapper;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.model.InputDataInfo;
import com.eruitong.model.net.I_QueryWaybill;
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
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zxing.activity.CaptureActivity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import zpSDK.zpSDK.zpSDK;

/**
 * 运单查询
 *
 * @author Administrator
 */
public class QueryWaybillActivity extends BaseActivity implements Hearer {

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

    @ViewInject(R.id.edt_delivery_deliverysignfor_jobnumber)
    EditText mKeyword;
    /**
     * 运单
     **/
    @ViewInject(R.id.item_quire_upload_waybillnumber)
    TextView mWaybillNumber;
    /**
     * 始发网点
     **/
    @ViewInject(R.id.item_quire_upload_addresseeDistName)
    TextView mTxtSourceZone;
    /**
     * 收派
     **/
    @ViewInject(R.id.item_quire_upload_empCode)
    TextView mEmpCode;
    /**
     * 目的网点
     **/
    @ViewInject(R.id.item_quire_upload_consignorDistName)
    TextView mTxtDestZone;
    /**
     * 寄方
     **/
    @ViewInject(R.id.query_waybill_consignorContName)
    TextView mConsignorContName;
    /**
     * 寄电
     **/
    @ViewInject(R.id.query_waybill_consignorPhone)
    TextView mConsignorPhone;
    /**
     * 收方
     **/
    @ViewInject(R.id.item_quire_upload_addresseeContName)
    TextView mAddresseeContName;
    /**
     * 收电
     **/
    @ViewInject(R.id.item_quire_upload_addresseePhone)
    TextView mAddresseePhone;
    /**
     * 代收
     **/
    @ViewInject(R.id.item_quire_upload_goodsChargeFee)
    TextView mGoodsChargeFee;
    /**
     * 付款
     **/
    @ViewInject(R.id.item_quire_upload_payMethod)
    TextView mPayMethod;
    /**
     * 货物
     **/
    @ViewInject(R.id.item_quire_upload_consName)
    TextView mConsName;
    /**
     * 件数
     **/
    @ViewInject(R.id.item_quire_upload_quantity)
    TextView mQuantity;
    /**
     * 重量
     **/
    @ViewInject(R.id.item_quire_upload_meterageWeightQty)
    TextView mMeterageWeightQty;
    /**
     * 运费
     **/
    @ViewInject(R.id.item_quire_upload_waybillNoFee)
    TextView mWaybillNoFee;

    /**
     * 打印标签
     **/
    @ViewInject(R.id.updata_btn_cancel)
    Button mPrint;
    /**
     * 客户联
     **/
    @ViewInject(R.id.updata_btn_ok)
    Button mClient;

    private String xiqopiaoAddress;
    private InputDataInfo inputDataInfo;
    private Cursor cursor;
    private StatusBox statusBoxBiaoqian;
    private StatusBox statusBoxKehulian;

    /**
     * 输入的运单号
     **/
    private String charsequence;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_sendpieces);
        ViewUtils.inject(this);
        inputDataInfo = new InputDataInfo();
        initView();

    }

    private void initView() {
        mTitleBase.setText("运单查询");
        mUserName.setText((new StringBuilder(String.valueOf(MyApp.mEmpName))).append(MyApp.mDeptName).toString());

        mKeyword.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent openCameraIntent = new Intent(QueryWaybillActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                return false;
            }
        });

        mKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                charsequence = mKeyword.getText().toString();
                if (charsequence.length() == 12) {
                    clearTxt();
                    if (MyUtils.isNetworkConnect(QueryWaybillActivity.this)) {
                        HttpUtils httpUtils = new HttpUtils();
                        // RequestParams params = new RequestParams();
                        // params.addBodyParameter("waybillNo", charsequence);
                        // params.addBodyParameter("devId", MyApp.mSession);

                        StringBuilder builder = new StringBuilder();
                        builder.append("?").append("waybillNo=").append(charsequence);
                        String url = MyApp.mPathServerURL + Conf.queryDeliverPrintListAction + builder.toString();

                        // http://copserver.jx-express.cn/jxe-cop/pda/queryWaybillByList.action?waybillNo=029143685850
                        // http://copserver.jx-express.cn/jxe-cop/pda/queryWaybillByList
                        // .action?waybillNo=029143685850&devId=865372022551524
                        httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

                            @Override
                            public void onFailure(HttpException arg0, String arg1) {
                                showT("网络异常，请稍候再试");
                            }

                            @Override
                            public void onSuccess(ResponseInfo<String> arg0) {
                                String result = arg0.result;
                                Gson gson = new Gson();
                                I_QueryWaybill i_Send = gson.fromJson(result, I_QueryWaybill.class);

                                if (i_Send == null || i_Send.pdaWaybillList == null || i_Send.pdaWaybillList.size()
                                        <= 0) {
                                    showT("数据为空");
                                    return;
                                }

                                if (i_Send.success) {
                                    inputDataInfo = i_Send.getPdaWaybillList().get(0);
                                    setOrderView(inputDataInfo);
                                    //queryDeliverCommission(inputDataInfo);
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
        });

        // 打印标签
        mPrint.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                statusBoxBiaoqian = new StatusBox(QueryWaybillActivity.this, mPrint);
                statusBoxBiaoqian.Show("正在打印 ...");
                if (!BluetoothUtil.OpenPrinter(QueryWaybillActivity.this, xiqopiaoAddress)) {
                    statusBoxBiaoqian.Close();
                    return;
                }
                xiaopiao();
                zpSDK.zp_close();
                statusBoxBiaoqian.Close();
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // 客户联
        mClient.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                statusBoxKehulian = new StatusBox(QueryWaybillActivity.this, mClient);
                statusBoxKehulian.Show("正在打印 ...");
                if (!BluetoothUtil.OpenPrinter(QueryWaybillActivity.this, xiqopiaoAddress)) {
                    statusBoxKehulian.Close();
                    return;
                }
                Kehejianlian();
                zpSDK.zp_close();
                statusBoxKehulian.Close();
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void Kehejianlian() {
        DecimalFormat df = new DecimalFormat("#.00");
        /*if(commission!=0){
            commission = Double.valueOf(df.format(commission));
		}*/
        //queryDeliverCommission(inputDataInfo);
        if (MyUtils.isEmpty(inputDataInfo.waybillFee)) {
            inputDataInfo.waybillFee = "0";
        }/*else{
			double waybillFee = Double.valueOf(inputDataInfo.waybillFee);
			inputDataInfo.deliverWaybillFee = df.format(waybillFee - commission);
		}*/
        if (MyUtils.isEmpty(inputDataInfo.signBackFee)) {
            inputDataInfo.signBackFee = "0";
        }
        if (MyUtils.isEmpty(inputDataInfo.waitNotifyFee)) {
            inputDataInfo.waitNotifyFee = "0";
        }
        if (MyUtils.isEmpty(inputDataInfo.deboursFee)) {
            inputDataInfo.deboursFee = "0";
        }
        if (MyUtils.isEmpty(inputDataInfo.deliverFee)) {
            inputDataInfo.deliverFee = "0";
            //	inputDataInfo.deliverFeeCommissio  = String.valueOf(Double.parseDouble(inputDataInfo.deliverFee) +
            // commission);
        } else {
            //	inputDataInfo.deliverFeeCommissio =String.valueOf(Double.parseDouble(inputDataInfo.deliverFee) +
            // commission);
        }
        if (MyUtils.isEmpty(inputDataInfo.goodsChargeFee)) {
            inputDataInfo.goodsChargeFee = "0";
        }

        if (MyUtils.isEmpty(inputDataInfo.signBackNo)) {
            inputDataInfo.signBackNo = "";
        }

        if (MyUtils.isEmpty(inputDataInfo.bankType)) {
            inputDataInfo.bankType = "";
        }
        if (MyUtils.isEmpty(inputDataInfo.bankNo)) {
            inputDataInfo.bankNo = "";
        }

        if (MyUtils.isEmpty(inputDataInfo.chargeAgentFee)) {
            inputDataInfo.chargeAgentFee = "";
        }

        if (MyUtils.isEmpty(inputDataInfo.transferDays)) {
            inputDataInfo.transferDays = "";
        }

        if (MyUtils.isEmpty(inputDataInfo.waybillRemk)) {
            inputDataInfo.waybillRemk = "";
        }

        // 打印
        if (!BluetoothPrintTool.kehuLian(inputDataInfo)) {
            statusBoxBiaoqian.Close();
        }
    }

    private void clearTxt() {
        mWaybillNumber.setText("");
        mEmpCode.setText("");
        mTxtSourceZone.setText("");
        mConsignorContName.setText("");
        mConsignorPhone.setText("");
        mTxtDestZone.setText("");

        mAddresseeContName.setText("");
        mAddresseePhone.setText("");

        mGoodsChargeFee.setText("");
        mPayMethod.setText("");
        mConsName.setText("");
        mQuantity.setText("");

        mMeterageWeightQty.setText("");
        mWaybillNoFee.setText("");
    }

    @SuppressWarnings("unchecked")
    private void setOrderView(InputDataInfo data) {
        double d = 0;
        double d1;
        double d2;
        double d3;
        double d4;
        double d5;
        double d6;
        double all;

        mWaybillNumber.setText(data.waybillNo);
        mEmpCode.setText(data.consigneeEmpCode);
        mConsignorContName.setText(data.consignorContName);

        if (MyUtils.isEmpty(data.consignorMobile)) {// 发货人电话
            mConsignorPhone.setText(data.consignorPhone);
        } else {
            mConsignorPhone.setText(data.consignorMobile);
        }

        mConsName.setText(data.consName);
        mAddresseeContName.setText(data.addresseeContName);

        // mTxtInsured.setText(data.insuranceAmount == null ? "0.0" :
        // data.insuranceAmount);// 保价
        mGoodsChargeFee.setText(data.goodsChargeFee == null ? "0.0" : data.goodsChargeFee);// 代收

        if (MyUtils.isEmpty(data.addresseeMobile)) {
            mAddresseePhone.setText(data.addresseePhone);
        } else {
            mAddresseePhone.setText(data.addresseeMobile);
        }

        mMeterageWeightQty.setText(data.meterageWeightQty);
        mQuantity.setText(data.quantity + "");
        mWaybillNoFee.setText(data.waybillFee);

        if ("1".equals(data.paymentTypeCode)) {// 寄付
            d = Double.parseDouble(data.goodsChargeFee == null ? "0.0" : data.goodsChargeFee);
            all = Double.valueOf(d);
        } else {// 2-到付
            d = Double.parseDouble(data.waybillFee == null ? "0.0" : data.waybillFee);
            d1 = Double.parseDouble(data.insuranceFee == null ? "0.0" : data.insuranceFee);
            d2 = Double.parseDouble(data.signBackFee == null ? "0.0" : data.signBackFee);
            d3 = Double.parseDouble(data.deboursFee == null ? "0.0" : data.deboursFee);
            d4 = Double.parseDouble(data.waitNotifyFee == null ? "0.0" : data.waitNotifyFee);
            d5 = Double.parseDouble(data.deliverFee == null ? "0.0" : data.deliverFee);
            d6 = Double.parseDouble(data.goodsChargeFee == null ? "0.0" : data.goodsChargeFee);
            all = Double.valueOf(d + d1 + d2 + d3 + d4 + d5 + d6);
        }
        String allMoney;
        if (all == 0) {
            allMoney = "0.0";
        } else {
            data.FeeCount = all;
            allMoney = data.FeeCount + "";
        }
        mPayMethod.setText(allMoney + "");

        if (data.childNoList != null) {
            data.childNoLists = new ArrayList<String>();
            String[] split = data.childNoList.split(",");

            for (int i = 0; i < split.length; i++) {
                data.childNoLists.add(split[i]);
            }

            // StringTokenizer childList = new StringTokenizer(data.childNoList,
            // ",");
            // if (childList.hasMoreElements()) {
            // String s = childList.nextToken();
            //
            // }
        }

        DepartmentDBWrapper departmentDB = DepartmentDBWrapper.getInstance(getApplication());
        cursor = departmentDB.rawQueryRank(data.sourceZoneCode);// 查询发货网点
        String districtCode = "";
        if (cursor.moveToFirst()) {
            data.outsiteName = cursor.getString(cursor.getColumnIndex("outsiteName"));
            data.sourceZoneAddress = cursor.getString(cursor.getColumnIndex("deptAddress"));// 发货地址
            data.sourceZoneName = cursor.getString(cursor.getColumnIndex("deptName"));// 发货网点
            data.sourcedistrictCode = cursor.getString(cursor.getColumnIndex("districtCode"));// 地区代码

        }
        cursor.close();

        Cursor adapterview = departmentDB.rawQueryRank(inputDataInfo.destZoneCode);
        if (adapterview.moveToFirst()) {
            inputDataInfo.partitionName = adapterview.getString(adapterview.getColumnIndex("partitionName"));
        }
        adapterview.close();

        mTxtSourceZone.setText(data.sourceZoneName);

        cursor = departmentDB.rawQueryRank(data.destZoneCode);// 查询收货网点
        if (!cursor.moveToNext()) {
            cursor.close();
            return;
        }
        data.destZoneName = cursor.getString(cursor.getColumnIndex("deptName"));// 收货网店
        mTxtDestZone.setText(data.destZoneName);

        Cursor districtCursor = DistrictDBWrapper.getInstance(getApplication()).rawQueryRank(data.sourcedistrictCode);
        if (districtCursor.moveToNext()) {
            data.provinceCode = districtCursor.getString(districtCursor.getColumnIndex("provinceCode"));
        } else {
            districtCursor.close();
        }

        // if ("02911".equals(inputDataInfo.sourcedistrictCode)) {
        // if (Double.parseDouble(inputDataInfo.meterageWeightQty) <= 5D) {
        // inputDataInfo.shangmenFee = "4.0";
        // double allP = Double.parseDouble(inputDataInfo.waybillFee)
        // - Double.parseDouble(inputDataInfo.shangmenFee);
        // inputDataInfo.waybillFee = MyUtils.round(allP);
        //
        // } else {
        // inputDataInfo.shangmenFee = MyUtils
        // .round(4D + (Double.parseDouble(inputDataInfo.meterageWeightQty) -
        // 5D) * 0.10000000000000001D);
        // inputDataInfo.waybillFee =
        // MyUtils.round(Double.parseDouble(inputDataInfo.waybillFee)
        // - Double.parseDouble(inputDataInfo.shangmenFee));
        //
        // }
        // }

    }

    private void xiaopiao() {
        appendData();

        BluetoothPrintTool.xiaopiao(inputDataInfo, inputDataInfo.childNoLists);
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

        mTxtSourceZone.setText(sourceDeptName);

        cursor = departmentDB.rawQueryRank(inputDataInfo.destZoneCode);// 查询收货网点
        if (!cursor.moveToNext()) {
            cursor.close();
            return;
        }

        inputDataInfo.destZoneAddress = cursor.getString(cursor.getColumnIndex("deptAddress"));// 收货地址
        String destDeptName = cursor.getString(cursor.getColumnIndex("deptName"));// 收货网店
        districtCode = cursor.getString(cursor.getColumnIndex("districtCode"));
        inputDataInfo.outsiteName = destDeptName;
        mTxtDestZone.setText(destDeptName);

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
            xiqopiaoAddress = cursor.getString(cursor.getColumnIndex("address"));
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
	
	/*public void queryDeliverCommission(InputDataInfo inputDataInfo) {
		if (inputDataInfo != null) {
			if (MyUtils.isNetworkConnect(QueryWaybillActivity.this)) {
				HttpUtils httpUtils = new HttpUtils();
				RequestParams params = new RequestParams();
				params.addBodyParameter("meterageWeightQty", inputDataInfo.meterageWeightQty);
				params.addBodyParameter("deptCode", inputDataInfo.destZoneCode);
				params.addBodyParameter("productTypeCode", inputDataInfo.productTypeCode);

				StringBuilder builder = new StringBuilder();
				builder.append("?").append("meterageWeightQty=").append(inputDataInfo.meterageWeightQty).append("&")
				.append("deptCode=").append(inputDataInfo.destZoneCode).append("&").append("productTypeCode=").append
				(inputDataInfo.productTypeCode);
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
								Toast.makeText(QueryWaybillActivity.this, "网络异常，请稍候再试", 0).show();
								commission = 0.0;
							}

							@Override
							public void onSuccess(ResponseInfo<String> arg0) {
								String result = arg0.result;
								Gson gson = new Gson();
								I_QueryWaybill i_Send = gson.fromJson(result,I_QueryWaybill.class);

								if (i_Send == null|| i_Send.getCommission()==null) {
									commission = 0.0;
									return;
								}

								if (i_Send.success) {
									commission = i_Send.getCommission();

								} else {
									Toast.makeText(
											QueryWaybillActivity.this,
											i_Send.error, 0).show();
								}
							}

						});

			} else {
				Toast.makeText(QueryWaybillActivity.this,
						"无网络连接，请稍后再试", 0).show();
			}
		}
	}*/
}
