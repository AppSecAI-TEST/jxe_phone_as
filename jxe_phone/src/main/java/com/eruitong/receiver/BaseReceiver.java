// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.eruitong.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Referenced classes of package com.dingzhou.base:
//            Hearer

public abstract class BaseReceiver extends BroadcastReceiver {

	protected Context mContext;
	protected Intent mData;
	protected IntentFilter mFilter;
	List mHearers;
	protected boolean mRegisted;

	public BaseReceiver(Context context) {
		mContext = context;
		mFilter = getFilter();
		mHearers = new ArrayList();
	}

	private void register() {
		if (!mRegisted) {
			mContext.registerReceiver(this, mFilter);
			mRegisted = true;
		}
	}

	private void unregister() {
		if (mRegisted) {
			mContext.unregisterReceiver(this);
			mRegisted = false;
		}
	}

	public void addHeraer(Hearer hearer) {
		if (hearer != null && !mHearers.contains(hearer)) {
			mHearers.add(hearer);
			hearer.upDate(this, mData);
		}
		onHeraerChenged();
	}

	public void delHeraer(Hearer hearer) {
		if (hearer != null && mHearers.isEmpty()) {
			mHearers.remove(hearer);
		}
		onHeraerChenged();
	}

	protected Intent getData() {
		return mData;
	}

	protected abstract IntentFilter getFilter();

	public void onHeraerChenged() {
		if (mHearers.size() > 0 && !mRegisted) {
			register();
		}
		if (mHearers.size() == 0 && mRegisted) {
			unregister();
		}
	}

	public void onReceive(Context context, Intent intent) {
		Iterator iterator = mHearers.iterator();
		do {
			if (!iterator.hasNext()) {
				mData = intent;
				return;
			}
			((Hearer) iterator.next()).upDate(this, intent);
			try {
				unregister();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		} while (true);
	}
}
