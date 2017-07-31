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

public class DeliveryDBWrapper {

	private static DeliveryDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private DeliveryDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1))
				.getWritableDatabase();
	}

	public static DeliveryDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DeliveryDBWrapper(context);
		}

		return sInstance;

	}

	public void TMdeleteRank(String s) {
		mDb.delete("deliverysignfor", "operTm=?", new String[] { s });
	}

	public void deleteRank() {
		mDb.delete("deliverysignfor", null, null);
	}

	public void deleteRank(int i) {
		mDb.delete("deliverysignfor", "opCode=?",
				new String[] { (new StringBuilder(String.valueOf(i)))
						.toString() });
	}

	public void deleteRank(String s) {
		mDb.delete("deliverysignfor", "waybillNo=?", new String[] { s });
	}

	public void insertRank(String s, int i, String s1, String s2, String s3,
			String s4, String s5, String s6, int j) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("operTm", s);
		contentvalues.put("opCode", Integer.valueOf(i));
		contentvalues.put("opName", s1);
		contentvalues.put("waybillNo", s2);
		contentvalues.put("recipients", s3);
		contentvalues.put("empname", s4);
		contentvalues.put("empCode", s5);
		contentvalues.put("inputType", s6);
		contentvalues.put("isupload", Integer.valueOf(j));
		mDb.insert("deliverysignfor", "", contentvalues);
	}

	public void insertRankOut(String s, int i, String s1, String s2, String s3,
			String s4, String s5, int j) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("operTm", s);
		contentvalues.put("opCode", Integer.valueOf(i));
		contentvalues.put("opName", s1);
		contentvalues.put("waybillNo", s2);
		contentvalues.put("empname", s3);
		contentvalues.put("empCode", s4);
		contentvalues.put("inputType", s5);
		contentvalues.put("isupload", Integer.valueOf(j));
		mDb.insert("deliverysignfor", "", contentvalues);
	}

	public Cursor rawQueryOpCode(int i) {
		String s = (new StringBuilder(
				"select * from deliverysignfor where opCode =\"")).append(i)
				.append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from deliverysignfor ASC", null);
	}

	public Cursor rawQueryRank(int i, int j) {
		String s = (new StringBuilder(
				"select * from deliverysignfor where isupload =\"")).append(i)
				.append("\"").append(" and ").append("opCode").append(" =\"")
				.append(j).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public Cursor rawQueryRank(int i, String s) {
		s = (new StringBuilder("select * from deliverysignfor where opCode =\""))
				.append(i).append("\"").append(" and ").append("waybillNo")
				.append(" =\"").append(s).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public void rawUpdateRank(int i, int j) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("isupload", Integer.valueOf(j));
		mDb.update("deliverysignfor", contentvalues, "opCode=?",
				new String[] { (new StringBuilder(String.valueOf(i)))
						.toString() });
	}

	public void rawUpdateRank(int i, String s, int j) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("opCode", Integer.valueOf(i));
		contentvalues.put("isupload", Integer.valueOf(j));
		mDb.update("deliverysignfor", contentvalues, "waybillNo=?",
				new String[] { s });
	}

	public void rawUpdateRank(String s, int i) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("isupload", Integer.valueOf(i));
		mDb.update("deliverysignfor", contentvalues, "waybillNo=?",
				new String[] { s });
	}
}
