// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.eruitong.utils;

import com.eruitong.eruitong.R;

import android.content.Context;
import android.media.MediaPlayer;

public class PlayRaws {

	public static MediaPlayer mPlayerError;
	public static MediaPlayer mPlayerScan;

	public PlayRaws() {
	}

	public static void PlayError(Context context) {
		mPlayerError = MediaPlayer.create(context, R.raw._error);
		mPlayerError
				.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {

					public void onCompletion(MediaPlayer mediaplayer) {
						mediaplayer.release();
					}

				});
		mPlayerError.start();
	}

	public static void PlayScan(Context context) {
		mPlayerScan = MediaPlayer.create(context, R.raw._scan);
		mPlayerScan
				.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {

					public void onCompletion(MediaPlayer mediaplayer) {
						mediaplayer.release();
					}

				});
		mPlayerScan.start();
	}

}
