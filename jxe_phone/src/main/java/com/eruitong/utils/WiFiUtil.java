package com.eruitong.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Message;
import android.widget.ImageView;

import com.eruitong.eruitong.R;

public class WiFiUtil {

	public WiFiUtil() {
	}

	public static int getWiFiLevel(Context context) {
		return Math.abs(((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
				.getConnectionInfo().getRssi());
	}

	public static Message wifiLevel(int i) {
		if (i <= 0 && i >= -50) {
			Message message = new Message();
			message.what = 1;
			return message;
		}
		if (i < -50 && i >= -70) {
			Message message1 = new Message();
			message1.what = 2;
			return message1;
		}
		if (i < -70 && i >= -80) {
			Message message2 = new Message();
			message2.what = 3;
			return message2;
		}
		if (i < -80 && i >= -100) {
			Message message3 = new Message();
			message3.what = 4;
			return message3;
		} else {
			Message message4 = new Message();
			message4.what = 5;
			return message4;
		}
	}

	public static ImageView wifiSignal(ImageView imageview, int i) {
		switch (i) {
		default:
			imageview.setImageResource(R.drawable.single0);
			return imageview;

		case 1: // '\001'
			imageview.setImageResource(R.drawable.single1);
			return imageview;

		case 2: // '\002'
			imageview.setImageResource(R.drawable.single2);
			return imageview;

		case 3: // '\003'
			imageview.setImageResource(R.drawable.single3);
			return imageview;

		case 4: // '\004'
			imageview.setImageResource(R.drawable.single3);
			return imageview;

		case 5: // '\005'
			imageview.setImageResource(R.drawable.single4);
			break;
		}
		return imageview;
	}
}
