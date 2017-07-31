package com.eruitong.activity.send;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eruitong.activity.BaseActivity;
import com.eruitong.bluetooth.StatusBox;
import com.eruitong.config.Conf;
import com.eruitong.db.BluetoothDBWrapper;
import com.eruitong.db.DepartmentDBWrapper;
import com.eruitong.db.DistrictDBWrapper;
import com.eruitong.db.ServiceProdPriceDBWrapper;
import com.eruitong.eruitong.MyApp;
import com.eruitong.eruitong.R;
import com.eruitong.model.InputDataInfo;
import com.eruitong.model.net.I_PdaUploadData;
import com.eruitong.model.net.I_QueryWaybill;
import com.eruitong.print.bluetooth.BluetoothPrintTool;
import com.eruitong.print.bluetooth.BluetoothUtil;
import com.eruitong.receiver.BaseReceiver;
import com.eruitong.receiver.BatteryReceiver;
import com.eruitong.receiver.Hearer;
import com.eruitong.utils.CheckCode;
import com.eruitong.utils.MyUtils;
import com.eruitong.utils.PlayRaws;
import com.eruitong.utils.VibratorUtil;
import com.eruitong.utils.WiFiUtil;
import com.eruitong.view.DialogListener;
import com.eruitong.view.NoscrollListView;
import com.eruitong.view.SyncHorizontalScrollView;
import com.eruitong.view.WritePadDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zxing.activity.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zpSDK.zpSDK.zpSDK;

/**
 * 派件签收
 *
 * @author yl.li
 */
public class SendPiecesSignActivity extends BaseActivity implements Hearer {

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

    /**
     * 运单号
     **/
    @ViewInject(R.id.edt_delivery_waybill_number)
    EditText mEdtWaybillNumber;
    /**
     * 验证码
     **/
    @ViewInject(R.id.edt_delivery_waybill_sign)
    EditText mEdtWaybillCheckSign;
    /**
     * 查询按钮
     **/
    @ViewInject(R.id.btn_delivery_waybill_query)
    Button mBtnWaybillQuery;
    /**
     * 签收人
     **/
    @ViewInject(R.id.edt_delivery_signforer)
    EditText mEdtSignforer;
    /**
     * 收派员
     **/
    @ViewInject(R.id.edt_delivery_courier)
    EditText mEdtCourier;


    /**
     * 返回
     **/
    @ViewInject(R.id.btn_delivery_deliverysignfor_back)
    Button mBack;
    /**
     * 签收 上传
     **/
    @ViewInject(R.id.btn_delivery_deliverysignfor_print)
    Button mUpLoad;
    /**
     * 签收 上传 打印
     **/
    @ViewInject(R.id.btn_delivery_deliverysignfor_ok)
    Button mSignfor;
    /**
     * 手写签名
     **/
    @ViewInject(R.id.btn_take_sgin)
    Button mCheckSgin;
    /**
     * 拍照签收
     **/
    @ViewInject(R.id.btn_take_photo)
    Button tackePhoto;
    /**
     * 显示 手写签名 或 拍照 之后的图片
     **/
    @ViewInject(R.id.image_picture)
    ImageView picture;

    /**
     * 表格头部
     **/
    @ViewInject(R.id.header_horizontal)
    SyncHorizontalScrollView mHeaderHorizontal;
    /**
     * 表格头部
     **/
    @ViewInject(R.id.data_horizontal)
    SyncHorizontalScrollView mDataHorizontal;
    /**
     * 列表数据
     **/
    @ViewInject(R.id.list_data)
    NoscrollListView mDataList;

    private DataAdapter mDataAdapter;

    private int screenW;
    private int screenH;

    private byte[] datas;
    private Uri imageUri;
    private String imageData;

    /**
     * 蓝牙地址
     **/
    private String xiqopiaoAddress;

    private ArrayList<InputDataInfo> itemDataList = new ArrayList<InputDataInfo>();
    private DepartmentDBWrapper departmentDB;
    private Cursor cursor;
    private StatusBox statusBox;
    /**
     * 签收人
     **/
    private String recipients;
    /**
     * 收件员
     **/
    private String deliverEmpCode;

    private ServiceProdPriceDBWrapper serviceProdPriceDB;

    private Context mContext;
    private Bitmap signBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_signpieces);
        ViewUtils.inject(this);
        mContext = SendPiecesSignActivity.this;
        initView();
    }

    protected void onStart() {
        initComps();
        super.onStart();
        BatteryReceiver.getInstant(this).addHeraer(this);
        isConnect();
    }

    private void isConnect() {
        Cursor cursor = BluetoothDBWrapper.getInstance(getApplication()).rawQueryRank("No1");
        do {
            if (!cursor.moveToNext()) {
                cursor.close();
                return;
            }
            xiqopiaoAddress = cursor.getString(cursor.getColumnIndex("address"));
        } while (true);
    }

    protected void onStop() {
        super.onStop();
        BatteryReceiver.getInstant(this).delHeraer(this);
    }

    private void initComps() {
        mUserName.setText((new StringBuilder(String.valueOf(MyApp.mEmpName))).append(MyApp.mDeptName).toString());
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

    private void initView() {
        mTitleBase.setText("派件");
        mEdtCourier.setText(MyApp.mEmpCode);

        // Header 随着 Data 一起滑动
        mDataHorizontal.setScrollView(mHeaderHorizontal);
        mHeaderHorizontal.setScrollView(mDataHorizontal);

        mDataAdapter = new DataAdapter();
        mDataList.setAdapter(mDataAdapter);

        // 长按扫二维码
        mEdtWaybillNumber.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent openCameraIntent = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                return false;
            }
        });

        // 返回
        mBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 签收上传
        mUpLoad.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isCheck()) return;

                recipients = mEdtSignforer.getText().toString();
                deliverEmpCode = mEdtCourier.getText().toString();
                if (TextUtils.isEmpty(recipients)) {
                    Toast.makeText(getApplicationContext(), "签收人不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(deliverEmpCode)) {
                    Toast.makeText(getApplicationContext(), "收件员不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                upLoad();
            }
        });

        // 签收上传 打印
        mSignfor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCheck()) return;

                recipients = mEdtSignforer.getText().toString();
                deliverEmpCode = mEdtCourier.getText().toString();
                if (TextUtils.isEmpty(recipients)) {
                    Toast.makeText(getApplicationContext(), "签收人不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(deliverEmpCode)) {
                    Toast.makeText(getApplicationContext(), "收件员不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (itemDataList.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "无订单", Toast.LENGTH_SHORT).show();
                    return;
                }

                statusBox = new StatusBox(mContext, mSignfor);
                statusBox.Show("正在打印 ...");
                if (!BluetoothUtil.OpenPrinter(mContext, xiqopiaoAddress)) {
                    statusBox.Close();
                    return;
                }
                upLoad();

                BluetoothPrintTool.printWaybillList(itemDataList, deliverEmpCode, signBitmap, getApplicationContext());
                zpSDK.zp_close();
                statusBox.Close();
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // 手写签名
        mCheckSgin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 检验是否有要签收的数据，格式与拍照签收一样
                if (itemDataList.size() > 0) {

                    // if (itemData != null && itemData.imagePath != null) {
                    // Toast.makeText(mContext, "签收联已上传,请勿重复上传数据", 0).show();
                    // return;
                    // }

                    // 调出手写窗口
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    screenW = dm.widthPixels;
                    screenH = dm.heightPixels;

                    WritePadDialog writeTabletDialog = new WritePadDialog(mContext, new DialogListener() {
                        @Override
                        public void refreshActivity(Object object) {
                            Bitmap mSignBitmap = (Bitmap) object;
                            signBitmap = mSignBitmap;
                            datas = Bitmap2Bytes(mSignBitmap);
                            picture.setImageBitmap(mSignBitmap);// 将图片显示在ImageView里
                            imageData = Base64.encodeToString(datas, Base64.DEFAULT);
                            upLoadImage();
                        }
                    }, screenW, screenH);
                    writeTabletDialog.show();

                } else {
                    Toast.makeText(mContext, "请查询需要签收的数据!", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        // 拍照签收
        tackePhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemDataList.size() > 0) {

                    // if (itemData != null && itemData.imagePath != null) {
                    // Toast.makeText(mContext, "签收联已上传,请勿重复上传数据", 0).show();
                    // return;
                    // }

                    // 创建File对象，用于保存图片
                    File outputImage = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    try {
                        outputImage.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageUri = Uri.fromFile(outputImage);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.meida.cation.IMAGE_CAPTURE"

                    intent.putExtra("outputX", 600);
                    intent.putExtra("outputY", 300);
                    intent.putExtra("scale", true);
                    intent.putExtra("return-data", false);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    startActivityForResult(intent, 1);

                } else {
                    Toast.makeText(mContext, "请查询需要签收的数据!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        // 查询
        mBtnWaybillQuery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                queryWaybill();
            }

        });
    }

    class DataAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemDataList == null ? 0 : itemDataList.size();
        }

        @Override
        public InputDataInfo getItem(int position) {
            return itemDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sendpieces_sign_data, null);
                holder.waybill_number = (TextView) convertView.findViewById(R.id.item_delivery_waybill_number);
                holder.empCode = (TextView) convertView.findViewById(R.id.item_quire_upload_empCode);
                holder.quantity = (TextView) convertView.findViewById(R.id.item_quire_upload_quantity);
                holder.allmoney = (TextView) convertView.findViewById(R.id.item_quire_upload_allmoney);
                holder.name = (TextView) convertView.findViewById(R.id.item_quire_upload_name);
                holder.weight = (TextView) convertView.findViewById(R.id.item_quire_upload_weight);
                holder.waybillMoney = (TextView) convertView.findViewById(R.id.item_quire_upload_waybillMoney);
                holder.collection = (TextView) convertView.findViewById(R.id.item_quire_upload_collection);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            InputDataInfo data = itemDataList.get(position);

            // 运单号
            holder.waybill_number.setText(data.waybillNo + "");

            // 收件人
            holder.empCode.setText(data.addresseeContName + "");

            // 件数
            holder.quantity.setText(data.quantity + "");

            // 合计
            // 计算中转费
            Cursor cursor = DepartmentDBWrapper.getInstance(getApplication()).rawQueryRank(MyApp.mDeptCode);
            if (cursor.moveToNext()) {
                int mtransferFee = cursor.getColumnIndex("transferFee");
                if (mtransferFee > -1) {
                    String transferFeeTxt = cursor.getString(cursor.getColumnIndex("transferFee"));
                    String distCode = cursor.getString(cursor.getColumnIndex("districtCode"));// 发货网点
                    if (null != distCode && "371".equals(distCode)) {
                        if (transferFeeTxt != null && !"B1".equals(data.productTypeCode)) {
                            if (Double.valueOf(data.meterageWeightQty) > 5) {
                                Double t = 3 + (Double.valueOf(data.meterageWeightQty) - 5) * 0.1;
                                data.transferFee = String.valueOf(Math.round(t));
                            } else {
                                data.transferFee = String.valueOf(3);
                            }
                        }
                    } else {
                        if (transferFeeTxt != null && !"B1".equals(data.productTypeCode)) {
                            if (Double.valueOf(data.meterageWeightQty) > 5) {
                                Double t = 5 + (Double.valueOf(data.meterageWeightQty) - 5) * 0.2;
                                data.transferFee = String.valueOf(Math.round(t));
                            } else {
                                data.transferFee = String.valueOf(5);
                            }

                        }
                    }
                }
            }

            // 中转费
            itemDataList.get(position).transferFee = itemDataList.get(position).transferFee == null ? "0.0" : data
                    .transferFee;

            departmentDB = DepartmentDBWrapper.getInstance(getApplication());
            cursor = departmentDB.rawQueryRank(data.sourceZoneCode);// 查询发货网点
            if (cursor.moveToNext()) {
                data.sourcedistrictCode = cursor.getString(cursor.getColumnIndex("districtCode"));
                Cursor mCursor = DistrictDBWrapper.getInstance(getApplication()).rawQueryRank(data.sourcedistrictCode);
                if (mCursor.moveToNext()) {
                    data.provinceCode = mCursor.getString(mCursor.getColumnIndex("provinceCode"));
                }
            }


            double d = 0;
            double d1;
            double d2;
            double d3;
            double d4;
            double d5;
            double d6;
            double d7;
            double d8;
            double d9;
            double d10;
            double all;

            // 计算合计
            if ("1".equals(data.paymentTypeCode)) {// 寄付
                d = Double.parseDouble(data.goodsChargeFee == null ? "0.0" : data.goodsChargeFee);
                d8 = Double.parseDouble(data.warehouseFee == null ? "0.0" : data.warehouseFee);
                d10 = Double.parseDouble(data.transferFee == null ? "0.0" : data.transferFee);
                all = Double.valueOf(d + d8 + d10);
            } else {// 2-到付
                d = Double.parseDouble(data.waybillFee == null ? "0.0" : data.waybillFee);
                d1 = Double.parseDouble(data.insuranceFee == null ? "0.0" : data.insuranceFee);
                d2 = Double.parseDouble(data.signBackFee == null ? "0.0" : data.signBackFee);
                d3 = Double.parseDouble(data.deboursFee == null ? "0.0" : data.deboursFee);
                d4 = Double.parseDouble(data.waitNotifyFee == null ? "0.0" : data.waitNotifyFee);
                d5 = Double.parseDouble(data.deliverFee == null ? "0.0" : data.deliverFee);
                if (MyUtils.isHenan(data.provinceCode)) {
                    d6 = Double.parseDouble(data.chargeAgentFee == null ? "0.0" : data.chargeAgentFee);
                } else {
                    d6 = Double.parseDouble(data.goodsChargeFee == null ? "0.0" : data.goodsChargeFee);
                }
                d7 = data.fuelServiceFee == null ? 0.0 : data.fuelServiceFee;
                d8 = Double.parseDouble(data.warehouseFee == null ? "0.0" : data.warehouseFee);
                d9 = Double.parseDouble(data.backCargoFee == null ? "0.0" : data.backCargoFee);
                d10 = Double.parseDouble(data.transferFee == null ? "0.0" : data.transferFee);
                all = Double.valueOf(d + d1 + d2 + d3 + d4 + d5 + d6 + d7 + d8 + d9 + d10);
            }
            String allMoney;
            if (all == 0) {
                allMoney = "0.0";
            } else {
                itemDataList.get(position).FeeCount = all;
                allMoney = data.FeeCount + "";
            }

            holder.allmoney.setText(allMoney + "");

            // 货物名
            holder.name.setText(data.consName + "");

            // 重量
            holder.weight.setText(data.meterageWeightQty + "");

            // 运费
            holder.waybillMoney.setText(data.waybillFee + "");

            // 代收
            holder.collection.setText(data.goodsChargeFee == null ? "0.0" : data.goodsChargeFee);

            //
            // TODO 有一些数据需要计算
            // 1. 看一下上传的数据，有哪些需要上传，在这里获取一下，并保存；
            // 下送费 : transferFee
            // 仓管费: warehouseFee

            // 2. 将xml里面的id写进ViewHolder，并且获取，显示
            // 3. 检查两边的代码，
            // 4. 对照那边的流程，完了再看这边的代码。

            return convertView;
        }

        class ViewHolder {
            TextView waybill_number;
            TextView empCode;
            TextView quantity;
            TextView allmoney;
            TextView name;
            TextView weight;
            TextView waybillMoney;
            TextView collection;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            // 二维码扫描返回
            if (requestCode == 0) {
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("result");
                mEdtWaybillNumber.setText(scanResult);
            }
            // 相机拍照返回
            if (requestCode == 1) {
                // Bitmap bitmap = (Bitmap) bundle.get("data");//
                // 获取相机返回的数据，并转换为Bitmap图片格式
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    Bitmap viewBitMap = scaleBitmap(bitmap);
                    datas = Bitmap2Bytes(viewBitMap);
                    picture.setImageBitmap(viewBitMap);// 将图片显示在ImageView里
                    imageData = Base64.encodeToString(datas, Base64.DEFAULT);
                    upLoadImage();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
            }
        }
    }

    public Bitmap getUrlImage(String url, String impgePath) {
        Bitmap img = null;
        try {
            impgePath = impgePath.replace("\\", "/");
            URL picurl = new URL(url + impgePath);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) picurl.openConnection();
            conn.setConnectTimeout(6000);// 设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);// 不缓存
            conn.connect();
            InputStream is = conn.getInputStream();// 获得图片的数据流
            img = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }

    private Bitmap scaleBitmap(Bitmap src) {
        int width = src.getWidth();// 原来尺寸大小
        int height = src.getHeight();
        final float destSize = 550;// 缩放到这个大小,你想放大多少就多少

        // 图片缩放比例，新尺寸/原来的尺寸
        float scaleWidth = ((float) destSize) / width;
        float scaleHeight = ((float) destSize) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // 返回缩放后的图片
        return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
    }

    private Bitmap scaleSignBitmap(Bitmap src) {
        int width = src.getWidth();// 原来尺寸大小
        int height = src.getHeight();
        final float widthSize = 200;// 缩放到这个大小,你想放大多少就多少
        final float destSize = 100;// 缩放到这个大小,你想放大多少就多少

        // 图片缩放比例，新尺寸/原来的尺寸
        float scaleWidth = ((float) widthSize) / width;
        float scaleHeight = ((float) destSize) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // 返回缩放后的图片
        return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
    }


    /**
     * 是否验收
     */
    private boolean isCheck() {
        if (itemDataList.size() <= 0) {
            Toast.makeText(getApplicationContext(), "无订单", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (int i = 0; i < itemDataList.size(); i++) {
            String auditedFlg = itemDataList.get(i).auditedFlg;

            if (MyUtils.isEmpty(auditedFlg)) {
                auditedFlg = "1";
            }

            if (!"1".equals(auditedFlg)) {
                Toast.makeText(getApplicationContext(), itemDataList.get(i).waybillNo + " 未审核，不能签收", Toast
                        .LENGTH_SHORT).show();
                return false;
            }
        }

        return true;

    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        return baos.toByteArray();
    }

    private void clearData() {
        finish();
        startActivity(new Intent(mContext, SendPiecesSignActivity.class));
    }

    /*
     * 查询单号
     */
    private void queryWaybill() {
        String waybillNo = mEdtWaybillNumber.getText().toString();
        String checkSign = mEdtWaybillCheckSign.getText().toString();
        if (waybillNo.length() == 12) {
            String s = waybillNo.substring(0, 3);
            String s2 = waybillNo.substring(3, 11);
            String s1 = waybillNo.substring(11, 12);
            s2 = (new StringBuilder(String.valueOf(CheckCode.genCheckCode(s2)))).toString();

            if (s.equals("333") || s.equals("121")) {
                mEdtWaybillNumber.setText("");
                Toast.makeText(this, "扫描码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                VibratorUtil.Vibrate(this, 500L);
                PlayRaws.PlayError(this);
                return;
            }
            if (s1.equals(s2)) {

                // 检验验证码
                if (TextUtils.isEmpty(checkSign) || checkSign.length() != 6) {
                    Toast.makeText(this, "验证码格式有误", Toast.LENGTH_SHORT).show();
                    return;
                }

                NetworkInfo networkinfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
                        .getActiveNetworkInfo();
                if (networkinfo != null && networkinfo.isConnected()) {
                    HttpUtils httpUtils = new HttpUtils();
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("deliverEmpCode", "");
                    params.addBodyParameter("waybillNo", waybillNo);
                    params.addBodyParameter("checkSign", checkSign);
                    params.addBodyParameter("deptCode", MyApp.mDeptCode);
                    params.addBodyParameter("devId", MyApp.mSession);

                    httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.queryWaybillNoTest, params, new
                            RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            Toast.makeText(getApplicationContext(), "网络异常，请稍候再试", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            String result = arg0.result;
                            Gson gson = new Gson();
                            I_QueryWaybill i_Send = gson.fromJson(result, I_QueryWaybill.class);

                            if (i_Send == null || i_Send.getPdaWaybillList() == null || i_Send.getPdaWaybillList()
                                    .size() <= 0) {
                                if ("waitNotifyFee is not null!".equals(i_Send.error)) {
                                    Toast.makeText(getApplicationContext(), "此运单为等通知派送货物!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "数据为空", Toast.LENGTH_SHORT).show();
                                }
                                return;
                            }

                            if (i_Send.success) {
                                List<InputDataInfo> items = i_Send.getPdaWaybillList();
                                if (items.size() <= 0) {
                                    Toast.makeText(getApplicationContext(), "数据为空", Toast.LENGTH_SHORT).show();
                                }

                                // 签收人如果没有填写，将第一个设置给他
                                if (TextUtils.isEmpty(recipients)) {
                                    mEdtSignforer.setText(items.get(0).addresseeContName);
                                    recipients = items.get(0).addresseeContName;
                                }

                                // 去除重复的
                                for (InputDataInfo my : itemDataList) {
                                    for (InputDataInfo target : items) {
                                        if (my.waybillNo.equals(target.waybillNo)) {
                                            items.remove(target);
                                            Toast.makeText(getApplicationContext(), "订单：" + target.waybillNo + " " +
                                                    "已存在", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (!my.addresseeContName.equals(target.addresseeContName)) {
                                            Toast.makeText(getApplicationContext(), "只能签收同一个人的快件", Toast
                                                    .LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                }

                                try {
                                    itemDataList.addAll(items);
                                    mDataAdapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    Toast.makeText(mContext, "数据异常", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(mContext, i_Send.error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    return;
                } else {
                    showT("网络未连接");
                    return;
                }
            } else {
                showT("运单号输入错误");
                return;
            }
        } else {
            showT("运单号输入错误");
            return;
        }
    }

    /**
     * 上传订单
     **/
    private void upLoad() {
        try {
            JSONArray jsonarray = new JSONArray();

            for (int i = 0; i < itemDataList.size(); i++) {
                InputDataInfo itemData = itemDataList.get(i);

                JSONObject jsonobject = new JSONObject();
                jsonobject.put("operTypeCode", "13");
                jsonobject.put("deliverEmpCode", deliverEmpCode);
                jsonobject.put("waybillNo", itemData.waybillNo);
                jsonobject.put("strOperTm", itemData.consignedTm);
                jsonobject.put("inputType", itemData.inputType);
                jsonobject.put("carCode", "");
                jsonobject.put("carNo", "");
                jsonobject.put("lineCode", "");
                jsonobject.put("stayWayCode", "");
                jsonobject.put("lockedCarCode", "");
                jsonobject.put("srcCarNo", "");
                jsonobject.put("bagCageNo", itemData.addresseeContName);
                jsonobject.put("deptCode", MyApp.mDeptCode);
                jsonobject.put("operEmpCode", MyApp.mEmpCode);
                jsonobject.put("macNo", MyApp.mSession);
                jsonobject.put("upLoadEmpCode", MyApp.mEmpCode);
                jsonobject.put("acquisitionInfo", "");
                if (TextUtils.isEmpty(itemData.transferFee) || "null".equals(itemData.transferFee)) {
                    itemData.transferFee = "0.0";
                }
                jsonobject.put("transferFee", itemData.transferFee);
                if (TextUtils.isEmpty(itemData.warehouseFee) || "null".equals(itemData.warehouseFee)) {
                    itemData.transferFee = "0.0";
                }
                jsonobject.put("warehouseFee", itemData.warehouseFee);
                jsonarray.put(jsonobject);
            }

            HttpUtils httpUtils = new HttpUtils();
            RequestParams params = new RequestParams();
            params.addBodyParameter("deptCode", MyApp.mDeptCode);
            params.addBodyParameter("empCode", MyApp.mEmpCode);
            params.addBodyParameter("devId", MyApp.mSession);
            params.addBodyParameter("jsonData", jsonarray.toString());

            httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.SigninWaybill, params, new
                    RequestCallBack<String>() {

                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    Toast.makeText(getApplicationContext(), "网络连接失败,请稍后再试", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(ResponseInfo<String> arg0) {
                    String result = arg0.result;
                    I_PdaUploadData uploadData = new Gson().fromJson(result, I_PdaUploadData.class);

                    if (uploadData.isSuccess()) {
                        Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                        clearData();
                    } else {
                        Toast.makeText(getApplicationContext(), uploadData.getError(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传图片
     */
    private void upLoadImage() {
        JSONArray jsonarray = new JSONArray();
        JSONObject jsonobject = new JSONObject();
        try {
            jsonobject.put("imageData", imageData);
            if (itemDataList.size() <= 0) {
                Toast.makeText(mContext, "无订单", Toast.LENGTH_SHORT).show();
                return;
            }

            // if (itemData != null && itemData.imagePath != null) {
            // Toast.makeText(SendPiecesActivity.this, "签收联已上传,请勿重复上传数据",
            // 0).show();
            // return;
            // }

            // 将多个订单以“，”连接，最后一个没有 逗号
            StringBuilder waybillNos = new StringBuilder();
            for (int i = 0; i < itemDataList.size(); i++) {
                InputDataInfo data = itemDataList.get(i);
                if (i == (itemDataList.size() - 1)) {
                    waybillNos.append(data.waybillNo);
                } else {
                    waybillNos.append(data.waybillNo).append(",");
                }
            }

            jsonobject.put("waybillNo", waybillNos.toString());
            jsonarray.put(jsonobject);

            HttpUtils httpUtils = new HttpUtils();
            RequestParams params = new RequestParams();
            params.addBodyParameter("deptCode", MyApp.mDeptCode);
            params.addBodyParameter("empCode", MyApp.mEmpCode);
            params.addBodyParameter("devId", MyApp.mSession);
            params.addBodyParameter("jsonData", jsonarray.toString());

            httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.SigninWaybillImageUpLoad, params, new
                    RequestCallBack<String>() {

                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    Toast.makeText(getApplicationContext(), "网络连接失败,请稍后再试", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(ResponseInfo<String> arg0) {
                    String result = arg0.result;
                    I_PdaUploadData uploadData = new Gson().fromJson(result, I_PdaUploadData.class);

                    if (uploadData.isSuccess()) {
                        Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                        //clearData();
                    } else {
                        Toast.makeText(getApplicationContext(), uploadData.getError(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
