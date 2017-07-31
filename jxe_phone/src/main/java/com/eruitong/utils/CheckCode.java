// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.eruitong.utils;

public class CheckCode {

	public CheckCode() {
	}

	public static char genCheckCode(String s) {
		if (s == null || s.length() != 8) {
			return ' ';
		} else {
			int j = s.charAt(7);
			int k = s.charAt(6);
			int l = s.charAt(5);
			int i1 = s.charAt(4);
			int j1 = s.charAt(3);
			int k1 = s.charAt(2);
			int l1 = s.charAt(1);
			int i = s.charAt(0);
			j -= 48;
			k -= 48;
			l -= 48;
			i1 -= 48;
			j1 -= 48;
			k1 -= 48;
			l1 -= 48;
			i -= 48;
			j *= j;
			k *= k;
			l *= l;
			i1 *= i1;
			j1 *= j1;
			k1 *= k1;
			l1 *= l1;
			i *= i;
			i = j / 10 + (j - (j / 10) * 10) + (k / 10 + (k - (k / 10) * 10))
					+ (l / 10 + (l - (l / 10) * 10))
					+ (i1 / 10 + (i1 - (i1 / 10) * 10))
					+ (j1 / 10 + (j1 - (j1 / 10) * 10))
					+ (k1 / 10 + (k1 - (k1 / 10) * 10))
					+ (l1 / 10 + (l1 - (l1 / 10) * 10))
					+ (i / 10 + (i - (i / 10) * 10));
			j = (i / 10 + 1) * 10;
			return String.valueOf(j - i - ((j - i) / 10) * 10).charAt(0);
		}
	}
}
