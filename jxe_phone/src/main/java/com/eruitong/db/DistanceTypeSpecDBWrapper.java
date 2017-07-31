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
import org.json.JSONObject;

import com.eruitong.model.DbDatafInfo;

// Referenced classes of package com.dingzhou.db:
//            DBHelper

/**
 * 距离型规格
 * 
 * @author Administrator
 * 
 */
public class DistanceTypeSpecDBWrapper {

	private static DistanceTypeSpecDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private DistanceTypeSpecDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1))
				.getWritableDatabase();
	}

	public static DistanceTypeSpecDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DistanceTypeSpecDBWrapper(context);
		}

		return sInstance;

	}

	public void deleteRank() {
		mDb.delete("DistanceTypeSpec", null, null);
	}

	public void insertRank(String s, String s1, String s2) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("sourcezonecode", s);
		contentvalues.put("destzonecode", s1);
		contentvalues.put("distancetypecode", s2);
		mDb.insert("DistanceTypeSpec", "", contentvalues);
	}

	public void insertRank(List<DbDatafInfo> dataList) {
		mDb.beginTransaction();
		for (DbDatafInfo info : dataList) {
			ContentValues contentvalues = new ContentValues();
			contentvalues.put("sourcezonecode", info.getSourcezonecode());
			contentvalues.put("destzonecode", info.getDestzonecode());
			contentvalues.put("distancetypecode", info.getDistancetypecode());
			mDb.insert("DistanceTypeSpec", "", contentvalues);
		}
		mDb.setTransactionSuccessful();
		mDb.endTransaction();

	}

	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from DistanceTypeSpec order by _id ASC",
				null);
	}

	public Cursor rawQueryRank(String s, String s1) {
		s = (new StringBuilder(
				"select * from DistanceTypeSpec where sourcezonecode =\""))
				.append(s).append("\"").append(" and ").append("destzonecode")
				.append(" =\"").append(s1).append("\"").toString();
		return mDb.rawQuery(s, null);
	}
}
