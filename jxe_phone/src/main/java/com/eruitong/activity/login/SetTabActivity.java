package com.eruitong.activity.login;

import com.eruitong.eruitong.R;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

@SuppressWarnings("deprecation")
public class SetTabActivity extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settab);

		Resources res = getResources(); // Resource object to get Drawables
		final TabHost tabHost = getTabHost(); // The activity TabHost

		Intent intent; // Reusable Intent for each tab

		View viewLeft = LayoutInflater.from(this).inflate(R.layout.tab_spec,
				null);
		TextView txtLeft = (TextView) viewLeft
				.findViewById(R.id.txt_spec_label);
		txtLeft.setText("设置操作");
		intent = new Intent().setClass(this, SetTabOperateActivity.class);
		// Initialize a TabSpec for each tab and add it to the TabHost
		TabSpec spec1 = tabHost.newTabSpec("operate").setIndicator(viewLeft)
				.setContent(intent);

		tabHost.addTab(spec1);

		View viewRight = LayoutInflater.from(this).inflate(R.layout.tab_spec,
				null);
		TextView txtRight = (TextView) viewRight
				.findViewById(R.id.txt_spec_label);
		txtRight.setText("打印设置");
		// Do the same for the other tabs
		intent = new Intent().setClass(this, SetTabPrintActivity.class);
		TabSpec spec2 = tabHost.newTabSpec("print").setIndicator(viewRight)
				.setContent(intent);
		tabHost.addTab(spec2);

		tabHost.setCurrentTab(0);

		tabHost.getTabWidget().getChildTabViewAt(0)
				.setBackgroundResource(R.drawable.tab_bg_press);
		tabHost.getTabWidget().getChildTabViewAt(1)
				.setBackgroundResource(R.drawable.tab_bg);
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				TabWidget tabWidget = tabHost.getTabWidget();
				View jianceMenu = tabWidget.getChildTabViewAt(0);
				View weihuMenu = tabWidget.getChildTabViewAt(1);

				if ("operate".equals(tabId)) {
					weihuMenu.setBackgroundResource(R.drawable.tab_bg);
					jianceMenu.setBackgroundResource(R.drawable.tab_bg_press);
				} else {
					weihuMenu.setBackgroundResource(R.drawable.tab_bg_press);
					jianceMenu.setBackgroundResource(R.drawable.tab_bg);
				}
			}
		});
	}

	public void return1(View view) {
		finish();
		startActivity(new Intent(this, LoginActivity.class));
	}

}
