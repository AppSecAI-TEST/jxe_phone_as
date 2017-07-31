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
 * 基础数据修改通知
 * 
 * @author Administrator
 * 
 */
public class BaseDataModifyNotifyDBWrapper {

	private static BaseDataModifyNotifyDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private BaseDataModifyNotifyDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1))
				.getWritableDatabase();
	}

	public static BaseDataModifyNotifyDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new BaseDataModifyNotifyDBWrapper(context);
		}

		return sInstance;

	}

	public void deleteRank() {
		mDb.delete("baseDataModifyNotify", null, null);
	}

	public void insertRank(String s, String s1, String s2) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("entityTypeCode", s);
		contentvalues.put("lastModifiedTm", s1);
		contentvalues.put("compCode", s2);
		mDb.insert("baseDataModifyNotify", "", contentvalues);
	}

	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from baseDataModifyNotify ASC", null);
	}

	public Cursor rawQueryRank(String s) {
		s = (new StringBuilder(
				"select * from baseDataModifyNotify where lastModifiedTm =\""))
				.append(s).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public void rawUpdateRank(String s, String s1) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("entityTypeCode", s);
		mDb.update("baseDataModifyNotify", contentvalues, "lastModifiedTm=?",
				new String[] { s1 });
	}

	public void rawUpdateRank(String s, String s1, String s2) {
	}
}
