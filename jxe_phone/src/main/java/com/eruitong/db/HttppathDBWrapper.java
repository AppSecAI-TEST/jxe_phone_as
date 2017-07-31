// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.eruitong.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

// Referenced classes of package com.dingzhou.db:
//            DBHelper

public class HttppathDBWrapper {

	private static HttppathDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private HttppathDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1))
				.getWritableDatabase();
	}

	public static HttppathDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new HttppathDBWrapper(context);
		}
		return sInstance;
	}

	public void deleteRank() {
		mDb.delete("httppath", null, null);
	}

	public void deleteRank(String s) {
		mDb.delete("httppath", "name=?", new String[] { s });
	}

	public void insertRank(String s, String s1) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("name", s);
		contentvalues.put("deviceId", s1);
		mDb.insert("httppath", "", contentvalues);
	}

	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from httppath ASC", null);
	}

	public Cursor rawQueryRank(String s) {
		s = (new StringBuilder("select * from httppath where name =\""))
				.append(s).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public void rawUpdateRank(String s, String s1) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("deviceId", s);
		mDb.update("httppath", contentvalues, "name=?", new String[] { s1 });
	}
}
