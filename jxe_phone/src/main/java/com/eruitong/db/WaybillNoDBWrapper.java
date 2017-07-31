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
import com.eruitong.model.OrderList;

// Referenced classes of package com.dingzhou.db:
//            DBHelper

public class WaybillNoDBWrapper {

	private static WaybillNoDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private WaybillNoDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1))
				.getWritableDatabase();
	}

	public static WaybillNoDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new WaybillNoDBWrapper(context);
		}
		return sInstance;
	}

	public Cursor WaybillQueryRank(String s) {
		s = (new StringBuilder("select * from waybillNo where waybillNos =\""))
				.append(s).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public void deleteRank() {
		mDb.delete("waybillNo", null, null);
	}

	public void deleteRank(String s) {
		mDb.delete("waybillNo", "waybillNos=?", new String[] { s });
	}

	public void insertRank(String s, String s1) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("areaCode", s);
		contentvalues.put("waybillNos", s1);
		mDb.insert("waybillNo", "", contentvalues);
	}

	public void insertRank(List<OrderList> dataList) {
		mDb.beginTransaction();
		for (OrderList info : dataList) {

			ContentValues contentvalues = new ContentValues();

			List<String> nosList = info.getWaybillNosList();

			for (String string : nosList) {
				contentvalues.put("areaCode", info.getAreaCode());
				contentvalues.put("waybillNos", string);
				mDb.insert("waybillNo", "", contentvalues);
			}
		}
		mDb.setTransactionSuccessful();
		mDb.endTransaction();

	}

	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from waybillNo ASC", null);
	}

	public Cursor rawQueryRank(String s) {
		try {
			s = (new StringBuilder("select * from waybillNo where areaCode =\""))
					.append(s).append("\"").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mDb.rawQuery(s, null);
	}

	public void rawUpdateRank(String s, String s1) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("waybillNos", s);
		mDb.update("waybillNo", contentValues, "areaCode=?", new String[] { s });
	}
}
