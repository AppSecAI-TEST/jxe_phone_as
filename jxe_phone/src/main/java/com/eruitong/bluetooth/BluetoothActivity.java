package com.eruitong.bluetooth;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eruitong.eruitong.R;

import zpSDK.zpSDK.zpSDK;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class BluetoothActivity extends Activity {
	public static BluetoothAdapter myBluetoothAdapter;
	public String SelectedBDAddress;
	StatusBox statusBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		if (!ListBluetoothDevice())
			finish();
		Button Button1 = (Button) findViewById(R.id.button1);
		statusBox = new StatusBox(this, Button1);
		Button1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				Print1(SelectedBDAddress);
			}
		});
		Button Button2 = (Button) findViewById(R.id.button2);
		Button2.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				Print2(SelectedBDAddress);
			}
		});
		Button Button3 = (Button) findViewById(R.id.button3);
		Button3.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				Print3(SelectedBDAddress);
			}
		});
		Button Button4 = (Button) findViewById(R.id.button4);
		Button4.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				Print4(SelectedBDAddress);
			}
		});
		Button Button5 = (Button) findViewById(R.id.button5);
		Button5.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				Print5(SelectedBDAddress);
			}
		});
		Button Button6 = (Button) findViewById(R.id.button6);
		Button6.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				Print6(SelectedBDAddress);
			}
		});
		// Button Button7 = (Button) findViewById(R.id.button7);
		// Button7.setOnClickListener(new Button.OnClickListener()
		// {
		// public void onClick(View arg0)
		// {
		// if(!OpenPrinter(SelectedBDAddress))
		// {
		// return;
		// }
		// if(!zpSDK.zp_page_create(30,32))
		// {
		//
		// return;
		// }
		// zpSDK.TextPosWinStyle = true;
		//
		// // Bitmap bmp = BitmapFactory.decodeFile("/12345.bmp");
		// Resources res=getResources();
		// InputStream is=res.openRawResource(R.drawable.abc);
		// BitmapDrawable bmpDraw=new BitmapDrawable(is);
		// Bitmap bmp=bmpDraw.getBitmap();
		//
		// zpSDK.zp_draw_bitmap(bmp, 0, 0);
		// zpSDK.zp_page_print(true);
		//
		// zpSDK.zp_page_free();
		// zpSDK.zp_close();
		//
		//
		// }
		// });
		Button Button8 = (Button) findViewById(R.id.button8);
		Button8.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				Print8(SelectedBDAddress);
			}
		});
	}

	public boolean ListBluetoothDevice() {
		final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		ListView listView = (ListView) findViewById(R.id.listView1);
		SimpleAdapter m_adapter = new SimpleAdapter(this, list,
				android.R.layout.simple_list_item_2, new String[] {
						"DeviceName", "BDAddress" }, new int[] {
						android.R.id.text1, android.R.id.text2 });
		listView.setAdapter(m_adapter);

		if ((myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()) == null) {
			Toast.makeText(this, "没有找到蓝牙适配器", Toast.LENGTH_LONG).show();
			return false;
		}

		if (!myBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, 2);
		}

		Set<BluetoothDevice> pairedDevices = myBluetoothAdapter
				.getBondedDevices();
		if (pairedDevices.size() <= 0)
			return false;
		for (BluetoothDevice device : pairedDevices) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("DeviceName", device.getName());
			map.put("BDAddress", device.getAddress());
			list.add(map);
		}
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				SelectedBDAddress = list.get(arg2).get("BDAddress");
				if (((ListView) arg0).getTag() != null) {
					((View) ((ListView) arg0).getTag())
							.setBackgroundDrawable(null);
				}
				((ListView) arg0).setTag(arg1);
				arg1.setBackgroundColor(Color.BLUE);

			}
		});
		return true;
	}

	public boolean OpenPrinter(String BDAddress) {
		if (BDAddress == "" || BDAddress == null) {
			Toast.makeText(this, "没有选择打印机", Toast.LENGTH_LONG).show();
			return false;
		}
		BluetoothDevice myDevice;
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (myBluetoothAdapter == null) {
			Toast.makeText(this, "读取蓝牙设备错误", Toast.LENGTH_LONG).show();
			return false;
		}
		myDevice = myBluetoothAdapter.getRemoteDevice(BDAddress);
		if (myDevice == null) {
			Toast.makeText(this, "读取蓝牙设备错误", Toast.LENGTH_LONG).show();
			return false;
		}
		if (zpSDK.zp_open(myBluetoothAdapter, myDevice) == false) {
			Toast.makeText(this, zpSDK.ErrorMessage, Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	public void Print1(String BDAddress) {
		statusBox.Show("正在打印...");
		if (!OpenPrinter(BDAddress)) {
			statusBox.Close();
			return;
		}

		if (!zpSDK.zp_page_create(80, 2000)) {
			Toast.makeText(this, "创建打印页面失败", Toast.LENGTH_LONG).show();
			statusBox.Close();
			return;
		}
		zpSDK.TextPosWinStyle = false;
		zpSDK.zp_draw_text_ex(0, 0, "XX汽车保险股份有限公司", "黑体", 3.4, 0, false, false,
				false);

		zpSDK.zp_draw_text_ex(18.3, 3.4, "XX汽车保险股份有限公司", "黑体", 3.4, 0, false,
				false, false);
		zpSDK.zp_draw_text_ex(17, 7.6, " 车险小额损失核损单", "黑体", 3.4, 0, false,
				false, false);
		zpSDK.zp_draw_text_ex(0, 12.4, "No. 1010000- 赔案号", "黑体", 2.3, 0, false,
				false, false);
		zpSDK.zp_draw_line(0, 14.5, 80, 14.5, 2); // 第一隔横线
		zpSDK.zp_draw_line(0, 18.5, 80, 18.5, 2); // 第二隔横线;
		zpSDK.zp_draw_line(0, 22.5, 80, 22.5, 2); // 第三隔横线;;
		zpSDK.zp_draw_line(0, 26.5, 80, 26.5, 2); // 第四格横线;;
		zpSDK.zp_draw_line(0, 30.5, 80, 30.5, 2); // 第五格横线;;
		zpSDK.zp_draw_line(0, 14.5, 0, 46.5, 2);// 第一格竖线;;分界线
		zpSDK.zp_draw_line(10, 14.5, 10, 26.5, 2);// 第二格竖线;;
		zpSDK.zp_draw_line(18, 14.5, 18, 22.5, 2);// 第三格竖线;;
		zpSDK.zp_draw_line(26, 18.5, 26, 22.5, 2);// 第四格竖线;;
		zpSDK.zp_draw_line(27, 14.5, 27, 18.5, 2);// 第四格竖线;;
		zpSDK.zp_draw_line(22, 22.5, 22, 26.5, 2);// 查勘地点 左竖行;
		zpSDK.zp_draw_line(32, 22.5, 32, 26.5, 2);// 查勘地点 右竖行;
		zpSDK.zp_draw_line(43, 18.5, 43, 22.5, 2);// 竖线
		zpSDK.zp_draw_line(46, 14.5, 46, 18.5, 2);// 竖线;;
		zpSDK.zp_draw_line(54, 14.5, 54, 26.5, 2);// 第四格竖线;;
		zpSDK.zp_draw_line(63, 22.5, 63, 26.5, 2);// 第四格竖线;;
		zpSDK.zp_draw_text_ex(0.4, 17, "牌照号码", "黑体", 2.4, 0, false, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(18.4, 17, "被保险人", "黑体", 2.2, 0, false, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(46.5, 17, "保单号", "黑体", 2.2, 0, false, false,
				false); // 第一行字体;;;
		zpSDK.zp_draw_text_ex(0.4, 21, "厂牌型号", "黑体", 2.4, 0, false, false,
				false);
		zpSDK.zp_draw_text_ex(18.5, 21, "车架号", "黑体", 2.2, 0, false, false,
				false);
		zpSDK.zp_draw_text_ex(43.5, 21, "损失标的", "黑体", 2.4, 0, false, false,
				false);
		zpSDK.zp_draw_text_ex(0.4, 25, "查勘时间", "黑体", 2.4, 0, false, false,
				false);
		zpSDK.zp_draw_text_ex(10.4, 25, " 年 月 日", "黑体", 2.2, 0, false, false,
				false);
		zpSDK.zp_draw_text_ex(22.5, 25, "查勘地点", "黑体", 2.4, 0, false, false,
				false);
		zpSDK.zp_draw_text_ex(54.5, 25, "承修单位", "黑体", 2.2, 0, false, false,
				false);
		zpSDK.zp_draw_line(72, 14.5, 72, 46.5, 2);// 末尾竖行边界线;;;
		/* 更换项目栏目开始画表格了 */
		zpSDK.zp_draw_line(6, 26.5, 6, 46.5, 2);// 竖线;; 序号右列;;
		zpSDK.zp_draw_line(22, 26.5, 22, 46.5, 2);// 竖线;; 金额左列;;
		zpSDK.zp_draw_line(31, 26.5, 31, 46.5, 2);// 竖线;; 金额右列;;
		zpSDK.zp_draw_line(38, 26.5, 38, 46.5, 2);// 竖线;; 工种右列;;
		zpSDK.zp_draw_line(62, 26.5, 62, 46.5, 2);// 竖线;;最后一行的金额左列;;
		zpSDK.zp_draw_text_ex(0.4, 29, "序号", "黑体", 2.6, 0, false, false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(6.8, 29, " 更 换 项 目", "黑体", 2.6, 0, false, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(23, 29, "金 额", "黑体", 2.6, 0, false, false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(31.5, 29, "工种", "黑体", 2.6, 0, false, false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(40.5, 29, " 维 修 项 目 ", "黑体", 2.6, 0, false,
				false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(63.5, 29, "金 额 ", "黑体", 2.6, 0, false, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_line(0, 34.5, 31, 34.5, 2); // 第一隔横线
		zpSDK.zp_draw_line(0, 38.5, 31, 38.5, 2); // 第二隔横线;
		zpSDK.zp_draw_line(0, 42.5, 31, 42.5, 2); // 第三隔横线;;
		zpSDK.zp_draw_line(0, 46.5, 80, 46.5, 2); // 第四格横线;;
		zpSDK.zp_draw_line(38, 34.5, 80, 34.5, 2); // 第一隔横线
		zpSDK.zp_draw_line(38, 38.5, 80, 38.5, 2); // 第二隔横线;
		zpSDK.zp_draw_line(38, 42.5, 80, 42.5, 2); // 第三隔横线;;
		// zpSDK.zp_draw_line(38, 46.5, 80,46.5,2); //第四格横线;;
		zpSDK.zp_draw_text_ex(33, 35.5, "机 ", "黑体", 3.3, 0, false, false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(33, 41.5, "电", "黑体", 3.3, 0, false, false, false);// 第一行字体;;
		// zpSDK.zp_goto_mark_label(1);
		zpSDK.zp_draw_line(0, 0 + 48, 0, 56 + 48, 2);// 第一边境 竖线 起始线
		zpSDK.zp_draw_line(0, 0 + 48, 80, 0 + 48, 2);// 第一边境 横线
		zpSDK.zp_draw_line(62, 0 + 48, 62, 48 + 48, 2);// 第一边境 末尾竖线
		zpSDK.zp_draw_line(72, 0 + 48, 72, 56 + 48, 2);// 第一边境 末尾竖线
		zpSDK.zp_draw_line(6, 0 + 48, 6, 48 + 48, 2);// 第2边境 竖线
		zpSDK.zp_draw_line(22, 0 + 48, 22, 52 + 48, 2);// 第3边境 竖线
		zpSDK.zp_draw_line(31, 0 + 48, 31, 48 + 48, 2);// 第3边境 竖线
		zpSDK.zp_draw_line(38, 0 + 48, 38, 52 + 48, 2);// 第4边境 竖线
		zpSDK.zp_draw_line(62, 0 + 48, 62, 52 + 48, 2);// 第4边境 竖线*/
		zpSDK.zp_draw_line(0, 4 + 48, 31, 4 + 48, 2); // 第一隔横线 拆装1
		zpSDK.zp_draw_line(0, 8 + 48, 31, 8 + 48, 2); // 第二隔横线; 拆装1
		zpSDK.zp_draw_line(0, 12 + 48, 31, 12 + 48, 2); // 第三隔横线;; 拆装1
		zpSDK.zp_draw_line(0, 16 + 48, 80, 16 + 48, 2); // 第四格横线;; 拆装1
		zpSDK.zp_draw_line(38, 4 + 48, 80, 4 + 48, 2); // 第一隔横线 拆装2
		zpSDK.zp_draw_line(38, 8 + 48, 80, 8 + 48, 2); // 第二隔横线; 拆装2
		zpSDK.zp_draw_line(38, 12 + 48, 80, 12 + 48, 2); // 第三隔横线;; 拆装2
		zpSDK.zp_draw_text_ex(33, 5 + 48, "拆 ", "黑体", 3.3, 0, false, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(33, 11 + 48, "装", "黑体", 3.3, 0, false, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_line(0, 20 + 48, 31, 20 + 48, 2); // 第一隔横线 钣金1
		zpSDK.zp_draw_line(0, 24 + 48, 31, 24 + 48, 2); // 第二隔横线; 钣金1
		zpSDK.zp_draw_line(0, 28 + 48, 31, 28 + 48, 2); // 第三隔横线;; 钣金1
		zpSDK.zp_draw_line(0, 32 + 48, 80, 32 + 48, 2); // 第四格横线;; 钣金1
		zpSDK.zp_draw_line(38, 20 + 48, 80, 20 + 48, 2); // 第一隔横线 钣金2
		zpSDK.zp_draw_line(38, 24 + 48, 80, 24 + 48, 2); // 第二隔横线; 钣金2
		zpSDK.zp_draw_line(38, 28 + 48, 80, 28 + 48, 2); // 第三隔横线;; 钣金2
		zpSDK.zp_draw_text_ex(33, 21 + 48, "钣 ", "黑体", 3.3, 0, false, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(33, 27 + 48, "金", "黑体", 3.3, 0, false, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_line(0, 36 + 48, 31, 36 + 48, 2); // 第一隔横线 油漆1
		zpSDK.zp_draw_line(0, 40 + 48, 31, 40 + 48, 2); // 第二隔横线; 油漆1
		zpSDK.zp_draw_line(0, 44 + 48, 31, 44 + 48, 2); // 第三隔横线;; 油漆1
		zpSDK.zp_draw_line(0, 48 + 48, 80, 48 + 48, 2); // 第四格横线;; 油漆1
		zpSDK.zp_draw_line(38, 36 + 48, 80, 36 + 48, 2); // 第一隔横线 油漆2
		zpSDK.zp_draw_line(38, 40 + 48, 80, 40 + 48, 2); // 第二隔横线; 油漆2
		zpSDK.zp_draw_line(38, 44 + 48, 80, 44 + 48, 2); // 第三隔横线;; 油漆2
		zpSDK.zp_draw_text_ex(33, 37 + 48, "油 ", "黑体", 3.3, 0, false, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(33, 41 + 48, "漆", "黑体", 3.3, 0, false, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_line(0, 52 + 48, 80, 52 + 48, 3); // 拆料费小记;;;;;;;;;;;;;;
		zpSDK.zp_draw_line(0, 56 + 48, 80, 56 + 48, 3); // 维修费小记;;;;;;;;;;;;;;
		zpSDK.zp_draw_line(13.5, 48 + 48, 13.5, 52 + 48, 2); // 材料费小记左线;;;;;;;;;竖线;;
		zpSDK.zp_draw_line(48, 48 + 48, 48, 52 + 48, 2); // 维修费小记左线
															// 竖线;;;;;;;;;;;;;;
		zpSDK.zp_draw_text_ex(0.6, 51.3 + 48, "材料费小记", "黑体", 2.6, 0, false,
				false, false);
		zpSDK.zp_draw_text_ex(22.4, 51.3 + 48, " 残 值 计", "黑体", 2.6, 0, false,
				false, false);
		zpSDK.zp_draw_text_ex(48.6, 51.3 + 48, "维修费小记", "黑体", 2.6, 0, false,
				false, false);
		zpSDK.zp_draw_text_ex(3, 54.9 + 48, "维修合计： 千 百 拾 元 （￥ .00) ", "黑体",
				3.1, 0, false, false, false);// 第一行字体;;

		zpSDK.zp_draw_text_ex(0, 2.6 + 108, "特别注意事项", "黑体", 3.1, 0, true,
				false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(0, 6.1 + 108, "1.", "黑体", 2.9, 0, false, false,
				false);
		zpSDK.zp_draw_text_box(4, 6.1 + 108, 66, 100,
				"受保险公司委托本核损单核定金额限2000元以内,超过此金额的损失核定以保险公司另行出具的估价单为准;", "黑体",
				2.9, 0, false, false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(0, 14 + 112, "2.", "黑体", 2.9, 0, false, false,
				false);
		zpSDK.zp_draw_text_box(
				4,
				14 + 112,
				66,
				100,
				"本书核定金额仅作为维修及损失确认依据之一，不构成XX保险股份有限公司对理赔责任承诺，实际的赔付金额 以保险合同上的约定理算为准；",
				"黑体", 2.9, 0, false, true, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(0, 27 + 112, "3 .查勘人意见：", "黑体", 2.9, 0, false,
				false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(2.8, 32 + 112, "查勘人电话： ", "黑体", 2.9, 0, false,
				false, false);// 第一行字体;;
		zpSDK.zp_draw_line(0, 35 + 112, 80, 35 + 112, 2); // 横线上面的;;
		zpSDK.zp_draw_line(0, 55 + 112, 80, 55 + 112, 2); // 横线下面的;;;
		zpSDK.zp_draw_line(0, 35 + 112, 0, 55 + 112, 2); // 竖线 左边的
		zpSDK.zp_draw_line(24, 35 + 112, 24, 55 + 112, 2); // 竖线
		zpSDK.zp_draw_line(48, 35 + 112, 48, 55 + 112, 2); // 48.--60竖线;;
		zpSDK.zp_draw_line(72, 35 + 112, 72, 55 + 112, 2); // 最右边竖线;;
		zpSDK.zp_draw_text_ex(0, 39 + 112, "被保险人(车方)签章:", "黑体", 2.6, 0, true,
				false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(26, 39 + 112, "承修方签章:", "黑体", 2.8, 0, true,
				false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(50, 39 + 112, "保险公司签章:", "黑体", 2.8, 0, true,
				false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(2, 47 + 112, "经办人", "黑体", 2.8, 0, true, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(26, 47 + 112, "经办人", "黑体", 2.8, 0, true, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(50, 47 + 112, "查勘人", "黑体", 2.8, 0, true, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(10, 52 + 112, " 年 月 日", "黑体", 2.8, 0, true,
				false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(34, 52 + 112, " 年 月 日", "黑体", 2.8, 0, true,
				false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(58, 52 + 112, " 年 月 日", "黑体", 2.8, 0, true,
				false, false);// 第一行字体;;

		int height1 = 164;
		zpSDK.zp_draw_text_ex(10, height1, " 年 月 日", "黑体", 2.8, 0, true, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(34, height1, " 年 月 日", "黑体", 2.8, 0, true, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(58, height1, " 年 月 日", "黑体", 2.8, 0, true, false,
				false);// 第一行字体;;

		zpSDK.zp_page_print(false);
		zpSDK.zp_printer_status_detect();
		// zpSDK.zp_goto_mark_right(150);
		if (zpSDK.zp_printer_status_get(8000) != 0) {
			Toast.makeText(this, zpSDK.ErrorMessage, Toast.LENGTH_LONG).show();
		}
		zpSDK.zp_page_free();
		zpSDK.zp_close();
		statusBox.Close();
	}

	public void Print2(String BDAddress) {
		statusBox.Show("正在打印...");

		if (!OpenPrinter(BDAddress)) {
			statusBox.Close();
			return;
		}
		if (!zpSDK.zp_page_create(82 + 24, 72)) {
			Toast.makeText(this, "创建打印页面失败", Toast.LENGTH_LONG).show();
			statusBox.Close();
			return;
		}
		zpSDK.TextPosWinStyle = false;
		zpSDK.zp_draw_text_ex(18.3, 2.7, "XX汽车保险股份有限公司", "黑体", 3.4, 0, true,
				false, false);
		zpSDK.zp_draw_text_ex(20, 6.6, " 车险小额损失核损单", "黑体", 3.4, 0, true, false,
				false);
		zpSDK.zp_draw_text_ex(0, 11.4, "No. 1010000- 赔案号", "黑体", 2.5, 0, false,
				false, false);

		zpSDK.zp_draw_line(0, 13.5, 80, 13.5, 2); // 第一隔横线
		zpSDK.zp_draw_line(0, 17.5, 80, 17.5, 2); // 第二隔横线;
		zpSDK.zp_draw_line(0, 21.5, 80, 21.5, 2); // 第三隔横线;;
		zpSDK.zp_draw_line(0, 25.5, 80, 25.5, 2); // 第四格横线;;
		zpSDK.zp_draw_line(0, 29.5, 80, 29.5, 2); // 第五格横线;;

		zpSDK.zp_draw_line(0, 13.5, 0, 69.5, 2);// 第一格竖线;;分界线
		zpSDK.zp_draw_line(13, 13.5, 13, 25.5, 2);// 第二格竖线;;
		zpSDK.zp_draw_line(21, 13.5, 21, 21.5, 2);// 第三格竖线;;
		zpSDK.zp_draw_line(30, 17.5, 30, 21.5, 2);// 第四格竖线;;
		zpSDK.zp_draw_line(31, 13.5, 31, 17.5, 2);// 第四格竖线;;
		zpSDK.zp_draw_line(26, 21.5, 26, 25.5, 2);// 查勘地点 左竖行;
		zpSDK.zp_draw_line(37, 21.5, 37, 25.5, 2);// 查勘地点 右竖行;
		zpSDK.zp_draw_line(47, 17.5, 47, 21.5, 2);// 竖线
		zpSDK.zp_draw_line(50, 13.5, 50, 17.5, 2);// 竖线;;
		zpSDK.zp_draw_line(60.5, 13.5, 60.5, 25.5, 2);// 第四格竖线;
		zpSDK.zp_draw_line(70, 21.5, 70, 25.5, 2);// 第四格竖线;;
		zpSDK.zp_draw_line(80, 13.5, 80, 69.5, 2);// 末尾竖行边界线;;;

		zpSDK.zp_draw_text_ex(1.1, 16, "牌照号码", "黑体", 2.6, 0, false, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(21, 16, "被保险人", "黑体", 2.6, 0, false, false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(51, 16, "保单号", "黑体", 2.6, 0, false, false, false);// 第一行字体;;

		zpSDK.zp_draw_text_ex(1.1, 20, "厂牌型号", "黑体", 2.6, 0, false, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(21, 20, "车架号", "黑体", 2.6, 0, false, false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(49.5, 20, "损失标的", "黑体", 2.6, 0, false, false,
				false);// 第一行字体;;

		zpSDK.zp_draw_text_ex(1.5, 24, "查勘时间", "黑体", 2.6, 0, false, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(13.5, 24, "年 月  日", "黑体", 2.6, 0, false, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(26.2, 24, "查勘地点", "黑体", 2.6, 0, false, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(60.5, 24, "承修单位", "黑体", 2.4, 0, false, false,
				false);// 第一行字体;;

		// 序号 更换项目 金额 工种 维修项目 金额
		zpSDK.zp_draw_text_ex(1.2, 28.3, "序号", "黑体", 2.9, 0, false, false,
				false);// 序号;;.
		zpSDK.zp_draw_text_ex(7.8, 28.3, " 更  换  项  目", "黑体", 2.9, 0, false,
				false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(27, 28.3, "金额", "黑体", 2.9, 0, false, false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(36.8, 28.3, "工种", "黑体", 2.9, 0, false, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(48, 28.3, "维  修  项  目", "黑体", 2.9, 0, false,
				false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(69, 28.3, "金 额", "黑体", 2.9, 0, false, false,
				false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(37.6, 35, "机", "黑体", 4, 6, true, false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(37.6, 41, "电", "黑体", 4.2, 0, true, false, false);// 第一行字体;;

		for (double i = 33.5; i < 44; i += 4) {
			zpSDK.zp_draw_line(0, i, 36, i, 2); // 机电 左侧
		}

		for (double i = 33.5; i < 44; i += 4) {
			zpSDK.zp_draw_line(44, i, 80, i, 2); // 机电 右线
		}

		zpSDK.zp_draw_line(0, 45.5, 80, 45.5, 2); // 机电最后一个横线

		zpSDK.zp_draw_line(7.2, 25.5, 7.2, 69.5, 2); // 机电 第一个竖线;;
		zpSDK.zp_draw_line(26, 25.5, 26, 69.5, 2); // 机电 第一个竖线;;
		zpSDK.zp_draw_line(36, 25.5, 36, 69.5, 2); // 机电 第一个竖线;;
		zpSDK.zp_draw_line(44, 25.5, 44, 69.5, 2); // 机电 第二个竖线;;
		zpSDK.zp_draw_line(68, 25.5, 68, 69.5, 2); // 机电 第二个竖线;;

		// 拆装 左右横线部分 //
		for (double i = 49.5; i < 65.6; i += 4) {
			zpSDK.zp_draw_line(0, i, 36, i, 2); // 拆装 左侧
		}

		for (double i = 49.5; i < 65.6; i += 4) {
			zpSDK.zp_draw_line(44, i, 80, i, 2); // 拆装 右线
		}
		zpSDK.zp_draw_line(0, 65.5, 80, 65.5, 2); // 最后横线;

		zpSDK.zp_draw_text_ex(37.6, 52, "维", "黑体", 4, 6, true, false, false);// 第一行字体;;
		zpSDK.zp_draw_text_ex(37.6, 55.8, "修", "黑体", 4.2, 0, true, false, false);// 第一行字体;;

		// 拆装 左右横线部分 //
		zpSDK.zp_draw_line(0, 69.5, 80, 69.5, 2); // 最后横线;;;
		zpSDK.zp_page_print(true);
		zpSDK.zp_printer_status_detect();
		// zpSDK.zp_goto_mark_right(150);
		if (zpSDK.zp_printer_status_get(8000) != 0) {
			Toast.makeText(this, zpSDK.ErrorMessage, Toast.LENGTH_LONG).show();
		}
		zpSDK.zp_page_free();
		zpSDK.zp_close();
		statusBox.Close();
	}

	public void Print3(String BDAddress) {
		statusBox.Show("正在打印...");
		if (!OpenPrinter(BDAddress)) {
			statusBox.Close();
			return;
		}
		if (!zpSDK.zp_page_create(72, 82 + 24 + 30)) {
			Toast.makeText(this, "创建打印页面失败", Toast.LENGTH_LONG).show();
			statusBox.Close();
			return;
		}
		zpSDK.TextPosWinStyle = true;

		zpSDK.zp_draw_rect(0.1, 0.1, 81.9, 71.9, 2);
		zpSDK.zp_draw_line(1, 1, 5, 5, 2); // 最后横线;;;
		zpSDK.zp_draw_text_ex(5, 4, "CODE128:", "宋体", 4, 0, false, false, false);
		zpSDK.zp_draw_barcode(25, 2, "12345678",
				zpSDK.BARCODE_TYPE.BARCODE_CODE128, 8, 2, 90);
		zpSDK.zp_draw_text_ex(5, 14, "CODE93:", "宋体", 4, 0, false, false, false);
		zpSDK.zp_draw_barcode(25, 12, "12345678",
				zpSDK.BARCODE_TYPE.BARCODE_CODE93, 8, 2, 180);
		zpSDK.zp_draw_text_ex(5, 24, "CODE39:", "宋体", 4, 0, false, false, false);
		zpSDK.zp_draw_barcode(25, 22, "12345678",
				zpSDK.BARCODE_TYPE.BARCODE_CODE39, 8, 2, 270);
		zpSDK.zp_draw_text_ex(5, 34, "EAN8:", "宋体", 4, 0, false, false, false);
		zpSDK.zp_draw_barcode(25, 32, "12345678",
				zpSDK.BARCODE_TYPE.BARCODE_EAN8, 8, 2, 0);
		zpSDK.zp_draw_text_ex(5, 44, "EAN13:", "宋体", 4, 0, false, false, false);
		zpSDK.zp_draw_barcode(25, 42, "1234567890123",
				zpSDK.BARCODE_TYPE.BARCODE_EAN13, 8, 2, 0);
		zpSDK.zp_draw_text_ex(5, 54, "UPC:", "宋体", 4, 0, false, false, false);
		zpSDK.zp_draw_barcode(25, 52, "123456789012",
				zpSDK.BARCODE_TYPE.BARCODE_UPC, 8, 2, 0);
		zpSDK.zp_draw_text_ex(5, 64, "CODABAR:", "宋体", 4, 0, false, false,
				false);
		zpSDK.zp_draw_barcode(25, 62, "A23456789012A",
				zpSDK.BARCODE_TYPE.BARCODE_CODABAR, 8, 2, 0);

		// zpSDK.zp_draw_text_box(25,65 , 30, 20, "111111111", "宋体", 2.14, 0,
		// false, false, false);
		// zpSDK.zp_draw_text_box(5, 5, 30,30,
		// "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890","宋体",2.5,0,false,false,false);

		zpSDK.zp_page_print(false);
		zpSDK.zp_printer_status_detect();
		// zpSDK.zp_goto_mark_right(150);
		if (zpSDK.zp_printer_status_get(8000) != 0) {
			Toast.makeText(this, zpSDK.ErrorMessage, Toast.LENGTH_LONG).show();
		}
		zpSDK.zp_page_free();
		zpSDK.zp_close();
		statusBox.Close();
	}

	public void Print4(String BDAddress) {
		if (!OpenPrinter(BDAddress))
			return;
		zpSDK.zp_goto_mark_left(150);
		zpSDK.zp_close();
	}

	public void Print5(String BDAddress) {
		if (!OpenPrinter(BDAddress))
			return;
		zpSDK.zp_goto_mark_right(150);
		zpSDK.zp_close();

	}

	public void Print6(String BDAddress) {
		if (!OpenPrinter(BDAddress))
			return;
		zpSDK.zp_goto_mark_label(150);
		zpSDK.zp_close();
	}// print6

	public void Print8(String BDAddress) {
		// TODO Auto-generated method stub

		statusBox.Show("正在打印...");
		if (!OpenPrinter(BDAddress)) {
			statusBox.Close();
			return;
		}
		if (!zpSDK.zp_page_create(82 + 24, 52)) {
			Toast.makeText(this, "创建打印页面失败", Toast.LENGTH_LONG).show();
			statusBox.Close();
			return;
		}
		zpSDK.TextPosWinStyle = true;

		// zpSDK.zp_draw_barcode(25,62, "A23456789012A",
		// zpSDK.BARCODE_TYPE.BARCODE_CODABAR, 8, 2, 0);

		zpSDK.zp_page_print(true);
		zpSDK.zp_printer_status_detect();

		if (zpSDK.zp_printer_status_get(8000) != 0) {
			Toast.makeText(this, zpSDK.ErrorMessage, Toast.LENGTH_LONG).show();
		}
		zpSDK.zp_page_free();
		zpSDK.zp_close();
		statusBox.Close();

	}

}
