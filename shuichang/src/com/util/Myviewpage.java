package com.util;

import com.lr.fragment.DataFragement;
import com.lr.fragment.HistoryData;

import android.content.Context;
import android.nfc.tech.IsoDep;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class Myviewpage extends ViewPager {
	private static boolean falg = false;
	static Context context;
	static AttributeSet attributeSet;

	public Myviewpage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (getCurrentItem() == 0 && DataFragement.isHint()) {

			return falg;// 防止冲突
		}
		return super.onInterceptTouchEvent(ev);

	}
}
