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
import com.eruitong.model.nextWeightPrices;

// Referenced classes of package com.dingzhou.db:
//            DBHelper

public class ProdpriceDBWrapper {

	private static ProdpriceDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private ProdpriceDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1))
				.getWritableDatabase();
	}

	public static ProdpriceDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new ProdpriceDBWrapper(context);
		}
		return sInstance;
	}

	public void deleteRank() {
		mDb.delete("prodprice", null, null);
	}

	public void insertRank(String s, String s1, String s2, String s3,
			String s4, String s5, String s6, String s7, Double double1,
			String s8, String s9, String s10, String s11) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("sourceZoneCode", s);
		contentvalues.put("destZoneCode", s1);
		contentvalues.put("serviceCode", s2);
		contentvalues.put("cargoTypeCode", s3);
		contentvalues.put("limitTypeCode", s4);
		contentvalues.put("expressTypeCode", s5);
		contentvalues.put("weightPriceQty", double1);
		contentvalues.put("startWeightQty", s6);
		contentvalues.put("maxWeightQty", s7);
		contentvalues.put("baseWeightQty", s8);
		contentvalues.put("basePrice", s9);
		contentvalues.put("weightRoundTypeCode", s10);
		contentvalues.put("priceRoundTypeCode", s11);
		mDb.insert("prodprice", "", contentvalues);
	}

	public void insertRank(List<DbDatafInfo> dataList) {
		mDb.beginTransaction();
		for (DbDatafInfo info : dataList) {

			List<nextWeightPrices> weightPrices = info.getNextWeightPrices();

			for (nextWeightPrices nextWeightPrices : weightPrices) {
				ContentValues contentvalues = new ContentValues();
				contentvalues.put("sourceZoneCode", info.getSourceZoneCode());
				contentvalues.put("destZoneCode", info.getDestZoneCode());
				contentvalues.put("serviceCode", info.getServiceCode());
				contentvalues.put("cargoTypeCode", info.getCargoTypeCode());
				contentvalues.put("limitTypeCode", info.getLimitTypeCode());
				contentvalues.put("expressTypeCode", info.getExpressTypeCode());

				contentvalues.put("weightPriceQty",
						nextWeightPrices.getWeightPriceQty());
				contentvalues.put("startWeightQty",
						nextWeightPrices.getStartWeightQty());
				contentvalues.put("maxWeightQty",
						nextWeightPrices.getMaxWeightQty());
				contentvalues.put("baseWeightQty",
						nextWeightPrices.getBaseWeightQty());
				contentvalues.put("basePrice", nextWeightPrices.getBasePrice());
				contentvalues.put("weightRoundTypeCode",
						nextWeightPrices.getWeightRoundTypeCode());
				contentvalues.put("priceRoundTypeCode",
						nextWeightPrices.getPriceRoundTypeCode());
				mDb.insert("prodprice", "", contentvalues);
			}

		}
		mDb.setTransactionSuccessful();
		mDb.endTransaction();

	}

	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from prodprice order by _id ASC", null);
	}

	public Cursor rawQueryRank(String s, String s1, String s2, String s3,
			String s4, String s5) {
		s = (new StringBuilder(
				"select * from prodprice where sourceZoneCode =\"")).append(s)
				.append("\"").append(" and ").append("destZoneCode")
				.append(" =\"").append(s1).append("\"").append(" and ")
				.append("serviceCode").append(" =\"").append(s2).append("\"")
				.append(" and ").append("cargoTypeCode").append(" =\"")
				.append(s3).append("\"").append(" and ")
				.append("limitTypeCode").append(" =\"").append(s4).append("\"")
				.append(" and ").append("expressTypeCode").append(" =\"")
				.append(s5).append("\"").toString();
		return mDb.rawQuery(s, null);
	}
}
