package com.eruitong.utils;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.eruitong.config.Conf;
import com.eruitong.db.DepartmentDBWrapper;
import com.eruitong.db.WaybillNoDBWrapper;
import com.eruitong.eruitong.MyApp;
import com.eruitong.model.net.I_GetOrder;
import com.eruitong.model.net.O_GetOrder;
import com.eruitong.model.OrderList;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;
import java.util.List;

public class UpdataWaybill {

    /**
     * 运单号-点击确定
     **/
    public static void waybill(final Context context) {
        String mDistrictCode = "";
        final WaybillNoDBWrapper WaybillNoDB = WaybillNoDBWrapper.getInstance(context);
        Cursor cursor = DepartmentDBWrapper.getInstance(context).rawQueryRank(MyApp.mDeptCode);
        do {
            if (!cursor.moveToNext()) {
                cursor.close();
                break;
            }
            mDistrictCode = cursor.getString(cursor.getColumnIndex("districtCode"));
        } while (true);

        if (TextUtils.isEmpty(mDistrictCode)) {
            return;
        }

        ArrayList<O_GetOrder> orders = new ArrayList<O_GetOrder>();
        O_GetOrder getOrder;

        Cursor queryRank001 = WaybillNoDB.rawQueryRank("001");
        int queryRank001Index = queryRank001.getCount();
        if (queryRank001Index <= 200) {
            // 子单号
            getOrder = new O_GetOrder();
            getOrder.setAreaCode("001");
            getOrder.setApplyCount(1000);
            getOrder.setEmpCode(MyApp.mEmpCode);
            getOrder.setDevId(MyApp.mSession);
            orders.add(getOrder);
        }

        Cursor queryRank123 = WaybillNoDB.rawQueryRank("123");
        int queryRank123Index = queryRank123.getCount();
        if (queryRank123Index <= 200) {
            // 签回单单号
            getOrder = new O_GetOrder();
            getOrder.setAreaCode("123");
            getOrder.setApplyCount(1000);
            getOrder.setEmpCode(MyApp.mEmpCode);
            getOrder.setDevId(MyApp.mSession);
            orders.add(getOrder);
        }

        Cursor mDistrictCodeNum = WaybillNoDB.rawQueryRank(mDistrictCode);
        int mDistrictCodeIndex = mDistrictCodeNum.getCount();
        if (mDistrictCodeIndex <= 200) {
            // 主单号
            getOrder = new O_GetOrder();
            getOrder.setAreaCode(mDistrictCode);
            getOrder.setApplyCount(1000);
            getOrder.setEmpCode(MyApp.mEmpCode);
            getOrder.setDevId(MyApp.mSession);
            orders.add(getOrder);
        }

        // 如果没有添加内容，表示没有需要更新的单号
        if (orders.size() <= 0) {
            orders = null;
            return;
        } else {
            httpUpdata(context, WaybillNoDB, orders);
        }
    }

    private static void httpUpdata(final Context context, final WaybillNoDBWrapper WaybillNoDB, ArrayList<O_GetOrder>
            orders) {
        String jsonString = new Gson().toJson(orders);

        HttpUtils httpUtils = new HttpUtils();

        RequestParams params = new RequestParams();
        params.addBodyParameter("jsonData", jsonString);
        String url = MyApp.mPathServerURL + Conf.waybillUpdataUrl;

        httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                MyUtils.showText(context, "运单号更新失败，请稍后重新更新");
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String result = arg0.result;

                I_GetOrder baseData = new Gson().fromJson(result, I_GetOrder.class);

                if (baseData.isSuccess()) {
                    List<OrderList> dataList = baseData.getDataList();
                    if (dataList != null && dataList.size() >= 0) {
                        // 插入新的数据
                        WaybillNoDB.insertRank(dataList);
                        MyUtils.showText(context, "运单号更新成功");
                    } else {
                        MyUtils.showText(context, "运单号列表为空");
                    }
                } else {
                    MyUtils.showText(context, baseData.getError());
                }
            }
        });
    }
}
