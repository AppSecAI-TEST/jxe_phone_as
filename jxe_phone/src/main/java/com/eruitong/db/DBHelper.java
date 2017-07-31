// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.eruitong.db;

import com.lidroid.xutils.util.LogUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String COLUMN_ACCEPTVISIT_ADDRESSEEADDR = "addresseeAddr";
	public static final String COLUMN_ACCEPTVISIT_ADDRESSEECOMPNAME = "addresseeCompName";
	public static final String COLUMN_ACCEPTVISIT_ADDRESSEECONTNAME = "addresseeContName";
	public static final String COLUMN_ACCEPTVISIT_ADDRESSEECUSTCODE = "addresseeCustCode";
	public static final String COLUMN_ACCEPTVISIT_ADDRESSEEMOBILE = "addresseeMobile";
	public static final String COLUMN_ACCEPTVISIT_ADDRESSEEPHONE = "addresseePhone";
	public static final String COLUMN_ACCEPTVISIT_BANKNO = "bankNo";
	public static final String COLUMN_ACCEPTVISIT_BANKTYPE = "bankType";
	public static final String COLUMN_ACCEPTVISIT_CHARGEAGENTFEE = "chargeAgentFee";
	public static final String COLUMN_ACCEPTVISIT_CONSIGNEDTM = "consignedTm";
	public static final String COLUMN_ACCEPTVISIT_CONSIGNEEEMPCODE = "consigneeEmpCode";
	public static final String COLUMN_ACCEPTVISIT_CONSIGNORADDR = "consignorAddr";
	public static final String COLUMN_ACCEPTVISIT_CONSIGNORCOMPNAME = "consignorCompName";
	public static final String COLUMN_ACCEPTVISIT_CONSIGNORCONTNAME = "consignorContName";
	public static final String COLUMN_ACCEPTVISIT_CONSIGNORMOBILE = "consignorMobile";
	public static final String COLUMN_ACCEPTVISIT_CONSIGNORPHONE = "consignorPhone";
	public static final String COLUMN_ACCEPTVISIT_CONSNAME = "consName";
	public static final String COLUMN_ACCEPTVISIT_CUSTCODE = "custCode";
	public static final String COLUMN_ACCEPTVISIT_DEBOURSFEE = "deboursFee";
	public static final String COLUMN_ACCEPTVISIT_DEBOURSFLAG = "deboursFlag";
	public static final String COLUMN_ACCEPTVISIT_DELIVERFEE = "deliverFee";
	public static final String COLUMN_ACCEPTVISIT_DESTZONECODE = "destZoneCode";
	public static final String COLUMN_ACCEPTVISIT_DISCOUNTEXPRESSFEE = "discountExpressFee";
	public static final String COLUMN_ACCEPTVISIT_DISTANCETYPECODE = "distanceTypeCode";
	public static final String COLUMN_ACCEPTVISIT_EMPNAME = "empName";
	public static final String COLUMN_ACCEPTVISIT_GOODSCHARGEFEE = "goodsChargeFee";
	public static final String COLUMN_ACCEPTVISIT_ID = "_id";
	public static final String COLUMN_ACCEPTVISIT_INPUTEREMPCODE = "inputerEmpCode";
	public static final String COLUMN_ACCEPTVISIT_INPUTTYPE = "inputType";
	public static final String COLUMN_ACCEPTVISIT_INSURANCEAMOUNT = "insuranceAmount";
	public static final String COLUMN_ACCEPTVISIT_INSURANCEFEE = "insuranceFee";
	public static final String COLUMN_ACCEPTVISIT_ISUPLOAD = "isUpload";
	public static final String COLUMN_ACCEPTVISIT_METERAGEWEIGHTQTY = "meterageWeightQty";
	public static final String COLUMN_ACCEPTVISIT_OPCODE = "opCode";
	public static final String COLUMN_ACCEPTVISIT_OPNAME = "opName";
	public static final String COLUMN_ACCEPTVISIT_ORDERNO = "orderNo";
	public static final String COLUMN_ACCEPTVISIT_PAYMENTTYPECODE = "paymentTypeCode";
	public static final String COLUMN_ACCEPTVISIT_PRODUCTTYPECODE = "productTypeCode";
	public static final String COLUMN_ACCEPTVISIT_QUANTITY = "quantity";
	public static final String COLUMN_ACCEPTVISIT_REALWEIGHTQTY = "realWeightQty";
	public static final String COLUMN_ACCEPTVISIT_SERVICETYPECODE = "serviceTypeCode";
	public static final String COLUMN_ACCEPTVISIT_SETTLEMENTTYPECODE = "settlementTypeCode";
	public static final String COLUMN_ACCEPTVISIT_SIGNBACKCOUNT = "signBackCount";
	public static final String COLUMN_ACCEPTVISIT_SIGNBACKFEE = "signBackFee";
	public static final String COLUMN_ACCEPTVISIT_SIGNBACKIDENTITY = "signBackIdentity";
	public static final String COLUMN_ACCEPTVISIT_SIGNBACKNO = "signBackNo";
	public static final String COLUMN_ACCEPTVISIT_SIGNBACKRECEIPT = "signBackReceipt";
	public static final String COLUMN_ACCEPTVISIT_SIGNBACKSEAL = "signBackSeal";
	public static final String COLUMN_ACCEPTVISIT_SIGNBACKSIZE = "signBackSize";
	public static final String COLUMN_ACCEPTVISIT_SOURCEZONECODE = "sourceZoneCode";
	public static final String COLUMN_ACCEPTVISIT_TEAMCODE = "teamCode";
	public static final String COLUMN_ACCEPTVISIT_TRANSFERDAYS = "transferDays";
	public static final String COLUMN_ACCEPTVISIT_VOLUME = "volume";
	public static final String COLUMN_ACCEPTVISIT_WAITNOTIFYFEE = "waitNotifyFee";
	public static final String COLUMN_ACCEPTVISIT_WAYBILLFEE = "waybillFee";
	public static final String COLUMN_ACCEPTVISIT_WAYBILLNO = "waybillNo";
	public static final String COLUMN_ACCEPTVISIT_WAYBILLREMK = "waybillRemk";
	public static final String COLUMN_BAROPTCODE_ID = "_id";
	public static final String COLUMN_BAROPTCODE_OPCODE = "opCode";
	public static final String COLUMN_BAROPTCODE_OPCODEID = "opCodeId";
	public static final String COLUMN_BAROPTCODE_OPNAME = "opName";
	public static final String COLUMN_BASEDATAMODIFYNOTIFY_COMPCODE = "compCode";
	public static final String COLUMN_BASEDATAMODIFYNOTIFY_ENTITYTYPECODE = "entityTypeCode";
	public static final String COLUMN_BASEDATAMODIFYNOTIFY_ID = "_id";
	public static final String COLUMN_BASEDATAMODIFYNOTIFY_LASTMODIFIEDTM = "lastModifiedTm";
	public static final String COLUMN_BILLEMPLOYEE_BILLEMPLOYEEID = "billEmployeeid";
	public static final String COLUMN_BILLEMPLOYEE_DEPTCODE = "deptCode";
	public static final String COLUMN_BILLEMPLOYEE_EMPCODE = "empCode";
	public static final String COLUMN_BILLEMPLOYEE_EMPNAME = "empName";
	public static final String COLUMN_BILLEMPLOYEE_ID = "_id";
	public static final String COLUMN_BLUETOOTH_ADDRESS = "address";
	public static final String COLUMN_BLUETOOTH_ID = "_id";
	public static final String COLUMN_BLUETOOTH_NAME = "name";
	public static final String COLUMN_BLUETOOTH_TYPE = "type";
	public static final String COLUMN_CHILDNO_CHILDWAYBILLNO = "childwaybillNo";
	public static final String COLUMN_CHILDNO_ID = "_id";
	public static final String COLUMN_CHILDNO_ISUPLOAD = "isUpload";
	public static final String COLUMN_CHILDNO_NUMBLE = "numble";
	public static final String COLUMN_CHILDNO_WAYBILLNO = "waybillNo";
	public static final String COLUMN_DELIVERYRANGE_AREACODE = "areaCode";
	public static final String COLUMN_DELIVERYRANGE_AREANAME = "areaName";
	public static final String COLUMN_DELIVERYRANGE_AREAREMARK = "areaRemark";
	public static final String COLUMN_DELIVERYRANGE_ID = "_id";
	public static final String COLUMN_DELIVERYRANGE_PARENTAREACODE = "parentAreaCode";
	public static final String COLUMN_DELIVERYRANGE_RANGESTATE = "rangeState";
	public static final String COLUMN_DELIVERYRANGE_TYPECODE = "typeCode";
	public static final String COLUMN_DELIVERY_EMPCODE = "empCode";
	public static final String COLUMN_DELIVERY_EMPNAME = "empname";
	public static final String COLUMN_DEPARTMENT_PARTITIONNAME = "partitionName";
	public static final String COLUMN_DELIVERY_ID = "_id";
	public static final String COLUMN_DELIVERY_INPUTTYPE = "inputType";
	public static final String COLUMN_DELIVERY_ISUPLOAD = "isupload";
	public static final String COLUMN_DELIVERY_OPCODE = "opCode";
	public static final String COLUMN_DELIVERY_OPERTM = "operTm";
	public static final String COLUMN_DELIVERY_OPNAME = "opName";
	public static final String COLUMN_DELIVERY_RECIPIENTS = "recipients";
	public static final String COLUMN_DELIVERY_WAYBILLNO = "waybillNo";
	public static final String COLUMN_DEPARTMENT_CODESUFFIX = "codeSuffix";
	public static final String COLUMN_DEPARTMENT_DEPARTMENTID = "departmentId";
	public static final String COLUMN_DEPARTMENT_DEPTADDRESS = "deptAddress";
	public static final String COLUMN_DEPARTMENT_DEPTCODE = "deptCode";
	public static final String COLUMN_DEPARTMENT_DEPTNAME = "deptName";
	public static final String COLUMN_DEPARTMENT_DISTRICTCODE = "districtCode";
	public static final String COLUMN_DEPARTMENT_ID = "_id";
	public static final String COLUMN_DEPARTMENT_INFEERATE = "inFeeRate";
	public static final String COLUMN_DEPARTMENT_LOCKFEE = "lockFee";
	public static final String COLUMN_DEPARTMENT_TRANSFER_FEE_RATE = "transferFee";
	public static final String COLUMN_DEPARTMENT_OUTFEERATE = "outFeeRate";
	public static final String COLUMN_DEPARTMENT_OUTSITENAME = "outsiteName";
	public static final String COLUMN_DEPARTMENT_PARENTCODE = "parentCode";
	public static final String COLUMN_DEPARTMENT_TYPECODE = "typeCode";
	public static final String COLUMN_DEPARTMENT_TYPELEVEL = "typeLevel";
	public static final String COLUMN_DISTANCETYPESPEC_DESTZONECODE = "destzonecode";
	public static final String COLUMN_DISTANCETYPESPEC_DISTANCETYPECODE = "distancetypecode";
	public static final String COLUMN_DISTANCETYPESPEC_DISTANCETYPESPECID = "DistanceTypeSpecid";
	public static final String COLUMN_DISTANCETYPESPEC_ID = "_id";
	public static final String COLUMN_DISTANCETYPESPEC_SOURCEZONECODE = "sourcezonecode";
	public static final String COLUMN_DISTRICT_CITYCODE = "cityCode";
	public static final String COLUMN_DISTRICT_DISTCODE = "distCode";
	public static final String COLUMN_DISTRICT_DISTNAME = "distName";
	public static final String COLUMN_DISTRICT_DISTRICTID = "districtId";
	public static final String COLUMN_DISTRICT_ID = "_id";
	public static final String COLUMN_DISTRICT_PARENTDISTCODE = "parentDistCode";
	public static final String COLUMN_DISTRICT_PROVINCECODE = "provinceCode";
	public static final String COLUMN_DISTRICT_TYPECODE = "typeCode";
	public static final String COLUMN_FAILCAUSE_CAUSECODE = "causecode";
	public static final String COLUMN_FAILCAUSE_CAUSECONT = "causecont";
	public static final String COLUMN_FAILCAUSE_ID = "_id";
	public static final String COLUMN_HTTPPATH_DEVICEID = "deviceId";
	public static final String COLUMN_HTTPPATH_ID = "_id";
	public static final String COLUMN_HTTPPATH_NAME = "name";
	public static final String COLUMN_LINE_DESTZONECODE = "destzonecode";
	public static final String COLUMN_LINE_ID = "_id";
	public static final String COLUMN_LINE_LINECODE = "linecode";
	public static final String COLUMN_LINE_LINEID = "LineId";
	public static final String COLUMN_LINE_LINENAME = "linename";
	public static final String COLUMN_LINE_SRCZONECODE = "srczonecode";
	public static final String COLUMN_LOGIN_DEPTCODE = "deptCode";
	public static final String COLUMN_LOGIN_EMPCODE = "empCode";
	public static final String COLUMN_LOGIN_EMPNAME = "empName";
	public static final String COLUMN_LOGIN_ID = "_id";
	public static final String COLUMN_LOGIN_PASSWORD = "password";
	public static final String COLUMN_LOGIN_PERMISSIONLIST = "permissionList";
	public static final String COLUMN_PRODPRICE_BASEPRICE = "basePrice";
	public static final String COLUMN_PRODPRICE_BASEWEIGHTQTY = "baseWeightQty";
	public static final String COLUMN_PRODPRICE_CARGOTYPECODE = "cargoTypeCode";
	public static final String COLUMN_PRODPRICE_DESTZONECODE = "destZoneCode";
	public static final String COLUMN_PRODPRICE_EXPRESSTYPECODE = "expressTypeCode";
	public static final String COLUMN_PRODPRICE_ID = "_id";
	public static final String COLUMN_PRODPRICE_LIMITTYPECODE = "limitTypeCode";
	public static final String COLUMN_PRODPRICE_MAXWEIGHTQTY = "maxWeightQty";
	public static final String COLUMN_PRODPRICE_PRICEROUNDTYPECODE = "priceRoundTypeCode";
	public static final String COLUMN_PRODPRICE_SERVICECODE = "serviceCode";
	public static final String COLUMN_PRODPRICE_SOURCEZONECODE = "sourceZoneCode";
	public static final String COLUMN_PRODPRICE_STARTWEIGHTQTY = "startWeightQty";
	public static final String COLUMN_PRODPRICE_WEIGHTPRICEQTY = "weightPriceQty";
	public static final String COLUMN_PRODPRICE_WEIGHTROUNDTYPECODE = "weightRoundTypeCode";
	public static final String COLUMN_SERVICEPRODPRICE_ATTRID = "attrId";
	public static final String COLUMN_SERVICEPRODPRICE_CURRENCYCODE = "currencyCode";
	public static final String COLUMN_SERVICEPRODPRICE_FEETYPECODE = "feeTypeCode";
	public static final String COLUMN_SERVICEPRODPRICE_ID = "_id";
	public static final String COLUMN_SERVICEPRODPRICE_PRICEDESC = "priceDesc";
	public static final String COLUMN_SERVICEPRODPRICE_PRICEID = "priceId";
	public static final String COLUMN_SERVICEPRODPRICE_PROPVALUE = "propValue";
	public static final String COLUMN_SERVICEPRODPRICE_SERVICECODE = "serviceCode";
	public static final String COLUMN_SERVICEPRODPRICE_SERVICEPRODPRICEID = "ServiceProdPriceid";
	public static final String COLUMN_SETPASSWORD_ID = "_id";
	public static final String COLUMN_SETPASSWORD_NAME = "name";
	public static final String COLUMN_WAYBILLNO_AREACODE = "areaCode";
	public static final String COLUMN_WAYBILLNO_ID = "_id";
	public static final String COLUMN_WAYBILLNO_WAYBILLNOS = "waybillNos";
	public static final String DB_NAME = "order.db";
	public static final int DB_VERSION = 1;
	public static final String TABLE_ACCEPTVISIT = "acceptvisit";
	public static final String TABLE_BAROPTCODE = "barOptCode";
	public static final String TABLE_BASEDATAMODIFYNOTIFY = "baseDataModifyNotify";
	public static final String TABLE_BILLEMPLOYEE = "billEmployee";
	public static final String TABLE_BLUETOOTH = "Bluetooth";
	public static final String TABLE_CHILDNO = "childNo";
	public static final String TABLE_DELIVERY = "deliverysignfor";
	public static final String TABLE_DELIVERYRANGE = "deliveryRange";
	public static final String TABLE_DEPARTMENT = "department";
	public static final String TABLE_DISTANCETYPESPEC = "DistanceTypeSpec";
	public static final String TABLE_DISTRICT = "district";
	public static final String TABLE_FAILCAUSE = "failCause";
	public static final String TABLE_HTTPPATH = "httppath";
	public static final String TABLE_LINE = "line";
	public static final String TABLE_LOGIN = "login";
	public static final String TABLE_PRODPRICE = "prodprice";
	public static final String TABLE_SERVICEPRODPRICE = "ServiceProdPrice";
	public static final String TABLE_SETPASSWORD = "SetPassWord";
	public static final String TABLE_WAYBILLNO = "waybillNo";

	public DBHelper(Context context, String s, android.database.sqlite.SQLiteDatabase.CursorFactory cursorfactory, int version) {
		super(context, "order.db", cursorfactory,5);
	}

	public void onCreate(SQLiteDatabase sqlitedatabase) {
		sqlitedatabase
				.execSQL("CREATE TABLE httppath ( _id INTEGER PRIMARY KEY AUTOINCREMENT, deviceId TEXT, name TEXT)");
		sqlitedatabase
				.execSQL("CREATE TABLE SetPassWord ( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
		sqlitedatabase
				.execSQL("CREATE TABLE Bluetooth ( _id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT, name TEXT, address TEXT)");
		sqlitedatabase
				.execSQL("CREATE TABLE login ( _id INTEGER PRIMARY KEY AUTOINCREMENT, deptCode TEXT, empCode TEXT, empName TEXT, permissionList TEXT, password TEXT)");
		sqlitedatabase
				.execSQL("CREATE TABLE waybillNo ( _id INTEGER PRIMARY KEY AUTOINCREMENT, areaCode TEXT, waybillNos TEXT)");
		sqlitedatabase
				.execSQL("CREATE TABLE department ( _id INTEGER PRIMARY KEY AUTOINCREMENT, departmentId TEXT, typeCode TEXT, typeLevel TEXT, deptAddress TEXT, deptName TEXT, deptCode TEXT, partitionName TEXT,outsiteName TEXT, districtCode TEXT, parentCode TEXT, codeSuffix TEXT, outFeeRate TEXT, inFeeRate TEXT, lockFee TEXT,transferFee Double)");
		sqlitedatabase
				.execSQL("CREATE TABLE billEmployee ( _id INTEGER PRIMARY KEY AUTOINCREMENT, billEmployeeid INTEGER, empName TEXT, empCode TEXT, deptCode TEXT)");
		sqlitedatabase
				.execSQL("CREATE TABLE deliveryRange ( _id INTEGER PRIMARY KEY AUTOINCREMENT, areaCode TEXT, areaName TEXT, areaRemark TEXT, parentAreaCode TEXT, typeCode TEXT, rangeState TEXT)");
		sqlitedatabase
				.execSQL("CREATE TABLE district ( _id INTEGER PRIMARY KEY AUTOINCREMENT, typeCode TEXT, distName TEXT, distCode TEXT, parentDistCode TEXT, cityCode TEXT, provinceCode TEXT, districtId INTEGER)");
		sqlitedatabase
				.execSQL("CREATE TABLE failCause ( _id INTEGER PRIMARY KEY AUTOINCREMENT, causecode TEXT, causecont TEXT)");
		sqlitedatabase
				.execSQL("CREATE TABLE line ( _id INTEGER PRIMARY KEY AUTOINCREMENT, LineId INTEGER, srczonecode TEXT, destzonecode TEXT, linecode TEXT, linename TEXT)");
		sqlitedatabase
				.execSQL("CREATE TABLE DistanceTypeSpec ( _id INTEGER PRIMARY KEY AUTOINCREMENT, DistanceTypeSpecid TEXT, sourcezonecode TEXT, destzonecode TEXT, distancetypecode TEXT)");
		sqlitedatabase
				.execSQL("CREATE TABLE prodprice ( _id INTEGER PRIMARY KEY AUTOINCREMENT, sourceZoneCode TEXT, destZoneCode TEXT, serviceCode TEXT, cargoTypeCode TEXT, limitTypeCode TEXT, expressTypeCode TEXT, startWeightQty TEXT, maxWeightQty TEXT, weightPriceQty Double, baseWeightQty TEXT, basePrice TEXT, weightRoundTypeCode TEXT, priceRoundTypeCode TEXT)");
		sqlitedatabase
				.execSQL("CREATE TABLE ServiceProdPrice ( _id INTEGER PRIMARY KEY AUTOINCREMENT, currencyCode TEXT, feeTypeCode TEXT, priceDesc TEXT, serviceCode TEXT, attrId INTEGER, priceId INTEGER, propValue TEXT)");
		sqlitedatabase
				.execSQL("CREATE TABLE barOptCode ( _id INTEGER PRIMARY KEY AUTOINCREMENT, opCode TEXT, opName TEXT, opCodeId TEXT)");
		sqlitedatabase
				.execSQL("CREATE TABLE acceptvisit ( _id INTEGER PRIMARY KEY AUTOINCREMENT, waybillNo TEXT, sourceZoneCode TEXT, destZoneCode TEXT, custCode TEXT, consignorCompName TEXT, consignorAddr TEXT, consignorPhone TEXT, consignorContName TEXT, consignorMobile TEXT, addresseeCustCode TEXT, addresseeCompName TEXT, addresseeAddr TEXT, addresseePhone TEXT, addresseeContName TEXT, addresseeMobile TEXT, realWeightQty Double , meterageWeightQty Double , quantity Integer , volume Double , consigneeEmpCode TEXT, consignedTm TEXT,custIdentityCard TEXT,  waybillRemk TEXT, consName TEXT, inputType TEXT, paymentTypeCode TEXT, settlementTypeCode TEXT, serviceTypeCode TEXT, waybillFee Double, goodsChargeFee Double default 0, chargeAgentFee Double default 0, bankNo TEXT, bankType TEXT, transferDays Integer, insuranceAmount TEXT, insuranceFee Double, signBackFee TEXT, signBackNo TEXT, signBackCount TEXT, signBackSize TEXT, signBackReceipt TEXT, signBackSeal TEXT, signBackIdentity TEXT, waitNotifyFee Double default 0, deboursFee Double default 0, deboursFlag TEXT, deliverFee Double default 0, productTypeCode TEXT, orderNo TEXT, teamCode TEXT, discountExpressFee Double, inputerEmpCode TEXT, distanceTypeCode TEXT, empName INTEGER, opCode INTEGER, opName TEXT, isUpload INTEGER ,fuelServiceFee Double default 0,versionNo TEXT,consType TEXT)");
		sqlitedatabase
				.execSQL("CREATE TABLE childNo ( _id INTEGER PRIMARY KEY AUTOINCREMENT, waybillNo TEXT, childwaybillNo TEXT, isUpload INTEGER, numble TEXT)");
		sqlitedatabase
				.execSQL("CREATE TABLE deliverysignfor ( _id INTEGER PRIMARY KEY AUTOINCREMENT, waybillNo TEXT, recipients TEXT, opCode INTEGER, opName TEXT, inputType TEXT, empCode TEXT, operTm TEXT, isupload INTEGER, empname TEXT)");

		sqlitedatabase
				.execSQL("CREATE TABLE dropin ( _id INTEGER PRIMARY KEY AUTOINCREMENT, dropinFeeId TEXT, baseWeightQty Double, baseDropInFee Double, weightDropInFeeQty Double,sourceZoneCode TEXT,priceRoundTypeCode TEXT,weightRoundTypeCode TEXT)");
		
		sqlitedatabase
				.execSQL("CREATE TABLE category ( _id INTEGER PRIMARY KEY AUTOINCREMENT,categoryPriceId TEXT, categoryId TEXT, sourceZoneCode TEXT, destZoneCode TEXT, price Double, priceRoundTypeCode TEXT, categoryCode TEXT, categoryDesc TEXT, unitName TEXT ,standardWeight Double,validFlg Long,lowerPrice Double , commissionAmt Double,serviceTypeCode,Text)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		if (oldVersion == 4) {
			viewsonUpdata2(db);
		}

	}
	/**
	 * 升级到第二个版本
	 */
	private void viewsonUpdata2(SQLiteDatabase db) {
		/*// 创建 dropin 表
		// db.execSQL("CREATE TABLE dropin ( _id INTEGER PRIMARY KEY AUTOINCREMENT, dropinFeeId TEXT, baseWeightQty Double, baseDropInFee Double, weightDropInFeeQty Double,sourceZoneCode TEXT,priceRoundTypeCode TEXT,weightRoundTypeCode TEXT)");

		// 给 acceptvisit 表添加 fuelServiceFee 字段
	      db.execSQL(" ALTER TABLE 'acceptvisit' ADD 'fuelServiceFee' Double default 0 ");
		db.execSQL(" ALTER TABLE 'acceptvisit' ADD 'versionNo' String ");
		*/
		//给department 表中添加
		//db.execSQL(" ALTER  TABLE 'department' ADD 'transferFee' Double ");
		//创建特殊品类表
		db.execSQL("CREATE TABLE category ( _id INTEGER PRIMARY KEY AUTOINCREMENT,categoryPriceId TEXT, categoryId TEXT, sourceZoneCode TEXT, destZoneCode TEXT, price Double, priceRoundTypeCode TEXT, categoryCode TEXT, categoryDesc TEXT, unitName TEXT ,standardWeight Double,validFlg Long,lowerPrice Double , commissionAmt Double,serviceTypeCode,Text)");
		// 给 acceptvisit 表添加 fuelServiceFee 字段
	    db.execSQL(" ALTER TABLE 'acceptvisit' ADD 'consType' String ");
		
	}

}
