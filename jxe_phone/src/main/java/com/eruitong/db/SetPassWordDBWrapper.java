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
 * 设置密码
 * 
 * @author Administrator
 * 
 */
public class SetPassWordDBWrapper {

	private static SetPassWordDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private SetPassWordDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1))
				.getWritableDatabase();
	}

	public static SetPassWordDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new SetPassWordDBWrapper(context);
		}

		return sInstance;

	}

	public void deleteRank() {
		mDb.delete("SetPassWord", null, null);
	}

	public void deleteRank(String s) {
		mDb.delete("SetPassWord", "name=?", new String[] { s });
	}

	public void insertRank(String s) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("name", s);
		mDb.insert("SetPassWord", "", contentvalues);
	}

	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from SetPassWord ASC", null);
	}

	public Cursor rawQueryRank(String s) {
		s = (new StringBuilder("select * from SetPassWord where name =\""))
				.append(s).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public void rawUpdateRank(String s) {
	}
}
