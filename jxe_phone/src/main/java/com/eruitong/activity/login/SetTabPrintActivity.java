package com.eruitong.activity.login;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import zpSDK.zpSDK.zpSDK;

import com.eruitong.activity.BaseActivity;
import com.eruitong.bluetooth.StatusBox;
import com.eruitong.db.BluetoothDBWrapper;
import com.eruitong.eruitong.R;
import com.eruitong.print.bluetooth.BluetoothUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SetTabPrintActivity extends BaseActivity {

	@ViewInject(R.id.print_btn_no_off)
	Button mOPen;
	@ViewInject(R.id.print_btn_search)
	Button mSearch;
	@ViewInject(R.id.print_lst)
	ListView mListView;
	@ViewInject(R.id.print_btn_save)
	Button mSave;
	@ViewInject(R.id.print_btn_test)
	Button mTest;

	private BluetoothAdapter mBluetoothAdapter;
	private List<String> mLstDevices;
	private String SelectedBDAddress = "";
	private String SelectedBDName = "";
	private MyBluetoothAdapter myAdapter;

	/** 标签地址 **/
	private String biaoqianAddress;

	private StatusBox statusBoxBiaoqiain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settab_print);
		ViewUtils.inject(this);

		initDatas();
		initView();
	}

	private void initView() {

		if (mBluetoothAdapter.isEnabled()) {
			mOPen.setText("关闭");
		} else {
			mOPen.setText("打开");
		}
		// 打开/关闭 蓝牙
		mOPen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mBluetoothAdapter.isEnabled()) {
					// 不做提示，强行打开
					mBluetoothAdapter.enable();
					mOPen.setText("关闭");
				} else {
					mBluetoothAdapter.disable();
					mOPen.setText("打开");
					mLstDevices.clear();
					myAdapter.notifyDataSetChanged();
				}
			}
		});

		// 搜索蓝牙
		mSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
					Toast.makeText(SetTabPrintActivity.this, "请先打开蓝牙设备", 0)
							.show();
				}
				// mBluetoothAdapter.startDiscovery();
				mLstDevices.clear();
				myAdapter.notifyDataSetChanged();

				Set<BluetoothDevice> btDevice = mBluetoothAdapter
						.getBondedDevices();
				if (btDevice.size() > 0) {
					Iterator<BluetoothDevice> iterator = btDevice.iterator();
					while (iterator.hasNext()) {
						BluetoothDevice device = iterator.next();
						String str = (new StringBuilder(String.valueOf(device
								.getName())).append("|").append(
								device.getAddress()).toString());
						if (str.length() > 0) {
							mLstDevices.add(str);
							myAdapter.notifyDataSetChanged();
						}
					}
				}
			}
		});

		// 保存标签
		mSave.setOnClickListener(new OnClickListener() {
			BluetoothDBWrapper dbWrapper;

			@Override
			public void onClick(View v) {
				if ("".equals(SelectedBDName) || "".equals(SelectedBDAddress)) {
					return;
				}
				dbWrapper = BluetoothDBWrapper.getInstance(getApplication());
				dbWrapper.deleteRank("No1");
				dbWrapper.insertRank("No1", SelectedBDName, SelectedBDAddress);
				biaoqianAddress = SelectedBDAddress;
				Toast.makeText(SetTabPrintActivity.this, R.string.save_succsee,
						0).show();
				isConnect();
			}
		});

		// 测试打印
		mTest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mBluetoothAdapter.isEnabled()) {
					// 不做提示，强行打开
					mBluetoothAdapter.enable();
					mOPen.setText("关闭");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				statusBoxBiaoqiain = new StatusBox(SetTabPrintActivity.this, v);
				isConnect();

				try {
					statusBoxBiaoqiain.Show("正在打印 ...");
					if (!BluetoothUtil.OpenPrinter(SetTabPrintActivity.this,
							biaoqianAddress)) {
						statusBoxBiaoqiain.Close();
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				biaoqian();
				zpSDK.zp_close();
				statusBoxBiaoqiain.Close();
				try {
					Thread.sleep(1000L);
					if (statusBoxBiaoqiain.isShow()) {
						statusBoxBiaoqiain.Close();
					}
					return;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		});
		myAdapter = new MyBluetoothAdapter();
		mListView.setAdapter(myAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String as[] = ((String) mLstDevices.get(position)).split("\\|");
				SelectedBDName = as[0];
				SelectedBDAddress = as[1];
				setTitle((new StringBuilder("Mac::")).append(SelectedBDAddress)
						.toString());
				if (parent.getTag() != null) {
					((View) parent.getTag())
							.setBackgroundResource(R.color.sky_blue);
				}
				parent.setTag(view);
				view.setBackgroundResource(R.color.gray);
			}
		});
	}

	private void isConnect() {
		Cursor cursor = BluetoothDBWrapper.getInstance(getApplication())
				.rawQueryRank("No1");
		cursor.moveToFirst();
		do {
			if (!cursor.moveToNext()) {
				cursor.close();
				return;
			}
			biaoqianAddress = cursor
					.getString(cursor.getColumnIndex("address"));
		} while (true);
	}

	private void biaoqian() {
		if (!zpSDK.zp_page_create(80D, 61D)) {
			statusBoxBiaoqiain.Close();
			return;
		}
		try {
			zpSDK.TextPosWinStyle = false;
			zpSDK.zp_draw_line(0.0D, 10D, 72D, 10D, 3);
			zpSDK.zp_draw_line(6D, 14D, 72D, 14D, 3);
			zpSDK.zp_draw_line(6D, 18D, 72D, 18D, 3);
			zpSDK.zp_draw_line(6D, 22D, 72D, 22D, 3);
			zpSDK.zp_draw_line(6D, 26D, 72D, 26D, 3);
			zpSDK.zp_draw_line(0.0D, 30D, 72D, 30D, 3);
			zpSDK.zp_draw_line(0.0D, 34D, 72D, 34D, 3);
			zpSDK.zp_draw_line(52D, 48D, 72D, 48D, 3);
			zpSDK.zp_draw_line(0.0D, 53D, 72D, 53D, 3);
			zpSDK.zp_draw_line(0.0D, 10D, 0.0D, 34D, 3);
			zpSDK.zp_draw_line(6D, 10D, 6D, 30D, 3);
			zpSDK.zp_draw_line(52D, 34D, 52D, 53D, 3);
			zpSDK.zp_draw_line(72D, 10D, 72D, 53D, 3);
			zpSDK.zp_draw_text_ex(1.0D, 4D, " JXE", "\u5B8B\u4F53", 4D, 0,
					true, true, false);
			zpSDK.zp_draw_text_ex(1.0D, 7D, " 04-07", "\u5B8B\u4F53", 3D, 0,
					true, true, false);
			zpSDK.zp_draw_text_ex(1.0D, 9.5D, " 10:30", "\u5B8B\u4F53", 3D, 0,
					true, true, false);
			zpSDK.zp_draw_text_ex(12D, 9D, "\u5B9D\u9E21", "\u5B8B\u4F53", 10D,
					0, true, false, false);
			zpSDK.zp_draw_text_ex(33D, 9D, "\u91D1\u6E2D\u8DEF",
					"\u5B8B\u4F53", 10D, 0, true, false, false);
			zpSDK.zp_draw_text_ex(61D, 9D, "98 ", "\u5B8B\u4F53", 10D, 0,
					false, false, false);
			zpSDK.zp_draw_text_box(0.5D, 15D, 5D, 5D, "\u738B\u6653\u971E",
					"\u5B8B\u4F53", 5D, 0, false, false, false);
			zpSDK.zp_draw_text_ex(
					7D,
					13.5D,
					"029524562125 \u897F\u5B89 029A \u9A6C\u5BB6\u6C9F  \u5F20\u4E09\u4E30 13256254856",
					"\u5B8B\u4F53", 2.7000000000000002D, 0, true, false, false);
			zpSDK.zp_draw_text_ex(
					7D,
					17.5D,
					"\u5B9D\u9E21 18229001286 \u5B9D\u9E21\u5E02\u91D1\u6E2D\u8DEF\u767E\u8111\u6C47\u5E02\u573A ",
					"\u5B8B\u4F53", 3D, 0, true, false, false);
			zpSDK.zp_draw_text_ex(
					7D,
					21.5D,
					"\u670D\u88C5  1/99\u4EF6 \u91CD\u91CF9999kg \u8FD0\u8D3911111 ",
					"\u5B8B\u4F53", 3.2000000000000002D, 0, true, false, false);
			zpSDK.zp_draw_text_ex(
					7D,
					25.5D,
					"\u4FDD\u4EF7\u8D391.2\u5143 \u4EE3\u6536\u8D3950000\u5143\u4ED3\u7BA1\u8D390.0\u5143000451",
					"\u5B8B\u4F53", 3D, 0, true, false, false);
			zpSDK.zp_draw_text_ex(
					7D,
					29.5D,
					"\u56DE\u5355 123458787837   \u6536\u6761   \u8EAB\u4EFD\u8BC1   \u76D6\u7AE0",
					"\u5B8B\u4F53", 3D, 0, true, false, false);
			zpSDK.zp_draw_text_ex(
					1.0D,
					33.5D,
					"\u7B7E\u56DE\u5355\u8D395\u5143  \u57AB\u4ED825\u5143  \u7B49\u901A\u77E5\u8D3910\u5143  \u5230\u4ED8500010\u5143",
					"\u5B8B\u4F53", 2.8999999999999999D, 0, true, false, false);
			zpSDK.zp_draw_text_ex(53D, 46D, "\u5FEB", "\u5B8B\u4F53", 14D, 0,
					true, false, false);
			zpSDK.zp_draw_text_ex(52D, 52D, "4008-111115", "\u5B8B\u4F53", 3D,
					0, true, false, false);
			zpSDK.zp_draw_barcode(8D, 35D, "029524562125",
					zpSDK.BARCODE_TYPE.BARCODE_CODE128, 14D, 3, 0);
			zpSDK.zp_draw_text_ex(10D, 52D, "029524562125", "\u5B8B\u4F53", 3D,
					0, false, false, false);
			zpSDK.zp_page_print(false);
			zpSDK.zp_goto_mark_label(120);
			zpSDK.zp_page_clear();
			zpSDK.zp_page_free();
		}
		// Misplaced declaration of an exception variable
		catch (Exception s) {
			s.printStackTrace();
		}
		try {
			if (!zpSDK.zp_page_create(80D, 61D)) {
				statusBoxBiaoqiain.Close();
				return;
			}
		} catch (Exception s) {
			return;
		}
		zpSDK.TextPosWinStyle = false;
		zpSDK.zp_draw_rect(0.0D, 0.0D, 72D, 53D, 3);
		zpSDK.zp_draw_line(12D, 4D, 72D, 4D, 3);
		zpSDK.zp_draw_line(0.0D, 8D, 72D, 8D, 3);
		zpSDK.zp_draw_line(0.0D, 12D, 72D, 12D, 3);
		zpSDK.zp_draw_line(0.0D, 16D, 72D, 16D, 3);
		zpSDK.zp_draw_line(0.0D, 20D, 72D, 20D, 3);
		zpSDK.zp_draw_line(0.0D, 24D, 72D, 24D, 3);
		zpSDK.zp_draw_line(0.0D, 28D, 72D, 28D, 3);
		zpSDK.zp_draw_line(56D, 42D, 72D, 42D, 3);
		zpSDK.zp_draw_line(0.0D, 10D, 0.0D, 34D, 3);
		zpSDK.zp_draw_line(12D, 0.0D, 12D, 16D, 3);
		zpSDK.zp_draw_line(56D, 28D, 56D, 53D, 3);
		zpSDK.zp_draw_text_ex(1.0D, 7D, "JXE", "\u5B8B\u4F53", 6D, 0, true,
				true, false);
		zpSDK.zp_draw_text_ex(1.0D, 11.5D, "04-14", "\u5B8B\u4F53", 3D, 0,
				true, true, false);
		zpSDK.zp_draw_text_ex(1.0D, 15.5D, "19:20", "\u5B8B\u4F53", 3D, 0,
				true, true, false);
		zpSDK.zp_draw_text_ex(
				13D,
				3.5D,
				"\u897F\u5B89  \u5218\u6765\u79D1  \u8FD0\u5355\u53F7 029545152514 ",
				"\u5B8B\u4F53", 3D, 0, true, false, false);
		zpSDK.zp_draw_text_ex(13D, 7.5D,
				"\u6986\u6797  \u5F20\u6765\u53D1  8546254/18524562586",
				"\u5B8B\u4F53", 3D, 0, true, false, false);
		zpSDK.zp_draw_text_ex(
				13D,
				11.5D,
				"\u670D\u88C5 999\u4EF6 \u91CD125kg  \u8FD0\u8D391250\u5143  \u57AB\u4ED8210\u5143",
				"\u5B8B\u4F53", 2.7999999999999998D, 0, true, false, false);
		zpSDK.zp_draw_text_ex(
				13D,
				15.5D,
				"\u58F0\u660E\u4EF7\u503C20000\u5143 \u4FDD\u4EF7\u8D3911\u5143  \u7B49\u901A\u77E510\u5143",
				"\u5B8B\u4F53", 3D, 0, true, false, false);
		zpSDK.zp_draw_text_ex(
				1.0D,
				19.5D,
				"\u4EE3\u6536\u6B3E40000\u5143  \u670D\u52A1\u8D393\u5143  \u5DE5\u884C  15\u5929\u8F6C ",
				"\u5B8B\u4F53", 3D, 0, true, false, false);
		zpSDK.zp_draw_text_ex(1.0D, 23.5D,
				"\u94F6\u884C\u8D26\u53F7:6221525554152561529", "\u5B8B\u4F53",
				3D, 0, true, false, false);
		zpSDK.zp_draw_text_ex(
				1.0D,
				27.5D,
				"\u56DE\u5355123489478893  \u56DE\u5355\u8D395\u5143       \u5BC4\u4ED8:421521\u5143",
				"\u5B8B\u4F53", 2.8999999999999999D, 0, true, false, false);
		zpSDK.zp_draw_text_ex(57D, 40D, "\u5FEB", "\u5B8B\u4F53", 13D, 0, true,
				false, false);
		zpSDK.zp_draw_text_box(60D, 46D, 12D, 4D, "4008 ", "\u5B8B\u4F53", 4D,
				0, true, false, false);
		zpSDK.zp_draw_text_box(56D, 50D, 15D, 4D, " 111115", "\u5B8B\u4F53",
				4D, 0, true, false, false);
		zpSDK.zp_draw_text_box(
				1.0D,
				31D,
				55D,
				3D,
				"\u5BA2\u6237\u987B\u77E5 1\uFF1A\u8D27\u7269\u5E94\u5F53\u7533\u660E\u4EF7\u503C\uFF0C \u6BCF\u4EF6 \u8D27\u7269\u9057\u5931\u6216\u635F\u6BC1\uFF0C\u627F\u8FD0\u4EBA\u6309\u603B\u4FDD\u4EF7\u7684\u5E73\u5747\u4EF7\u503C\u8D54\u507F\u30022\uFF1A\u6BCF\u7968\u603B\u4EF7\u503C\u5982\u679C\u8D85\u8FC72\u4E07\u5143\uFF0C\u4E0D\u4E88\u627F\u8FD0\u30023\uFF1A\u51FA\u73B0\u5F02\u5E38\uFF0C90\u5929\u5185\u8054\u7CFB\u5904\u7406\uFF0C\u8D85\u671F\u4E0D\u4E88\u5904\u7406",
				"\u5B8B\u4F53", 2.8999999999999999D, 0, true, false, false);
		zpSDK.zp_draw_text_ex(1.0D, 51D,
				"\u5907\u6CE8:\u7B49\u539F\u7968\u6216\u4F20\u771F",
				"\u5B8B\u4F53", 3D, 0, true, false, false);
		zpSDK.zp_draw_text_box(26D, 47D, 4D, 4D, "\u7B7E\u5B57",
				"\u5B8B\u4F53", 4D, 0, true, false, false);
		zpSDK.zp_page_print(false);
		zpSDK.zp_goto_mark_label(120);
		zpSDK.zp_page_clear();
		zpSDK.zp_page_free();
		return;
	}

	public void showMessage(String str) {
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 找到设备
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				String str = (new StringBuilder(
						String.valueOf(device.getName()))).append("|")
						.append(device.getAddress()).toString();
				if (str.length() > 0) {
					mLstDevices.add(str);
				}
				myAdapter.notifyDataSetChanged();
				return;
			}
			// 搜索完成
			else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				if (myAdapter.getCount() == 0) {
					Toast.makeText(SetTabPrintActivity.this, "没有找到蓝牙设备", 0)
							.show();
				}

				return;
			}
			// 执行更新列表的代码
		}
	};

	private void initDatas() {
		// IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		// registerReceiver(mReceiver, filter);
		// filter = new
		// IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		// registerReceiver(mReceiver, filter);

		IntentFilter intentfilter = new IntentFilter();
		intentfilter.addAction("android.bluetooth.device.action.FOUND");
		intentfilter
				.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
		registerReceiver(mReceiver, intentfilter);

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		mLstDevices = new ArrayList<String>();
	}

	class MyBluetoothAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mLstDevices.size();
		}

		@Override
		public String getItem(int position) {
			return mLstDevices.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(SetTabPrintActivity.this)
						.inflate(R.layout.item_bluetooth, null);
				holder.textView = (TextView) convertView
						.findViewById(R.id.item_bluetooth_msg);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			String string = getItem(position);
			holder.textView.setText(string + "");

			return convertView;
		}

		class ViewHolder {
			TextView textView;
		}

	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
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
