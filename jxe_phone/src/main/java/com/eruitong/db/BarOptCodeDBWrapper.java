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
/**
 * 条形选择码
 * 
 * @author Administrator
 * 
 */
public class BarOptCodeDBWrapper {

	private static BarOptCodeDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private BarOptCodeDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1))
				.getWritableDatabase();
	}

	public static BarOptCodeDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new BarOptCodeDBWrapper(context);
		}

		return sInstance;

	}

	public void deleteRank() {
		mDb.delete("barOptCode", null, null);
	}

	public void deleteRank(String s) {
		mDb.delete("barOptCode", "opCode=?", new String[] { s });
	}

	public void insertRank(int i, String s, String s1) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("opCodeId", Integer.valueOf(i));
		contentvalues.put("opName", s);
		contentvalues.put("opCode", s1);
		mDb.insert("barOptCode", "", contentvalues);
	}

	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from barOptCode ASC", null);
	}

	public Cursor rawQueryRank(String s) {
		s = (new StringBuilder("select * from barOptCode where opCode =\""))
				.append(s).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public void rawUpdateRank(int i, String s, String s1) {
	}

	public void rawUpdateRank(String s, String s1) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("opName", s);
		mDb.update("barOptCode", contentvalues, "opCode=?", new String[] { s1 });
	}
}
