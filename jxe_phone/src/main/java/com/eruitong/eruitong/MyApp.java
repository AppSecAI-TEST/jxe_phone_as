package com.eruitong.eruitong;

import android.app.Application;
import android.os.Environment;

import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;

public class MyApp extends Application {

    public static String mEmpName = "";
    public static String mEmpCode = "";

    public static String mDeptName = "";
    public static String mDeptCode = "";

    public static String mSession = "";
    public static String mPathServerURL = "";

    public static String mPermission = "";
    public static String PATH = "";

    @Override
    public void onCreate() {
        super.onCreate();
        // 900014150
        CrashReport.initCrashReport(getApplicationContext(), "900014150", false);

        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        PATH = sdPath + File.separator + "jxe_back" + File.separator;
    }
}
