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

public class LoginDBWrapper {

	private static LoginDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private LoginDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1))
				.getWritableDatabase();
	}

	public static LoginDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new LoginDBWrapper(context);
		}

		return sInstance;

	}

	public void deleteRank() {
		mDb.delete("login", null, null);
	}

	public void deleteRank(String s) {
		mDb.delete("login", "empCode=?", new String[] { s });
	}

	public void insertRank(String s, String s1, String s2, String s3, String s4) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("deptCode", s2);
		contentvalues.put("empCode", s1);
		contentvalues.put("empName", s);
		contentvalues.put("password", s3);
		contentvalues.put("permissionList", s4);
		mDb.insert("login", "", contentvalues);
	}

	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from login ASC", null);
	}

	public Cursor rawQueryRank(String s) {
		s = (new StringBuilder("select * from login where empCode =\""))
				.append(s).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public void rawUpdateRank(String s, String s1, String s2, String s3) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("deptCode", s2);
		contentvalues.put("empName", s);
		contentvalues.put("password", s3);
		mDb.update("login", contentvalues, "empCode=?", new String[] { s1 });
	}
}
