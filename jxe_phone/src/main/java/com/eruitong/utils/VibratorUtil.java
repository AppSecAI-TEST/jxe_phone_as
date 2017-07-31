// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.eruitong.utils;

import android.app.Activity;
import android.os.Vibrator;

public class VibratorUtil {

	public VibratorUtil() {
	}

	public static void Vibrate(Activity activity, long l) {
		((Vibrator) activity.getSystemService("vibrator")).vibrate(l);
	}

	public static void Vibrate(Activity activity, long al[], boolean flag) {
		activity = (Activity) activity.getSystemService("vibrator");
		int i;
		if (flag) {
			i = 1;
		} else {
			i = -1;
		}
		// ((Vibrator) activity).vibrate(al, i);
	}
}
