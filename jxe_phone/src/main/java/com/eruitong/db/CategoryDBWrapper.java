// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.eruitong.db;

import java.util.List;

import com.eruitong.model.DbDatafInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



/**
 * 特殊品类
 * 
 * @author Administrator
 * 
 */
public class CategoryDBWrapper {

	private static CategoryDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private CategoryDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1)).getWritableDatabase();
	}

	public static CategoryDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new CategoryDBWrapper(context);
		}

		return sInstance;

	}

	public void deleteRank() {
		mDb.delete("category", null, null);
	}

	public void deleteRank(String s) {
		mDb.delete("category", "categoryCode=?", new String[] { s });
	}
	
	public void insertRank(List<DbDatafInfo> dataList) {
		mDb.beginTransaction();
		for (DbDatafInfo info : dataList) {
			ContentValues contentvalues = new ContentValues();
			contentvalues.put("categoryPriceId", info.getCategoryPriceId());
			//contentvalues.put("categoryId", info.getCategoryId());
			contentvalues.put("sourceZoneCode", info.getSourceZoneCode());
			contentvalues.put("destZoneCode", info.getDestZoneCode());
			contentvalues.put("price", info.getPrice());
			contentvalues.put("priceRoundTypeCode", info.getPriceRoundTypeCode());
			contentvalues.put("categoryCode", info.getCategoryCode());
			//contentvalues.put("categoryName", info.getCategoryName());
			//contentvalues.put("categoryDesc", info.getCategoryDesc());
			//contentvalues.put("unitName", info.getParentDistCode());
			contentvalues.put("standardWeight", info.getStandardWeight());
			//contentvalues.put("validFlg", info.getProvinceCode());
			contentvalues.put("lowerPrice", info.getLowerPrice());
			contentvalues.put("commissionAmt", info.getCommissionAmt());
			contentvalues.put("serviceTypeCode", info.getServiceTypeCode());
			mDb.insert("category", "", contentvalues);
		}
		mDb.setTransactionSuccessful();
		mDb.endTransaction();

	}

	// TODO
	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from category ", null);
	}

	public Cursor rawQueryRank(String s) {
		s = (new StringBuilder("select * from category where categoryCode =\"")).append(s).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	
	public Cursor rawQueryRank(String s, String s1, String s2, String s3) {
		s = (new StringBuilder(
				"select * from category where sourceZoneCode =\"")).append(s)
				.append("\"").append(" and ").append("destZoneCode")
				.append(" =\"").append(s1).append("\"").append(" and ")
				.append("serviceTypeCode").append(" =\"").append(s2).append("\"")
				.append(" and ").append("categoryCode").append(" =\"")
				.append(s3).append("\"").toString();
		return mDb.rawQuery(s, null);
	}
	
	public void rawUpdateRank(String s, String s1, String s2, String s3, String s4, String s5, int i) {
	}
}
