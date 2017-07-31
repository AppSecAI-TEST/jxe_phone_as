// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.eruitong.print.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zpSDK.zpSDK.zpSDK;

public class BluetoothUtil {

	public static final int PRINT_NO_ONR = 1;
	public static final int PRINT_NO_TWO = 2;
	static Context context;
	static BluetoothAdapter myBluetoothAdapter;

	public BluetoothUtil() {
	}

	public static boolean OpenPrinter(Context context, String BDAddress) {
		if (BDAddress == "" || BDAddress == null) {
			Toast.makeText(context, "没有选择打印机", Toast.LENGTH_LONG).show();
			return false;
		}
		BluetoothDevice myDevice;
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (myBluetoothAdapter == null) {
			Toast.makeText(context, "读取蓝牙设备错误", Toast.LENGTH_LONG).show();
			return false;
		}
		myDevice = myBluetoothAdapter.getRemoteDevice(BDAddress);
		if (myDevice == null) {
			Toast.makeText(context, "读取蓝牙设备错误", Toast.LENGTH_LONG).show();
			return false;
		}
		int i = 0;
		while (true) {
			BluetoothDevice localBluetoothDevice = myBluetoothAdapter
					.getRemoteDevice(BDAddress);
			if (localBluetoothDevice == null) {
				Toast.makeText(context, "读取蓝牙设备错误", 0).show();
				return false;
			}

			while (i <= 2) {
				int j = 0;

				if (!zpSDK.zp_open(myBluetoothAdapter, localBluetoothDevice)) {
					myBluetoothAdapter.disable();
					try {
						Thread.sleep(500L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					myBluetoothAdapter.enable();
					j = 0;
					if (i == 2) {
						Toast.makeText(context, "读取蓝牙设备错误", 0).show();
						return false;
					}
				} else {
					return true;
				}
				while (j <= 2000) {
					if (myBluetoothAdapter.isEnabled())
						break;
					j += 400;
					try {
						Thread.sleep(200L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
				i++;
			}
		}

	}

	public static void print(String s, List list) {
		list = new ArrayList();
		if (!OpenPrinter(context, s)) {
			return;
		}
		int i = 0;
		do {
			if (i >= list.size()) {
				zpSDK.zp_close();
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return;
			}
			switch (((Integer) list.get(i)).intValue()) {
			case 1: // '\001'
			case 2: // '\002'
			default:
				i++;
				break;
			}
		} while (true);
	}
}
