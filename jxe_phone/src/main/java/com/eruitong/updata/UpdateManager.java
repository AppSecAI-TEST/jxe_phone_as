// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.eruitong.updata;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Xml;
import android.view.KeyEvent;
import android.widget.Toast;

public class UpdateManager {

	private String mVersionCodeUrl = "http://cop.jx-express.cn:887/launcher/download_phone_jxe/version.xml";
	private String mApkUrl = "http://cop.jx-express.cn:887/launcher/download_phone_jxe/jxe_phone.apk";

	private Context mContext;
	Handler handler;

	public UpdateManager(Handler handler, Context context) {
		this.mContext = context;
		this.handler = handler;
	}

	public void checkUpdateInfo() {
		if (isUpdate()) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					showNoticeDialog();
				}
			});

			return;
		} else {
			handler.post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(mContext, "已是最新新版本", 0).show();
				}
			});
			return;
		}
	}

	private void showNoticeDialog() {
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
				mContext);
		builder.setTitle("版本更新");
		builder.setMessage("发现新版本");
		builder.setPositiveButton("下载",
				new android.content.DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialoginterface, int i) {
						mContext.startActivity(new Intent(Intent.ACTION_VIEW,
								Uri.parse(mApkUrl)));
						dialoginterface.dismiss();
					}

				});

		/*
		 * builder.setNegativeButton("以后再说", new
		 * android.content.DialogInterface.OnClickListener() {
		 * 
		 * public void onClick(DialogInterface dialoginterface, int i) {
		 * dialoginterface.dismiss(); }
		 * 
		 * });
		 */
		builder.setCancelable(false);
		builder.setOnKeyListener(new android.content.DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return false;
				}
				return true;
			}
		});
		builder.create().show();
	}

	private boolean isUpdate() {
		int versionCode;
		HashMap mHashMap;
		try {
			versionCode = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionCode;
			HttpURLConnection urlConnection = (HttpURLConnection) new URL(
					mVersionCodeUrl).openConnection();
			urlConnection.connect();
			InputStream stream = urlConnection.getInputStream();

			mHashMap = parseXml(stream);

		} catch (Exception exception1) {
			exception1.printStackTrace();
			return true;
		}

		return mHashMap != null
				&& Integer.valueOf((String) mHashMap.get("version")).intValue() > versionCode;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private HashMap parseXml(InputStream stream) {
		XmlPullParser parser = Xml.newPullParser(); // 由android.util.Xml创建一个XmlPullParser实例
		HashMap map = null;
		try {
			parser.setInput(stream, "UTF-8");

			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("update")) {
						map = new HashMap<String, String>();
					} else if (parser.getName().equals("version")) {
						eventType = parser.next();
						map.put("version", parser.getText());
					} else if (parser.getName().equals("name")) {
						eventType = parser.next();
						map.put("name", parser.getText());
					}
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("update")) {

					}
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}
}
