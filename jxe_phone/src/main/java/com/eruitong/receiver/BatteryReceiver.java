// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.eruitong.receiver;

import android.content.Context;
import android.content.IntentFilter;

public class BatteryReceiver extends BaseReceiver {

	private static BatteryReceiver sInstant;

	private BatteryReceiver(Context context) {
		super(context);
	}

	public static BatteryReceiver getInstant(Context context) {

		if (sInstant == null) {
			sInstant = new BatteryReceiver(context);
		}

		return sInstant;

	}

	protected IntentFilter getFilter() {
		IntentFilter intentfilter = new IntentFilter();
		intentfilter.addAction("android.intent.action.BATTERY_CHANGED");
		return intentfilter;
	}
}
