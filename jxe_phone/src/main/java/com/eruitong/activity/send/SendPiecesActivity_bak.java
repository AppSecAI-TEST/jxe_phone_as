package com.eruitong.activity.send;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zpSDK.zpSDK.zpSDK;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eruitong.activity.BaseActivity;
import com.eruitong.bluetooth.StatusBox;
import com.eruitong.config.Conf;
import com.eruitong.db.BluetoothDBWrapper;
import com.eruitong.db.DepartmentDBWrapper;
import com.eruitong.db.DistrictDBWrapper;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.model.net.I_PdaUploadData;
import com.eruitong.receiver.BaseReceiver;
import com.eruitong.receiver.BatteryReceiver;
import com.eruitong.receiver.Hearer;
import com.eruitong.print.bluetooth.BluetoothUtil;
import com.eruitong.utils.CheckCode;
import com.eruitong.utils.MyUtils;
import com.eruitong.utils.PlayRaws;
import com.eruitong.utils.VibratorUtil;
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

/**
 * 派件签收
 * 
 * @author Administrator
 * 
 */
public class SendPiecesActivity_bak extends BaseActivity implements Hearer {

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

	@ViewInject(R.id.edt_delivery_waybill_number)
	EditText mEdtWaybillNumber;
	@ViewInject(R.id.btn_delivery_waybill_query)
	Button mBtnWaybillQuery;
	/** 签收人 **/
	@ViewInject(R.id.edt_delivery_signforer)
	EditText mEdtSignforer;
	/** 收派员 **/
	@ViewInject(R.id.edt_delivery_courier)
	EditText mEdtCourier;

	/** 运单 **/
	@ViewInject(R.id.item_quire_upload_waybillnumber)
	TextView mTxtWaybill;
	/** 收派 **/
	@ViewInject(R.id.item_quire_upload_empCode)
	TextView mTxtEmpCode;
	/** 发货网点 **/
	@ViewInject(R.id.item_quire_upload_sourceZoneCode)
	TextView mTxtSourceZone;
	/** 收货网点 **/
	@ViewInject(R.id.item_quire_upload_destZoneCode)
	TextView mTxtDestZone;
	/** 寄方 **/
	@ViewInject(R.id.item_quire_upload_consignorContName)
	TextView mTxtConsignorContName;
	/** 寄电 **/
	@ViewInject(R.id.item_quire_upload_consignorPhone)
	TextView mTxtConsignorContNumber;
	/** 收方 **/
	@ViewInject(R.id.item_quire_upload_addresseeContName)
	TextView mTxtAddresseeContName;
	/** 收电 **/
	@ViewInject(R.id.item_quire_upload_addresseePhone)
	TextView mTxtAddresseePhone;
	/** 代收 **/
	@ViewInject(R.id.item_quire_upload_collection)
	TextView mTxtCollection;
	/** 保价 **/
	@ViewInject(R.id.item_quire_upload_insured)
	TextView mTxtInsured;
	/** 运费 **/
	@ViewInject(R.id.item_quire_upload_waybillMoney)
	TextView mTxtWaybillMoney;
	/** 合计 **/
	@ViewInject(R.id.item_quire_upload_allmoney)
	TextView mTxtAllmoney;
	/** 货物 **/
	@ViewInject(R.id.item_quire_upload_consName)
	TextView mTxtConsName;
	/** 件数 **/
	@ViewInject(R.id.item_quire_upload_quantity)
	TextView mTxtQuantity;
	/** 重量 **/
	@ViewInject(R.id.item_quire_upload_weight)
	TextView mTxtWeight;
	/** 返回 **/
	@ViewInject(R.id.btn_delivery_deliverysignfor_back)
	Button mBack;
	/** 签收 上传 **/
	@ViewInject(R.id.btn_delivery_deliverysignfor_print)
	Button mUpLoad;
	/** 签收 上传 打印 **/
	@ViewInject(R.id.btn_delivery_deliverysignfor_ok)
	Button mSignfor;

	/** 蓝牙地址 **/
	private String xiqopiaoAddress;

	private itemData itemData;
	private DepartmentDBWrapper departmentDB;
	private Cursor cursor;
	private StatusBox statusBox;
	/** 签收人 **/
	private String recipients;
	/** 收件员 **/
	private String deliverEmpCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_pieces);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		mTitleBase.setText("派件");
		mEdtWaybillNumber.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Intent openCameraIntent = new Intent(
						SendPiecesActivity_bak.this, CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
				return false;
			}
		});
		// 返回
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// 签收上传
		mUpLoad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isCheck())
					return;

				recipients = mEdtSignforer.getText().toString();
				deliverEmpCode = mEdtCourier.getText().toString();
				if (TextUtils.isEmpty(recipients)) {
					Toast.makeText(SendPiecesActivity_bak.this, "签收人不能为空", 0)
							.show();
					return;
				}

				if (TextUtils.isEmpty(deliverEmpCode)) {
					Toast.makeText(SendPiecesActivity_bak.this, "收件员不能为空", 0)
							.show();
					return;
				}

				if (TextUtils.isEmpty(itemData.waybillNo)) {
					Toast.makeText(SendPiecesActivity_bak.this, "运单号不能为空", 0)
							.show();
					return;
				} else {
					upLoad();
					return;
				}
			}
		});

		// 签收上传 打印
		mSignfor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isCheck())
					return;

				recipients = mEdtSignforer.getText().toString();
				deliverEmpCode = mEdtCourier.getText().toString();
				if (TextUtils.isEmpty(recipients)) {
					Toast.makeText(SendPiecesActivity_bak.this, "签收人不能为空", 0)
							.show();
					return;
				}
				if (TextUtils.isEmpty(deliverEmpCode)) {
					Toast.makeText(SendPiecesActivity_bak.this, "收件员不能为空", 0)
							.show();
					return;
				}
				if (TextUtils.isEmpty(itemData.waybillNo)) {
					Toast.makeText(SendPiecesActivity_bak.this, "运单号不能为空", 0)
							.show();
					return;
				}

				statusBox = new StatusBox(SendPiecesActivity_bak.this, mSignfor);
				statusBox.Show("正在打印 ...");
				if (!BluetoothUtil.OpenPrinter(SendPiecesActivity_bak.this,
						xiqopiaoAddress)) {
					statusBox.Close();
					return;
				}
				upLoad();

				quireWaybillNo();
				zpSDK.zp_close();
				statusBox.Close();
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		// 查询
		mBtnWaybillQuery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				queryWaybill();
			}

		});
	}

	/**
	 * 是否验收
	 */
	private boolean isCheck() {
		if (itemData == null) {
			Toast.makeText(SendPiecesActivity_bak.this, "无订单", 0).show();
			return false;
		}

		if (MyUtils.isEmpty(itemData.auditedFlg)) {
			itemData.auditedFlg = "1";
		}
		if ("1".equals(itemData.auditedFlg)) {
			return true;
		} else {
			Toast.makeText(SendPiecesActivity_bak.this, "未审核，不能签收", 0).show();
			return false;
		}
	}

	private void quireWaybillNo() {

		if (!zpSDK.zp_page_create(80D, 61D)) {
			statusBox.Close();
			return;
		}

		zpSDK.TextPosWinStyle = false;
		zpSDK.zp_draw_rect(0.0D, 0.0D, 72D, 50D, 3);
		zpSDK.zp_draw_line(44D, 0.0D, 44D, 10D, 3);
		zpSDK.zp_draw_line(44D, 5D, 72D, 5D, 3);
		zpSDK.zp_draw_line(0.0D, 10D, 72D, 10D, 3);
		zpSDK.zp_draw_line(0.0D, 14D, 72D, 14D, 3);
		zpSDK.zp_draw_line(0.0D, 18D, 72D, 18D, 3);
		zpSDK.zp_draw_line(0.0D, 22D, 72D, 22D, 3);
		zpSDK.zp_draw_line(0.0D, 26D, 72D, 26D, 3);
		zpSDK.zp_draw_line(0.0D, 30D, 72D, 30D, 3);
		zpSDK.zp_draw_line(0.0D, 34D, 72D, 34D, 3);
		zpSDK.zp_draw_line(0.0D, 38D, 72D, 38D, 3);
		zpSDK.zp_draw_line(0.0D, 42D, 72D, 42D, 3);
		zpSDK.zp_draw_line(0.0D, 46D, 72D, 46D, 3);
		zpSDK.zp_draw_bitmap(BitmapFactory.decodeResource(getResources(),
				R.drawable.jxe_icon), 5D, 10D);
		zpSDK.zp_draw_text_ex(50D, 5D, "客服电话", "宋体", 4D, 0, true, false, false);
		zpSDK.zp_draw_text_ex(47D, 9.5D, "4008111115", "宋体", 4D, 0, true,
				false, false);
		zpSDK.zp_draw_text_ex(
				1.0D,
				13.5D,
				(new StringBuilder("运单号: ")).append(itemData.waybillNo)
						.append(" ").toString(), "宋体", 3D, 0, false, false,
				false);
		if (!TextUtils.isEmpty(itemData.consignorPhone)
				&& !itemData.consignorPhone.equals("null")) {
			zpSDK.zp_draw_text_ex(
					1.0D,
					17.5D,
					(new StringBuilder(String
							.valueOf(itemData.consignorDistName))).append(" ")
							.append(itemData.sourceZoneCode).append(" ")
							.append(itemData.consignorContName).append(" ")
							.append(itemData.consignorPhone).toString(), "宋体",
					3D, 0, true, false, false);
		} else {
			zpSDK.zp_draw_text_ex(
					1.0D,
					17.5D,
					(new StringBuilder(String
							.valueOf(itemData.consignorDistName))).append(" ")
							.append(itemData.sourceZoneCode).append(" ")
							.append(itemData.consignorContName).append(" ")
							.append(itemData.consignorMobile).toString(), "宋体",
					3D, 0, true, false, false);
		}

		if (!TextUtils.isEmpty(itemData.addresseeMobile)
				&& !itemData.addresseeMobile.equals("null")) {
			zpSDK.zp_draw_text_ex(
					1.0D,
					21.5D,
					(new StringBuilder(String
							.valueOf(itemData.addresseeContName))).append(" ")
							.append(itemData.addresseeMobile).append(" ")
							.append(itemData.addresseeAddr).toString(), "宋体",
					2.8999999999999999D, 0, true, false, false);
		} else {
			zpSDK.zp_draw_text_ex(
					1.0D,
					21.5D,
					(new StringBuilder(String
							.valueOf(itemData.addresseeContName))).append(" ")
							.append(itemData.addresseePhone).append(" ")
							.append(itemData.addresseeAddr).toString(), "宋体",
					3D, 0, true, false, false);
		}

		zpSDK.zp_draw_text_ex(
				1.0D,
				25.5D,
				(new StringBuilder(String.valueOf(itemData.consName)))
						.append(" ").append(itemData.quantity).append("件 重量 ")
						.append(itemData.meterageWeightQty).append("kg ")
						.append("元 派件员 ").append(deliverEmpCode).toString(),
				"宋体", 3D, 0, true, false, false);
		zpSDK.zp_draw_text_ex(
				1.0D,
				29.5D,
				(new StringBuilder("运费 ")).append(itemData.waybillFee)
						.append("元 保价金额 ").append(itemData.insuranceAmount)
						.append("元 保价费 ").append(itemData.insuranceFee)
						.append("元  ").toString(), "宋体", 3D, 0, true, false,
				false);
		if (MyUtils.isEmpty(itemData.goodsChargeFee)) {
			itemData.goodsChargeFee = "0.0";
		}
		if (MyUtils.isEmpty(itemData.chargeAgentFee)) {
			itemData.chargeAgentFee = "0.0";
		}
		if (MyUtils.isEmpty(itemData.signBackFee)) {
			itemData.signBackFee = "0.0";
		}
		zpSDK.zp_draw_text_ex(1.0D, 33.5D,
				(new StringBuilder("代收金额 ")).append(itemData.goodsChargeFee)
						.append("元 代收服务费 ").append(itemData.chargeAgentFee)
						.append("元  ").toString(), "宋体", 3D, 0, true, false,
				false);
		if (MyUtils.isEmpty(itemData.signBackFee)) {
			itemData.signBackFee = "0.0";
		}
		if (itemData.fuelServiceFee == null) {
			itemData.fuelServiceFee = 0.0;
		}
		zpSDK.zp_draw_text_ex(
				1.0D,
				37.5D,
				(new StringBuilder("签回单费 ")).append(itemData.signBackFee)
						.append("元      取件费 ")
						.append(itemData.fuelServiceFee + "元").toString(),
				"宋体", 3D, 0, true, false, false);
		if (MyUtils.isEmpty(itemData.signBackNo)) {
			itemData.signBackNo = "";
		}
		zpSDK.zp_draw_text_ex(
				1.0D,
				41.5D,
				(new StringBuilder("回单号 ")).append(itemData.signBackNo)
						.append("  收条   身份证   盖章").toString(), "宋体", 3D, 0,
				true, false, false);
		if (MyUtils.isEmpty(itemData.deboursFee)) {
			itemData.deboursFee = "0.0";
		}
		if (MyUtils.isEmpty(itemData.waitNotifyFee)) {
			itemData.waitNotifyFee = "0.0";
		}
		if (MyUtils.isEmpty(itemData.WarehouseFee)) {
			itemData.WarehouseFee = "0.0";
		}
		zpSDK.zp_draw_text_ex(
				1.0D,
				45.5D,
				(new StringBuilder("垫付 ")).append(itemData.deboursFee)
						.append("元  等通知费 ").append(itemData.waitNotifyFee)
						.append("元 仓管费 ").append(itemData.WarehouseFee)
						.append(" 元").toString(), "宋体", 3D, 0, true, false,
				false);
		if (itemData.FeeCount == null || "null".equals(itemData.FeeCount)) {
			itemData.FeeCount = 0.0;
		}
		if (!itemData.paymentTypeCode.equals("1")) {
			if (!itemData.paymentTypeCode.equals("2")) {

			} else {
				zpSDK.zp_draw_text_ex(1.0D, 49.5D, (new StringBuilder("到付 "))
						.append(itemData.FeeCount).append("元").toString(),
						"宋体", 2.8999999999999999D, 0, true, false, false);
			}
		} else {
			zpSDK.zp_draw_text_ex(1.0D, 49.5D, (new StringBuilder("寄付 "))
					.append(itemData.FeeCount).append("元").toString(), "宋体",
					2.8999999999999999D, 0, true, false, false);
		}

		zpSDK.zp_page_print(false);
		zpSDK.zp_page_clear();
		zpSDK.zp_goto_mark_label(120);
		zpSDK.zp_page_free();
		return;

	}

	/** 上传订单 **/
	private void upLoad() {

		JSONArray jsonarray = new JSONArray();
		JSONObject jsonobject = new JSONObject();
		try {
			jsonobject.put("operTypeCode", "13");
			jsonobject.put("deliverEmpCode", deliverEmpCode);
			jsonobject.put("waybillNo", itemData.waybillNo);
			jsonobject.put("strOperTm", itemData.consignedTm);
			jsonobject.put("inputType", itemData.inputType);
			jsonobject.put("carCode", "");
			jsonobject.put("carNo", "");
			jsonobject.put("lineCode", "");
			jsonobject.put("stayWayCode", "");
			jsonobject.put("lockedCarCode", "");
			jsonobject.put("srcCarNo", "");
			jsonobject.put("bagCageNo", recipients);
			jsonobject.put("deptCode", MyApp.mDeptCode);
			jsonobject.put("operEmpCode", MyApp.mEmpCode);
			jsonobject.put("macNo", MyApp.mSession);
			jsonobject.put("upLoadEmpCode", MyApp.mEmpCode);
			jsonobject.put("acquisitionInfo", "");
			jsonarray.put(jsonobject);

			HttpUtils httpUtils = new HttpUtils();
			RequestParams params = new RequestParams();
			params.addBodyParameter("deptCode", MyApp.mDeptCode);
			params.addBodyParameter("empCode", MyApp.mEmpCode);
			params.addBodyParameter("devId", MyApp.mSession);
			params.addBodyParameter("jsonData", jsonarray.toString());

			httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL
					+ Conf.SigninWaybill, params,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							Toast.makeText(SendPiecesActivity_bak.this,
									"网络连接失败,请稍后再试", 0).show();
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							String result = arg0.result;
							I_PdaUploadData uploadData = new Gson().fromJson(
									result, I_PdaUploadData.class);

							if (uploadData.isSuccess()) {

								Toast.makeText(SendPiecesActivity_bak.this,
										"上传成功", 0).show();
							} else {
								Toast.makeText(SendPiecesActivity_bak.this,
										uploadData.getError(), 0).show();
							}

						}
					});

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/** 查询单号 **/
	private void queryWaybill() {
		String waybillNo = mEdtWaybillNumber.getText().toString();
		if (waybillNo.length() == 12) {
			String s = waybillNo.substring(0, 3);
			String s2 = waybillNo.substring(3, 11);
			String s1 = waybillNo.substring(11, 12);
			s2 = (new StringBuilder(String.valueOf(CheckCode.genCheckCode(s2))))
					.toString();

			if (s.equals("333") || s.equals("121")) {
				mEdtWaybillNumber.setText("");
				Toast.makeText(this, "扫描码不正确，请重新输入", 0).show();
				VibratorUtil.Vibrate(this, 500L);
				PlayRaws.PlayError(this);
				return;
			}
			if (s1.equals(s2)) {
				itemData data = new itemData();
				mEdtSignforer.getEditableText().clear();
				clearTxt();
				NetworkInfo networkinfo = ((ConnectivityManager) getSystemService("connectivity"))
						.getActiveNetworkInfo();
				if (networkinfo != null && networkinfo.isConnected()) {
					HttpUtils httpUtils = new HttpUtils();
					RequestParams params = new RequestParams();
					params.addBodyParameter("deliverEmpCode", "");
					params.addBodyParameter("waybillNo", waybillNo);
					params.addBodyParameter("deptCode", MyApp.mDeptCode);
					params.addBodyParameter("devId", MyApp.mSession);

					httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL
							+ Conf.queryWaybillNo, params,
							new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									Toast.makeText(SendPiecesActivity_bak.this,
											"网络异常，请稍候再试", 0).show();
								}

								@Override
								public void onSuccess(ResponseInfo<String> arg0) {
									String result = arg0.result;
									Gson gson = new Gson();
									I_Send i_Send = gson.fromJson(result,
											I_Send.class);

									if (i_Send == null
											|| i_Send.getPdaWaybillList() == null
											|| i_Send.getPdaWaybillList()
													.size() <= 0) {
										Toast.makeText(
												SendPiecesActivity_bak.this,
												"数据为空", 0).show();
										return;
									}

									if (i_Send.success) {
										itemData = i_Send.getPdaWaybillList()
												.get(0);
										setOrderView(itemData);

									} else {
										Toast.makeText(
												SendPiecesActivity_bak.this,
												i_Send.error, 0).show();
									}
								}

							});

					return;
				} else {
					Toast.makeText(this, "网络未连接 ", 0).show();
					return;
				}
			} else {
				Toast.makeText(this, "运单号输入错误", 0).show();
				return;
			}
		} else {
			Toast.makeText(this, "运单号输入错误", 0).show();
			return;
		}
	}

	private void clearTxt() {
		mTxtWaybill.setText("");
		mTxtEmpCode.setText("");
		mTxtConsignorContName.setText("");
		mTxtConsignorContNumber.setText("");
		mTxtAddresseeContName.setText("");
		mTxtAddresseePhone.setText("");
		mTxtConsName.setText("");

		mTxtWeight.setText("");
		mTxtQuantity.setText("");
		mTxtWaybillMoney.setText("");

		mTxtCollection.setText("");
		mTxtInsured.setText("");
		mTxtAllmoney.setText("");

		mTxtSourceZone.setText("");
		mTxtDestZone.setText("");
	}

	@SuppressWarnings("unchecked")
	private void setOrderView(itemData data) {
		double d = 0;
		double d1;
		double d2;
		double d3;
		double d4;
		double d5;
		double d6;
		double d7;
		double all;

		mEdtSignforer.setText(data.addresseeContName);
		mEdtCourier.setText(MyApp.mEmpCode);

		mTxtWaybill.setText(data.waybillNo);
		mTxtEmpCode.setText(data.consigneeEmpCode);
		mTxtConsignorContName.setText(data.consignorContName);

		if (MyUtils.isEmpty(data.consignorMobile)) {// 发货人
			mTxtConsignorContNumber.setText(data.consignorPhone);
		} else {
			mTxtConsignorContNumber.setText(data.consignorMobile);
		}

		mTxtConsName.setText(data.consName);
		mTxtAddresseeContName.setText(data.addresseeContName);

		mTxtInsured.setText(data.insuranceAmount == null ? "0.0"
				: data.insuranceAmount);// 保价
		mTxtCollection.setText(data.goodsChargeFee == null ? "0.0"
				: data.goodsChargeFee);// 代收

		if (MyUtils.isEmpty(data.addresseeMobile)) {
			mTxtAddresseePhone.setText(data.addresseePhone);
		} else {
			mTxtAddresseePhone.setText(data.addresseeMobile);
		}

		mTxtWeight.setText(data.meterageWeightQty);
		mTxtQuantity.setText(data.quantity);
		mTxtWaybillMoney.setText(data.waybillFee);

		if ("1".equals(data.paymentTypeCode)) {// 寄付
			d = Double.parseDouble(data.goodsChargeFee == null ? "0.0"
					: data.goodsChargeFee);
			all = Double.valueOf(d);
		} else {// 2-到付
			d = Double.parseDouble(data.waybillFee == null ? "0.0"
					: data.waybillFee);
			d1 = Double.parseDouble(data.insuranceFee == null ? "0.0"
					: data.insuranceFee);
			d2 = Double.parseDouble(data.signBackFee == null ? "0.0"
					: data.signBackFee);
			d3 = Double.parseDouble(data.deboursFee == null ? "0.0"
					: data.deboursFee);
			d4 = Double.parseDouble(data.waitNotifyFee == null ? "0.0"
					: data.waitNotifyFee);
			d5 = Double.parseDouble(data.deliverFee == null ? "0.0"
					: data.deliverFee);
			d6 = Double.parseDouble(data.goodsChargeFee == null ? "0.0"
					: data.goodsChargeFee);
			d7 = data.fuelServiceFee == null ? 0.0 : data.fuelServiceFee;
			all = Double.valueOf(d + d1 + d2 + d3 + d4 + d5 + d6 + d7);
		}
		String allMoney;
		if (all == 0) {
			allMoney = "0.0";
		} else {
			data.FeeCount = all;
			allMoney = data.FeeCount + "";
		}
		mTxtAllmoney.setText(allMoney + "");

		if (data.childNoList != null) {
			data.childNoLists = new ArrayList<String>();
			StringTokenizer childList = new StringTokenizer(data.childNoList,
					",");
			if (childList.hasMoreElements()) {
				String s = childList.nextToken();
				data.childNoLists.add(s);
			}
		}

		departmentDB = DepartmentDBWrapper.getInstance(getApplication());
		cursor = departmentDB.rawQueryRank(data.sourceZoneCode);// 查询发货网点
		if (!cursor.moveToNext()) {
			cursor.close();
			return;
		}

		data.outsiteName = cursor.getString(cursor
				.getColumnIndex("outsiteName"));
		data.sourceZoneAddress = cursor.getString(cursor
				.getColumnIndex("deptAddress"));// 发货地址
		String sourceDeptName = cursor.getString(cursor
				.getColumnIndex("deptName"));// 发货网点

		mTxtSourceZone.setText(sourceDeptName);

		itemData.sourcedistrictCode = cursor.getString(cursor
				.getColumnIndex("districtCode"));

		cursor = departmentDB.rawQueryRank(data.destZoneCode);// 查询收货网点
		if (!cursor.moveToNext()) {
			cursor.close();
			return;
		}
		data.destZoneAddress = cursor.getString(cursor
				.getColumnIndex("deptAddress"));// 收货地址
		String destDeptName = cursor.getString(cursor
				.getColumnIndex("deptName"));// 收货网店
		mTxtDestZone.setText(destDeptName);

		Cursor mCursor = DistrictDBWrapper.getInstance(getApplication())
				.rawQueryRank(itemData.sourcedistrictCode);
		if (!mCursor.moveToNext()) {
			mCursor.close();
			return;
		}

		data.addresseeDistName = mCursor.getString(mCursor
				.getColumnIndex("distName"));
		data.consignorDistName = mCursor.getString(mCursor
				.getColumnIndex("distName"));

		// if ("02911".equals(itemData.sourcedistrictCode)) {
		// DecimalFormat decimalformat = new DecimalFormat("0.0");
		// if (Double.parseDouble(itemData.meterageWeightQty) <= 5D) {
		// itemData.shangmenFee = "4.0";
		// itemData.waybillFee = (new
		// StringBuilder(String.valueOf(decimalformat.format(Double
		// .parseDouble(itemData.waybillFee) -
		// Double.parseDouble(itemData.shangmenFee))))).toString();
		// } else {
		// itemData.shangmenFee = (new
		// StringBuilder(String.valueOf(decimalformat.format(4D + (Double
		// .parseDouble(itemData.meterageWeightQty) - 5D) *
		// 0.10000000000000001D)))).toString();
		// itemData.waybillFee = (new
		// StringBuilder(String.valueOf(decimalformat.format(Double
		// .parseDouble(itemData.waybillFee) -
		// Double.parseDouble(itemData.shangmenFee))))).toString();
		//
		// }
		// }

	}

	protected void onStart() {
		initComps();
		super.onStart();
		BatteryReceiver.getInstant(this).addHeraer(this);
		isConnect();
	}

	private void isConnect() {
		Cursor cursor = BluetoothDBWrapper.getInstance(getApplication())
				.rawQueryRank("No1");
		do {
			if (!cursor.moveToNext()) {
				cursor.close();
				return;
			}
			xiqopiaoAddress = cursor
					.getString(cursor.getColumnIndex("address"));
		} while (true);
	}

	protected void onStop() {
		super.onStop();
		BatteryReceiver.getInstant(this).delHeraer(this);
	}

	private void initComps() {
		mUserName.setText((new StringBuilder(String.valueOf(MyApp.mEmpName)))
				.append(MyApp.mDeptName).toString());
	}

	public void upDate(BaseReceiver basereceiver, Intent intent) {
		if (intent != null) {
			int i = intent.getIntExtra("level", 0);
			mBatterySignal.setImageLevel(i);
			String date = (new SimpleDateFormat("yyyy-MM-dd HH:mm"))
					.format(new Date());
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
			mEdtWaybillNumber.setText(scanResult);
		}
	}

	class I_Send {
		public String error;
		public List<itemData> pdaWaybillList;
		public boolean success;

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public List<itemData> getPdaWaybillList() {
			return pdaWaybillList;
		}

		public void setPdaWaybillList(List<itemData> pdaWaybillList) {
			this.pdaWaybillList = pdaWaybillList;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

	}

	class itemData {

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

		itemData() {
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
			goodsChargeFee = "0.0";
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
		}

	}

}
