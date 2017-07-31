package com.eruitong.activity.update;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eruitong.activity.BaseActivity;
import com.eruitong.config.Conf;
import com.eruitong.db.BarOptCodeDBWrapper;
import com.eruitong.db.BillEmployeeDBWrapper;
import com.eruitong.db.CategoryDBWrapper;
import com.eruitong.db.DepartmentDBWrapper;
import com.eruitong.db.DistanceTypeSpecDBWrapper;
import com.eruitong.db.DistrictDBWrapper;
import com.eruitong.db.DropinFeeDBWrapper;
import com.eruitong.db.FailCauseDBWrapper;
import com.eruitong.db.LineDBWrapper;
import com.eruitong.db.ProdpriceDBWrapper;
import com.eruitong.db.ServiceProdPriceDBWrapper;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.model.DbDatafInfo;
import com.eruitong.model.net.I_SyncBaseData;
import com.eruitong.receiver.BaseReceiver;
import com.eruitong.receiver.BatteryReceiver;
import com.eruitong.receiver.Hearer;
import com.eruitong.utils.MyUtils;
import com.eruitong.utils.WiFiUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpdataBaseListActivity extends BaseActivity implements Hearer {

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

    @ViewInject(R.id.updata_list)
    ListView mListView;
    @ViewInject(R.id.updata_btn_ok)
    Button mOK;
    @ViewInject(R.id.updata_btn_cancel)
    Button mCancel;

    /**
     * 列表所选择的记录
     **/
    private List<String> seletlist;

    private List mSynclist;
    /**
     * 列表显示的文字
     **/
    private String[] arraylist;
    private MyUpdataAdapter myAdapter;

    private BarOptCodeDBWrapper mBarOptCodeDB;
    private BillEmployeeDBWrapper mBillEmployeeDB;
    private DistanceTypeSpecDBWrapper mDistanceTypeSpecDB;
    private DistrictDBWrapper mDistrictDB;
    private FailCauseDBWrapper mFailCauseDB;
    private ProdpriceDBWrapper mProdpriceDB;
    private ServiceProdPriceDBWrapper mServiceProdPriceDB;
    private LineDBWrapper mLineDB;
    private DepartmentDBWrapper mDepartmentDB;
    private DropinFeeDBWrapper mDropinFeeDBWrapper;
    private CategoryDBWrapper mCategoryDBWrapper;

    private Dialog dialog;
    // 运单号
    private String mDistrictCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata_list);
        ViewUtils.inject(this);

        initData();
        initView();
    }

    private void initView() {
        dialog = createUpdataDialog();
        mTitleBase.setText("基础数据更新");
        myAdapter = new MyUpdataAdapter();
        mListView.setAdapter(myAdapter);
        // 返回
        mCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyUtils.isNetworkConnect(UpdataBaseListActivity.this)) {
                    showT("网络未连接，请检查网络");
                    return;
                }

                dialog.show();

                HttpUtils httpUtils = new HttpUtils();

                int i = 0;
                int size = seletlist.size();
                do {
                    if (i >= size) {
                        return;
                    }

                    switch (Integer.parseInt(seletlist.get(i))) {

                        case 0: {// 行政区域
                            httpUtils.send(HttpMethod.GET, doGetString("District"), new RequestCallBack<String>() {
                                @Override
                                public void onFailure(HttpException arg0, String arg1) {
                                    showT("数据获取失败 ！");
                                    dialog.cancel();
                                }

                                @Override
                                public void onSuccess(ResponseInfo<String> arg0) {
                                    String result = arg0.result;
                                    I_SyncBaseData baseData = new Gson().fromJson(result, I_SyncBaseData.class);
                                    if (baseData.isSuccess()) {
                                        mDistrictDB.deleteRank();// 删除原有数据
                                        List<DbDatafInfo> dataList = baseData.getDataList();
                                        if (dataList != null && dataList.size() >= 0) {
                                            // 插入新的数据
                                            mDistrictDB.insertRank(dataList);
                                            showT("行政区域更新成功");
                                        } else {
                                            showT("行政区域列表为空");
                                        }
                                    } else {
                                        showT(baseData.getError() + "");
                                    }
                                    dialog.cancel();
                                }
                            });
                            break;
                        }
                        case 1: {// 网点部门
                            httpUtils.send(HttpMethod.GET, doGetString("Department"), new RequestCallBack<String>() {
                                @Override
                                public void onFailure(HttpException arg0, String arg1) {
                                    showT("数据获取失败 ！");
                                    dialog.cancel();
                                }

                                @Override
                                public void onSuccess(ResponseInfo<String> arg0) {
                                    String result = arg0.result;

                                    I_SyncBaseData baseData = new Gson().fromJson(result, I_SyncBaseData.class);
                                    if (baseData.isSuccess()) {
                                        mDepartmentDB.deleteRank();// 删除原有数据
                                        List<DbDatafInfo> dataList = baseData.getDataList();
                                        if (dataList != null && dataList.size() >= 0) {
                                            // 插入新的数据
                                            mDepartmentDB.insertRank(dataList);
                                            showT("网点部门更新成功");
                                        } else {
                                            showT("网点部门列表为空");
                                        }
                                    } else {
                                        showT(baseData.getError() + "");
                                    }
                                    dialog.cancel();
                                }
                            });
                            break;
                        }
                        case 2: {// 员工
                            httpUtils.send(HttpMethod.GET, doGetString("BillEmployee"), new RequestCallBack<String>() {
                                @Override
                                public void onFailure(HttpException arg0, String arg1) {
                                    showT("数据获取失败 ！");
                                    dialog.cancel();
                                }

                                @Override
                                public void onSuccess(ResponseInfo<String> arg0) {
                                    String result = arg0.result;
                                    I_SyncBaseData baseData = new Gson().fromJson(result, I_SyncBaseData.class);
                                    if (baseData.isSuccess()) {
                                        mBillEmployeeDB.deleteRank();// 删除原有数据
                                        List<DbDatafInfo> dataList = baseData.getDataList();
                                        if (dataList != null && dataList.size() >= 0) {
                                            // 插入新的数据
                                            mBillEmployeeDB.insertRank(dataList);
                                            showT("员工更新成功");
                                        } else {
                                            showT("员工列表为空");
                                        }
                                    } else {
                                        showT(baseData.getError() + "");
                                    }
                                    dialog.cancel();
                                }
                            });
                            break;
                        }
                        case 3: {// 线路编码
                            httpUtils.send(HttpMethod.GET, doGetString("Line"), new RequestCallBack<String>() {
                                @Override
                                public void onFailure(HttpException arg0, String arg1) {
                                    showT("数据获取失败 ！");
                                    dialog.cancel();
                                }

                                @Override
                                public void onSuccess(ResponseInfo<String> arg0) {
                                    String result = arg0.result;
                                    I_SyncBaseData baseData = new Gson().fromJson(result, I_SyncBaseData.class);
                                    if (baseData.isSuccess()) {
                                        mLineDB.deleteRank();// 删除原有数据
                                        List<DbDatafInfo> dataList = baseData.getDataList();
                                        if (dataList != null && dataList.size() >= 0) {
                                            // 插入新的数据
                                            mLineDB.insertRank(dataList);
                                            showT("线路编码更新成功");
                                        } else {
                                            showT("线路编码列表为空");
                                        }
                                    } else {
                                        showT(baseData.getError() + "");
                                    }
                                    dialog.cancel();
                                }
                            });
                            break;
                        }
                        case 4: {// 价格
                            httpUtils.send(HttpMethod.GET, doGetString("ProdPrice"), new RequestCallBack<String>() {
                                @Override
                                public void onFailure(HttpException arg0, String arg1) {
                                    showT("数据获取失败 ！");
                                    dialog.cancel();
                                }

                                @Override
                                public void onSuccess(ResponseInfo<String> arg0) {
                                    String result = arg0.result;
                                    I_SyncBaseData baseData = new Gson().fromJson(result, I_SyncBaseData.class);
                                    if (baseData.isSuccess()) {
                                        mProdpriceDB.deleteRank();// 删除原有数据
                                        List<DbDatafInfo> dataList = baseData.getDataList();
                                        if (dataList != null && dataList.size() >= 0) {
                                            // 插入新的数据
                                            mProdpriceDB.insertRank(dataList);
                                            showT("价格更新成功");
                                        } else {
                                            showT("价格列表为空");
                                        }
                                    } else {
                                        showT(baseData.getError() + "");
                                    }
                                    dialog.cancel();
                                }
                            });
                            break;
                        }
                        case 5: {// 地区间关系
                            httpUtils.send(HttpMethod.GET, doGetString("DistanceTypeSpec"), new
                                    RequestCallBack<String>() {
                                @Override
                                public void onFailure(HttpException arg0, String arg1) {
                                    showT("数据获取失败 ！");
                                    dialog.cancel();
                                }

                                @Override
                                public void onSuccess(ResponseInfo<String> arg0) {
                                    String result = arg0.result;
                                    I_SyncBaseData baseData = new Gson().fromJson(result, I_SyncBaseData.class);
                                    if (baseData.isSuccess()) {
                                        mDistanceTypeSpecDB.deleteRank();// 删除原有数据
                                        List<DbDatafInfo> dataList = baseData.getDataList();
                                        if (dataList != null && dataList.size() >= 0) {
                                            // 插入新的数据
                                            mDistanceTypeSpecDB.insertRank(dataList);
                                            showT("地区间关系更新成功");
                                        } else {
                                            showT("地区间关系列表为空");
                                        }
                                    } else {
                                        showT(baseData.getError() + "");
                                    }
                                    dialog.cancel();
                                }
                            });
                            break;
                        }
                        case 6: {// 增值服务
                            httpUtils.send(HttpMethod.GET, doGetString("ServiceProdPrice"), new
                                    RequestCallBack<String>() {
                                @Override
                                public void onFailure(HttpException arg0, String arg1) {
                                    showT("数据获取失败 ！");
                                    dialog.cancel();
                                }

                                @Override
                                public void onSuccess(ResponseInfo<String> arg0) {
                                    String result = arg0.result;
                                    I_SyncBaseData baseData = new Gson().fromJson(result, I_SyncBaseData.class);
                                    if (baseData.isSuccess()) {
                                        mServiceProdPriceDB.deleteRank();// 删除原有数据
                                        List<DbDatafInfo> dataList = baseData.getDataList();
                                        if (dataList != null && dataList.size() >= 0) {
                                            // 插入新的数据
                                            mServiceProdPriceDB.insertRank(dataList);
                                            showT("增值服务更新成功");
                                        } else {
                                            showT("增值服务列表为空");
                                        }
                                    } else {
                                        showT(baseData.getError() + "");
                                    }
                                    dialog.cancel();
                                }
                            });
                            break;
                        }
                        case 7: {// 原因代码
                            httpUtils.send(HttpMethod.GET, doGetString("FailCause"), new RequestCallBack<String>() {
                                @Override
                                public void onFailure(HttpException arg0, String arg1) {
                                    showT("数据获取失败 ！");
                                    dialog.cancel();
                                }

                                @Override
                                public void onSuccess(ResponseInfo<String> arg0) {
                                    String result = arg0.result;
                                    I_SyncBaseData baseData = new Gson().fromJson(result, I_SyncBaseData.class);
                                    if (baseData.isSuccess()) {
                                        mFailCauseDB.deleteRank();// 删除原有数据
                                        List<DbDatafInfo> dataList = baseData.getDataList();
                                        if (dataList != null && dataList.size() >= 0) {
                                            // 插入新的数据
                                            mFailCauseDB.insertRank(dataList);
                                            showT("原因代码更新成功");
                                        } else {
                                            showT("原因代码列表为空");
                                        }
                                    } else {
                                        showT(baseData.getError() + "");
                                    }
                                    dialog.cancel();
                                }
                            });
                            break;
                        }
                        case 8: {// 上门费
                            httpUtils.send(HttpMethod.GET, doGetString("DropInFeeConfig"), new
                                    RequestCallBack<String>() {
                                @Override
                                public void onFailure(HttpException arg0, String arg1) {
                                    showT("数据获取失败 ！");
                                    dialog.cancel();
                                }

                                @Override
                                public void onSuccess(ResponseInfo<String> arg0) {
                                    String result = arg0.result;
                                    I_SyncBaseData baseData = new Gson().fromJson(result, I_SyncBaseData.class);
                                    if (baseData.isSuccess()) {
                                        mDropinFeeDBWrapper.deleteRank();// 删除原有数据
                                        List<DbDatafInfo> dataList = baseData.getDataList();
                                        if (dataList != null && dataList.size() >= 0) {
                                            // 插入新的数据
                                            mDropinFeeDBWrapper.insertRank(dataList);
                                            showT("上门费更新成功");
                                        } else {
                                            showT("上门费列表为空");
                                        }
                                    } else {
                                        showT(baseData.getError() + "");
                                    }
                                    dialog.cancel();
                                }
                            });
                            break;
                        }
                        case 9: {// 特殊品类
                            httpUtils.send(HttpMethod.GET, doGetString("Category"), new RequestCallBack<String>() {
                                @Override
                                public void onFailure(HttpException arg0, String arg1) {
                                    showT("数据获取失败 ！");
                                    dialog.cancel();
                                }

                                @Override
                                public void onSuccess(ResponseInfo<String> arg0) {
                                    String result = arg0.result;
                                    I_SyncBaseData baseData = new Gson().fromJson(result, I_SyncBaseData.class);
                                    if (baseData.isSuccess()) {
                                        mCategoryDBWrapper.deleteRank();// 删除原有数据
                                        List<DbDatafInfo> dataList = baseData.getDataList();
                                        if (dataList != null && dataList.size() >= 0) {
                                            // 插入新的数据
                                            mCategoryDBWrapper.insertRank(dataList);
                                            showT("特殊品类更新成功");
                                        } else {
                                            showT("特殊品类列表为空");
                                        }
                                    } else {
                                        showT(baseData.getError() + "");
                                    }
                                    dialog.cancel();
                                }
                            });
                            break;
                        }

                        default:
                            break;
                    }
                    i++;
                } while (true);
            }
        });

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder tagView = (ViewHolder) view.getTag();
                tagView.checkBox.toggle();
                int i = position;
                switch (i) {
                    default:
                        return;

                    case 0: // '\0'
                        if (tagView.checkBox.isChecked()) {
                            seletlist.add("0");
                            ((ItemData) mSynclist.get(i)).IsChecked = tagView.checkBox.isChecked();
                            return;
                        } else {
                            seletlist.remove("0");
                            ((ItemData) mSynclist.get(i)).IsChecked = tagView.checkBox.isChecked();
                            return;
                        }

                    case 1: // '\001'
                        if (tagView.checkBox.isChecked()) {
                            seletlist.add("1");
                            ((ItemData) mSynclist.get(i)).IsChecked = tagView.checkBox.isChecked();
                            return;
                        } else {
                            seletlist.remove("1");
                            ((ItemData) mSynclist.get(i)).IsChecked = tagView.checkBox.isChecked();
                            return;
                        }

                    case 2: // '\002'
                        if (tagView.checkBox.isChecked()) {
                            seletlist.add("2");
                            ((ItemData) mSynclist.get(i)).IsChecked = tagView.checkBox.isChecked();
                            return;
                        } else {
                            seletlist.remove("2");
                            ((ItemData) mSynclist.get(i)).IsChecked = tagView.checkBox.isChecked();
                            return;
                        }

                    case 3: // '\003'
                        if (tagView.checkBox.isChecked()) {
                            seletlist.add("3");
                            ((ItemData) mSynclist.get(i)).IsChecked = tagView.checkBox.isChecked();
                            return;
                        } else {
                            seletlist.remove("3");
                            ((ItemData) mSynclist.get(i)).IsChecked = tagView.checkBox.isChecked();
                            return;
                        }

                    case 4: // '\004'
                        if (tagView.checkBox.isChecked()) {
                            seletlist.add("4");
                            ((ItemData) mSynclist.get(i)).IsChecked = tagView.checkBox.isChecked();
                            return;
                        } else {
                            seletlist.remove("4");
                            ((ItemData) mSynclist.get(i)).IsChecked = tagView.checkBox.isChecked();
                            return;
                        }

                    case 5: // '\005'
                        if (tagView.checkBox.isChecked()) {
                            seletlist.add("5");
                            ((ItemData) mSynclist.get(i)).IsChecked = tagView.checkBox.isChecked();
                            return;
                        } else {
                            seletlist.remove("5");
                            ((ItemData) mSynclist.get(i)).IsChecked = tagView.checkBox.isChecked();
                            return;
                        }

                    case 6: // '\006'
                        if (tagView.checkBox.isChecked()) {
                            seletlist.add("6");
                            ((ItemData) mSynclist.get(i)).IsChecked = tagView.checkBox.isChecked();
                            return;
                        } else {
                            seletlist.remove("6");
                            ((ItemData) mSynclist.get(i)).IsChecked = tagView.checkBox.isChecked();
                            return;
                        }

                    case 7: // '\007'
                        if (tagView.checkBox.isChecked()) {
                            seletlist.add("7");
                            ((ItemData) mSynclist.get(i)).IsChecked = tagView.checkBox.isChecked();
                            return;
                        } else {
                            seletlist.remove("7");
                            ((ItemData) mSynclist.get(i)).IsChecked = tagView.checkBox.isChecked();
                            return;
                        }
                    case 8: // '\008'
                        if (tagView.checkBox.isChecked()) {
                            seletlist.add("8");
                            ((ItemData) mSynclist.get(i)).IsChecked = tagView.checkBox.isChecked();
                            return;
                        } else {
                            seletlist.remove("8");
                            ((ItemData) mSynclist.get(i)).IsChecked = tagView.checkBox.isChecked();
                            return;
                        }
                }
            }
        });

    }

    @SuppressWarnings("unchecked")
    private void initData() {
        mDistrictDB = DistrictDBWrapper.getInstance(getApplication());
        mDepartmentDB = DepartmentDBWrapper.getInstance(getApplication());
        mBillEmployeeDB = BillEmployeeDBWrapper.getInstance(getApplication());
        mLineDB = LineDBWrapper.getInstance(getApplication());
        mProdpriceDB = ProdpriceDBWrapper.getInstance(getApplication());
        mDistanceTypeSpecDB = DistanceTypeSpecDBWrapper.getInstance(getApplication());
        mServiceProdPriceDB = ServiceProdPriceDBWrapper.getInstance(getApplication());
        mBarOptCodeDB = BarOptCodeDBWrapper.getInstance(getApplication());
        mFailCauseDB = FailCauseDBWrapper.getInstance(getApplication());
        mDropinFeeDBWrapper = DropinFeeDBWrapper.getInstance(getApplication());
        mCategoryDBWrapper = CategoryDBWrapper.getInstance(getApplication());
        arraylist = getResources().getStringArray(R.array.updata_base);

        seletlist = new ArrayList<String>();
        mSynclist = new ArrayList();

        int i = 0;
        do {
            if (i >= arraylist.length) {
                return;
            }
            ItemData itemdata = new ItemData();
            itemdata.itemName = arraylist[i];
            itemdata.IsChecked = true;
            seletlist.add((new StringBuilder(String.valueOf(i))).toString());

            mSynclist.add(itemdata);
            i++;
        } while (true);
    }

    /**
     * 请求数据的下载地址
     **/
    private String doGetString(String entityName) {
        return new StringBuilder().append(MyApp.mPathServerURL).append(Conf.bsseUpdataUrl).append("?entityName=")
                .append(entityName).append("&deptCode=").append(MyApp.mDeptCode).toString();
    }

    private Dialog createUpdataDialog() {
        ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setProgressStyle(0);
        progressdialog.setTitle("数据更新");
        progressdialog.setMessage("正在更新，请稍等...");
        progressdialog.setCancelable(false);
        return progressdialog;
    }

    protected void onStart() {
        initComps();
        super.onStart();
        BatteryReceiver.getInstant(this).addHeraer(this);
        Cursor cursor = DepartmentDBWrapper.getInstance(getApplication()).rawQueryRank(MyApp.mDeptCode);
        do {
            if (!cursor.moveToNext()) {
                cursor.close();
                return;
            }
            mDistrictCode = cursor.getString(cursor.getColumnIndex("districtCode"));
        } while (true);
    }

    protected void onStop() {
        super.onStop();
        BatteryReceiver.getInstant(this).delHeraer(this);
    }

    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
    }

    private void initComps() {
        Cursor mCursor = DepartmentDBWrapper.getInstance(getApplication()).rawQueryRank(MyApp.mDeptCode);
        int i = mCursor.getColumnIndex("deptName");
        String mDeptName = "";
        if (mCursor.moveToFirst()) {
            mDeptName = (new StringBuilder("-")).append(mCursor.getString(i)).toString();
        }
        mCursor.close();
        mUserName.setText((new StringBuilder(String.valueOf(MyApp.mEmpName))).append(mDeptName).toString());
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

    class MyUpdataAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arraylist.length;
        }

        @Override
        public Object getItem(int position) {
            return arraylist[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            ViewHolder viewgroup;
            if (view == null) {
                viewgroup = new ViewHolder();
                view = getLayoutInflater().inflate(R.layout.syncdata_item, null);
                viewgroup.itemName = (TextView) view.findViewById(R.id.txt_syncdata);
                viewgroup.checkBox = (CheckBox) view.findViewById(R.id.chb_syncdata);
                view.setTag(viewgroup);
            } else {
                viewgroup = (ViewHolder) view.getTag();
            }
            viewgroup.itemName.setText(((ItemData) mSynclist.get(i)).itemName);
            viewgroup.checkBox.setChecked(((ItemData) mSynclist.get(i)).IsChecked);
            return view;
        }

    }

    class ViewHolder {
        CheckBox checkBox;
        TextView itemName;
    }

    class ItemData {
        boolean IsChecked;
        String itemName;
    }
}
