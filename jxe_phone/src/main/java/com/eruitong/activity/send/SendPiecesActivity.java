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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.StringTokenizer;

import zpSDK.zpSDK.zpSDK;

/**
 * 派件签收
 *
 * @author Administrator
 */
public class SendPiecesActivity extends BaseActivity implements Hearer {

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

    @ViewInject(R.id.edt_delivery_waybill_number)
    EditText mEdtWaybillNumber;
    @ViewInject(R.id.btn_delivery_waybill_query)
    Button mBtnWaybillQuery;
    /**
     * 标准派送费
     **/
    @ViewInject(R.id.edt_deliver_commission)
    EditText mEdtDeliverCommission;
    /**
     * 派送费
     **/
    @ViewInject(R.id.edt_down_deliverFee)
    EditText mEdtDownDeliverFee;

    /**
     * 标准派送费
     **/
    @ViewInject(R.id.edt_delivery_signforer)
    EditText mEdtSignforer;
    /**
     * 下送费
     **/
    @ViewInject(R.id.edt_delivery_courier)
    EditText mEdtCourier;


    /**
     * 运单
     **/
    @ViewInject(R.id.item_quire_upload_waybillnumber)
    TextView mTxtWaybill;
    /**
     * 收派
     **/
    @ViewInject(R.id.item_quire_upload_empCode)
    TextView mTxtEmpCode;
    /**
     * 发货网点
     **/
    @ViewInject(R.id.item_quire_upload_sourceZoneCode)
    TextView mTxtSourceZone;
    /**
     * 收货网点
     **/
    @ViewInject(R.id.item_quire_upload_destZoneCode)
    TextView mTxtDestZone;
    /**
     * 寄方
     **/
    @ViewInject(R.id.item_quire_upload_consignorContName)
    TextView mTxtConsignorContName;
    /**
     * 寄电
     **/
    @ViewInject(R.id.item_quire_upload_consignorPhone)
    TextView mTxtConsignorContNumber;
    /**
     * 收方
     **/
    @ViewInject(R.id.item_quire_upload_addresseeContName)
    TextView mTxtAddresseeContName;
    /**
     * 收电
     **/
    @ViewInject(R.id.item_quire_upload_addresseePhone)
    TextView mTxtAddresseePhone;
    /**
     * 代收
     **/
    @ViewInject(R.id.item_quire_upload_collection)
    TextView mTxtCollection;
    /**
     * 保价
     **/
    @ViewInject(R.id.item_quire_upload_insured)
    TextView mTxtInsured;

    /**
     * 派送费
     **/
    @ViewInject(R.id.item_quire_upload_deliverFee)
    TextView mTxtDeliverFee;

    /**
     * 运费
     **/
    @ViewInject(R.id.item_quire_upload_waybillMoney)
    TextView mTxtWaybillMoney;

    /**
     * 原返费
     **/
    @ViewInject(R.id.item_quire_upload_backFee)
    TextView mTxtBackFee;
    /**
     * 仓管费
     **/
    @ViewInject(R.id.item_quire_upload_storeFee)
    TextView mTxtStoreFee;
    /**
     * 中转费
     **/
    @ViewInject(R.id.item_quire_upload_transferFee)
    TextView mTxtTransferFee;
    /**
     * 垫付费
     **/
    @ViewInject(R.id.item_quire_upload_deboursFee)
    TextView mTxtDeboursFee;
    /**
     * 等通知费
     **/
    @ViewInject(R.id.item_quire_upload_waitNotifyFee)
    TextView mTxtWaitNotifyFee;
    /**
     * 上门费
     **/
    @ViewInject(R.id.item_quire_upload_fuelServiceFee)
    TextView mTxtFuelServiceFee;
    /**
     * 保价费
     **/
    @ViewInject(R.id.item_quire_upload_insuranceFee)
    TextView mTxtInsuranceFee;

    /**
     * 合计
     **/
    @ViewInject(R.id.item_quire_upload_allmoney)
    TextView mTxtAllmoney;
    /**
     * 货物
     **/
    @ViewInject(R.id.item_quire_upload_consName)
    TextView mTxtConsName;
    /**
     * 件数
     **/
    @ViewInject(R.id.item_quire_upload_quantity)
    TextView mTxtQuantity;
    /**
     * 重量
     **/
    @ViewInject(R.id.item_quire_upload_weight)
    TextView mTxtWeight;
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

    @ViewInject(R.id.btn_take_photo)
    Button tackePhoto;
    @ViewInject(R.id.image_picture)
    ImageView picture;

    private byte[] datas;
    private Uri imageUri;
    private String imageData;

    /**
     * 蓝牙地址
     **/
    private String xiqopiaoAddress;

    private InputDataInfo itemData;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_pieces);
        ViewUtils.inject(this);
        initView();
    }

    private void initView() {
        mTitleBase.setText("派件");
        mEdtWaybillNumber.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent openCameraIntent = new Intent(SendPiecesActivity.this, CaptureActivity.class);
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
                    showT("签收人不能为空");
                    return;
                }

                if (TextUtils.isEmpty(deliverEmpCode)) {
                    showT("收件员不能为空");
                    return;
                }

                if (TextUtils.isEmpty(itemData.waybillNo)) {
                    showT("运单号不能为空");
                    return;
                } else {
                    upLoad();
                    return;
                }
            }
        });

        // 签收上传 打印
        mSignfor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCheck()) return;

                recipients = mEdtSignforer.getText().toString();
                deliverEmpCode = mEdtCourier.getText().toString();
                itemData.downDeliverFee = mEdtDownDeliverFee.getText().toString();
                if (TextUtils.isEmpty(recipients)) {
                    showT("签收人不能为空");
                    return;
                }
                if (TextUtils.isEmpty(deliverEmpCode)) {
                    showT("收件员不能为空");
                    return;
                }
                if (TextUtils.isEmpty(itemData.waybillNo)) {
                    showT("运单号不能为空");
                    return;
                }

                statusBox = new StatusBox(SendPiecesActivity.this, mSignfor);
                statusBox.Show("正在打印 ...");
                if (!BluetoothUtil.OpenPrinter(SendPiecesActivity.this, xiqopiaoAddress)) {
                    statusBox.Close();
                    return;
                }
                upLoad();

                BluetoothPrintTool.printWaybill(itemData, deliverEmpCode);
                zpSDK.zp_close();
                statusBox.Close();
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

	/*	// 签收上传 打印
                tackePhoto.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.meida.cation
						.IMAGE_CAPTURE"
						startActivityForResult(intent, 1);
						
					}
				});

				
				
		// 查询
		mBtnWaybillQuery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				queryWaybill();
			}

		});
		
		serviceProdPriceDB = ServiceProdPriceDBWrapper.getInstance(getApplication());
	}
 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  
        // TODO Auto-generated method stub  
        super.onActivityResult(requestCode, resultCode, data);  
        if (resultCode == Activity.RESULT_OK) {        
            Bundle bundle = data.getExtras();  
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
           // bitmap.createScaledBitmap(bitmap, 200, 200, true);
           Bitmap viewBitMap = scaleBitmap(bitmap);
            byte[] datas=Bitmap2Bytes(viewBitMap);
  
            try {  
                imageData = Base64.encodeToString(datas, Base64.DEFAULT);
                picture.setImageBitmap(viewBitMap);// 将图片显示在ImageView里  
                upLoadImage();
            } catch (Exception e) {  
                e.printStackTrace();  
            } finally {}  
           
            
        }  
    }  */


        // 签收上传 打印
        tackePhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemData != null && itemData.waybillNo != null) {
                    if (itemData != null && itemData.imagePath != null) {
                        showT("签收联已上传,请勿重复上传数据");
                        return;
                    } else {
                        //创建File对象，用于保存图片
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
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.meida.cation
                        // .IMAGE_CAPTURE"

                        intent.putExtra("outputX", 600);
                        intent.putExtra("outputY", 300);
                        intent.putExtra("scale", true);
                        intent.putExtra("return-data", false);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                        startActivityForResult(intent, 1);
                    }
                } else {
                    showT("请查询需要签收的数据!");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("result");
                mEdtWaybillNumber.setText(scanResult);
            }
            if (requestCode == 1) {
                // Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
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

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        return baos.toByteArray();
    }

    private Bitmap scaleBitmap(Bitmap src) {
        int width = src.getWidth();//原来尺寸大小
        int height = src.getHeight();
        final float destSize = 550;//缩放到这个大小,你想放大多少就多少

        //图片缩放比例，新尺寸/原来的尺寸
        float scaleWidth = ((float) destSize) / width;
        float scaleHeight = ((float) destSize) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        //返回缩放后的图片
        return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
    }
        /*
        super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {  
		case 1:
			if(resultCode == RESULT_OK){
				try {
				//String	picPath = data.getStringExtra("photo_path");
					Bitmap	bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
					picture.setImageBitmap(bitmap);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, 2);
			}
			break;
        case 2:
        	if(resultCode == RESULT_OK){
        		try {
					Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
					picture.setImageBitmap(bitmap);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        	}
		default:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("result");
				mEdtWaybillNumber.setText(scanResult);
			}
			break;
		}
	}*/


    /**
     * 是否验收
     */
    private boolean isCheck() {
        if (itemData == null) {
            showT("无订单");
            return false;
        }

        if (MyUtils.isEmpty(itemData.auditedFlg)) {
            itemData.auditedFlg = "1";
        }
        if ("1".equals(itemData.auditedFlg)) {
            return true;
        } else {
            showT("未审核，不能签收");
            return false;
        }
    }

    /**
     * 上传订单
     **/
    private void upLoad() {

        JSONArray jsonarray = new JSONArray();
        JSONObject jsonobject = new JSONObject();

        itemData.downDeliverFee = mEdtDownDeliverFee.getText().toString();
        try {
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
            jsonobject.put("bagCageNo", recipients);
            jsonobject.put("deptCode", MyApp.mDeptCode);
            jsonobject.put("operEmpCode", MyApp.mEmpCode);
            jsonobject.put("macNo", MyApp.mSession);
            jsonobject.put("upLoadEmpCode", MyApp.mEmpCode);
            jsonobject.put("acquisitionInfo", "");
            jsonarray.put(jsonobject);

            HttpUtils httpUtils = new HttpUtils();
            RequestParams params = new RequestParams();
            params.addBodyParameter("deptCode", MyApp.mDeptCode);
            params.addBodyParameter("empCode", MyApp.mEmpCode);
            params.addBodyParameter("devId", MyApp.mSession);
            if (itemData.transferFee == null || "null".equals(itemData.transferFee)) {
                itemData.transferFee = "0.0";
            }
            params.addBodyParameter("transferFee", itemData.transferFee);
            params.addBodyParameter("warehouseFee", itemData.warehouseFee);
            params.addBodyParameter("downDeliverFee", itemData.downDeliverFee);

            params.addBodyParameter("jsonData", jsonarray.toString());

            httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.SigninWaybillTest, params, new
                    RequestCallBack<String>() {

                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    showT("网络连接失败,请稍后再试");
                }

                @Override
                public void onSuccess(ResponseInfo<String> arg0) {
                    String result = arg0.result;
                    I_PdaUploadData uploadData = new Gson().fromJson(result, I_PdaUploadData.class);

                    if (uploadData.isSuccess()) {
                        showT("上传成功");
                    } else {
                        showT(uploadData.getError());
                    }

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传图片
     **/
    private void upLoadImage() {

        JSONArray jsonarray = new JSONArray();
        JSONObject jsonobject = new JSONObject();
        try {
            jsonobject.put("imageData", imageData);
            if (itemData == null && null == itemData.waybillNo) {
                showT("请查询需要签收的数据");
                return;
            }
            if (itemData != null && itemData.imagePath != null) {
                showT("签收联已上传,请勿重复上传数据");
                return;
            }
            jsonobject.put("waybillNo", itemData.waybillNo);
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
                    showT("网络连接失败,请稍后再试");
                }

                @Override
                public void onSuccess(ResponseInfo<String> arg0) {
                    String result = arg0.result;
                    I_PdaUploadData uploadData = new Gson().fromJson(result, I_PdaUploadData.class);

                    if (uploadData.isSuccess()) {

                        showT("上传成功");
                    } else {
                        showT(uploadData.getError());
                    }

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询单号
     **/
    private void queryWaybill() {
        String waybillNo = mEdtWaybillNumber.getText().toString();
        if (waybillNo.length() == 12) {
            String s = waybillNo.substring(0, 3);
            String s2 = waybillNo.substring(3, 11);
            String s1 = waybillNo.substring(11, 12);
            s2 = (new StringBuilder(String.valueOf(CheckCode.genCheckCode(s2)))).toString();

            if (s.equals("333") || s.equals("121")) {
                mEdtWaybillNumber.setText("");
                showT("扫描码不正确，请重新输入");
                VibratorUtil.Vibrate(this, 500L);
                PlayRaws.PlayError(this);
                return;
            }
            if (s1.equals(s2)) {
                InputDataInfo data = new InputDataInfo();
                mEdtSignforer.getEditableText().clear();
                clearTxt();
                NetworkInfo networkinfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
                        .getActiveNetworkInfo();
                if (networkinfo != null && networkinfo.isConnected()) {
                    HttpUtils httpUtils = new HttpUtils();
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("deliverEmpCode", MyApp.mEmpCode);
                    params.addBodyParameter("waybillNo", waybillNo);
                    params.addBodyParameter("deptCode", MyApp.mDeptCode);
                    params.addBodyParameter("devId", MyApp.mSession);

                    httpUtils.send(HttpMethod.POST, MyApp.mPathServerURL + Conf.queryWaybillNoTest, params, new
                            RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            showT("网络异常，请稍候再试");
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            String result = arg0.result;
                            Gson gson = new Gson();
                            I_QueryWaybill i_Send = gson.fromJson(result, I_QueryWaybill.class);

                            if (i_Send == null || i_Send.getPdaWaybillList() == null || i_Send.getPdaWaybillList()
                                    .size() <= 0) {
                                if ("waitNotifyFee is not null!".equals(i_Send.error)) {
                                    showT("此运单为等通知派送货物!");
                                } else {
                                    showT("数据为空");
                                }
                                return;
                            }

                            if (i_Send.success) {
                                itemData = i_Send.getPdaWaybillList().get(0);

                                setOrderView(itemData);

                            } else {
                                showT(i_Send.error);
                            }
                        }

                    });

                    return;
                } else {
                    showT("网络未连接 ");
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

    private void clearTxt() {
        mTxtWaybill.setText("");
        mTxtEmpCode.setText("");
        mTxtConsignorContName.setText("");
        mTxtConsignorContNumber.setText("");
        mTxtAddresseeContName.setText("");
        mTxtAddresseePhone.setText("");
        mTxtConsName.setText("");

        mTxtWeight.setText("");
        mTxtQuantity.setText("");
        mTxtWaybillMoney.setText("");

        mTxtBackFee.setText("");
        mTxtStoreFee.setText("");
        mTxtDeliverFee.setText("");

        mTxtCollection.setText("");
        mTxtInsured.setText("");
        mTxtAllmoney.setText("");

        mTxtSourceZone.setText("");
        mTxtDestZone.setText("");
    }

    private void setOrderView(InputDataInfo data) {
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

        mEdtSignforer.setText(data.addresseeContName);
        mEdtCourier.setText(MyApp.mEmpCode);

        mTxtWaybill.setText(data.waybillNo);
        mTxtEmpCode.setText(data.consigneeEmpCode);
        mTxtConsignorContName.setText(data.consignorContName);

        if (MyUtils.isEmpty(data.consignorMobile)) {// 发货人
            mTxtConsignorContNumber.setText(data.consignorPhone);
        } else {
            mTxtConsignorContNumber.setText(data.consignorMobile);
        }

        mTxtConsName.setText(data.consName);
        mTxtAddresseeContName.setText(data.addresseeContName);

        mTxtBackFee.setText(data.backCargoFee == null ? "0.0" : data.backCargoFee);
        mTxtStoreFee.setText(data.warehouseFee == null ? "0.0" : data.warehouseFee);
        mTxtDeliverFee.setText(data.deliverFee == null ? "0.0" : data.deliverFee);

        mTxtInsured.setText(data.insuranceAmount == null ? "0.0" : data.insuranceAmount);// 保价
        mTxtCollection.setText(data.goodsChargeFee == null ? "0.0" : data.goodsChargeFee);// 代收

        mTxtDeboursFee.setText(data.deboursFee == null ? "0.0" : data.deboursFee);//保价费
        mTxtWaitNotifyFee.setText(data.waitNotifyFee == null ? "0.0" : data.waitNotifyFee);//等通知

        mTxtFuelServiceFee.setText(String.valueOf(data.fuelServiceFee == null ? 0.0 : data.fuelServiceFee));//上门费
        mTxtInsuranceFee.setText(data.insuranceFee == null ? "0.0" : data.insuranceFee);//保价费
        //计算中转费
        Cursor cursor = DepartmentDBWrapper.getInstance(getApplication()).rawQueryRank(MyApp.mDeptCode);
        if (cursor.moveToNext()) {
            int mtransferFee = cursor.getColumnIndex("transferFee");
            String distCode = cursor.getString(cursor.getColumnIndex("districtCode"));// 发货网点
            if (mtransferFee > -1) {
                String transferFeeTxt = cursor.getString(cursor.getColumnIndex("transferFee"));
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
        mTxtTransferFee.setText(data.transferFee == null ? "0.0" : data.transferFee);//中转费

        if (MyUtils.isEmpty(data.addresseeMobile)) {
            mTxtAddresseePhone.setText(data.addresseePhone);
        } else {
            mTxtAddresseePhone.setText(data.addresseeMobile);
        }

        mTxtWeight.setText(data.meterageWeightQty);
        mTxtQuantity.setText(String.valueOf(data.quantity));
        mTxtWaybillMoney.setText(data.waybillFee);
        mEdtDeliverCommission.setText(data.deliverCommission);
        mEdtDownDeliverFee.setText(data.downDeliverFee);

        if (data.childNoList != null) {
            data.childNoLists = new ArrayList<String>();
            StringTokenizer childList = new StringTokenizer(data.childNoList, ",");
            if (childList.hasMoreElements()) {
                String s = childList.nextToken();
                data.childNoLists.add(s);
            }
        }

        departmentDB = DepartmentDBWrapper.getInstance(getApplication());
        cursor = departmentDB.rawQueryRank(data.sourceZoneCode);// 查询发货网点
        if (cursor.moveToNext()) {
            data.outsiteName = cursor.getString(cursor.getColumnIndex("outsiteName"));
            data.sourceZoneAddress = cursor.getString(cursor.getColumnIndex("deptAddress"));// 发货地址
            String sourceDeptName = cursor.getString(cursor.getColumnIndex("deptName"));// 发货网点
            mTxtSourceZone.setText(sourceDeptName);
            itemData.sourcedistrictCode = cursor.getString(cursor.getColumnIndex("districtCode"));
        }


        cursor = departmentDB.rawQueryRank(data.destZoneCode);// 查询收货网点
        if (cursor.moveToNext()) {
            data.destZoneAddress = cursor.getString(cursor.getColumnIndex("deptAddress"));// 收货地址
            String destDeptName = cursor.getString(cursor.getColumnIndex("deptName"));// 收货网店
            mTxtDestZone.setText(destDeptName);
        }

        Cursor mCursor = DistrictDBWrapper.getInstance(getApplication()).rawQueryRank(itemData.sourcedistrictCode);
        if (mCursor.moveToNext()) {
            data.addresseeDistName = mCursor.getString(mCursor.getColumnIndex("distName"));
            data.consignorDistName = mCursor.getString(mCursor.getColumnIndex("distName"));

            itemData.provinceCode = mCursor.getString(mCursor.getColumnIndex("provinceCode"));
        }


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
        /*	if(MyUtils.isHenan(itemData.provinceCode)){
				d6 = Double.parseDouble(data.chargeAgentFee == null ? "0.0" : data.chargeAgentFee);
			}else{*/
            d6 = Double.parseDouble(data.goodsChargeFee == null ? "0.0" : data.goodsChargeFee);
            //}
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
            data.FeeCount = all;
            allMoney = data.FeeCount + "";
        }
        mTxtAllmoney.setText(allMoney + "");

//		if ("02911".equals(itemData.sourcedistrictCode)) {
//			DecimalFormat decimalformat = new DecimalFormat("0.0");
//			if (Double.parseDouble(itemData.meterageWeightQty) <= 5D) {
//				itemData.shangmenFee = "4.0";
//				itemData.waybillFee = (new StringBuilder(String.valueOf(decimalformat.format(Double
//						.parseDouble(itemData.waybillFee) - Double.parseDouble(itemData.shangmenFee))))).toString();
//			} else {
//				itemData.shangmenFee = (new StringBuilder(String.valueOf(decimalformat.format(4D + (Double
//						.parseDouble(itemData.meterageWeightQty) - 5D) * 0.10000000000000001D)))).toString();
//				itemData.waybillFee = (new StringBuilder(String.valueOf(decimalformat.format(Double
//						.parseDouble(itemData.waybillFee) - Double.parseDouble(itemData.shangmenFee))))).toString();
//
//			}
//		}
        //根据运单号去查询是否上传过签收图片
        //	querySignImge();

        if (data.imagePath != null && !"".equals(data.imagePath)) {
            Bitmap img = getUrlImage("http://copserver.jx-express.cn:8888/uploadFile/", data.imagePath);
            //	Bitmap img = getUrlImage("http://192.168.1.114/uploadFile/",data.imagePath);

            picture.setImageBitmap(img);
        }

    }


    public Bitmap getUrlImage(String url, String impgePath) {
        Bitmap img = null;
        try {
            impgePath = impgePath.replace("\\", "/");
            URL picurl = new URL(url + impgePath);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) picurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            img = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
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


	/* private void uploadFile(String path)
	  {
	    String end = "\r\n";
	    String twoHyphens = "--";
	    String boundary = "*****";
	    try
	    {
	      URL url =new URL("");
	      HttpURLConnection con=(HttpURLConnection)url.openConnection();  
	       允许Input、Output，不使用Cache 
	      con.setDoInput(true);
	      con.setDoOutput(true);
	      con.setUseCaches(false);
	       设定传送的method=POST 
	      con.setRequestMethod("POST");
	       setRequestProperty 
	      con.setRequestProperty("Connection", "Keep-Alive");
	      con.setRequestProperty("Charset", "UTF-8");
	      con.setRequestProperty("Content-Type",
	                         "multipart/form-data;boundary="+boundary);
	       设定DataOutputStream 
	      DataOutputStream ds = 
	        new DataOutputStream(con.getOutputStream());
	      ds.writeBytes(twoHyphens + boundary + end);
	      ds.writeBytes("Content-Disposition: form-data; " +
	                    "name=\"uploadfile\";filename=\"" +
	                    newName +"\"" + end);
	      ds.writeBytes(end);   

	       取得文件的FileInputStream 
	      FileInputStream fStream = new FileInputStream(path);
	       设定每次写入1024bytes 
	      int bufferSize = 1024;
	      byte[] buffer = new byte[bufferSize];

	      int length = -1;
	       从文件读取数据到缓冲区 
	      while((length = fStream.read(buffer)) != -1)
	      {
	         将数据写入DataOutputStream中 
	        ds.write(buffer, 0, length);
	      }
	      ds.writeBytes(end);
	      ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

	       close streams 
	      fStream.close();
	      ds.flush();
	       取得Response内容 
	      InputStream is = con.getInputStream();
	      int ch;
	      StringBuffer b =new StringBuffer();
	      while( ( ch = is.read() ) != -1 )
	      {
	        b.append( (char)ch );
	      }
	       将Response显示于Dialog 
	     // showDialog(b.toString().trim());
	       关闭DataOutputStream 
	      ds.close();
	    }
	    catch(Exception e)
	    {
	     // showDialog(""+e);
	    }
	  }*/
}
