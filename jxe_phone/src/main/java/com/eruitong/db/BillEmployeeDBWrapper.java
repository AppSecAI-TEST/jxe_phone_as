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

/**
 * 员工
 * 
 * @author Administrator
 * 
 */
public class BillEmployeeDBWrapper {

	private static BillEmployeeDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private BillEmployeeDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1))
				.getWritableDatabase();
	}

	public static BillEmployeeDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new BillEmployeeDBWrapper(context);
		}

		return sInstance;

	}

	public Cursor SeaQueryRank(String s) {
		s = (new StringBuilder(
				"select * from billEmployee where empCode like '%")).append(s)
				.append("%'").toString();
		return mDb.rawQuery(s, null);
	}

	public void deleteRank() {
		mDb.delete("billEmployee", null, null);
	}

	public void deleteRank(String s) {
		mDb.delete("billEmployee", "empName=?", new String[] { s });
	}

	public void insertRank(int billEmployeeid, String empCode, String empName,
			String s2) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("billEmployeeid", Integer.valueOf(billEmployeeid));
		contentvalues.put("empCode", empCode);
		contentvalues.put("empName", empName);
		contentvalues.put("deptCode", s2);
		mDb.insert("billEmployee", "", contentvalues);
	}

	public void insertRank(List<DbDatafInfo> dataList) {
		mDb.beginTransaction();
		for (DbDatafInfo info : dataList) {
			ContentValues contentvalues = new ContentValues();
			contentvalues.put("billEmployeeid",
					Integer.valueOf(info.getId() == null ? "0" : info.getId()));
			contentvalues.put("empCode", info.getEmpCode());
			contentvalues.put("empName", info.getEmpName());
			contentvalues.put("deptCode", info.getDeptCode());
			mDb.insert("billEmployee", "", contentvalues);
		}
		mDb.setTransactionSuccessful();
		mDb.endTransaction();

	}

	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from billEmployee order by _id  ASC",
				null);
	}

	public Cursor rawQueryRank(String s) {
		s = (new StringBuilder("select * from billEmployee where empCode =\""))
				.append(s).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public void rawUpdateRank(String s, String s1, String s2) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("empCode", s1);
		contentvalues.put("deptCode", s2);
		mDb.update("billEmployee", contentvalues, "empName=?",
				new String[] { s });
	}
}
