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
import org.json.JSONException;
import org.json.JSONObject;

import com.eruitong.model.DbDatafInfo;

// Referenced classes of package com.dingzhou.db:
//            DBHelper

public class LineDBWrapper {

	private static LineDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private LineDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1))
				.getWritableDatabase();
	}

	public static LineDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new LineDBWrapper(context);
		}

		return sInstance;

	}

	public Cursor SeaQueryRank(String s) {
		s = (new StringBuilder("select * from line where linecode like '%"))
				.append(s).append("%'").toString();
		return mDb.rawQuery(s, null);
	}

	public Cursor SeleQueryRank(String s) {
		s = (new StringBuilder("select * from line where srczonecode =\""))
				.append(s).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public void deleteRank() {
		mDb.delete("line", null, null);
	}

	public void deleteRank(String s) {
		mDb.delete("line", "linename=?", new String[] { s });
	}

	public void insertRank(String s, String s1, String s2, String s3, int i) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("srczonecode", s);
		contentvalues.put("destzonecode", s1);
		contentvalues.put("linecode", s2);
		contentvalues.put("linename", s3);
		contentvalues.put("LineId", Integer.valueOf(i));
		mDb.insert("line", "", contentvalues);
	}

	public void insertRank(List<DbDatafInfo> dataList) {
		mDb.beginTransaction();
		ContentValues contentvalues;
		for (DbDatafInfo info : dataList) {
			contentvalues = new ContentValues();
			contentvalues.put("srczonecode", info.getSrczonecode());
			contentvalues.put("destzonecode", info.getDestzonecode());
			contentvalues.put("linecode", info.getLinecode());
			contentvalues.put("linename", info.getLinename());
			contentvalues.put("LineId",
					Integer.valueOf(info.getId() == null ? "0" : info.getId()));
			mDb.insert("line", "", contentvalues);
		}
		mDb.setTransactionSuccessful();
		mDb.endTransaction();

	}

	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from line ASC", null);
	}

	public Cursor rawQueryRank(String s) {
		s = (new StringBuilder("select * from line where linename =\""))
				.append(s).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public void rawUpdateRank(int i, String s, String s1) {
	}

	public void rawUpdateRank(String s, String s1, String s2, String s3) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("srczonecode", s);
		contentvalues.put("destzonecode", s1);
		contentvalues.put("linename", s3);
		mDb.update("line", contentvalues, "linecode=?", new String[] { s2 });
	}
}
