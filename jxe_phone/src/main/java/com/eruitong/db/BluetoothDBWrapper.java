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

public class BluetoothDBWrapper {

	private static BluetoothDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private BluetoothDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1))
				.getWritableDatabase();
	}

	public static BluetoothDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new BluetoothDBWrapper(context);
		}

		return sInstance;

	}

	public Cursor AddQueryRank(String s) {
		s = (new StringBuilder("select * from Bluetooth where address =\""))
				.append(s).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public void deleteRank() {
		mDb.delete("Bluetooth", null, null);
	}

	public void deleteRank(String s) {
		mDb.delete("Bluetooth", "type=?", new String[] { s });
	}

	public void insertRank(String s, String s1, String s2) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("type", s);
		contentvalues.put("name", s1);
		contentvalues.put("address", s2);
		mDb.insert("Bluetooth", "", contentvalues);
	}

	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from Bluetooth ASC", null);
	}

	public Cursor rawQueryRank(String s) {
		s = (new StringBuilder("select * from Bluetooth where type =\""))
				.append(s).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public void rawUpdateRank(String s) {
	}
}
