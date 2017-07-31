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

public class DeliveryRangeDBWrapper {

	private static DeliveryRangeDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private DeliveryRangeDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1))
				.getWritableDatabase();
	}

	public static DeliveryRangeDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DeliveryRangeDBWrapper(context);
		}

		return sInstance;

	}

	public void deleteRank() {
		mDb.delete("deliveryRange", null, null);
	}

	public void deleteRank(String s) {
		mDb.delete("deliveryRange", "areaName=?", new String[] { s });
	}

	public void insertRank(String s, String s1, String s2, String s3,
			String s4, String s5) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("areaCode", s);
		contentvalues.put("areaName", s1);
		contentvalues.put("areaRemark", s2);
		contentvalues.put("parentAreaCode", s3);
		contentvalues.put("typeCode", s4);
		contentvalues.put("rangeState", s5);
		mDb.insert("deliveryRange", "", contentvalues);
	}

	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from deliveryRange ASC", null);
	}

	public Cursor rawQueryRank(String s) {
		s = (new StringBuilder("select * from deliveryRange where typeCode =\""))
				.append(s).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public void rawUpdateRank(String s, String s1, String s2, String s3,
			String s4, String s5) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("areaCode", s);
		contentvalues.put("areaName", s1);
		contentvalues.put("areaRemark", s2);
		contentvalues.put("parentAreaCode", s3);
		contentvalues.put("typeCode", s4);
		contentvalues.put("rangeState", s5);
		mDb.update("deliveryRange", contentvalues, "typeCode=?",
				new String[] { s4 });
	}
}
