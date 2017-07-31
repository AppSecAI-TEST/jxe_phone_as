package com.eruitong.activity.login;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.eruitong.activity.BaseActivity;
import com.eruitong.db.AcceptInputDBWrapper;
import com.eruitong.db.DeliveryDBWrapper;
import com.eruitong.db.HttppathDBWrapper;
import com.eruitong.db.WaybillNoDBWrapper;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.utils.DateUtils;
import com.eruitong.utils.KeyBoardUtils;
import com.eruitong.utils.MyUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class SetTabOperateActivity extends BaseActivity {

    @ViewInject(R.id.set_spinner)
    Spinner mSpinnerHttp;
    @ViewInject(R.id.set_txt_addree)
    TextView mEdtNetAddress;
    /**
     * 员工号
     **/
    @ViewInject(R.id.set_txt_staffid)
    TextView mStaffId;
    /**
     * 自动上传的时间
     **/
    @ViewInject(R.id.set_txt_auto_upload)
    TextView mAutoUpload;
    /**
     * 自动删除数据
     **/
    @ViewInject(R.id.set_txt_delete_data)
    TextView mAutoDeleteData;
    /**
     * 删除用户数据
     **/
    @ViewInject(R.id.set_btn_delete_data)
    TextView mAutoDeleteUserData;

    private String deviceId;
    private String mStaffIdTxt;
    private String mAutoUploadTimeTxt;
    private String mDeleteDataTimeTxt;
    private String mDeleteBeforeDataTxt;

    private HttppathDBWrapper httppathDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settab_operate);
        ViewUtils.inject(this);

        httppathDB = HttppathDBWrapper.getInstance(getApplication());
        deviceId = MyUtils.deviceId(SetTabOperateActivity.this);

        initView();
        refresh();
    }

    protected void onStart() {
        super.onStart();
        String s = "";
        httppathDB = HttppathDBWrapper.getInstance(getApplication());
        Cursor cursor = httppathDB.rawQueryRank();
        do {
            if (!cursor.moveToNext()) {
                cursor.close();
                if (s.equals("http://copserver.jx-express.cn/jxe-cop")) {
                    mSpinnerHttp.setSelection(0, true);
                } else if (s.equals("http://124.115.26.70/jxe-cop")) {
                    mSpinnerHttp.setSelection(1, true);
                } else if (s.equals("http://192.168.1.114:8080/jxe-cop")) {
                    mSpinnerHttp.setSelection(2, true);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        delSqlite();
                    }
                }).start();
                return;
            }
            s = cursor.getString(cursor.getColumnIndex("name"));
            mEdtNetAddress.setText(s);
        } while (true);
    }

    private void refresh() {

        SharedPreferences preferences = getSharedPreferences("cache_data", 0);
        mStaffIdTxt = preferences.getString("mStaffIdTxt", "");
        mDeleteDataTimeTxt = preferences.getString("mDeleteDataTimeTxt", "");
        mAutoUploadTimeTxt = preferences.getString("mAutoUpload", "");
        mDeleteBeforeDataTxt = preferences.getString("mDeleteBeforeDataTxt", "");

        Editor edit = preferences.edit();

        mStaffId.setText(mStaffIdTxt + "");
        if (MyUtils.isEmpty(mAutoUploadTimeTxt)) {
            mAutoUploadTimeTxt = "30";
            edit.putString("mAutoUpload", mAutoUploadTimeTxt);
        }
        mAutoUpload.setText(mAutoUploadTimeTxt + "");
        if (MyUtils.isEmpty(mDeleteDataTimeTxt)) {
            mDeleteDataTimeTxt = "90";
            edit.putString("mDeleteDataTimeTxt", mDeleteDataTimeTxt);
        }
        mAutoDeleteData.setText(mDeleteDataTimeTxt + "");

        mAutoDeleteUserData.setTag(mDeleteBeforeDataTxt + "");
        edit.commit();
    }

    private void initView() {
        // 设置下拉选择 地址
        mSpinnerHttp.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String httpPath = mSpinnerHttp.getSelectedItem().toString().trim();
                httppathDB.deleteRank();
                httppathDB.insertRank(httpPath, deviceId);
                mEdtNetAddress.setText(httpPath + "");
                MyApp.mPathServerURL = httpPath;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<String> paths = Arrays.asList(getResources().getStringArray(R.array.http_path));
        ArrayAdapter<String> arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paths);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerHttp.setAdapter(arr_adapter);

        // 点击设置员工号
        mStaffId.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createSetDialog("设置收派员工号", mStaffIdTxt, 1);
            }
        });

        // 设置自动上传
        mAutoUpload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createSetDialog("自动上传时间间隔是(分钟)", mAutoUploadTimeTxt, 2);
            }
        });

        // 自动删除
        mAutoDeleteData.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createSetDialog("数据库删除管理(天)", mDeleteDataTimeTxt, 3);
            }
        });
        // 清空数据
        mAutoDeleteUserData.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                delAllSqlite();
                Toast.makeText(SetTabOperateActivity.this, "全部清空成功", 0).show();
            }
        });
    }

    private void delAllSqlite() {
        AcceptInputDBWrapper.getInstance(getApplication()).deleteRank();
        DeliveryDBWrapper.getInstance(getApplication()).deleteRank();
        WaybillNoDBWrapper.getInstance(getApplication()).deleteRank();
    }

    /**
     * 创建设置对话框
     **/
    @SuppressWarnings("unused")
    private void createSetDialog(String title, String msg, final int index) {

        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(title);
        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        ((TextView) view.findViewById(R.id.txt_dialog_layout_before)).setVisibility(View.GONE);
        final EditText edtText = (EditText) view.findViewById(R.id.edt_dialog_layout);
        edtText.setText(msg + "");
        builder.setView(view);
        builder.setPositiveButton(getString(R.string.ok), new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i) {
                String str = edtText.getText().toString().trim();
                if (MyUtils.isEmpty(str)) return;

                SharedPreferences preferences = getSharedPreferences("cache_data", 0);
                Editor edit = preferences.edit();

                switch (index) {
                    case 1:// 设置员工号
                        edit.putString("mStaffIdTxt", str);
                        edit.commit();
                        mStaffId.setText(str);
                        break;
                    case 2:// 设置自动上传的时间
                        edit.putString("mAutoUpload", str);
                        edit.commit();
                        mAutoUpload.setText(str);
                        break;
                    case 3:// 设置删除的时间
                        edit.putString("mDeleteDataTimeTxt", str);
                        edit.commit();
                        mAutoDeleteData.setText(str);
                        break;

                    default:
                        break;
                }

                dialoginterface.dismiss();
            }

        });
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.create().show();

        KeyBoardUtils.openKeybord(edtText, SetTabOperateActivity.this);
    }

    private void delSqlite() {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
        String setday = mDeleteDataTimeTxt;
        if (MyUtils.isEmpty(mDeleteDataTimeTxt)) {
            setday = "90";
        }
        setday = simpledateformat.format(DateUtils.diffDate(new Date(), Integer.parseInt(setday)));
        // XX天之前的时间

        AcceptInputDBWrapper inputDBWrapper = AcceptInputDBWrapper.getInstance(getApplication());
        Cursor mCursor = inputDBWrapper.rawQueryRank();
        do {
            if (!mCursor.moveToNext()) {
                mCursor.close();
                return;
            }
            int i = mCursor.getColumnIndex("consignedTm");
            String s2 = mCursor.getString(i);
            String s3 = s2.substring(0, 10);
            try {
                if (simpledateformat.parse(setday).after(simpledateformat.parse(s3))) {
                    inputDBWrapper.TMdeleteRank(s2);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } while (true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        return super.onKeyDown(keyCode, event);
    }
}
