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

public class childNoListDBWrapper {

	private static childNoListDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private childNoListDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1))
				.getWritableDatabase();
	}

	public static childNoListDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new childNoListDBWrapper(context);
		}

		return sInstance;

	}

	public Cursor SeaQueryRank(String s) {
		s = (new StringBuilder("select * from line where linecode like '%"))
				.append(s).append("%'").toString();
		return mDb.rawQuery(s, null);
	}

	public Cursor SeleQueryRank(String s) {
		s = (new StringBuilder("select * from childNo where waybillNo =\""))
				.append(s).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public void deleteRank() {
		mDb.delete("childNo", null, null);
	}

	public void deleteRank(String s) {
		mDb.delete("childNo", "waybillNo=?", new String[] { s });
	}

	public void insertRank(String s, String s1, String s2, int i) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("waybillNo", s);
		contentvalues.put("childwaybillNo", s1);
		contentvalues.put("isUpload", Integer.valueOf(i));
		contentvalues.put("numble", s2);
		mDb.insert("childNo", "", contentvalues);
	}

	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from childNo ASC", null);
	}

	public Cursor rawQueryRank(String s, int i) {
		s = (new StringBuilder("select * from childNo where waybillNo =\""))
				.append(s).append("\"").append(" and ").append("isUpload")
				.append(" =\"").append(i).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public void rawUpdateRank(String s, int i) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("isUpload", Integer.valueOf(i));
		mDb.update("childNo", contentvalues, "waybillNo=?", new String[] { s });
	}
}
