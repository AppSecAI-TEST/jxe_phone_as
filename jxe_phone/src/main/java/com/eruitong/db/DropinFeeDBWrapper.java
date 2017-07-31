// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.eruitong.db;

import java.util.List;

import com.eruitong.model.DbDatafInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

// Referenced classes of package com.dingzhou.db:
//            DBHelper
/**
 * 条形选择码
 * 
 * @author Administrator
 * 
 */
public class DropinFeeDBWrapper {

	private static DropinFeeDBWrapper sInstance;
	private SQLiteDatabase mDb;

	private DropinFeeDBWrapper(Context context) {
		mDb = (new DBHelper(context, "order.db", null, 1))
				.getWritableDatabase();
	}

	public static DropinFeeDBWrapper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DropinFeeDBWrapper(context);
		}

		return sInstance;

	}

	public void deleteRank() {
		mDb.delete("dropin", null, null);
	}

	public void deleteRank(String dropinFeeId) {
		mDb.delete("dropin", "dropinFeeId=?", new String[] { dropinFeeId });
	}

	public void insertRank(List<DbDatafInfo> infos) {
		mDb.beginTransaction();
		ContentValues contentvalues;
		for (DbDatafInfo info : infos) {
			contentvalues = new ContentValues();
			contentvalues.put("dropinFeeId", info.getDropinFeeId());
			contentvalues.put("baseWeightQty", info.getBaseWeightQty());
			contentvalues.put("baseDropInFee", info.getBaseDropInFee());
			contentvalues.put("weightDropInFeeQty",
					info.getWeightDropInFeeQty());
			contentvalues.put("sourceZoneCode", info.getSourceZoneCode());
			contentvalues.put("priceRoundTypeCode",
					info.getPriceRoundTypeCode());
			contentvalues.put("weightRoundTypeCode",
					info.getWeightRoundTypeCode());

			mDb.insert("dropin", "", contentvalues);
		}
		mDb.setTransactionSuccessful();
		mDb.endTransaction();
	}

	public Cursor rawQueryRank() {
		return mDb.rawQuery("select * from dropin ASC", null);
	}

	public Cursor rawQueryRank(String sourceZoneCode) {
		sourceZoneCode = (new StringBuilder(
				"select * from dropin where sourceZoneCode =\""))
				.append(sourceZoneCode).append("\"").toString();
		return mDb.rawQuery(sourceZoneCode, null);
	}

	// public void rawUpdateRank(String s, String s1) {
	// ContentValues contentvalues = new ContentValues();
	// contentvalues.put("opName", s);
	// mDb.update("dropin", contentvalues, "opCode=?", new String[] { s1 });
	// }
}
