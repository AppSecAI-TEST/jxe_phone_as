package com.eruitong.activity.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eruitong.activity.BaseActivity;
import com.eruitong.activity.MainActivity;
import com.eruitong.config.Conf;
import com.eruitong.db.BarOptCodeDBWrapper;
import com.eruitong.db.BillEmployeeDBWrapper;
import com.eruitong.db.DepartmentDBWrapper;
import com.eruitong.db.DistanceTypeSpecDBWrapper;
import com.eruitong.db.DistrictDBWrapper;
import com.eruitong.db.DropinFeeDBWrapper;
import com.eruitong.db.FailCauseDBWrapper;
import com.eruitong.db.HttppathDBWrapper;
import com.eruitong.db.LineDBWrapper;
import com.eruitong.db.LoginDBWrapper;
import com.eruitong.db.ProdpriceDBWrapper;
import com.eruitong.db.ServiceProdPriceDBWrapper;
import com.eruitong.db.SetPassWordDBWrapper;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.model.DbDatafInfo;
import com.eruitong.model.net.I_DevLogin;
import com.eruitong.model.net.I_SyncBaseData;
import com.eruitong.utils.MyUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Map;

public class LoginActivity extends BaseActivity {
    public String BluetoothAddress;
    public String BluetoothName;

    String LoginName;
    String MACOne;
    String MACSecond;
    String RegisterTime;
    String SerialNum;
    String WebSite;
    String deviceId;
    String isFlagPhone;

    @ViewInject(R.id.btn_login_exit)
    Button mBtnExit;
    @ViewInject(R.id.btn_login_login)
    Button mBtnLogin;

    @ViewInject(R.id.btn_login_sethttp)
    Button mBtnSet;
    @ViewInject(R.id.login_edt_name)
    EditText mEdtName;
    @ViewInject(R.id.login_edt_password)
    EditText mEdtPassword;

    @ViewInject(R.id.txt_login_deviceid_version)
    public TextView mTxtDeviceId;

    public Cursor mCursor;

    String mEmpCode;
    Map mMap;
    String mPassword;
    String mPathServerURL;
    String isUpdate;

    public LoginDBWrapper mLoginDB;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy((new android.os.StrictMode.ThreadPolicy.Builder()).permitAll().build());

        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);

        initDatas();
        initView();
    }

    private void initDatas() {
        deviceId = MyUtils.deviceId(LoginActivity.this);
        MyApp.mSession = deviceId;
        Cursor cursor = HttppathDBWrapper.getInstance(getApplication()).rawQueryRank();
        do {
            if (!cursor.moveToNext()) {
                cursor.close();
                return;
            }
            mPathServerURL = cursor.getString(cursor.getColumnIndex("name"));
            MyApp.mPathServerURL = mPathServerURL;
        } while (true);
    }

    private void initView() {
        mBtnExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createExitDialog();
            }
        });

        mBtnSet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createSetDialog().show();
            }
        });

        mBtnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MyUtils.isNetworkConnect(LoginActivity.this)) {
                    login();
                    return;
                } else {
                    showT("网络未连接，离线登陆");
                    userLogin();
                    return;
                }

            }
        });

        String string = MyUtils.deviceId(LoginActivity.this);
        mTxtDeviceId.setText("设备号:" + string + "\n版本号:" + MyUtils.getVersionName(LoginActivity.this));
    }

    private void login() {
        mEmpCode = mEdtName.getText().toString().trim();
        mPassword = mEdtPassword.getText().toString().trim();

        mLoginDB = LoginDBWrapper.getInstance(getApplication());

        int versionCode = MyUtils.getVersionCode(LoginActivity.this);

        if (TextUtils.isEmpty(mPathServerURL)) {
            createSetDialog().show();
            showT("请设置服务器地址");
            return;
        }
        if (TextUtils.isEmpty(mEmpCode)) {
            showT("请输入用户姓名");
            return;
        }
        if (TextUtils.isEmpty(mPassword)) {
            showT("请输入密码");
            return;
        }
        if (TextUtils.isEmpty(deviceId) && versionCode != 0) {
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
            finish();
            showT("设备id获取为空，请重新登陆");
            return;
        }
        /*
         * if (versionCode < 19) { Toast.makeText(this, "请安装最新版本");
		 * return; }
		 */
        else {

            StringBuilder builder = new StringBuilder();
            builder.append(mPathServerURL + Conf.LoginUrl);
            builder.append("?").append("username=").append(mEmpCode);
            builder.append("&").append("password=").append(mPassword);
            builder.append("&").append("devId=").append(deviceId);
            builder.append("&").append("versionCode=").append(versionCode);
            String url = builder.toString();

            HttpUtils httpUtils = new HttpUtils();
            httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
                private Dialog dialog;

                @Override
                public void onStart() {
                    super.onStart();
                    try {
                        dialog = createLoginDialog();
                        dialog.show();
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    try {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.cancel();
                        }
                    } catch (Exception e) {
                    } finally {
                        showT("登陆失败");
                    }
                }

                @Override
                public void onSuccess(ResponseInfo<String> arg0) {
                    String result = arg0.result;
                    I_DevLogin devLogin = null;

                    if (dialog != null && dialog.isShowing()) {
                        dialog.cancel();
                    }

                    try {
                        devLogin = new Gson().fromJson(result, I_DevLogin.class);

                        if (devLogin.isSuccess()) {
                            updateLogin(devLogin);

                            MyApp.mEmpCode = mEmpCode;
                            MyApp.mEmpName = devLogin.getEmpName();
                            MyApp.mDeptCode = devLogin.getDeptCode();
                            // 更新基础数据
                            if ("1".equals(devLogin.getIsUpdate())) {
                                initData();
                                updateIsUpdate();
                            }
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            showT(devLogin.getError());
                        }
                    } catch (Exception e) {
                        showT("登陆失败,请重新登录");
                        return;
                    }
                }
            });

            return;
        }

    }

    private void updateLogin(I_DevLogin devLogin) {
        mLoginDB = LoginDBWrapper.getInstance(getApplication());

        mCursor = mLoginDB.rawQueryRank();

        mCursor.moveToFirst();

        String listStr = devLogin.getAuthList().toString();

        MyApp.mPermission = listStr;

        System.err.println("列表的：" + listStr);

        if (!mCursor.moveToNext()) {
            mCursor.close();
            mLoginDB.insertRank(devLogin.getEmpName(), mEmpCode, devLogin.getDeptCode(), mPassword, listStr);
            return;
        }

        int i = mCursor.getColumnIndex("empCode");
        if (!mCursor.getString(i).equals(devLogin.getEmpName())) {
            mLoginDB.deleteRank(mCursor.getString(i));
            mLoginDB.insertRank(devLogin.getEmpName(), mEmpCode, devLogin.getDeptCode(), mPassword, listStr);
            mCursor.close();
        }
    }

    private void userLogin() {
        mEmpCode = mEdtName.getText().toString();
        mPassword = mEdtPassword.getText().toString();
        mLoginDB = LoginDBWrapper.getInstance(getApplication());
        mCursor = mLoginDB.rawQueryRank();
        do {
            if (!mCursor.moveToNext()) {
                if (mCursor != null) {
                    mCursor.close();
                }
                return;
            }
            int i = mCursor.getColumnIndex("empName");
            String s = mCursor.getString(i);
            i = mCursor.getColumnIndex("empCode");
            String s2 = mCursor.getString(i);
            i = mCursor.getColumnIndex("deptCode");
            String s1 = mCursor.getString(i);
            i = mCursor.getColumnIndex("password");
            String s3 = mCursor.getString(i);
            int versionCode = MyUtils.getVersionCode(LoginActivity.this);
            if (versionCode < 19) {
                showT("请安装最新版本系统");
            }
            if (TextUtils.isEmpty(mEmpCode)) {
                showT("用户姓名不能为空");
            } else if (mEmpCode.equals(s2) && !mEmpCode.isEmpty()) {
                if (mPassword.equals(s3)) {
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    MyApp.mEmpCode = mEmpCode;
                    MyApp.mEmpName = s;
                    MyApp.mDeptCode = s1;
                    showT("离线登陆成功");
                    finish();
                } else if (TextUtils.isEmpty(mPassword)) {
                    showT("密码不能为空");
                } else if (!mPassword.equals(s3)) {
                    showT("密码输入错误");
                }
            }
        } while (true);
    }

    private void initData() {

        HttpUtils httpUtils = new HttpUtils();

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

        // 行政区域
        httpUtils.send(HttpMethod.GET, doGetString("District"), new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                showT("数据获取失败 ！");
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
                    showT(String.valueOf(baseData.getError()));
                }
            }
        });

        // 网点部门
        httpUtils.send(HttpMethod.GET, doGetString("Department"), new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                showT("数据获取失败");
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
                        showT("");
                        showT("网点部门更新成功");
                    } else {
                        showT("网点部门列表为空");
                    }
                } else {
                    showT(baseData.getError() + "");
                }
            }
        });
        // 员工
        httpUtils.send(HttpMethod.GET, doGetString("BillEmployee"), new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                showT("数据获取失败 ！");
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
            }
        });
        // 线路编码
        httpUtils.send(HttpMethod.GET, doGetString("Line"), new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                showT("数据获取失败 ！");
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
            }
        });
        // 价格
        httpUtils.send(HttpMethod.GET, doGetString("ProdPrice"), new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                showT("数据获取失败 ！");
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
            }
        });
        // 地区间关系
        httpUtils.send(HttpMethod.GET, doGetString("DistanceTypeSpec"), new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                showT("数据获取失败 ！");
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
            }
        });
        // 增值服务
        httpUtils.send(HttpMethod.GET, doGetString("ServiceProdPrice"), new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                showT("数据获取失败 ！");
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
            }
        });
        // 原因代码
        httpUtils.send(HttpMethod.GET, doGetString("FailCause"), new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                showT("数据获取失败 ！");
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
            }
        });
        // 上门费
        httpUtils.send(HttpMethod.GET, doGetString("DropInFeeConfig"), new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                showT("数据获取失败 ！");
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
            }
        });
    }

    /**
     * 请求数据的下载地址
     **/
    private String doGetString(String entityName) {
        return new StringBuilder().append(MyApp.mPathServerURL).append(Conf.bsseUpdataUrl).append("?entityName=")
                .append(entityName).append("&deptCode=").append(MyApp.mDeptCode).toString();
    }

    /**
     * 登记代收款
     **/
    private void updateIsUpdate() {

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", MyApp.mEmpCode);

        httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.updateIsUpdateAction, params, new
                RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                showT("网络连接失败,请稍后再试");
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                showT("更新成功");
                return;
            }
        });
    }

    /**
     * 创建退出对话框
     **/
    private void createExitDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.exit));
        builder.setIcon(R.drawable.ic_launcher);
        builder.setMessage(getString(R.string.exit_app_tishi));
        builder.setPositiveButton(getString(R.string.ok), new android.content.DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.create().show();
    }

    /**
     * 创建登陆对话框
     *
     * @return
     */
    private Dialog createLoginDialog() {
        ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setProgressStyle(0);
        progressdialog.setTitle("登录");
        progressdialog.setMessage("正在登录...");
        progressdialog.setCancelable(false);
        return progressdialog;
    }

    private String passwordStr = "";

    /**
     * 创建设置对话框
     **/
    private Dialog createSetDialog() {
        SetPassWordDBWrapper setPassWordDB = SetPassWordDBWrapper.getInstance(getApplication());
        Cursor cursor = setPassWordDB.rawQueryRank();
        if (!cursor.moveToFirst()) {
            setPassWordDB.deleteRank();
            setPassWordDB.insertRank("999999");
        }
        cursor.close();
        Cursor rawQueryRank = setPassWordDB.rawQueryRank();
        do {
            if (!rawQueryRank.moveToNext()) {
                rawQueryRank.close();

                AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("设置");
                View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                ((TextView) view.findViewById(R.id.txt_dialog_layout_before)).setText("验证码：");
                final EditText edtText = (EditText) view.findViewById(R.id.edt_dialog_layout);
                edtText.setText("");
                builder.setView(view);
                builder.setPositiveButton(getString(R.string.ok), new android.content.DialogInterface.OnClickListener
                        () {

                    public void onClick(DialogInterface dialoginterface, int i) {
                        if (edtText.getText().toString().equals(passwordStr)) {
                            gotoActivity();
                            edtText.setText("");
                        } else {
                            showT("输入有误");
                        }
                    }

                });
                builder.setNegativeButton(getString(R.string.cancel), null);
                return builder.create();
            }
            passwordStr = rawQueryRank.getString(rawQueryRank.getColumnIndex("name"));
        } while (true);
    }

    private void gotoActivity() {
        Intent intent = new Intent(this, SetTabActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            createExitDialog();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
