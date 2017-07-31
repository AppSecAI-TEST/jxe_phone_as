package com.eruitong.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.widget.TextView;

import com.eruitong.config.Conf;
import com.eruitong.db.AcceptInputDBWrapper;
import com.eruitong.db.childNoListDBWrapper;
import com.eruitong.eruitong.MyApp;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class UpdateWaybill {

	private static UpdateWaybill updateActivity = new UpdateWaybill();

	private UpdateWaybill() {
	}

	public static UpdateWaybill getUpdateActivity() {
		return updateActivity;
	}

	public void update(final Context mContext, final int type,
			final TextView textView) {
		JSONArray jsonarray = new JSONArray();
		childNoListDBWrapper childnolistdbwrapper;
		AcceptInputDBWrapper acceptinputdbwrapper = AcceptInputDBWrapper
				.getInstance(mContext);
		childnolistdbwrapper = childNoListDBWrapper.getInstance(mContext);
		Cursor mCursor = acceptinputdbwrapper.rawQueryRank();
		do {

			if (mCursor.moveToNext()) {
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
				String s46;
				String custIdentityCard;
				String s47;
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
				j1 = mCursor.getColumnIndex("fuelServiceFee");
				s46 = mCursor.getString(j1);
				j1 = mCursor.getColumnIndex("versionNo");
				s47 = mCursor.getString(j1);
				j1 = mCursor.getColumnIndex("custIdentityCard");
				custIdentityCard = mCursor.getString(j1);

				String childNoList = "";
				cursor = childnolistdbwrapper.rawQueryRank(s3, 0);

				while (cursor.moveToNext()) {
					String s45 = cursor.getString(cursor
							.getColumnIndex("childwaybillNo"));
					if ("".equals(childNoList)) {
						childNoList = s45;
					} else {
						childNoList = childNoList + "," + s45;
					}
				}
				cursor.close();

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
					jsonobject.put("childNoList", childNoList);
					jsonobject.put("teamCode", s42);
					jsonobject.put("discountExpressFee", Double.valueOf(d8));
					jsonobject.put("inputerEmpCode", s43);
					jsonobject.put("distanceTypeCode", s44);
					jsonobject.put("fuelServiceFee", s46);
					jsonobject.put("versionNo", s47);
					jsonobject.put("custIdentityCard", custIdentityCard);
					jsonarray.put(jsonobject);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} while (true);

		if (!jsonarray.toString().equals("[]")) {

			/*
			 * // 讲数据保存到SD卡 String sdCardExist =
			 * Environment.getExternalStorageState(); // sd卡 if
			 * (sdCardExist.equals(Environment.MEDIA_MOUNTED)) {
			 * 
			 * SharedPreferences preferences =
			 * mContext.getSharedPreferences("isday", 0);
			 * 
			 * String oldDate = preferences.getString("day", "yyyy-MM-dd");
			 * Editor edit = preferences.edit();
			 * 
			 * String newDate = (new SimpleDateFormat("yyyy-MM-dd")).format(new
			 * Date()); edit.putString("day", newDate);
			 * 
			 * try { if (newDate.equals(oldDate)) { LogUtils.newFile(MyApp.PATH,
			 * oldDate + ".txt", jsonarray.toString(), true); } else {
			 * LogUtils.newFile(MyApp.PATH, newDate + ".txt",
			 * jsonarray.toString(), true); } } catch (IOException e) {
			 * e.printStackTrace(); } }
			 */
			HttpUtils httpUtils = new HttpUtils();
			RequestParams params = new RequestParams();
			String str = jsonarray.toString();

			System.err.println(str);

			params.addBodyParameter("jsonData", str);
			httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL
					+ Conf.updateWaybill, params,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							MyUtils.showText(mContext, "网络异常");
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {/*
																		 * String
																		 * result
																		 * =
																		 * arg0
																		 * .result
																		 * ;
																		 * 
																		 * I_SaveWaybill
																		 * saveWaybill
																		 * = new
																		 * Gson
																		 * ()
																		 * .fromJson
																		 * (
																		 * result
																		 * ,
																		 * I_SaveWaybill
																		 * .
																		 * class
																		 * ); if
																		 * (
																		 * saveWaybill
																		 * .
																		 * isSuccess
																		 * ()) {
																		 * 
																		 * AcceptInputDBWrapper
																		 * inputDBWrapper
																		 * =
																		 * AcceptInputDBWrapper
																		 * .
																		 * getInstance
																		 * (
																		 * mContext
																		 * );
																		 * childNoListDBWrapper
																		 * childNoListDBWrapper2
																		 * =
																		 * childNoListDBWrapper
																		 * .
																		 * getInstance
																		 * (
																		 * mContext
																		 * ); //
																		 * 查询到未上传的
																		 * Cursor
																		 * mCursor
																		 * =
																		 * inputDBWrapper
																		 * .
																		 * rawQueryRank
																		 * (0);
																		 * do {
																		 * if
																		 * (mCursor
																		 * .
																		 * moveToNext
																		 * ()) {
																		 * int i
																		 * =
																		 * mCursor
																		 * .
																		 * getColumnIndex
																		 * (
																		 * "waybillNo"
																		 * );
																		 * String
																		 * waybillNo
																		 * =
																		 * mCursor
																		 * .
																		 * getString
																		 * (i);
																		 * //
																		 * 改成已上传
																		 * inputDBWrapper
																		 * .
																		 * updateRank
																		 * (
																		 * waybillNo
																		 * , 1);
																		 * 
																		 * //
																		 * 根据单号查询是否有子单号
																		 * Cursor
																		 * cursor
																		 * =
																		 * childNoListDBWrapper2
																		 * .
																		 * rawQueryRank
																		 * (
																		 * waybillNo
																		 * , 0);
																		 * do {
																		 * if
																		 * (cursor
																		 * .
																		 * moveToNext
																		 * ()) {
																		 * childNoListDBWrapper2
																		 * .
																		 * rawUpdateRank
																		 * (
																		 * waybillNo
																		 * , 1);
																		 * }
																		 * else
																		 * {
																		 * cursor
																		 * .
																		 * close
																		 * ();
																		 * break
																		 * ; } }
																		 * while
																		 * (
																		 * true)
																		 * ; }
																		 * else
																		 * {
																		 * mCursor
																		 * .
																		 * close
																		 * ();
																		 * Toast
																		 * .
																		 * makeText
																		 * (
																		 * mContext
																		 * ,
																		 * "上传成功"
																		 * ,
																		 * 1).show
																		 * ();
																		 * if
																		 * (type
																		 * == 1)
																		 * {
																		 * textView
																		 * .
																		 * setText
																		 * ((new
																		 * StringBuilder
																		 * (
																		 * "未上传：[ "
																		 * )
																		 * ).append
																		 * (
																		 * "0").
																		 * append
																		 * (
																		 * " ]")
																		 * .
																		 * toString
																		 * ());
																		 * }
																		 * return
																		 * ; } }
																		 * while
																		 * (
																		 * true)
																		 * ; }
																		 * else
																		 * {
																		 * Toast
																		 * .
																		 * makeText
																		 * (
																		 * mContext
																		 * ,
																		 * saveWaybill
																		 * .
																		 * getError
																		 * (),
																		 * 0)
																		 * .show
																		 * (); }
																		 */
						}
					});

		}
	}
}
