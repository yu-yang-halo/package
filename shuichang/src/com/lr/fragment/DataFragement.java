package com.lr.fragment;

import com.example.huoyuyunshu.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class DataFragement extends Fragment {
	private RadioGroup radioGroup;
	private FragmentManager manager;
	private RelData relData;
	private HistoryData historyData;
	private RadioButton rel;
	private Fragment mContent;
	private TextView internet;
	private static boolean isHint = false;
	private static boolean isResum = false;
	private MyInternetBrodcast myInternetBrodcast;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle bundle = getArguments();
		manager = getChildFragmentManager();
		View view = inflater.inflate(R.layout.data, null);
		radioGroup = (RadioGroup) view.findViewById(R.id.check);
		rel = (RadioButton) view.findViewById(R.id.relData);
		internet = (TextView) view.findViewById(R.id.internet);
		internet.setBackgroundColor(Color.rgb(248, 237, 182));
		internet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (android.os.Build.VERSION.SDK_INT > 10) {
					// 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
					startActivity(new Intent(
							android.provider.Settings.ACTION_SETTINGS));
				} else {
					startActivity(new Intent(
							android.provider.Settings.ACTION_WIRELESS_SETTINGS));
				}
			}
		});
		rel.setChecked(true);
		relData = new RelData();
		relData.setArguments(bundle);
		historyData = new HistoryData();
		historyData.setArguments(bundle);
		if (rel.isChecked()) {
			FragmentTransaction beginTransaction = manager.beginTransaction();
			beginTransaction.add(R.id.lay, relData);
			beginTransaction.commit();
			mContent = relData;
		}
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == R.id.relData) {
					isHint = false;
					switchContent(relData);

				} else if (checkedId == R.id.historyData) {
					isHint = true;
					switchContent(historyData);
				}
			}

		});

		return view;
	}

	public void switchContent(Fragment to) {
		if (mContent != to) {

			FragmentTransaction transaction = getChildFragmentManager()
					.beginTransaction();
			if (!to.isAdded()) { // 先判断是否被add过

				transaction.hide(mContent).add(R.id.lay, to); // 隐藏当前的fragment，add下一个到Activity中
				transaction.commit();

			} else {
				transaction.hide(mContent).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}

			mContent = to;

		}

	}

	@Override
	public void onResume() {
		isResum = true;
		myInternetBrodcast = new MyInternetBrodcast();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("internet");
		getActivity().registerReceiver(myInternetBrodcast, intentFilter);
		super.onResume();
	}

	public static boolean isHint() {
		return isHint;
	}

	public static boolean getResume() {
		return isResum;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		getActivity().unregisterReceiver(myInternetBrodcast);
		super.onDestroy();
	}

	public static void setResumer(boolean isresum) {
		isResum = isresum;

	}

	class MyInternetBrodcast extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			boolean booleanExtra = arg1.getBooleanExtra("isNet", false);
			if (booleanExtra) {
				internet.setVisibility(View.GONE);
			} else {
				internet.setVisibility(View.VISIBLE);
			}

		}
	}

}
