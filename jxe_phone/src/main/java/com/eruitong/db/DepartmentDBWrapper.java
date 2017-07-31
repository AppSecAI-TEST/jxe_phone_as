// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.eruitong.db;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eruitong.model.DbDatafInfo;
import com.eruitong.utils.MyUtils;

// Referenced classes of package com.dingzhou.db:
//            DBHelper

public class DepartmentDBWrapper {

	private static DepartmentDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private DepartmentDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1))
				.getWritableDatabase();
	}

	public static DepartmentDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DepartmentDBWrapper(context);
		}

		return sInstance;

	}

	public void deleteRank() {
		mDb.delete("department", null, null);
	}

	public void deleteRank(String s) {
		mDb.delete("department", "deptName=?", new String[] { s });
	}

	public void insertRank(int i, String s, String s1, String s2, String s3,
			String s4, String s5, String s6, String s7, String s8, String s9) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("departmentId", Integer.valueOf(i));
		contentvalues.put("typeCode", s);
		contentvalues.put("typeLevel", s1);
		contentvalues.put("deptName", s2);
		contentvalues.put("deptCode", s3);
		contentvalues.put("deptAddress", s4);
		contentvalues.put("districtCode", s5);
		contentvalues.put("parentCode", s6);
		contentvalues.put("codeSuffix", s7);
		contentvalues.put("outFeeRate", s8);
		contentvalues.put("inFeeRate", s9);
		mDb.insert("department", "", contentvalues);
	}

	public void insertRank(List<DbDatafInfo> dataList) {
		mDb.beginTransaction();
		for (DbDatafInfo info : dataList) {
			ContentValues contentvalues = new ContentValues();
			contentvalues.put("departmentId",
					Integer.valueOf(info.getId() == null ? "0" : info.getId()));
			contentvalues.put("typeCode", info.getTypeCode());
			contentvalues.put("typeLevel", info.getTypeLevel());
			contentvalues.put("deptName", info.getDeptName());
			contentvalues.put("deptCode", info.getDeptCode());
			contentvalues.put("partitionName",
					MyUtils.isNull(info.getPartitionName()));
			contentvalues.put("deptAddress", info.getDeptAddress());
			contentvalues.put("outsiteName", info.getOutsiteName());
			contentvalues.put("districtCode", info.getDistrictCode());
			contentvalues.put("parentCode", info.getParentCode());
			contentvalues.put("codeSuffix", info.getCodeSuffix());
			contentvalues.put("outFeeRate", info.getOutFeeRate());
			contentvalues.put("inFeeRate", info.getInFeeRate());
			contentvalues.put("transferFee", info.getTransferFee());
			mDb.insert("department", "", contentvalues);
		}
		mDb.setTransactionSuccessful();
		mDb.endTransaction();

	}

	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from district order by _id  ASC", null);
	}

	public Cursor rawQueryRank(String s) {
		s = (new StringBuilder("select * from department where deptCode =\""))
				.append(s).append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public Cursor rawQueryRankdistrictCode(String s) {
		s = (new StringBuilder(
				"select * from department where districtCode =\"")).append(s)
				.append("\"").toString();
		return mDb.rawQuery(s, null);
	}

	public void rawUpdateRank(String s, String s1) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("deptCode", s1);
		mDb.update("department", contentvalues, "deptName=?",
				new String[] { s });
	}
}
