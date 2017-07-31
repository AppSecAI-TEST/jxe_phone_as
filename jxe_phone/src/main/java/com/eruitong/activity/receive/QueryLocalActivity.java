package com.eruitong.activity.receive;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eruitong.activity.BaseActivity;
import com.eruitong.bluetooth.StatusBox;
import com.eruitong.db.AcceptInputDBWrapper;
import com.eruitong.db.BluetoothDBWrapper;
import com.eruitong.db.DepartmentDBWrapper;
import com.eruitong.db.DistrictDBWrapper;
import com.eruitong.db.childNoListDBWrapper;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.model.InputDataInfo;
import com.eruitong.print.bluetooth.BluetoothPrintTool;
import com.eruitong.receiver.BaseReceiver;
import com.eruitong.receiver.BatteryReceiver;
import com.eruitong.receiver.Hearer;
import com.eruitong.print.bluetooth.BluetoothUtil;
import com.eruitong.utils.MyUtils;
import com.eruitong.utils.WiFiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import zpSDK.zpSDK.zpSDK;

/**
 * 查询本地
 *
 * @author Administrator
 */
public class QueryLocalActivity extends BaseActivity implements Hearer {

    @ViewInject(R.id.img_basetitle_signal)
    ImageView mWifiSignal;
    @ViewInject(R.id.txt_basetitle_title)
    TextView mTitleBase;
    @ViewInject(R.id.img_basetitle_electric_quantity)
    ImageView mBatterySignal;
    @ViewInject(R.id.txt_basetitle_username)
    TextView mUserName;
    @ViewInject(R.id.txt_basetitle_date)
    TextView mDate;

    @ViewInject(R.id.edt_delivery_deliverysignfor_jobnumber)
    EditText mKeyword;
    @ViewInject(R.id.query_number)
    ListView mListQuery;
    @ViewInject(R.id.query_uploaded_poll)
    TextView mQueryUploadedPoll;
    @ViewInject(R.id.query_uploaded_piece)
    TextView mQueryUploadedPiece;
    @ViewInject(R.id.updata_btn_cancel)
    Button mCancel;
    @ViewInject(R.id.updata_btn_ok)
    Button mOk;

    /**
     * 列表的数据
     **/
    private List<InputDataInfo> listOrder;
    private LstOrderAdapter myAdapter;
    private String mSelectDate;

    private String mDeptAddress;
    private String consignorDistName;
    private String sourcedistrictCode;

    public String BarCodeBDAddress;
    public String BarCodeBDName;

    private AcceptInputDBWrapper dbWrapper;
    private Cursor mCursor;

    private InputDataInfo dataInfo;
    List childList;

    private StatusBox statusBoxBiaoqian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_uploaded);
        ViewUtils.inject(this);
        listOrder = new ArrayList<InputDataInfo>();
        setView();
    }

    @SuppressLint("SimpleDateFormat")
    protected void onStart() {
        super.onStart();
        BatteryReceiver.getInstant(this).addHeraer(this);
        mUserName.setText((new StringBuilder(String.valueOf(MyApp.mEmpName))).append(MyApp.mDeptName).toString());

        Cursor cursor = DepartmentDBWrapper.getInstance(getApplication()).rawQueryRank(MyApp.mDeptCode);
        if (cursor.moveToFirst()) {
            mDeptAddress = cursor.getString(cursor.getColumnIndex("deptAddress"));
            sourcedistrictCode = cursor.getString(cursor.getColumnIndex("districtCode"));
        }
        cursor.close();
        cursor = DistrictDBWrapper.getInstance(getApplication()).rawQueryRank(sourcedistrictCode);
        if (cursor.moveToFirst()) {
            consignorDistName = cursor.getString(cursor.getColumnIndex("distName"));
        }
        cursor.close();
        isConnect();
        String s = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        addData(s);

    }

    private void isConnect() {
        Cursor cursor = BluetoothDBWrapper.getInstance(getApplication()).rawQueryRank("No1");
        do {
            if (!cursor.moveToNext()) {
                cursor.close();
                return;
            }
            BarCodeBDAddress = cursor.getString(cursor.getColumnIndex("address"));
            BarCodeBDName = cursor.getString(cursor.getColumnIndex("name"));
        } while (true);
    }

    protected void onStop() {
        super.onStop();
        BatteryReceiver.getInstant(this).delHeraer(this);
    }

    private void setView() {
        mCancel.setText("标签");
        mOk.setText("客户联");
        mTitleBase.setText("运单查询");
        mQueryUploadedPiece.setVisibility(View.GONE);
        mQueryUploadedPoll.setVisibility(View.GONE);

        myAdapter = new LstOrderAdapter();
        mListQuery.setAdapter(myAdapter);
        mListQuery.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dataInfo = (InputDataInfo) listOrder.get(position);
                Cursor adapterview = childNoListDBWrapper.getInstance(getApplication()).rawQueryRank(dataInfo
                        .waybillNo, 0);
                childList = new ArrayList();
                do {
                    if (adapterview.moveToNext()) {
                        String childwaybillNo = adapterview.getString(adapterview.getColumnIndex("childwaybillNo"));
                        childList.add(childwaybillNo);
                    } else {
                        adapterview.close();
                        break;
                    }
                } while (true);

                String outsiteName = "";

                adapterview = DepartmentDBWrapper.getInstance(getApplication()).rawQueryRank(dataInfo.destZoneCode);
                if (!adapterview.moveToFirst()) {
                    adapterview.close();
                } else {
                    outsiteName = adapterview.getString(adapterview.getColumnIndex("outsiteName"));
                }

                if (TextUtils.isEmpty(outsiteName) || outsiteName.length() < 3) {
                    dataInfo.outsiteName = outsiteName;
                } else {
                    dataInfo.outsiteName = outsiteName.substring(0, 3);
                }

                dataInfo.addresseeDistrictCode = adapterview.getString(adapterview.getColumnIndex("districtCode"));
                dataInfo.partitionName = adapterview.getString(adapterview.getColumnIndex("partitionName"));

                mCursor = DistrictDBWrapper.getInstance(getApplication()).rawQueryRank(dataInfo.addresseeDistrictCode);
                if (mCursor.moveToFirst()) {
                    dataInfo.provinceCode = mCursor.getString(mCursor.getColumnIndex("provinceCode"));
                    dataInfo.addresseeDistName = mCursor.getString(mCursor.getColumnIndex("distName"));
                }
                mCursor.close();
                return;

            }
        });

        // 选择日期
        mKeyword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                @SuppressLint("WrongConstant") Dialog dialog = new DatePickerDialog(QueryLocalActivity.this, new
                        OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        if (dayOfMonth >= 10 && monthOfYear >= 9) {
                            mSelectDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        } else {
                            if (dayOfMonth < 10) {
                                mSelectDate = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                            }
                            if (monthOfYear < 9) {
                                mSelectDate = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                            }
                        }
                        mKeyword.setText(mSelectDate);
                        addData(mSelectDate);
                        myAdapter.notifyDataSetChanged();
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        // 标签
        mCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                print(0, mCancel);
            }
        });

        // 客户联
        mOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                print(1, mOk);

            }
        });

    }

    private void print(int type, Button mbtn) {
        if (dataInfo == null) {
            MyUtils.showText(QueryLocalActivity.this, "请选择您要打印的订单");
            return;
        }

        statusBoxBiaoqian = new StatusBox(QueryLocalActivity.this, mbtn);

        try {
            statusBoxBiaoqian.Show("正在打印 ...");
            if (!BluetoothUtil.OpenPrinter(this, BarCodeBDAddress)) {
                statusBoxBiaoqian.Close();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (type) {
            case 0:// 标签
                quireWaybillNo(dataInfo);
                break;
            case 1:// 客户联
                Kehejianlian(dataInfo);
                break;

        }
        zpSDK.zp_close();
        statusBoxBiaoqian.Close();
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return;
    }

    private void Kehejianlian(InputDataInfo inputdatainfo) {
        BluetoothPrintTool.kehuLian(inputdatainfo);
    }

    private void quireWaybillNo(InputDataInfo inputdatainfo) {
        BluetoothPrintTool.xiaopiao(inputdatainfo, childList);
    }

    private void addData(String s) {
        int i;
        listOrder.clear();

        dbWrapper = AcceptInputDBWrapper.getInstance(getApplication());
        i = 0;
        if (MyUtils.isEmpty(s)) {
            mCursor = dbWrapper.rawQueryRank(0);
        } else {
            mCursor = dbWrapper.dataQueryRank(s, 0);
        }
        do {

            if (!mCursor.moveToNext()) {
                mCursor.close();
                myAdapter.notifyDataSetChanged();
                mQueryUploadedPoll.setVisibility(View.VISIBLE);
                mQueryUploadedPoll.setText((new StringBuilder("共录单")).append(i).append("条").toString());
                return;
            }
            InputDataInfo inputdatainfo = new InputDataInfo();
            inputdatainfo.sourcedistrictCode = sourcedistrictCode;
            inputdatainfo.consignorDistName = consignorDistName;
            int j = mCursor.getColumnIndex("waybillNo");
            inputdatainfo.waybillNo = mCursor.getString(j);
            j = mCursor.getColumnIndex("consigneeEmpCode");
            inputdatainfo.consigneeEmpCode = mCursor.getString(j);
            j = mCursor.getColumnIndex("consignorContName");
            inputdatainfo.consignorContName = mCursor.getString(j);
            j = mCursor.getColumnIndex("consignorPhone");
            inputdatainfo.consignorPhone = mCursor.getString(j);
            j = mCursor.getColumnIndex("addresseeContName");
            inputdatainfo.addresseeContName = mCursor.getString(j);
            j = mCursor.getColumnIndex("addresseePhone");
            inputdatainfo.addresseePhone = mCursor.getString(j);
            j = mCursor.getColumnIndex("addresseeMobile");
            inputdatainfo.addresseeMobile = mCursor.getString(j);
            j = mCursor.getColumnIndex("consName");
            inputdatainfo.consName = mCursor.getString(j);
            j = mCursor.getColumnIndex("quantity");
            inputdatainfo.quantity = mCursor.getInt(j);
            j = mCursor.getColumnIndex("meterageWeightQty");
            inputdatainfo.meterageWeightQty = mCursor.getString(j);
            j = mCursor.getColumnIndex("waybillFee");
            inputdatainfo.waybillFee = mCursor.getString(j);
            j = mCursor.getColumnIndex("goodsChargeFee");
            inputdatainfo.goodsChargeFee = mCursor.getString(j);
            j = mCursor.getColumnIndex("paymentTypeCode");
            inputdatainfo.paymentTypeCode = mCursor.getString(j);
            j = mCursor.getColumnIndex("productTypeCode");
            inputdatainfo.productTypeCode = mCursor.getString(j);
            j = mCursor.getColumnIndex("waybillNo");
            inputdatainfo.waybillNo = mCursor.getString(j);
            j = mCursor.getColumnIndex("consigneeEmpCode");
            inputdatainfo.consigneeEmpCode = mCursor.getString(j);
            j = mCursor.getColumnIndex("sourceZoneCode");
            inputdatainfo.sourceZoneCode = mCursor.getString(j);
            j = mCursor.getColumnIndex("destZoneCode");
            inputdatainfo.destZoneCode = mCursor.getString(j);
            j = mCursor.getColumnIndex("addresseeAddr");
            inputdatainfo.addresseeAddr = mCursor.getString(j);
            j = mCursor.getColumnIndex("consignorMobile");
            inputdatainfo.consignorMobile = mCursor.getString(j);
            j = mCursor.getColumnIndex("realWeightQty");
            inputdatainfo.realWeightQty = mCursor.getString(j);
            j = mCursor.getColumnIndex("consignedTm");
            inputdatainfo.consignedTm = mCursor.getString(j);
            j = mCursor.getColumnIndex("waybillFee");
            inputdatainfo.waybillFee = mCursor.getString(j);
            j = mCursor.getColumnIndex("waitNotifyFee");
            inputdatainfo.waitNotifyFee = mCursor.getString(j);
            j = mCursor.getColumnIndex("signBackFee");
            inputdatainfo.signBackFee = mCursor.getString(j);
            j = mCursor.getColumnIndex("signBackNo");
            inputdatainfo.signBackNo = mCursor.getString(j);
            j = mCursor.getColumnIndex("deboursFee");
            inputdatainfo.deboursFee = mCursor.getString(j);
            j = mCursor.getColumnIndex("deliverFee");
            inputdatainfo.deliverFee = mCursor.getString(j);
            j = mCursor.getColumnIndex("chargeAgentFee");
            inputdatainfo.chargeAgentFee = mCursor.getString(j);
            j = mCursor.getColumnIndex("insuranceAmount");
            inputdatainfo.insuranceAmount = mCursor.getString(j);
            j = mCursor.getColumnIndex("insuranceFee");
            inputdatainfo.insuranceFee = mCursor.getString(j);
            j = mCursor.getColumnIndex("bankType");
            inputdatainfo.bankType = mCursor.getString(j);
            j = mCursor.getColumnIndex("bankNo");
            inputdatainfo.bankNo = mCursor.getString(j);
            j = mCursor.getColumnIndex("transferDays");
            inputdatainfo.transferDays = mCursor.getString(j);
            j = mCursor.getColumnIndex("waybillRemk");
            inputdatainfo.waybillRemk = mCursor.getString(j);
            j = mCursor.getColumnIndex("teamCode");
            inputdatainfo.teamCode = mCursor.getString(j);
            inputdatainfo.fuelServiceFee = mCursor.getDouble(mCursor.getColumnIndex("fuelServiceFee"));

            if (inputdatainfo.teamCode.toString().equals("null") || inputdatainfo.teamCode.isEmpty()) {
                inputdatainfo.teamCode = "";
            }
            Double double1 = 0.0;
            if (MyUtils.isEmpty(inputdatainfo.goodsChargeFee)) {
                inputdatainfo.goodsChargeFee = "1";
            }
            if (inputdatainfo.paymentTypeCode.equals("1")) {
                double1 = Double.valueOf(double1.doubleValue() + Double.parseDouble(inputdatainfo.goodsChargeFee));
            } else if (inputdatainfo.paymentTypeCode.equals("2")) {
                double1 = Double.valueOf(Double.parseDouble(inputdatainfo.waybillFee) + Double.parseDouble
                        (inputdatainfo.signBackFee) + Double.parseDouble(inputdatainfo.waitNotifyFee) + Double
                        .parseDouble(inputdatainfo.deboursFee) + Double.parseDouble(inputdatainfo.insuranceFee) +
                        Double.parseDouble(inputdatainfo.deliverFee));
            }

            inputdatainfo.totalFee = double1;

            // if ("02911".equals(sourcedistrictCode)) {
            // if (Double.parseDouble(inputdatainfo.meterageWeightQty) <= 5D) {
            // inputdatainfo.shangmenFee = "4.0";
            // double allP = Double.parseDouble(inputdatainfo.waybillFee) -
            // Double.parseDouble(inputdatainfo.shangmenFee);
            // inputdatainfo.waybillFee = MyUtils.round(allP);
            //
            // } else {
            // inputdatainfo.shangmenFee = MyUtils
            // .round(4D + (Double.parseDouble(inputdatainfo.meterageWeightQty)
            // - 5D) * 0.10000000000000001D);
            // inputdatainfo.waybillFee =
            // MyUtils.round(Double.parseDouble(inputdatainfo.waybillFee)
            // - Double.parseDouble(inputdatainfo.shangmenFee));
            //
            // }
            // }

            listOrder.add(inputdatainfo);
            i++;
        } while (true);
    }

    public void upDate(BaseReceiver basereceiver, Intent intent) {
        if (intent != null) {
            int i = intent.getIntExtra("level", 0);
            mBatterySignal.setImageLevel(i);
            String date = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(new Date());
            mDate.setText(date);
            mWifiSignal.setImageLevel(WiFiUtil.getWiFiLevel(this));
        }
    }

    class LstOrderAdapter extends BaseAdapter {

        public int getCount() {
            if (listOrder == null) {
                return 0;
            } else {
                return listOrder.size();
            }
        }

        public Object getItem(int i) {
            return listOrder.get(i);
        }

        public long getItemId(int i) {
            return i;
        }

        public View getView(int i, View view, ViewGroup viewg) {
            ViewHolder viewholder;
            if (view == null) {
                viewholder = new ViewHolder();

                view = getLayoutInflater().inflate(R.layout.item_quire, null);
                viewholder.totalFee = (TextView) view.findViewById(R.id.item_quire_payMethod);
                viewholder.waybillnumber = (TextView) view.findViewById(R.id.item_quire_waybillnumber);
                viewholder.empCode = (TextView) view.findViewById(R.id.item_quire_empCode);
                viewholder.consignorContName = (TextView) view.findViewById(R.id.item_quire_consignorContName);
                viewholder.consignorPhone = (TextView) view.findViewById(R.id.item_quire_consignorPhone);
                viewholder.addresseeContName = (TextView) view.findViewById(R.id.item_quire_addresseeContName);
                viewholder.addresseePhone = (TextView) view.findViewById(R.id.item_quire_addresseePhone);
                viewholder.consName = (TextView) view.findViewById(R.id.item_quire_consName);
                viewholder.quantity = (TextView) view.findViewById(R.id.item_quire_quantity);
                viewholder.meterageWeightQty = (TextView) view.findViewById(R.id.item_quire_meterageWeightQty);
                viewholder.waybillFee = (TextView) view.findViewById(R.id.item_quire_waybillNoFee);
                viewholder.goodsChargeFee = (TextView) view.findViewById(R.id.item_quire_goodsChargeFee);
                viewholder.payMethod = (TextView) view.findViewById(R.id.item_quire_payMethod);

                view.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) view.getTag();
            }

            viewholder.totalFee.setText((new StringBuilder()).append((listOrder.get(i)).totalFee).toString());

            viewholder.waybillnumber.setText((listOrder.get(i)).waybillNo);

            viewholder.empCode.setText((listOrder.get(i)).consigneeEmpCode);

            viewholder.consignorContName.setText((listOrder.get(i)).consignorContName);

            if (!MyUtils.isEmpty(listOrder.get(i).consignorPhone)) {
                viewholder.consignorPhone.setText((listOrder.get(i)).consignorPhone);
            } else {
                viewholder.consignorPhone.setText((listOrder.get(i)).consignorMobile);
            }

            viewholder.addresseeContName.setText((listOrder.get(i)).addresseeContName);

            if (MyUtils.isEmpty(listOrder.get(i).addresseePhone)) {
                viewholder.addresseePhone.setText((listOrder.get(i)).addresseeMobile);
            } else {
                viewholder.addresseePhone.setText((listOrder.get(i)).addresseePhone);
            }

            viewholder.consName.setText((listOrder.get(i)).consName);

            viewholder.quantity.setText((listOrder.get(i)).quantity + "");

            viewholder.meterageWeightQty.setText((listOrder.get(i)).meterageWeightQty);

            viewholder.waybillFee.setText((listOrder.get(i)).waybillFee);

            viewholder.goodsChargeFee.setText((listOrder.get(i)).goodsChargeFee);

            String payMethodStr = "";
            if (listOrder.get(i).paymentTypeCode.equals("1")) {
                payMethodStr = "寄付";
            } else {
                payMethodStr = "到付";
            }
            viewholder.payMethod.setText(payMethodStr);
            return view;
        }
    }

    class ViewHolder {
        TextView addresseeContName;
        TextView addresseePhone;
        TextView consName;
        TextView consignorContName;
        TextView consignorPhone;
        TextView empCode;
        TextView goodsChargeFee;
        TextView meterageWeightQty;
        TextView payMethod;
        TextView quantity;
        TextView totalFee;
        TextView waybillFee;
        TextView waybillnumber;
    }

}
