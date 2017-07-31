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
import org.json.JSONException;
import org.json.JSONObject;

import com.eruitong.model.DbDatafInfo;

/**
 * 地区
 * 
 * @author Administrator
 * 
 */
public class DistrictDBWrapper {

	private static DistrictDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private DistrictDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1))
				.getWritableDatabase();
	}

	public static DistrictDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DistrictDBWrapper(context);
		}

		return sInstance;

	}

	public void deleteRank() {
		mDb.delete("district", null, null);
	}

	public void deleteRank(String s) {
		mDb.delete("district", "distCode=?", new String[] { s });
	}

	public void insertRank(String typeCode, String distName, String distCode,
			String parentDistCode, String cityCode, String provinceCode,
			int districtId) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("typeCode", typeCode);
		contentvalues.put("distName", distName);
		contentvalues.put("distCode", distCode);
		contentvalues.put("parentDistCode", parentDistCode);
		contentvalues.put("cityCode", cityCode);
		contentvalues.put("provinceCode", provinceCode);
		contentvalues.put("districtId", Integer.valueOf(districtId));
		mDb.insert("district", "", contentvalues);
	}

	public void insertRank(List<DbDatafInfo> dataList) {
		mDb.beginTransaction();
		for (DbDatafInfo info : dataList) {
			ContentValues contentvalues = new ContentValues();
			contentvalues.put("typeCode", info.getTypeCode());
			contentvalues.put("distName", info.getDistName());
			contentvalues.put("distCode", info.getDistCode());
			contentvalues.put("parentDistCode", info.getParentDistCode());
			contentvalues.put("cityCode", info.getCityCode());
			contentvalues.put("provinceCode", info.getProvinceCode());
			contentvalues.put("districtId",
					Integer.valueOf(info.getId() == null ? "0" : info.getId()));
			mDb.insert("district", "", contentvalues);
		}
		mDb.setTransactionSuccessful();
		mDb.endTransaction();

	}

	// TODO
	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from district order by _id  ASC", null);
	}

	public Cursor rawQueryRank(String s) {
		s = (new StringBuilder("select * from district where distCode =\""))
				.append(s).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public void rawUpdateRank(String s, String s1, String s2, String s3,
			String s4, String s5) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("distName", s1);
		contentvalues.put("distCode", s2);
		contentvalues.put("parentDistCode", s3);
		contentvalues.put("cityCode", s4);
		contentvalues.put("provinceCode", s5);
		mDb.update("district", contentvalues, "typeCode=?", new String[] { s });
	}

	public void rawUpdateRank(String s, String s1, String s2, String s3,
			String s4, String s5, int i) {
	}
}
