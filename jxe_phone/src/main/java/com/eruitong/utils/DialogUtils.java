package com.eruitong.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by lyl on 2017/7/27.
 */

public class DialogUtils {

    public static Dialog createUpdataDialog(Context context){
        ProgressDialog progressdialog = new ProgressDialog(context);
        progressdialog.setProgressStyle(0);
        progressdialog.setTitle("提示");
        progressdialog.setMessage("正在加载，请稍等...");
        progressdialog.setCancelable(true);
        return progressdialog;
    }

    public static Dialog createLoginDialog(Context context) {
        ProgressDialog progressdialog = new ProgressDialog(context);
        progressdialog.setProgressStyle(0);
        progressdialog.setTitle("数据查询");
        progressdialog.setMessage("正在查询，请稍等...");
        progressdialog.setCancelable(false);
        return progressdialog;
    }
}
