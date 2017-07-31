package com.eruitong.activity.receive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

import com.eruitong.db.AcceptInputDBWrapper;
import com.eruitong.db.childNoListDBWrapper;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.receiver.BaseReceiver;
import com.eruitong.receiver.BatteryReceiver;
import com.eruitong.receiver.Hearer;
import com.eruitong.utils.MyUtils;
import com.eruitong.utils.WiFiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 查询已上传订单
 *
 * @author Administrator
 */
public class QueryUploadedActivity extends Activity implements Hearer {

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

    private String waybill;
    private int waybillNum;
    private int waybillCount;

    private String mSelectDate;

    private List<ItemData> listOrder;
    private AcceptInputDBWrapper dbWrapper;
    private childNoListDBWrapper childNoListDB;
    private Cursor mCursor;

    private LstOrderAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_uploaded);
        ViewUtils.inject(this);

        setView();
    }

    protected void onStart() {
        super.onStart();
        dbWrapper = AcceptInputDBWrapper.getInstance(getApplication());
        childNoListDB = childNoListDBWrapper.getInstance(getApplication());
        String s = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        listOrder = new ArrayList<QueryUploadedActivity.ItemData>();
        addData(s);

        BatteryReceiver.getInstant(this).addHeraer(this);
        mUserName.setText((new StringBuilder(String.valueOf(MyApp.mEmpName))).append(MyApp.mDeptName).toString());
    }

    private void addData(String s) {
        listOrder.clear();
        mCursor = dbWrapper.dataQueryRank(s, 1);
        do {
            int i;
            ItemData itemdata;
            if (!mCursor.moveToNext()) {
                mCursor.close();
                mQueryUploadedPoll.setText((new StringBuilder("共")).append(waybillNum).append("票").toString());
                mQueryUploadedPiece.setText((new StringBuilder("共")).append(waybillCount).append("件").toString());
                myAdapter.notifyDataSetChanged();
                return;
            }
            itemdata = new ItemData();
            i = mCursor.getColumnIndex("waybillNo");
            itemdata.waybillnumber = mCursor.getString(i);
            i = mCursor.getColumnIndex("consigneeEmpCode");
            itemdata.empCode = mCursor.getString(i);
            i = mCursor.getColumnIndex("consignorContName");
            itemdata.consignorContName = mCursor.getString(i);
            i = mCursor.getColumnIndex("consignorPhone");
            itemdata.consignorPhone = mCursor.getString(i);
            i = mCursor.getColumnIndex("consignorMobile");
            itemdata.consignorMobile = mCursor.getString(i);
            i = mCursor.getColumnIndex("addresseeContName");
            itemdata.addresseeContName = mCursor.getString(i);
            i = mCursor.getColumnIndex("addresseePhone");
            itemdata.addresseePhone = mCursor.getString(i);
            i = mCursor.getColumnIndex("addresseeMobile");
            itemdata.addresseeMobile = mCursor.getString(i);
            i = mCursor.getColumnIndex("consName");
            itemdata.consName = mCursor.getString(i);
            i = mCursor.getColumnIndex("quantity");
            itemdata.quantity = mCursor.getString(i);
            i = mCursor.getColumnIndex("meterageWeightQty");
            itemdata.meterageWeightQty = mCursor.getString(i);
            i = mCursor.getColumnIndex("waybillFee");
            itemdata.waybillFee = mCursor.getString(i);
            i = mCursor.getColumnIndex("goodsChargeFee");
            itemdata.goodsChargeFee = mCursor.getString(i);
            i = mCursor.getColumnIndex("paymentTypeCode");
            itemdata.payMethod = mCursor.getString(i);
            i = mCursor.getColumnIndex("chargeAgentFee");
            itemdata.chargeAgentFee = mCursor.getString(i);
            i = mCursor.getColumnIndex("signBackFee");
            itemdata.signBackFee = mCursor.getString(i);
            i = mCursor.getColumnIndex("waitNotifyFee");
            itemdata.waitNotifyFee = mCursor.getString(i);
            i = mCursor.getColumnIndex("deboursFee");
            itemdata.deboursFee = mCursor.getString(i);
            i = mCursor.getColumnIndex("insuranceFee");
            itemdata.insuranceFee = mCursor.getString(i);
            i = mCursor.getColumnIndex("deliverFee");
            itemdata.deliverFee = mCursor.getString(i);

            waybillNum = waybillNum + 1;
            waybillCount = waybillCount + Integer.parseInt(itemdata.quantity);

            if (MyUtils.isEmpty(itemdata.goodsChargeFee)) {
                itemdata.goodsChargeFee = "0";
            }

            if (itemdata.payMethod.equals("2")) {
                itemdata.totalFee = Double.valueOf(Double.parseDouble(itemdata.chargeAgentFee) + Double.parseDouble
                        (itemdata.waybillFee) + Double.parseDouble(itemdata.signBackFee) + Double.parseDouble
                        (itemdata.waitNotifyFee) + Double.parseDouble(itemdata.deboursFee) + Double.parseDouble
                        (itemdata.insuranceFee) + Double.parseDouble(itemdata.deliverFee) + Double.parseDouble
                        (itemdata.goodsChargeFee));
            } else if (itemdata.payMethod.equals("1")) {
                itemdata.totalFee = Double.valueOf(Double.parseDouble(itemdata.chargeAgentFee) + Double.parseDouble
                        (itemdata.waybillFee) + Double.parseDouble(itemdata.signBackFee) + Double.parseDouble
                        (itemdata.waitNotifyFee) + Double.parseDouble(itemdata.deboursFee) + Double.parseDouble
                        (itemdata.insuranceFee) + Double.parseDouble(itemdata.deliverFee));
            }

            listOrder.add(itemdata);
        } while (true);
    }

    protected void onStop() {
        super.onStop();
        BatteryReceiver.getInstant(this).delHeraer(this);
    }

    private void setView() {
        mTitleBase.setText("已上传运单查询");
        mCancel.setVisibility(View.GONE);
        mOk.setText("转换未上传");

        myAdapter = new LstOrderAdapter();
        mListQuery.setAdapter(myAdapter);
        mListQuery.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                waybill = ((ItemData) listOrder.get(position)).waybillnumber;
            }
        });

        mKeyword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                @SuppressLint("WrongConstant") Dialog dialog = new DatePickerDialog(QueryUploadedActivity.this, new
                        OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        waybillNum = 0;
                        waybillCount = 0;
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
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();

            }
        });

        mOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (MyUtils.isEmpty(waybill)) {
                    return;
                }
                Builder view = new android.app.AlertDialog.Builder(QueryUploadedActivity.this);
                view.setMessage((new StringBuilder("确认更改运单号:")).append(waybill).append(" 的状态吗 ？").toString());
                view.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbWrapper.updateRank(waybill, 0);
                        Cursor dialoginterface = childNoListDB.rawQueryRank(waybill, 1);
                        do {
                            if (!dialoginterface.moveToNext()) {
                                dialoginterface.close();
                                String s = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
                                addData(s);
                                myAdapter.notifyDataSetChanged();
                                return;
                            }
                            childNoListDB.rawUpdateRank(waybill, 0);
                        } while (true);
                    }
                });
                view.setNegativeButton(getString(R.string.cancel), null);
                view.create();
                view.show();
            }
        });

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
            view = viewg;
            if (view != null) {
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

            viewholder.waybillnumber.setText((listOrder.get(i)).waybillnumber);

            viewholder.empCode.setText((listOrder.get(i)).empCode);

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

            viewholder.quantity.setText((listOrder.get(i)).quantity);

            viewholder.meterageWeightQty.setText((listOrder.get(i)).meterageWeightQty);

            viewholder.waybillFee.setText((listOrder.get(i)).waybillFee);

            viewholder.goodsChargeFee.setText((listOrder.get(i)).goodsChargeFee);
            String payMethodStr = "";
            if (listOrder.get(i).payMethod.equals("1")) {
                payMethodStr = "寄付";
            } else {
                payMethodStr = "到付";
            }
            viewholder.payMethod.setText(payMethodStr);
            // viewholder.payMethod.setText((listOrder.get(i)).payMethod);
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

    class ItemData {
        String addresseeContName;
        String addresseeMobile;
        String addresseePhone;
        String chargeAgentFee;
        String consName;
        String consignorContName;
        String consignorMobile;
        String consignorPhone;
        String deboursFee;
        String deliverFee;
        String empCode;
        String goodsChargeFee;
        String insuranceFee;
        String meterageWeightQty;
        String payMethod;
        String quantity;
        String signBackFee;
        Double totalFee;
        String waitNotifyFee;
        String waybillFee;
        String waybillnumber;
    }
}
