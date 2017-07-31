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

// Referenced classes of package com.dingzhou.db:
//            DBHelper

/**
 * 失败原因
 * 
 * @author Administrator
 * 
 */
public class FailCauseDBWrapper {

	private static FailCauseDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private FailCauseDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1))
				.getWritableDatabase();
	}

	public static FailCauseDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new FailCauseDBWrapper(context);
		}

		return sInstance;

	}

	public Cursor SeleQueryRank(String s) {
		s = (new StringBuilder("select * from failCause where causecode =\""))
				.append(s).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public void deleteRank() {
		mDb.delete("failCause", null, null);
	}

	public void insertRank(String s, String s1) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("causecode", s);
		contentvalues.put("causecont", s1);
		mDb.insert("failCause", "", contentvalues);
	}

	public void insertRank(List<DbDatafInfo> dataList) {
		mDb.beginTransaction();
		for (DbDatafInfo info : dataList) {
			ContentValues contentvalues = new ContentValues();
			contentvalues.put("causecode", info.getCausecode());
			contentvalues.put("causecont", info.getCausecont());
			mDb.insert("failCause", "", contentvalues);
		}
		mDb.setTransactionSuccessful();
		mDb.endTransaction();

	}

	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from failCause ASC", null);
	}

	public Cursor rawQueryRank(String s) {
		s = (new StringBuilder("select * from failCause where causecont =\""))
				.append(s).append("\"").toString();
		return mDb.rawQuery(s, null);
	}
}
