// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.eruitong.db;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import com.eruitong.model.DbDatafInfo;
import com.eruitong.model.serviceProdProps;

// Referenced classes of package com.dingzhou.db:
//            DBHelper

public class ServiceProdPriceDBWrapper {

	private static ServiceProdPriceDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private ServiceProdPriceDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1))
				.getWritableDatabase();
	}

	public static ServiceProdPriceDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new ServiceProdPriceDBWrapper(context);
		}
		return sInstance;
	}

	public void deleteRank() {
		mDb.delete("ServiceProdPrice", null, null);
	}

	public void insertRank(String s, String s1, String s2, String s3, int i,
			int j, String s4) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("currencyCode", s);
		contentvalues.put("feeTypeCode", s1);
		contentvalues.put("priceDesc", s2);
		contentvalues.put("serviceCode", s3);
		contentvalues.put("attrId", Integer.valueOf(i));
		contentvalues.put("priceId", Integer.valueOf(j));
		contentvalues.put("propValue", s4);
		mDb.insert("ServiceProdPrice", "", contentvalues);
	}

	public void insertRank(List<DbDatafInfo> dataList) {
		mDb.beginTransaction();
		String s1;
		String s2;
		String s3;
		String s4;
		for (DbDatafInfo info : dataList) {
			s1 = info.getCurrencyCode();
			s2 = info.getFeeTypeCode();
			s3 = info.getPriceDesc();
			s4 = info.getServiceCode();

			List<serviceProdProps> serviceProdProps = info
					.getServiceProdProps();
			for (serviceProdProps serviceInfo : serviceProdProps) {
				ContentValues contentvalues = new ContentValues();
				contentvalues.put("currencyCode", s1);
				contentvalues.put("feeTypeCode", s2);
				contentvalues.put("priceDesc", s3);
				contentvalues.put("serviceCode", s4);

				contentvalues.put("attrId", Integer.valueOf(serviceInfo
						.getAttrId() == null ? "0" : serviceInfo.getAttrId()));
				contentvalues
						.put("priceId", Integer.valueOf(serviceInfo
								.getPriceId() == null ? "0" : serviceInfo
								.getPriceId()));
				contentvalues.put("propValue", serviceInfo.getPropValue());
				mDb.insert("ServiceProdPrice", "", contentvalues);
			}
		}
		mDb.setTransactionSuccessful();
		mDb.endTransaction();

	}

	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from ServiceProdPrice order by _id ASC",
				null);
	}

	public Cursor rawQueryRank(String s, int i) {
		s = (new StringBuilder(
				"select * from ServiceProdPrice where feeTypeCode =\""))
				.append(s).append("\"").append(" and ").append("attrId")
				.append(" =\"").append(i).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public Cursor rawQueryRank(String s, String s1) {
		s = (new StringBuilder(
				"select * from ServiceProdPrice where feeTypeCode =\""))
				.append(s).append("\"").append(" and ").append("propValue")
				.append(" =\"").append(s1).append("\"").toString();
		return mDb.rawQuery(s, null);
	}
	
	public Cursor rawQueryRanks(String s, int i) {
		s = (new StringBuilder("select * from ServiceProdPrice where feeTypeCode =\"")).append(s).append("\"")
				.append(" and ").append("priceId").append(" =\"").append(i).append("\"").toString();
		return mDb.rawQuery(s, null);
	}
}
